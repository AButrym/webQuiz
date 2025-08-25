package engine.model.entity

import org.hibernate.proxy.HibernateProxy

abstract class JpaBase {
    abstract var id: Int?

    private fun getClass(o: Any): Class<*> = if (o is HibernateProxy)
        o.hibernateLazyInitializer.persistentClass
    else o.javaClass

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null) return false
        if (getClass(o) != getClass(this)) return false
        val other: JpaBase = o as JpaBase
        return id != null && id == other.id
    }

    override fun hashCode(): Int = getClass(this).hashCode()

    override fun toString(): String = "${getClass(this).simpleName}[id=$id]"
}