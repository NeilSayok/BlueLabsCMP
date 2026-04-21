#!/bin/bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo "🔨 Building BlueLabs..."

./gradlew clean

./gradlew :buildSrc:build

echo "🗺️  Generating sitemap..."
./gradlew :composeApp:generateSitemap

echo "⚙️  Building Wasm distribution..."
./gradlew :composeApp:wasmJsBrowserDistribution

echo "📄 Generating blog pages..."
node scripts/generate-blog-pages.js

echo "🚀 Deploying to Firebase..."
firebase deploy

echo "✅ Done!"
