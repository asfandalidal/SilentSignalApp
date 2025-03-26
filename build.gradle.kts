// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

buildscript {
    repositories {
        google()  // Ensure Google repository is included
        mavenCentral()  // Add Maven Central repository
    }

    dependencies {
        // Add classpath for Google services plugin
        classpath("com.google.gms:google-services:4.3.15")  // Make sure this is correct version
        // Optional: add other dependencies here (like for Firebase, if needed)
    }
}

