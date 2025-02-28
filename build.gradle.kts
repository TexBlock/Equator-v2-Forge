plugins {
	base
	java
	idea
	`maven-publish`
	alias(libs.plugins.architectury.loom)
}

group = libs.versions.maven.group.get()
version = "${libs.versions.minecraft.get()}-${libs.versions.mod.get()}"

base {
	archivesName.set(libs.versions.archives.name)
}

repositories {
	mavenCentral()
}

dependencies {
	minecraft(libs.minecraft)
	mappings(libs.yarn) { artifact { classifier = "v2" } }
	forge(libs.forge)

	api(libs.mixbox)
	api(libs.word.wrap)
	api(libs.guava.mini)

	include(libs.mixbox)
	include(libs.word.wrap)
	include(libs.guava.mini)
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17

	withSourcesJar()
}

tasks {
	processResources {
		inputs.property("version", libs.versions.mod.get())

		filesMatching("fabric.mod.json") {
			expand(mapOf("version" to libs.versions.mod.get()))
		}
	}

	jar {
		from("LICENSE") {
			rename { "${it}_${base.archivesName}" }
		}
	}

	register("collectJars", Copy::class.java) {
		dependsOn("deleteCollectedJars")
		group = "build"

		val destination = file("${layout.buildDirectory.get()}/libs/latest")

		from("${layout.buildDirectory.get()}/libs/${base.archivesName.get()}-$version.jar")
		from("${layout.buildDirectory.get()}/libs/${base.archivesName.get()}-$version-sources.jar")

		subprojects.forEach {
			from("${it.layout.buildDirectory.get()}/libs/${it.base.archivesName.get()}-$version.jar")
			from("${it.layout.buildDirectory.get()}/libs/${it.base.archivesName.get()}-$version-sources.jar")
		}

		into(destination)
	}

	register("deleteCollectedJars", Delete::class.java) {
		group = "build"

		val destination = file("${layout.buildDirectory.get()}/libs/latest")
		delete(destination)

		onlyIf { destination.exists() }
	}
}

publishing {
	publications {
		create<MavenPublication>("mavenJava") {
			from(components["java"])
		}
	}

	repositories {
	}
}
