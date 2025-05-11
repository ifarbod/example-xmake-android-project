import java.util.regex.Pattern

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.xmakeGradle)
}

val SUPPORTED_ABIS = setOf("armeabi-v7a", "arm64-v8a", "riscv64", "x86", "x86_64")
// val SUPPORTED_ABIS = setOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")

fun getCurrentBuildType(): String {
    val tskReqStr = gradle.startParameter.taskRequests.toString()

    val pattern = when {
        tskReqStr.contains("assemble") -> Pattern.compile("assemble(\\w+)?(ReleaseDbg|Release|Debug)")
        tskReqStr.contains("bundle") -> Pattern.compile("bundle(\\w+)?(ReleaseDbg|Release|Debug)")
        else -> Pattern.compile("generate(\\w+)?(ReleaseDbg|Release|Debug)")
    }

    val matcher = pattern.matcher(tskReqStr)

    return if (matcher.find()) {
        matcher.group(2).lowercase()
    } else {
        "debug"
    }
}

android {
    namespace = "com.ifarbod.myapplicationcpp2"
    compileSdk = 36

    buildToolsVersion = "36.0.0"
    ndkVersion = "27.2.12479018"

    defaultConfig {
        applicationId = "com.ifarbod.myapplicationcpp2"
        minSdk = 21
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += SUPPORTED_ABIS
        }
    }

    splits {
        abi {
            isEnable = true
            reset()
            include(*SUPPORTED_ABIS.toTypedArray())
            isUniversalApk = true
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        create("releaseDbg") {

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    externalNativeBuild {
        xmake {
            path = "../xmake.lua"
            //logLevel = "debug"
            abiFilters += SUPPORTED_ABIS
            buildMode = getCurrentBuildType().lowercase()
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
