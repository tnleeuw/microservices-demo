package io.pivotal.microservices.accounts

import java.io.Serializable
import java.math.BigDecimal

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * Persistent account entity with JPA markup. Accounts are stored in an H2
 * relational database.

 * @author Paul Chapman
 */
@Entity
@Table(name = "T_ACCOUNT")
class Account : Serializable {

    @Id
    protected var id: Long? = null

    lateinit var number: String
        protected set

    @Column(name = "name")
    lateinit var owner: String
        protected set

    protected var balance: BigDecimal
        get() = field.setScale(2, BigDecimal.ROUND_HALF_EVEN)

    /**
     * Default constructor for JPA only.
     */
    protected constructor() {
        balance = BigDecimal.ZERO
    }

    constructor(number: String, owner: String) {
        id = nextId
        this.number = number
        this.owner = owner
        balance = BigDecimal.ZERO
    }

    fun getId(): Long {
        return id!!
    }

    /**
     * Set JPA id - for testing and JPA only. Not intended for normal use.

     * @param id
     * *            The new id.
     */
    protected fun setId(id: Long) {
        this.id = id
    }

    fun withdraw(amount: BigDecimal) {
        balance = balance.subtract(amount)
    }

    fun deposit(amount: BigDecimal) {
        balance = balance.add(amount)
    }

    override fun toString(): String {
        return "$number [$owner]: $$balance"
    }

    companion object {

        private val serialVersionUID = 1L

        /**
         * This is a very simple, and non-scalable solution to generating unique
         * ids. Not recommended for a real application. Consider using the
         * @GeneratedValue annotation and a sequence to generate ids.

         * @return The next available id.
         */
        var nextId: Long = 0L
            get() = synchronized(field) {
                return field++
            }

    }

}
