package java.util

import scala.annotation.tailrec

abstract class AbstractSet[E]() extends java.util.AbstractCollection[E] with java.util.Set[E] {

  override def equals(o: Any): Boolean = {
    def _equals =
      o match {
        case s: java.util.Collection[E] =>
          if (s.size == this.size) containsAll(s)
          else false
        case _ => false
      }

    o match {
      case ar: AnyRef =>
        if (this eq ar) true
        else _equals
      case _ => _equals
    }
  }

  override def hashCode(): Int = {
    @tailrec
    def sumAll(i: Iterator[E], part: Int): Int =
      if (i.hasNext) sumAll(i, part + i.next.hashCode)
      else part

    sumAll(iterator, 0)
  }

  override def removeAll(c: java.util.Collection[E]): Boolean = {
    @tailrec
    def remAll(iter: Iterator[E], comp: java.util.Collection[E], res: Boolean = false): Boolean = {
      if (iter.hasNext) {
        val elem = iter.next
        if (comp.contains(elem)) {
          remove(elem)
          remAll(iter, comp, true)
        } else remAll(iter, comp, res)
      } else res
    }

    if (size > c.size) remAll(c.iterator, this)
    else remAll(iterator, c)
  }
}
