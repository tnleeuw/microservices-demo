package io.pivotal.microservices.services.accounts

import java.util.logging.Logger

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Import

import io.pivotal.microservices.accounts.AccountRepository
import io.pivotal.microservices.accounts.AccountsConfiguration

/**
 * Run as a micro-service, registering with the Discovery Server (Eureka).
 *
 *
 * Note that the configuration for this application is imported from
 * [AccountsConfiguration]. This is a deliberate separation of concerns.

 * @author Paul Chapman
 */
@EnableAutoConfiguration
@EnableDiscoveryClient
@Import(AccountsConfiguration::class)
class AccountsServer {

    @Autowired
    protected var accountRepository: AccountRepository? = null

    protected var logger = Logger.getLogger(AccountsServer::class.java!!.getName())

    companion object {

        /**
         * Run the application using Spring Boot and an embedded servlet engine.

         * @param args
         * *            Program arguments - ignored.
         */
        @JvmStatic fun main(args: Array<String>) {
            // Tell server to look for accounts-server.properties or
            // accounts-server.yml
            System.setProperty("spring.config.name", "accounts-server")

            SpringApplication.run(AccountsServer::class.java, *args)
        }
    }
}
