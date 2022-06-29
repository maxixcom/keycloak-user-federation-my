import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.21"
}

group = "studio.buket.keycloak"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.keycloak:keycloak-core:18.0.0")
    compileOnly("org.keycloak:keycloak-server-spi:18.0.0")
    compileOnly("org.keycloak:keycloak-services:18.0.0")
    compileOnly("org.jboss.logging:jboss-logging:3.5.0.Final")
//    compileOnly("org.hibernate:hibernate-core:5.6.5.Final")
//    compileOnly("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final")
    compileOnly("org.jboss.spec.javax.ejb:jboss-ejb-api_3.2_spec:1.0.0.Final")
    compileOnly("org.apache.commons:commons-lang3:3.12.0")

    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.4.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
//
// tasks.withType<Jar>() {
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//    configurations["compileClasspath"]
//        .forEach { file: File ->
//            println(file)
//            from(zipTree(file.absoluteFile))
//        }
// }
//
tasks.withType<Jar>() {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    configurations["runtimeClasspath"]
        .forEach { file: File ->
            println(file)
            from(zipTree(file.absoluteFile))
        }
}
