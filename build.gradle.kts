plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.3.1").apply(false)
    id("com.android.library").version("7.3.1").apply(false)
    kotlin("android").version("1.7.10").apply(false)
    kotlin("multiplatform").version("1.7.10").apply(false)
}

mapOf(
    // KOTLIN
    "kotlinCoroutines" to "1.6.4",

    // ANDROID
    "minSdk" to 22,
    "targetSdk" to 33,
    "compiledSdk" to 33
).entries.forEach {
    project.extra.set(it.key, it.value)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
