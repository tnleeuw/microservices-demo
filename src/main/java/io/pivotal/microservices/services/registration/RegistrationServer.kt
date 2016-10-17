package io.pivotal.microservices.services.registration

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

/**
 * All you need to run a Eureka registration server.

 * @author Paul Chapman
 */
@SpringBootApplication
@EnableEurekaServer
object RegistrationServer {

    /**
     * Run the application using Spring Boot and an embedded servlet engine.

     * @param args
     * *            Program arguments - ignored.
     */
    @JvmStatic fun main(args: Array<String>) {
        // Tell server to look for registration.properties or registration.yml
        System.setProperty("spring.config.name", "registration-server")

        SpringApplication.run(RegistrationServer::class.java, *args)
    }
}
