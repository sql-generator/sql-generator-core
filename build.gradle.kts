import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    `maven-publish`
}

group = "io.github.sqlgenerator"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group as String?
            artifactId = rootProject.name
            version = version

            pom {
                name.set("SQL-Generator Core")
                url.set("https://github.com/sql-generator/sql-generator-core")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://raw.githubusercontent.com/sql-generator/sql-generator-core/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("julianahrens")
                        name.set("Julian Ahrens")
                        email.set("opensource@julianahrens.de")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com:sql-generator/sql-generator-core.git")
                    developerConnection.set("scm:git:ssh://github.com:sql-generator/sql-generator-core.git")
                    url.set("https://github.com/sql-generator/sql-generator-core.git")
                }
            }

            from(components["java"])
        }
    }
}
