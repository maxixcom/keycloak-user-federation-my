package studio.buket.keycloak.dao

import studio.buket.keycloak.models.User
import javax.persistence.EntityManager

class UserDaoImpl(
    private val entityManager: EntityManager
) : UserDao {
    override fun updateUser(user: User): User {
        val transaction = entityManager.transaction
        transaction.begin()
        entityManager.merge(user)
        transaction.commit()

        return user
    }

    override fun close() {
        entityManager.close()
    }
}
