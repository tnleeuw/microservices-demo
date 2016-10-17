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
    lateinit var number: String
        protected set
    lateinit var owner: String
        protected set
    var balance: BigDecimal = BigDecimal.ZERO
        get() = field.setScale(2, BigDecimal.ROUND_HALF_EVEN)
        set(value) { field = value.setScale(2, BigDecimal.ROUND_HALF_EVEN) }

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

    override fun toString(): String {
        return "$number [$owner]: $$balance"
    }

}
