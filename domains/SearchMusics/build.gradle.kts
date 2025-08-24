plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.dagger)
    implementation(libs.dagger.hilt.core)
    ksp(libs.dagger.hilt.compiler)
}