keystore=release_keystore.jks
app_publisher_android_key=app-publisher-android-key.json
deployment_properties=deployment.properties

# Keystore
sh file_generator.sh content=$KEYSTORE_CONTENT file_name=../keys/$keystore

# App publisher Android key
sh file_generator.sh content=$APP_PUBLISHER_ANDROID_KEY_CONTENT file_name=../keys/$app_publisher_android_key

# Deployment properties
deployment_property_list=(
  "GITHUB_TOKEN=$GIHUB_API_TOKEN]"
  "KEYSTORE_FILE_PATH=../automation/keys/$keystore"
  "SIGNING_STORE_PASSWORD=$SIGNING_STORE_PASSWORD"
  "SIGNING_KEY_PASSWORD=$SIGNING_KEY_PASSWORD"
  "SIGNING_KEY_ALIAS=$SIGNING_KEY_ALIAS"
  "APP_PUBLISHER_ANDROID_KEY_FILE_PATH=../automation/keys/$app_publisher_android_key"
  "PACKAGE_NAME=com.mecofarid.summy"
)
deployment_properties_content=$(printf '%s\n' "${deployment_property_list[@]}")
sh file_generator.sh content=$deployment_properties_content file_name=../keys/$deployment_properties

# Appfile
appfile_property_list=(
  "json_key_file('../automation/$app_publisher_android_key')"
  "package_name('com.mecofarid.summy')"
  "ENV['PROPERTIES_FILE_PATH']=\"../automation/keys/$deployment_properties\""
)
appfile_content=$(printf '%s\n' "${appfile_property_list[@]}")
sh file_generator.sh content=$appfile_content file_name=../../androidApp/fastlane/Appfile

cd "../../androidApp" && fastlane release
