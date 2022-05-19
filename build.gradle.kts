import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}


dependencies {
    implementation("org.litote.kmongo:kmongo-serialization:4.2.4")
    implementation("org.litote.kmongo:kmongo-id-serialization:4.2.4")
    implementation("org.json:json:20201115")
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("org.slf4j:slf4j-log4j12:1.7.25")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.apache.poi:poi:5.0.0")
    implementation("org.apache.poi:poi-ooxml:5.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.1")

}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}