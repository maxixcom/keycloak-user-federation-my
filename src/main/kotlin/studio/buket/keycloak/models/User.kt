package studio.buket.keycloak.models

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "t_user")
open class User {
    @Id
    @Column(name = "id", nullable = false)
    open var id: UUID? = null

    @Column(unique = true)
    open var username: String? = null

    @Column(unique = true)
    open var email: String? = null
    open var password: String? = null
    open var phone: String? = null
}
