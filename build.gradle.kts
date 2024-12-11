// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false
}

// Menambahkan bagian untuk mendefinisikan versi AGP
buildscript {
    dependencies {
        // Ubah versi AGP ke 8.1.0
        classpath("com.android.tools.build:gradle:8.1.0")
    }
}