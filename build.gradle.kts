plugins {
    id("java")
    id("eclipse")
    id("idea")
    id("maven-publish")
    id("net.neoforged.gradle.userdev") version "7.0.163"
}

version = project.property("mod_version") as String
group = project.property("mod_group_id") as String

repositories {
    mavenLocal()
    mavenCentral()

    // Local libs directory for manually downloaded dependencies
    flatDir {
        dirs("libs")
    }

    maven {
        name = "Moze Maven (ProjectE)"
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        name = "FTB Maven (ProjectEX)"
        url = uri("https://maven.saps.dev/releases/")
    }
    maven {
        name = "Jared's Maven (JEI)"
        url = uri("https://maven.blamejared.com/")
    }
}

base {
    archivesName.set("${project.property("mod_id")}")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

runs {
    configureEach {
        systemProperty("forge.logging.markers", "REGISTRIES")
        systemProperty("forge.logging.console.level", "debug")
        modSource(sourceSets.main.get())
    }

    create("client") {
        systemProperty("forge.enabledGameTestNamespaces", project.property("mod_id") as String)
    }

    create("server") {
        systemProperty("forge.enabledGameTestNamespaces", project.property("mod_id") as String)
        programArgument("--nogui")
    }

    create("gameTestServer") {
        systemProperty("forge.enabledGameTestNamespaces", project.property("mod_id") as String)
    }

    create("data") {
        programArguments.addAll(
            "--mod", project.property("mod_id") as String,
            "--all",
            "--output", file("src/generated/resources/").absolutePath,
            "--existing", file("src/main/resources/").absolutePath
        )
    }
}

sourceSets.main.get().resources.srcDir("src/generated/resources")

dependencies {
    implementation("net.neoforged:neoforge:${project.property("neoforge_version")}")

    // ProjectE - Local JAR file from libs/ directory
    implementation(files("libs/ProjectE-1.21.1-PE1.1.0.jar"))

    // Refined Storage 2.0 - Optional integration, compileOnly for lightweight build
    compileOnly(files("libs/refinedstorage-neoforge-2.0.0.jar"))

    // ProjectEX - Not available for 1.21.1 NeoForge yet
    // See: https://github.com/FTBTeam/FTB-ProjectEX/issues/137
    // Will add when released for 1.21.1

    // JEI - Optional, will add later if needed

    // JUnit 5 for unit testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}

tasks.withType<ProcessResources> {
    val replaceProperties = mapOf(
        "minecraft_version" to project.property("minecraft_version"),
        "neoforge_version" to project.property("neoforge_version"),
        "mod_id" to project.property("mod_id"),
        "mod_name" to project.property("mod_name"),
        "mod_license" to project.property("mod_license"),
        "mod_version" to project.property("mod_version"),
        "mod_authors" to project.property("mod_authors"),
        "mod_description" to project.property("mod_description")
    )

    inputs.properties(replaceProperties)

    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${project.projectDir}/repo")
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
