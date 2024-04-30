
# Copy the README.md And CHANGELOG.md to their respective tabs in MKDocs
cp README.md docs/index.md
cp CHANGELOG.md docs/changelog.md
cp LICENSE docs/license.md
cp CONTRIBUTING.md docs/contributing.md

# README is located on the root of the project, and index is inside of docs
# so wee need to replace ./docs/assets references with ./assets
sed -i '' 's/\.\/docs\/assets/\.\/assets/g' docs/index.md


# Generate Dokka documentation for the project
./gradlew dokkaHtmlMultiModule
mv build/dokka/htmlMultiModule docs/api