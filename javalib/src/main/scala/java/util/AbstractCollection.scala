package java.util

import scala.annotation.tailrec
import scala.collection.immutable.{List => SList}

abstract class AbstractCollection[E]() extends java.util.Collection[E] {
	def iterator(): Iterator[E]
	def size(): Int

	@tailrec
	private def iterateAndDo[X](i: Iterator[E], f: (E) => X, part: SList[X] = SList()): SList[X] =
		if (i.hasNext) {
			iterateAndDo(i, f, part.::(f(i.next)))
		} else part

	@tailrec
	private def iterateApplyAndDo[X](i: Iterator[E], f: (Iterator[E],E) => X, part: SList[X] = SList()): SList[X] =
		if (i.hasNext) {
			iterateApplyAndDo(i, f, part.::(f(i,i.next)))
		} else part

	@tailrec
	private def iterateAndCheck[X](i: Iterator[X], f: (X) => Boolean): Boolean =
		if (i.hasNext) {
			if (f(i.next)) true
			else iterateAndCheck(i, f)
		} else false

	@tailrec
	private def iterateAndApply(i: Iterator[E], f: (Iterator[E]) => Unit): Unit =
		if (i.hasNext) {
			f(i)
			iterateAndApply(i,f)
		} else return	

	@tailrec
	private def iterateCheckAndApply(i: Iterator[E], f: (Iterator[E],E) => Boolean): Boolean =
		if (i.hasNext) {
			if (f(i,i.next)) true
			else iterateCheckAndApply(i, f)
		} else false

	def isEmpty(): Boolean = (size == 0)
	def contains(o: Any): Boolean = 
		iterateAndCheck[E](iterator, {_.equals(o)})
	def containsAll(c: java.util.Collection[_]): Boolean = 
		!iterateAndCheck(c.iterator, {x: Any => !contains(x)})
	def add(e: E): Boolean =
		throw new UnsupportedOperationException()
	def addAll[E1 <: E](c: java.util.Collection[E1]): Boolean =
		throw new UnsupportedOperationException()
	def	clear(): Unit =
		iterateAndApply(iterator, {_.remove})
	def remove(o: Any): Boolean =
		iterateCheckAndApply(iterator, {
			case (i,e) if e.equals(o) => i.remove; true
			case _ => false})		
	def removeAll(c: java.util.Collection[E]): Boolean = 
		iterateApplyAndDo(iterator, {
			case (i,e) if (c.contains(e)) => {i.remove; true}
			case _ => false
		}).exists(_ == true)
	def retainAll(c: java.util.Collection[E]): Boolean = 
		iterateApplyAndDo(iterator, {
			case (i,e) if (!c.contains(e)) => {i.remove; true}
			case _ => false
		}).exists(_ == true)
	override def toString(): String =
		iterateAndDo(iterator, {java.lang.String.valueOf(_)}).mkString("[",",","]")
	def toArray[X](a: Array[X]): Array[X] = {
		val iter = iterator
		for {
			i <- 0.to(size)
		} {
			a(i) = iter.next.asInstanceOf[X]
		}
		a
	}

	def toArray(): Array[Any] = {
		val iter = iterator
		(for {i <- 0.to(size)}
		yield (iter.next)).toArray[Any]
	}

}