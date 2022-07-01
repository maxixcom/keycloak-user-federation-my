package studio.buket.keycloak.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object ExposedUsers : UUIDTable("users") {
    val username: Column<String> = varchar("username", 255)
    val email: Column<String> = varchar("email", 255)
    val firstName: Column<String> = varchar("first_name", 255)
    val lastName: Column<String> = varchar("last_name", 255)
    val password: Column<String> = varchar("password", 255)
}

class ExposedUser(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ExposedUser>(ExposedUsers)
}
