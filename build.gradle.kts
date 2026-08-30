plugins {
    id("java-library")
    idea
}

group = "dev.sbs"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven(url = "https://central.sonatype.com/repository/maven-snapshots")
    maven(url = "https://jitpack.io")
}

dependencies {
    // Simplified Annotations
    compileOnly(libs.simplified.annotations)
    annotationProcessor(libs.simplified.annotations)
    testCompileOnly(libs.simplified.annotations)
    testAnnotationProcessor(libs.simplified.annotations)

    // Tests
    testImplementation(libs.hamcrest)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.platform.launcher)

    // Sibling - Mojang API (SimplifiedContract returns MojangProfile / MojangUsername)
    api("com.github.simplified-api:mojang") { version { strictly("911319a") } }

    // Simplified Libraries (github.com/simplified-dev)
    api("com.github.simplified-dev:collections") { version { strictly("9696ca5") } }
    api("com.github.simplified-dev:utils") { version { strictly("3d8af56") } }
    api("com.github.simplified-dev:reflection") { version { strictly("158edbc") } }
    api("com.github.simplified-dev:gson-extras") { version { strictly("ed1d77e") } }
    api("com.github.simplified-dev:client") { version { strictly("2ced9a4") } }

    // The repository contracts, for the write instruction a queued envelope rebuilds. Contracts
    // only - nothing here reaches an ORM.
    api("com.github.simplified-dev:persistence-contracts") { version { strictly("master-SNAPSHOT") } }

    // Gson - Deserializer/TypeAdapter usage plus SerializedName/JsonAdapter annotations
    api(libs.gson)
}

idea {
    module {
        excludeDirs.addAll(listOf(
            layout.projectDirectory.dir(".schema").asFile
        ))
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}
