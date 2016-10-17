package io.pivotal.microservices.accounts

import java.util.logging.Logger

import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

import io.pivotal.microservices.exceptions.AccountNotFoundException

abstract class AbstractAccountControllerTests {

    @Autowired
    internal var accountController: AccountsController? = null

    @Test
    fun validAccountNumber() {
        Logger.getGlobal().info("Start validAccountNumber test")
        val account = accountController!!.byNumber(ACCOUNT_1)

        Assert.assertNotNull(account)
        Assert.assertEquals(ACCOUNT_1, account.number)
        Assert.assertEquals(ACCOUNT_1_NAME, account.owner)
        Logger.getGlobal().info("End validAccount test")
    }

    @Test
    fun validAccountOwner() {
        Logger.getGlobal().info("Start validAccount test")
        val accounts = accountController!!.byOwner(ACCOUNT_1_NAME)
        Logger.getGlobal().info("In validAccount test")

        Assert.assertNotNull(accounts)
        Assert.assertEquals(1, accounts.size.toLong())

        val account = accounts[0]
        Assert.assertEquals(ACCOUNT_1, account.number)
        Assert.assertEquals(ACCOUNT_1_NAME, account.owner)
        Logger.getGlobal().info("End validAccount test")
    }

    @Test
    fun validAccountOwnerMatches1() {
        Logger.getGlobal().info("Start validAccount test")
        val accounts = accountController!!.byOwner("Keri")
        Logger.getGlobal().info("In validAccount test")

        Assert.assertNotNull(accounts)
        Assert.assertEquals(1, accounts.size.toLong())

        val account = accounts[0]
        Assert.assertEquals(ACCOUNT_1, account.number)
        Assert.assertEquals(ACCOUNT_1_NAME, account.owner)
        Logger.getGlobal().info("End validAccount test")
    }

    @Test
    fun validAccountOwnerMatches2() {
        Logger.getGlobal().info("Start validAccount test")
        val accounts = accountController!!.byOwner("keri")
        Logger.getGlobal().info("In validAccount test")

        Assert.assertNotNull(accounts)
        Assert.assertEquals(1, accounts.size.toLong())

        val account = accounts[0]
        Assert.assertEquals(ACCOUNT_1, account.number)
        Assert.assertEquals(ACCOUNT_1_NAME, account.owner)
        Logger.getGlobal().info("End validAccount test")
    }

    @Test
    fun invalidAccountNumber() {
        try {
            accountController!!.byNumber("10101010")
            Assert.fail("Expected an AccountNotFoundException")
        } catch (e: AccountNotFoundException) {
            // Worked!
        }

    }

    @Test
    fun invalidAccountName() {
        try {
            accountController!!.byOwner("Fred Smith")
            Assert.fail("Expected an AccountNotFoundException")
        } catch (e: AccountNotFoundException) {
            // Worked!
        }

    }

    companion object {
        @JvmStatic
        protected val ACCOUNT_1 = "123456789"
        @JvmStatic
        protected val ACCOUNT_1_NAME = "Keri Lee"
    }
}
