package java.util

trait SortedSet[E] extends java.util.Set[E] {

  def comparator(): java.util.Comparator[E]

  def first(): E

  def headSet(toElement: E): SortedSet[E]

  def last(): E

  def subSet(fromElement: E, toElement: E): java.util.SortedSet[E]

  def tailSet(fromElement: E): java.util.SortedSet[E]

}
