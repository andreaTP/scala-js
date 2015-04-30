package java.util

import scala.annotation.tailrec
import scala.collection.JavaConversions._

trait Map[K,V] {
	def clear(): Unit
	def containsKey(key: Any): Boolean
	def containsValue(value: Any): Boolean
	def	entrySet(): Set[Map.Entry[K,V]]
	def	equals(o: Any): Boolean
	def get(key: Any): V
	def hashCode(): Int
	def	isEmpty(): Boolean
	def keySet(): java.util.Set[K]
	def put(key: K, value: V): V
	def putAll[K2 <: K, V2 <: V](m: Map[K2,V2]): Unit
	def remove(key: Any): V
	def size(): Int
	def values(): Collection[V]
}

object Map {

	trait Entry[K,V] {
		def equals(o: Any): Boolean
		def getKey(): K
		def getValue(): V
		def hashCode(): Int
		def setValue(value: V): V
	}

}

object AbstractMap {

	private def equalsEntry[K,V](entry: Map.Entry[K,V], other: Any): Boolean = {
		other match {
			case entry2: Map.Entry[_,_] => 
				(
					(entry.getKey == null && entry2.getKey == null) ||
					entry.getKey.equals(entry2.getKey)
				) && (
					(entry.getValue == null && entry2.getValue == null) ||
					entry.getValue.equals(entry2.getValue)
				)
			case _ => false
		}
	}
	private def calcHashCode[K,V](entry: Map.Entry[K,V]): Int = {
		val keyHash = 
			if (entry.getKey==null) 0
			else entry.getKey.hashCode
		val valueHash = 
			if (entry.getValue==null) 0
			else entry.getValue.hashCode

		keyHash ^ valueHash
	}

	class SimpleEntry[K,V](key: K, value: V) extends Map.Entry[K,V] with Serializable {
		private var _key: K = key
		private var _value: V = value

		def this(entry: Map.Entry[K,V]) =
			this(entry.getKey,entry.getValue)

		override def equals(o: Any): Boolean = 
			equalsEntry(this, o)
		def getKey(): K = _key
		def getValue(): V = _value
		override def hashCode(): Int = calcHashCode(this)
		def setValue(value: V): V = {
			_value = value 
			_value
		}
	}

	class SimpleImmutableEntry[K,V](key: K, value: V)extends Map.Entry[K,V] with Serializable {
		def this(entry: Map.Entry[K,V]) =
			this(entry.getKey,entry.getValue)

		override def equals(o: Any): Boolean = 
			equalsEntry(this, o)
		def getKey(): K = key
		def getValue(): V = value
		override def hashCode(): Int = calcHashCode(this)
		def setValue(value: V): V = throw new UnsupportedOperationException()
	}
}

abstract class AbstractMap[K,V]() extends java.util.Map[K,V] {
	self =>

	@tailrec
	private def findFirst(i: Iterator[Map.Entry[K,V]])
						 (f: (Map.Entry[K,V]) => Boolean, 
						 fi: (Iterator[Map.Entry[K,V]]) => Unit = {_ => ()}): Map.Entry[K,V] = 
		if (!i.hasNext) null
		else {
			val o = i.next
			if (f(o)) {
				fi(i)
				o
			}
			else findFirst(i)(f)
		}

	@tailrec
	private def onAll[IK,IV](i: Iterator[Map.Entry[IK,IV]])
					 (f: (Map.Entry[IK,IV]) => Unit): Unit = 
		if (!i.hasNext) return
		else {
			f(i.next)
			onAll(i)(f)
		}

	def size(): Int = entrySet.size
	def isEmpty(): Boolean = size == 0
	def containsValue(value: Any): Boolean =
		findFirst(entrySet.iterator)({x => value.equals(x.getValue)}) != null
	def containsKey(key: Any): Boolean =
		get(key) != null
	def get(key: Any): V = {
		val res = findFirst(entrySet.iterator)({x => key.equals(x.getKey)})
		if (res == null) res.asInstanceOf[V]
		else res.getValue
	}
	def put(key: K, value: V): V = 
		throw new UnsupportedOperationException()
	def remove(key: Any): V =
		findFirst(entrySet.iterator)({x => key.equals(x.getKey)}, {i => i.remove}).getValue
	def putAll[K2 <: K,V2 <: V](m: Map[K2,V2]): Unit = 
		onAll(m.entrySet.iterator){e: Map.Entry[K2,V2] => put(e.getKey, e.getValue)}
	def clear(): Unit = 
		entrySet.clear
	def keySet(): java.util.Set[K] = 
		new java.util.Set[K] {
			override def size = self.size
			override def contains(k: Any) = self.containsKey(k)
			def add(x: K): Boolean = throw new UnsupportedOperationException()
			def addAll(x: java.util.Collection[_ <: K]): Boolean = throw new UnsupportedOperationException()
			def clear(): Unit = self.clear
			def containsAll(l: java.util.Collection[_]): Boolean = 
				!(for {
					e <- l
				} yield {self.containsKey(e)}).exists(_ == false) //To be verified
			def isEmpty(): Boolean = self.isEmpty
			def iterator(): java.util.Iterator[K] = throw new UnsupportedOperationException()
			def remove(x: Any): Boolean = self.remove(x) != null
			def removeAll(l: java.util.Collection[_]): Boolean = 
				!(for {
					e <- l
				} yield {self.remove(e)!=null}).exists(_ == true)
			def retainAll(x: java.util.Collection[_]): Boolean = throw new UnsupportedOperationException()
			def toArray[T](x: Array[T with Object]): Array[T with Object] = throw new UnsupportedOperationException()
			def toArray(): Array[Object] = throw new UnsupportedOperationException()
		}
	def values(): java.util.Collection[V] = 
		new java.util.Collection[V] {
			override def size = self.size
			override def contains(k: Any) = self.containsKey(k)
			def add(x: V): Boolean = throw new UnsupportedOperationException()
			def addAll(x: java.util.Collection[_ <: V]): Boolean = throw new UnsupportedOperationException()
			def clear(): Unit = self.clear
			def containsAll(l: java.util.Collection[_]): Boolean = 
				!(for {
					e <- l
				} yield {self.containsValue(e)}).exists(_ == false) //To be verified
			def isEmpty(): Boolean = self.isEmpty
			def iterator(): java.util.Iterator[V] = throw new UnsupportedOperationException()
			def remove(x: Any): Boolean = self.remove(x) != null //Really do this in java???
			def removeAll(l: java.util.Collection[_]): Boolean = 
				!(for {
					e <- l
				} yield {self.remove(e)!=null}).exists(_ == true)
			def retainAll(x: java.util.Collection[_]): Boolean = throw new UnsupportedOperationException()
			def toArray[T](x: Array[T with Object]): Array[T with Object] = throw new UnsupportedOperationException()
			def toArray(): Array[Object] = throw new UnsupportedOperationException()
		}
	override def equals(o: Any): Boolean =
		if (o==this) true
		else {
			o match {
				case m: Map[K,V] => 
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
		(for{
			e <- entrySet
		} yield {
			e.hashCode
		}).sum
}