package studio.buket.keycloak.dao

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import java.util.UUID

object Users : UUIDTable("users") {
    val username: Column<String?> = varchar("username", 255).nullable().uniqueIndex()
    val email: Column<String?> = varchar("email", 255).nullable().uniqueIndex()
    val firstName: Column<String?> = varchar("first_name", 255).nullable()
    val lastName: Column<String?> = varchar("last_name", 255).nullable()
    val password: Column<String?> = varchar("password", 255).nullable()
}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(Users)

    var username by Users.username
    var email by Users.email
    var firstName by Users.firstName
    var lastName by Users.lastName
    var password by Users.password
}
