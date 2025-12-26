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

    // ProjectE, ProjectEX, and JEI dependencies temporarily disabled
    // These mods are not available in public Maven repositories for NeoForge 1.21.1
    // Will need to:
    // 1. Download JARs manually from CurseForge
    // 2. Place in libs/ directory
    // 3. Use flatDir repository or local dependencies

    // TODO: Re-enable once we have the JAR files
    // compileOnly("moze_intel.projecte:ProjectE:${project.property("projecte_version")}")
    // runtimeOnly("moze_intel.projecte:ProjectE:${project.property("projecte_version")}")
    // compileOnly("dev.ftb.mods:ftb-projectex:${project.property("projectex_version")}")
    // runtimeOnly("dev.ftb.mods:ftb-projectex:${project.property("projectex_version")}")
    // compileOnly("mezz.jei:jei-${project.property("minecraft_version")}-neoforge-api:${project.property("jei_version")}")
    // runtimeOnly("mezz.jei:jei-${project.property("minecraft_version")}-neoforge:${project.property("jei_version")}")
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
