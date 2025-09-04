package engine.model.entity

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class JpaBase {
    abstract var id: Int?

    operator fun component1() = id ?: error("ID not set")

    private fun getClass(o: Any): Class<*> = if (o is HibernateProxy)
        o.hibernateLazyInitializer.persistentClass
    else o.javaClass

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (getClass(other) != getClass(this)) return false
        val other: JpaBase = other as JpaBase
        return id != null && id == other.id
    }

    override fun hashCode(): Int = getClass(this).hashCode()

    override fun toString(): String =
        "${getClass(this).simpleName.replace("Entity","")}[id=$id]"
}