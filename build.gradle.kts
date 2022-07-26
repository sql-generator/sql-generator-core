import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    `maven-publish`
    id("io.gitlab.arturbosch.detekt").version("1.21.0")
}

group = "io.github.sqlgenerator"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10")

    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.test {
    useJUnitPlatform()

    testLogging {
        lifecycle {
            events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
        }
        info.events = lifecycle.events
        info.exceptionFormat = lifecycle.exceptionFormat
    }

    val failedTests = mutableListOf<TestDescriptor>()
    val skippedTests = mutableListOf<TestDescriptor>()

    // See https://github.com/gradle/kotlin-dsl/issues/836
    addTestListener(object : TestListener {
        override fun beforeSuite(suite: TestDescriptor) {}
        override fun beforeTest(testDescriptor: TestDescriptor) {}
        override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
            when (result.resultType) {
                TestResult.ResultType.FAILURE -> failedTests.add(testDescriptor)
                TestResult.ResultType.SKIPPED -> skippedTests.add(testDescriptor)
                else -> Unit
            }
        }

        override fun afterSuite(suite: TestDescriptor, result: TestResult) {
            if (suite.parent == null) { // root suite
                logger.lifecycle("----")
                logger.lifecycle("Test result: ${result.resultType}")
                logger.lifecycle(
                    "Test summary: ${result.testCount} tests, " +
                            "${result.successfulTestCount} succeeded, " +
                            "${result.failedTestCount} failed, " +
                            "${result.skippedTestCount} skipped")
                failedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tFailed Tests")
                skippedTests.takeIf { it.isNotEmpty() }?.prefixedSummary("\tSkipped Tests:")
            }
        }

        private infix fun List<TestDescriptor>.prefixedSummary(subject: String) {
            logger.lifecycle(subject)
            forEach { test -> logger.lifecycle("\t\t${test.displayName()}") }
        }

        private fun TestDescriptor.displayName() = parent?.let { "${it.name} - $name" } ?: name
    })
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
