
# Copy the README.md And CHANGELOG.md to their respective tabs in MKDocs
cp README.md docs/index.md
cp CHANGELOG.md docs/changelog.md

# Generate Dokka documentation for the project
./gradlew dokkaHtmlMultiModule
mv build/dokka/htmlMultiModule docs/api