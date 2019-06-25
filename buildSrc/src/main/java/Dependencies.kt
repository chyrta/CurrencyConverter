object PluginDependencies {
    val android = "com.android.tools.build:gradle:${Versions.gradleAndroidPlugin}"
    val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
}

object ProjectDependencies {
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val ktlint = "com.pinterest:ktlint:${Versions.ktlint}"

    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val retrofitAdapter = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    val rxjava = "io.reactivex.rxjava2:rxjava:${Versions.rxjava}"
    val rxandroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxandroid}"

    val koin = "org.koin:koin-android:${Versions.koin}"
    val koinViewModels = "org.koin:koin-android-viewmodel:${Versions.koin}"

    val supportAppCompat = "com.android.support:appcompat-v7:${Versions.supportLibrary}"
    val supportRecyclerView = "com.android.support:recyclerview-v7:${Versions.supportLibrary}"
    val supportDesign = "com.android.support:design:${Versions.supportLibrary}"

    val mockitoKotlin = "com.nhaarman:mockito-kotlin:${Versions.mockitoKotlin}"
    val junit = "junit:junit:${Versions.junit}"
    val mockito = "org.mockito:mockito-all:${Versions.mockito}"
    val hamcrest = "org.hamcrest:hamcrest-all:${Versions.hamcrest}"
    val androidTestRunner = "com.android.support.test:runner:${Versions.androidTestRunner}"
    val espresso = "com.android.support.test.espresso:espresso-core:${Versions.espresso}"

    val lifecycle = "android.arch.lifecycle:extensions:${Versions.lifecycle}"
}

object Versions {
    val gradleAndroidPlugin = "3.4.1"

    val buildTools = "29.0.0"
    val compileSdk = 28
    val targetSdk = 28
    val minSdk = 15
    val releaseVersionCode = 1
    val releaseVersionName = "1.0"

    val supportLibrary = "28.0.0"
    val kotlin = "1.3.31"
    val retrofit = "2.6.0"
    val rxjava = "2.2.9"
    val rxandroid = "2.1.1"

    val koin = "2.0.1"
    val ktlint = "0.33.0"
    val lifecycle = "1.1.1"

    val junit = "4.12"
    val mockito = "1.10.19"
    val mockitoKotlin = "1.5.0"
    val androidTestRunner = "1.0.2"
    val hamcrest = "1.3"
    val espresso = "3.0.2"
}