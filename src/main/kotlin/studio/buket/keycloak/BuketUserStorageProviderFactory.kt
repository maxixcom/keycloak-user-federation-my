package studio.buket.keycloak

import org.jboss.logging.Logger
import org.keycloak.component.ComponentModel
import org.keycloak.models.KeycloakSession
import org.keycloak.storage.UserStorageProviderFactory
import javax.naming.InitialContext

class BuketUserStorageProviderFactory : UserStorageProviderFactory<BuketStorageProvider> {
    override fun create(session: KeycloakSession, model: ComponentModel): BuketStorageProvider {
        return try {
            val ctx = InitialContext()
            val provider = ctx.lookup("java:global/" + BuketStorageProvider::class.java.simpleName)
                as BuketStorageProvider
            provider.apply {
                setModel(model)
                setSession(session)
            }
        } catch (e: java.lang.Exception) {
            throw RuntimeException(e)
        }
    }

    override fun getId(): String = PROVIDER_ID

    override fun getHelpText(): String = HELP_TEXT

    override fun close() {
        logger.info("<<<<<<< Closing factory")
    }

    companion object {
        const val PROVIDER_ID = "buket-user-storage"
        const val HELP_TEXT = "Buket users"
        val logger: Logger = Logger.getLogger(BuketUserStorageProviderFactory::class.java)
    }
}
