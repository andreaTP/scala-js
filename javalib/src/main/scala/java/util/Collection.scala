package java.util

trait Collection[E] {

	def add(e: E): Boolean
	def addAll[E1 <:E](c: java.util.Collection[E]): Boolean
	def clear(): Unit
	def contains(o: Any): Boolean
	def containsAll(c: Collection[_]): Boolean
	def equals(o: Any): Boolean
	def isEmpty(): Boolean
	def hashCode(): Int
	def iterator(): java.util.Iterator[E]
	def remove(o: Any): Boolean
	def removeAll(c: java.util.Collection[E]): Boolean
	def retainAll(c: java.util.Collection[E]): Boolean
	def size(): Int
	def toArray(): Array[Any]
	def toArray[X](a: Array[X]): Array[X]

}