 # Check if the file exists
MAVEN_CENTRAL_USERNAME=$1
MAVEN_CENTRAL_PASSWORD=$2
if [ ! -f "gradle.properties" ]; then
  echo "gradle.properties does not exist."
  exit 1
fi
# Append -SNAPSHOT to the VERSION_NAME
sed -i -e '/VERSION_NAME/s/$/-SNAPSHOT/' "gradle.properties"

./gradlew publishAllPublicationsToMavenCentralRepository \
 -PmavenCentralUsername="$MAVEN_CENTRAL_USERNAME" \
 -PmavenCentralPassword="$MAVEN_CENTRAL_PASSWORD"
