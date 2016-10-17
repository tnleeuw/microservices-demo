package io.pivotal.microservices.services.web

import java.math.BigDecimal

import com.fasterxml.jackson.annotation.JsonRootName

/**
 * Account DTO - used to interact with the [WebAccountsService].

 * @author Paul Chapman
 */
@JsonRootName("Account")
class Account
/**
 * Default constructor for JPA only.
 */
protected constructor() {

    protected var id: Long? = null
    var number: String
        protected set
    var owner: String
        protected set
    protected var balance: BigDecimal

    init {
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

    fun getBalance(): BigDecimal {
        return balance.setScale(2, BigDecimal.ROUND_HALF_EVEN)
    }

    protected fun setBalance(value: BigDecimal) {
        balance = value
        balance.setScale(2, BigDecimal.ROUND_HALF_EVEN)
    }

    override fun toString(): String {
        return "$number [$owner]: $$balance"
    }

}
