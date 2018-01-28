
allprojects {
    group = "com.multisource"
    version = "1.0-SNAPSHOT"
}

subprojects {

    apply {
        plugin("java")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        compile("org.springframework", "spring-context","5.0.3.RELEASE")

        testCompile("org.junit.platform", "junit-platform-commons", "1.0.3")
        testCompile("org.junit.jupiter", "junit-jupiter-params", "5.0.3")
        testCompile("org.junit.jupiter", "junit-jupiter-api", "5.0.3")

    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}