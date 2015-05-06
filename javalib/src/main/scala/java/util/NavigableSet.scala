package java.util

trait NavigableSet[E] extends java.util.SortedSet[E] {

  def ceiling(e: E): E

  def descendingIterator(): java.util.Iterator[E]

  def descendingSet(): java.util.NavigableSet[E]

  def floor(e: E): E

  def headSet(toElement: E): java.util.SortedSet[E]

  def headSet(toElement: E, inclusive: Boolean): java.util.NavigableSet[E]

  def higher(e: E): E

  def iterator(): java.util.Iterator[E]

  def lower(e: E): E

  def pollFirst(): E

  def pollLast(): E

  def subSet(fromElement: E, fromInclusive: Boolean, toElement: E, toInclusive: Boolean): java.util.NavigableSet[E]

  def subSet(fromElement: E, toElement: E): java.util.SortedSet[E]

  def tailSet(fromElement: E): java.util.SortedSet[E]

  def tailSet(fromElement: E, inclusive: Boolean): java.util.NavigableSet[E]

}
