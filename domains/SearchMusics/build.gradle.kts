plugins {
    alias(libs.plugins.java.library)
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.ksp)
    `java-test-fixtures`
}

dependencies {
    implementation(libs.dagger)
    implementation(libs.dagger.hilt.core)
    ksp(libs.dagger.hilt.compiler)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
}