#!/usr/bin/env node
'use strict';

const https = require('https');
const fs = require('fs');
const path = require('path');

const OUTPUT_DIR = process.argv[2] || 'composeApp/build/dist/wasmJs/productionExecutable';

function getConfig() {
  if (process.env.FIREBASE_BASE_URL) {
    return {
      baseUrl: process.env.BASE_URL || 'https://bluelabs.in',
      firebaseUrl: process.env.FIREBASE_BASE_URL,
      apiKey: process.env.FIREBASE_BEARER,
    };
  }

  const propsPath = path.join(__dirname, '..', 'local.properties');
  if (!fs.existsSync(propsPath)) {
    throw new Error('local.properties not found and no env vars set');
  }

  const props = {};
  fs.readFileSync(propsPath, 'utf8').split('\n').forEach(line => {
    const trimmed = line.trim();
    if (trimmed && !trimmed.startsWith('#')) {
      const eq = trimmed.indexOf('=');
      if (eq > 0) {
        props[trimmed.slice(0, eq).trim()] = trimmed.slice(eq + 1).trim();
      }
    }
  });

  return {
    baseUrl: props['BASE_URL'] || 'https://bluelabs.in',
    firebaseUrl: props['FIREBASE_BASE_URL'],
    apiKey: props['FIREBASE_BEARER'],
  };
}

function fetchBlogs(firebaseUrl, apiKey) {
  return new Promise((resolve) => {
    const url = `${firebaseUrl}?key=${apiKey}`;
    https.get(url, { headers: { 'Content-Type': 'application/json' } }, (res) => {
      let data = '';
      res.on('data', chunk => { data += chunk; });
      res.on('end', () => {
        if (res.statusCode !== 200) {
          console.warn(`⚠️  Firestore returned HTTP ${res.statusCode} — skipping blog pages`);
          resolve([]);
          return;
        }
        try {
          const json = JSON.parse(data);
          const documents = json.documents || [];
          const blogs = [];
          for (const doc of documents) {
            const fields = doc.fields || {};
            if (!fields.isPublished?.booleanValue) continue;
            const title = fields.title?.stringValue;
            if (!title) continue;
            const slug = fields.url_str?.stringValue
              || title.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '');
            const description = fields.description?.stringValue || title;
            const image = fields.bigImg?.stringValue || '';
            blogs.push({ title, slug, description, image });
          }
          console.log(`   ✅ Fetched ${blogs.length} published blog posts`);
          resolve(blogs);
        } catch (e) {
          console.warn('⚠️  Failed to parse Firestore response:', e.message);
          resolve([]);
        }
      });
    }).on('error', (e) => {
      console.warn('⚠️  Network error fetching blogs:', e.message);
      resolve([]);
    });
  });
}

function escapeHtml(str) {
  return String(str || '')
    .replace(/&/g, '&amp;')
    .replace(/"/g, '&quot;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;');
}

function generateHtml(blog, baseUrl) {
  const ogImage = blog.image || `${baseUrl}/meta_images/og-bluelabs.png`;
  const pageUrl = `${baseUrl}/blog/${blog.slug}`;
  return `<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="google-adsense-account" content="ca-pub-8051825813385802">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <base href="/">
    <title>${escapeHtml(blog.title)} : Blue Labs</title>

    <meta property="og:type"         content="article">
    <meta property="og:site_name"    content="Blue Labs">
    <meta property="og:title"        content="${escapeHtml(blog.title)}">
    <meta property="og:description"  content="${escapeHtml(blog.description)}">
    <meta property="og:image"        content="${escapeHtml(ogImage)}">
    <meta property="og:url"          content="${escapeHtml(pageUrl)}">
    <meta name="twitter:card"        content="summary_large_image">
    <meta name="twitter:title"       content="${escapeHtml(blog.title)}">
    <meta name="twitter:description" content="${escapeHtml(blog.description)}">
    <meta name="twitter:image"       content="${escapeHtml(ogImage)}">

    <link rel="icon" type="image/x-icon" href="favicon.ico">
    <link rel="icon" type="image/png" sizes="192x192" href="logo192.png">
    <link rel="icon" type="image/png" sizes="512x512" href="logo512.png">
    <link rel="apple-touch-icon" href="logo192.png">
    <link href="styles.css" rel="stylesheet" type="text/css">
    <script src="composeApp.js" type="application/javascript"></script>
    <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-8051825813385802"
            crossorigin="anonymous"></script>
</head>
<body>
</body>
</html>`;
}

async function main() {
  console.log('🔄 Generating per-blog static HTML pages...');

  const config = getConfig();
  const blogs = await fetchBlogs(config.firebaseUrl, config.apiKey);

  if (blogs.length === 0) {
    console.log('ℹ️  No blogs found — skipping blog page generation');
    return;
  }

  for (const blog of blogs) {
    const blogDir = path.join(OUTPUT_DIR, 'blog', blog.slug);
    fs.mkdirSync(blogDir, { recursive: true });
    fs.writeFileSync(path.join(blogDir, 'index.html'), generateHtml(blog, config.baseUrl));
  }

  console.log(`✅ Generated ${blogs.length} blog HTML pages → ${OUTPUT_DIR}/blog/`);
}

main().catch(e => {
  console.error('❌ Blog page generation failed:', e.message);
  process.exit(1);
});