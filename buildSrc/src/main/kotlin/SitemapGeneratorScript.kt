import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import org.json.JSONObject

/**
 * Standalone script to generate sitemap.xml with dynamic blog data from Firebase.
 * This is executed during the Gradle build process.
 */
object SitemapGeneratorScript {

    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size < 4) {
            println("❌ Usage: <baseUrl> <firebaseUrl> <authToken> <outputPath>")
            return
        }
        println("Usage: <baseUrl> <firebaseUrl> <authToken> <outputPath>")

        val baseUrl = args[0]
        val firebaseUrl = args[1]
        val authToken = args[2]
        val outputPath = args[3]

        println("🔄 Fetching blog data from Firebase...")
        val blogData = fetchBlogData(firebaseUrl, authToken)

        println("🗺️  Generating sitemap with ${blogData.size} blog posts...")
        val sitemap = generateSitemap(baseUrl, blogData)

        val outputFile = File(outputPath)
        outputFile.parentFile?.mkdirs()
        outputFile.writeText(sitemap)

        println("✅ Sitemap generated successfully at: $outputPath")
        println("📊 Total URLs: ${5 + blogData.size} (5 static + ${blogData.size} blogs)")
    }

    private fun fetchBlogData(firebaseUrl: String, apiKey: String): List<BlogPost> {
        return try {
            // Append API key as query parameter for Firestore REST API
            val urlWithKey = if (firebaseUrl.contains("?")) {
                "$firebaseUrl&key=$apiKey"
            } else {
                "$firebaseUrl?key=$apiKey"
            }

            val url = URL(urlWithKey)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            val responseCode = connection.responseCode
            if (responseCode == 200) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                parseBlogResponse(response)
            } else {
                val errorBody = connection.errorStream?.bufferedReader()?.use { it.readText() }
                println("⚠️  Failed to fetch blogs (HTTP $responseCode)")
                if (errorBody != null) {
                    println("   Error: $errorBody")
                }
                println("   Generating sitemap with static pages only")
                emptyList()
            }
        } catch (e: Exception) {
            println("⚠️  Error fetching blog data: ${e.message}")
            e.printStackTrace()
            println("   Generating sitemap with static pages only")
            emptyList()
        }
    }

    private fun parseBlogResponse(jsonResponse: String): List<BlogPost> {
        return try {
            val json = JSONObject(jsonResponse)
            val documents = json.optJSONArray("documents") ?: return emptyList()

            val blogs = mutableListOf<BlogPost>()
            for (i in 0 until documents.length()) {
                val doc = documents.getJSONObject(i)
                val fields = doc.optJSONObject("fields") ?: continue

                // Check if blog is published
                val isPublished = fields.optJSONObject("isPublished")
                    ?.optBoolean("booleanValue", false) ?: false

                if (!isPublished) {
                    continue // Skip unpublished blogs
                }

                // Get title
                val title = fields.optJSONObject("title")
                    ?.optString("stringValue") ?: continue

                // Get URL slug (prefer url_str if available)
                val urlSlug = fields.optJSONObject("url_str")
                    ?.optString("stringValue")
                    ?: title.replace(" ", "-").lowercase()
                        .replace(Regex("[^a-z0-9-]"), "")

                // Get last modified date
                val lastModified = fields.optJSONObject("posted")
                    ?.optString("timestampValue")
                    ?.substringBefore("T") // Extract date part (YYYY-MM-DD)
                    ?: LocalDate.now().toString()

                blogs.add(BlogPost(title, urlSlug, lastModified))
            }

            println("   ✅ Successfully parsed ${blogs.size} published blog posts")
            blogs
        } catch (e: Exception) {
            println("⚠️  Error parsing blog data: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

    private fun generateSitemap(baseUrl: String, blogs: List<BlogPost>): String {
        val today = LocalDate.now()

        return buildString {
            appendLine("""<?xml version="1.0" encoding="UTF-8"?>""")
            appendLine("""<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">""")

            // Static pages
            appendUrl(baseUrl, today.toString(), "weekly", "1.0")
            appendUrl("$baseUrl/portfolio", today.toString(), "monthly", "0.9")
            appendUrl("$baseUrl/me", today.toString(), "monthly", "0.7")
            appendUrl("$baseUrl/search", today.toString(), "daily", "0.7")
            appendUrl("$baseUrl/privacy-policy", today.toString(), "yearly", "0.3")

            // Dynamic blog pages
            blogs.forEach { blog ->
                appendUrl("$baseUrl/blog/${blog.slug}", blog.lastModified, "monthly", "0.8")
            }

            appendLine("</urlset>")
        }
    }

    private fun StringBuilder.appendUrl(
        loc: String,
        lastmod: String,
        changefreq: String,
        priority: String
    ) {
        appendLine("  <url>")
        appendLine("    <loc>${escapeXml(loc)}</loc>")
        appendLine("    <lastmod>$lastmod</lastmod>")
        appendLine("    <changefreq>$changefreq</changefreq>")
        appendLine("    <priority>$priority</priority>")
        appendLine("  </url>")
    }

    private fun escapeXml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }

    data class BlogPost(val title: String, val slug: String, val lastModified: String)
}
