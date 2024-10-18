
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

buildscript {


    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath (libs.androidx.navigation.safe.args.gradle.plugin)
        classpath ("com.google.gms:google-services:4.4.2")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}