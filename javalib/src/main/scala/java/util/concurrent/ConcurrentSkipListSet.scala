package java.util.concurrent

import scala.collection.JavaConversions.asJavaCollection

class ConcurrentSkipListSet[E >: Null](comparator: java.util.Comparator[E],
  collection: java.util.Collection[E])
    extends java.util.AbstractSet[E]
    with java.util.NavigableSet[E]
    with java.lang.Cloneable
    with java.io.Serializable {

  def this() =
    this(null, asJavaCollection(List[E]()))

  def this(comp: java.util.Comparator[E]) =
    this(comp, asJavaCollection(List[E]()))

  def this(sortedSet: java.util.SortedSet[E]) = 
  	///this(sortedSet.comparator(), asJavaCollection(sortedSet.toSet))
  	this(null, null)

  def this(collection: java.util.Collection[E]) = 
  	this(null, collection)

  override def add(e: E): Boolean = ???

  def ceiling(e: E): E = ???

  override def clear(): Unit = ???

  override def clone(): java.util.concurrent.ConcurrentSkipListSet[E] = ???

  def comparator(): java.util.Comparator[E] = ???

  override def contains(o: Any): Boolean = ???

  def descendingIterator(): java.util.Iterator[E] = ???

  def descendingSet(): java.util.NavigableSet[E] = ???

  override def equals(o: Any): Boolean = ???

  def first(): E = ???

  def floor(e: E): E = ???

  def headSet(toElement: E): java.util.NavigableSet[E] = ???

  def headSet(toElement: E, inclusive: Boolean): java.util.NavigableSet[E] = ???

  def higher(e: E): E = ???

  override def isEmpty(): Boolean = ???

  def iterator(): java.util.Iterator[E] = ???

  def last(): E = ???

  def lower(e: E): E = ???

  def pollFirst(): E = ???

  def pollLast(): E = ???

  override def remove(o: Any): Boolean = ???

  override def removeAll(c: java.util.Collection[E]): Boolean = ???

  def size(): Int = ???

  def subSet(fromElement: E, fromInclusive: Boolean, toElement: E, toInclusive: Boolean): java.util.NavigableSet[E] = ???

  def subSet(fromElement: E, toElement: E): java.util.NavigableSet[E] = ???

  def tailSet(fromElement: E): java.util.NavigableSet[E] = ???

  def tailSet(fromElement: E, inclusive: Boolean): java.util.NavigableSet[E] = ???

}
