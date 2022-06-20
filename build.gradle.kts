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
    compileOnly("org.jboss.logging:jboss-logging:3.5.0.Final")
    compileOnly("org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.0.Final")
    compileOnly("org.jboss.spec.javax.ejb:jboss-ejb-api_3.2_spec:1.0.0.Final")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
