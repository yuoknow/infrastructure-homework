import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly


plugins {
    id("org.springframework.boot") version "2.7.1"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.9.22" apply false
    id("ru.yoomoney.gradle.plugins.check-dependencies-plugin") version "9.0.1"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
// 	  id("io.gitlab.arturbosch.detekt")
    jacoco
}

allprojects {
    group = "com.stringconcat"
    version = "0.0.1-SNAPSHOT"

//    apply(plugin = "io.gitlab.arturbosch.detekt")
//    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    // detekt {
    // 	  buildUponDefaultConfig = true
    //  }

    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
            // allWarningsAsErrors = true
        }
    }

    configurations.all {
        resolutionStrategy.eachDependency {
            val version = requested.version?.toLowerCaseAsciiOnly()
            val isSnapshot = version?.contains("snapshot") ?: false
            val innerDependency = requested.module.group.startsWith("com.stringconcat")
//            if (isSnapshot && !innerDependency) {
//                throw IllegalArgumentException("Snapshot versions not allowed: ${requested.module}:${requested.version}")
//            }
        }
    }
}

jacoco {
    toolVersion = "0.8.11"
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly2 by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly.get())
    }
}

dependencies {
    // spring modules
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(project(":presentation"))
    implementation(project(":persistence"))
    implementation(project(":useCasePeople"))
    implementation(project(":businessPeople"))
    implementation(project(":quoteGarden"))
    implementation(project(":avatarsDicebear"))

    // dev tools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // persistance
    implementation("org.postgresql:postgresql:42.3.4")
    implementation("org.liquibase:liquibase-core:4.9.1")

    // tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")
}

tasks.test {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.check {
    finalizedBy(tasks.jacocoTestReport)
    finalizedBy(tasks.getByName("printNewDependencies"))
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.10".toBigDecimal()
            }
        }
    }
}
