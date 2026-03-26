plugins {
    id("scaffold.android.feature")
    id("scaffold.android.compose")
}

android {
    namespace = "com.lhz.feature.map"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}