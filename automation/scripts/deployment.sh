while getopts ":a:b:c:d:e:f:" opt; do
  case $opt in
    a) GTHB_PERSONAL_TOKEN="$OPTARG";;
    b) SIGNING_STORE_PASSWORD="$OPTARG";;
    c) SIGNING_KEY_PASSWORD="$OPTARG";;
    d) SIGNING_KEY_ALIAS="$OPTARG";;
    e) KEYSTORE_CONTENT="$OPTARG";;
    f) APP_PUBLISHER_ANDROID_KEY_CONTENT_ENCODED="$OPTARG";;
    \?) echo "Invalid option -$OPTARG" >&2
    exit 1
    ;;
  esac

  case $OPTARG in
    -*) echo "Option $opt needs a valid argument"
    exit 1
    ;;
  esac
done

echo "ARG IS ${#APP_PUBLISHER_ANDROID_KEY_CONTENT} $APP_PUBLISHER_ANDROID_KEY_CONTENT"

keys_dir="../keys"
keystore="release_keystore.jks"
app_publisher_android_key="app-publisher-android-key.json"
fastlane_dir="../../androidApp/fastlane"

# Create keys folder
mkdir $keys_dir

# Keystore
echo "$KEYSTORE_CONTENT" | base64 --decode > "$keys_dir/$keystore"

# App publisher Android key
echo "$APP_PUBLISHER_ANDROID_KEY_CONTENT_ENCODED" | base64 --decode > "$keys_dir/$app_publisher_android_key"

# Deployment environment variable
deployment_property_list=(
  "ENV[\"GTHB_PERSONAL_TOKEN\"]=\"$GTHB_PERSONAL_TOKEN\""
  "ENV[\"KEYSTORE_FILE_PATH\"]=\"../automation/keys/$keystore\""
  "ENV[\"SIGNING_STORE_PASSWORD\"]=\"$SIGNING_STORE_PASSWORD\""
  "ENV[\"SIGNING_KEY_PASSWORD\"]=\"$SIGNING_KEY_PASSWORD\""
  "ENV[\"SIGNING_KEY_ALIAS\"]=\"$SIGNING_KEY_ALIAS\""
)
deployment_properties_content=$(printf '%s\n' "${deployment_property_list[@]}")
echo "$deployment_properties_content" > "$fastlane_dir/env.rb"


# Appfile
appfile_property_list=(
  "json_key_file(\"../automation/keys/$app_publisher_android_key\")"
  "package_name(\"com.mecofarid.summy\")"
)
appfile_content=$(printf '%s\n' "${appfile_property_list[@]}")
echo "$appfile_content" > "$fastlane_dir/Appfile"

pushd "../../androidApp" || exit
gem install fastlane -N
bundle install
bundle exec fastlane release
popd || exit
