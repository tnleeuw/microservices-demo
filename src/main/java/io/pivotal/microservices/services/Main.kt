package io.pivotal.microservices.services

import io.pivotal.microservices.services.accounts.AccountsServer
import io.pivotal.microservices.services.registration.RegistrationServer
import io.pivotal.microservices.services.web.WebServer

/**
 * Allow the servers to be invoked from the command-line. The jar is built with
 * this as the `Main-Class` in the jar's `MANIFEST.MF`.

 * @author Paul Chapman
 */
object Main {

    @JvmStatic fun main(args: Array<String>) {

        var serverName = "NO-VALUE"

        when (args.size) {
            2 -> {
                // Optionally set the HTTP port to listen on, overrides
                // value in the <server-name>-server.yml file
                System.setProperty("server.port", args[1])
                serverName = args[0].toLowerCase()
            }
        // Fall through into ..

            1 -> serverName = args[0].toLowerCase()

            else -> {
                usage()
                return
            }
        }

        if (serverName == "registration" || serverName == "reg") {
            RegistrationServer.main(args)
        } else if (serverName == "accounts") {
            AccountsServer.main(args)
        } else if (serverName == "web") {
            WebServer.main(args)
        } else {
            println("Unknown server type: " + serverName)
            usage()
        }
    }

    protected fun usage() {
        println("Usage: java -jar ... <server-name> [server-port]")
        println("     where server-name is 'reg', 'registration', " + "'accounts' or 'web' and server-port > 1024")
    }
}
