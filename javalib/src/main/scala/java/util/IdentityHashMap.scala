package java.util

import scala.collection.mutable
import scala.annotation.tailrec

import scala.collection.JavaConversions._

class IdentityHashMap[K, V] protected (inner: mutable.Map[K, V])
    extends AbstractMap[K, V] with Map[K, V] with Serializable with Cloneable {

  def this() =
    this(mutable.HashMap.empty[K, V])

  def this(initialCapacity: Int) = {
    this()
    if (initialCapacity < 0)
      throw new IllegalArgumentException("initialCapacity < 0")
  }

  def this(m: Map[_ <: K, _ <: V]) = {
    this()
    putAll(m)
  }

  override def get(key: Any): V = {
    entrySet.iterator.find(_.getKey === key).fold[V] {
      null.asInstanceOf[V]
    } { entry =>
      entry.getValue
    }
  }

  private def refCompare(v1: Any, v2: Any): Boolean = {
    (v1, v2) match {
      case (ar1: AnyRef, ar2: AnyRef) => ar1 eq ar2
      case _ => v1 == v2
    }
  }

  override def containsKey(key: Any): Boolean = 
    entrySet.iterator.exists(entry => refCompare(key, entry.getKey))

  //TO BE VERIFIED
  override def containsValue(value: Any): Boolean =
    entrySet.iterator.exists(entry => refCompare(value, entry.getValue))

  override def put(key: K, value: V): V =
    inner.put(key, value).getOrElse(null.asInstanceOf[V])

  override def remove(key: Any): V = {
    @tailrec
    def findAndRemove(iter: Iterator[Map.Entry[K, V]]): V = {
      if (iter.hasNext) {
        val item = iter.next()
        if (refCompare(key, item.getKey)) {
          iter.remove()
          item.getValue
        } else
          findAndRemove(iter)
      } else
        null.asInstanceOf[V]
    }
    findAndRemove(entrySet.iterator)
  }

  override def equals(a: Any) = {
    a match {
      case m: Map[_, _] => entrySet.equals(m.entrySet)
      case _ => false
    }
  }

  override def hashCode(): Int =
    entrySet.map(_.hashCode).sum

  //override def keySet(): Set[K]
  //override def values(): Collection[V]
  override def entrySet(): Set[Map.Entry[K, V]] =
    throw new Exception("no entry set")
}