package java.util

import scala.collection.mutable
import scala.collection.JavaConversions._

class HashSet[E] extends AbstractSet[E] with Set[E]
                                        with Cloneable
                                        with Serializable {
  def this(initialCapacity: Int, loadFactor: Float) =
    this()

  def this(initialCapacity: Int) =
    this()

  def this(c: Collection[_ <: E]) = {
    this()
    addAll(c)
  }

  protected val inner: mutable.Map[Int, Box[E]] =
    new mutable.HashMap[Int, Box[E]]()

  override def contains(o: Any): Boolean =
    inner.contains(Box(o).hashCode)

  override def remove(o: Any): Boolean =
    inner.remove(Box(o).hashCode).isDefined

  override def containsAll(c: Collection[_]): Boolean =
    c.iterator.forall(e => inner.contains(Box(e).hashCode))

  override def removeAll(c: Collection[_]): Boolean = {
    val iter = c.iterator
    var changed = false
    while (iter.hasNext)
      changed = remove(iter.next()) || changed
    changed
  }

  override def retainAll(c: Collection[_]): Boolean = {
    val iter = iterator
    var changed = false
    while (iter.hasNext) {
      val value = iter.next
      if (!c.contains(value))
        changed = remove(value) || changed
    }
    changed
  }

  override def add(e: E): Boolean = {
    val value = Box(e)
    val hc = value.hashCode
    if (!inner.contains(hc)) {
      inner += (hc -> value)
      true
    } else false
  }

  override def addAll(c: Collection[_ <: E]): Boolean = {
    val iter = c.iterator()
    var changed = false
    while (iter.hasNext)
      changed = add(iter.next()) || changed
    changed
  }

  override def clear(): Unit = inner.clear()

  override def size(): Int = inner.size

  def iterator(): Iterator[E] = {
    new Iterator[E] {
      private val keysCopy = inner.keysIterator

      private var actualKey: Int = 0
      private var pos: Int = 0

      def hasNext(): Boolean = keysCopy.hasNext

      def next(): E = {
        val k = keysCopy.next()
        val v = inner(k)
        actualKey = k
        pos += 1
        v.inner
      }

      def remove(): Unit = {
        if (pos > 0)
          inner.remove(actualKey)
      }
    }
  }

}
