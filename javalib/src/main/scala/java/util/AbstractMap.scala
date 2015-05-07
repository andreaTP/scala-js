package java.util

import scala.annotation.tailrec
import scala.collection.JavaConversions._

object AbstractMap {

  private def entryEquals[K, V](entry: Map.Entry[K, V], other: Any): Boolean = {
    other match {
      case other: Map.Entry[_, _] =>
        entry.getKey == other.getKey && entry.getValue == other.getValue
      case _ => false
    }
  }

  private def entryHashCode[K, V](entry: Map.Entry[K, V]): Int = {
    val keyHash =
      if (entry.getKey == null) 0
      else entry.getKey.hashCode
    val valueHash =
      if (entry.getValue == null) 0
      else entry.getValue.hashCode

    keyHash ^ valueHash
  }

  class SimpleEntry[K, V](private var _key: K, private var _value: V) extends Map.Entry[K, V] with Serializable {

    def this(entry: Map.Entry[K, V]) =
      this(entry.getKey, entry.getValue)

    override def equals(o: Any): Boolean =
      entryEquals(this, o)

    def getKey(): K = _key

    def getValue(): V = _value

    override def hashCode(): Int = entryHashCode(this)

    def setValue(value: V): V = {
      val oldValue = _value
      _value = value
      oldValue
    }
  }

  class SimpleImmutableEntry[K, V](key: K, value: V) extends Map.Entry[K, V] with Serializable {
    def this(entry: Map.Entry[K, V]) =
      this(entry.getKey, entry.getValue)

    override def equals(o: Any): Boolean =
      entryEquals(this, o)

    def getKey(): K = key

    def getValue(): V = value

    override def hashCode(): Int = entryHashCode(this)

    def setValue(value: V): V = throw new UnsupportedOperationException()
  }
}

abstract class AbstractMap[K, V]() extends java.util.Map[K, V] { self =>

  @tailrec
  private def findFirst(i: Iterator[Map.Entry[K, V]])(f: (Map.Entry[K, V]) => Boolean,
      fi: (Iterator[Map.Entry[K, V]]) => Unit = { _ => () }): Map.Entry[K, V] =
    if (!i.hasNext) null
    else {
      val o = i.next
      if (f(o)) {
        fi(i)
        o
      } else {
        findFirst(i)(f)
      }
    }

  @tailrec
  private def onAll[IK, IV](i: Iterator[Map.Entry[IK, IV]])(f: (Map.Entry[IK, IV]) => Unit): Unit =
    if (i.hasNext) {
      f(i.next)
      onAll(i)(f)
    }

  def size(): Int = entrySet.size

  def isEmpty(): Boolean = size == 0

  def containsValue(value: Any): Boolean =
    findFirst(entrySet.iterator)({ x => value.equals(x.getValue) }) != null

  def containsKey(key: Any): Boolean =
    get(key) != null

  def get(key: Any): V = {
    val res = findFirst(entrySet.iterator)({ x => key == x.getKey })
    if (res == null) null.asInstanceOf[V]
    else res.getValue
  }

  def put(key: K, value: V): V =
    throw new UnsupportedOperationException()

  def remove(key: Any): V = {
    val item = findFirst(entrySet.iterator)({ x => key.equals(x.getKey) }, _.remove())
    if (item eq null) null.asInstanceOf[V]
    else item.getValue
  }

  def putAll[K2 <: K, V2 <: V](m: Map[K2, V2]): Unit =
    onAll(m.entrySet.iterator) { e: Map.Entry[K2, V2] => put(e.getKey, e.getValue) }

  def clear(): Unit =
    entrySet.clear

  def keySet(): java.util.Set[K] =
    new java.util.Set[K] {
      override def size = self.size

      override def contains(k: Any) = self.containsKey(k)

      def containsAll(l: java.util.Collection[_]): Boolean =
        !(for {
          e <- l
        } yield { self.containsKey(e) }).exists(_ == false) //To be verified

      def add(x: K): Boolean = throw new UnsupportedOperationException()
      def addAll[K1 <: K](x: java.util.Collection[K1]): Boolean = 
        throw new UnsupportedOperationException()

      def clear(): Unit = self.clear

      def isEmpty(): Boolean = self.isEmpty

      def iterator(): java.util.Iterator[K] = throw new UnsupportedOperationException()

      def remove(x: Any): Boolean = self.remove(x) != null

      def removeAll(l: java.util.Collection[K]): Boolean =
        !(for {
          e <- l
        } yield { self.remove(e) != null }).exists(_ == true)

      def retainAll(x: java.util.Collection[K]): Boolean = throw new UnsupportedOperationException()

      def toArray[T](x: Array[T]): Array[T] = throw new UnsupportedOperationException()
      def toArray(): Array[Any] = throw new UnsupportedOperationException()
    }

  def values(): java.util.Collection[V] =
    new java.util.Collection[V] {
      override def size = self.size

      override def contains(k: Any) = self.containsKey(k)
      def containsAll(l: java.util.Collection[_]): Boolean =
        !(for {
          e <- l
        } yield { self.containsValue(e) }).exists(_ == false) //To be verified

      def add(x: V): Boolean = throw new UnsupportedOperationException()
      def addAll[V1 <: V](x: java.util.Collection[V1]): Boolean = throw new UnsupportedOperationException()

      def clear(): Unit = self.clear

      def isEmpty(): Boolean = self.isEmpty

      def iterator(): java.util.Iterator[V] = throw new UnsupportedOperationException()

      def remove(x: Any): Boolean = self.remove(x) != null //Really do this in java???
      
      def removeAll(l: java.util.Collection[V]): Boolean =
        !(for {
          e <- l
        } yield { self.remove(e) != null }).exists(_ == true)

      def retainAll(x: java.util.Collection[V]): Boolean = throw new UnsupportedOperationException()

      def toArray[T](x: Array[T]): Array[T] = throw new UnsupportedOperationException()
      def toArray(): Array[Any] = throw new UnsupportedOperationException()
    }

  override def equals(o: Any): Boolean =
    if (o.asInstanceOf[AnyRef] eq this) true
    else {
      o match {
        case m: Map[_, _] =>
          if (self.size == m.size) {
            !(for {
              e <- entrySet
            } yield {
              m.get(e.getKey) == e.getValue
            }).exists(_ == false)
          } else false
        case _ => false
      }
    }

  override def hashCode(): Int =
    (for {
      e <- entrySet
    } yield {
      e.hashCode
    }).sum
}
