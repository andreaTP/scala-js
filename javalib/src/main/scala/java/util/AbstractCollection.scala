package java.util

import scala.annotation.tailrec
import scala.collection.immutable.{List => SList}

//To Be Done...

abstract class AbstractCollection[E]() extends java.util.Collection[E] {
	def iterator(): Iterator[E]
	def size(): Int

	@tailrec
	private def iterateAndDo[X](i: Iterator[E], f: (E) => X, part: SList[X] = SList()): SList[X] =
		if (i.hasNext) {
			iterateAndDo(i, f, part.::(f(i.next)))
		} else part

	@tailrec
	private def iterateAndCheck(i: Iterator[E], f: (E) => Boolean): Boolean =
		if (i.hasNext) {
			if (f(i.next)) true
			else iterateAndCheck(i, f)
		} else false

	def isEmpty(): Boolean = (size == 0)
	def contains(o: Any): Boolean = 
		iterateAndCheck(iterator, {_.equals(o)})

/*
	def add(e: E): Boolean
	def addAll[E1 <: E](c: java.util.Collection[E1]): Boolean
	def	clear(): Unit
	def contains(o: Any): Boolean
	def containsAll(c: Collection[_]): Boolean
	
	def remove(o: Any): Boolean
	def removeAll(c: java.util.Collection[E]): Boolean
	def retainAll(c: java.util.Collection[E]): Boolean
	
	def toArray(): Array[Any]
	def toArray(a: Array[T]): Array[T]
	def toString(): String
*/
}