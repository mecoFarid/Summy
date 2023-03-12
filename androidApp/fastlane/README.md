fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

### test

```sh
[bundle exec] fastlane test
```

Runs all the tests

### build_app

```sh
[bundle exec] fastlane build_app
```

Build release apk and bundle

### github_release

```sh
[bundle exec] fastlane github_release
```

Release to GitHub

### beta

```sh
[bundle exec] fastlane beta
```

Submit a new Beta Build to Crashlytics Beta

### deploy

```sh
[bundle exec] fastlane deploy
```

Deploy a new version to the Google Play

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
