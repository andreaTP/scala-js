package java.util.concurrent

import scala.collection.JavaConversions.asJavaCollection

class ConcurrentSkipListSet[+E >: Null] /*(comparator: java.util.Comparator[E],
  collection: java.util.Collection[E])
    extends java.util.AbstractSet[E]
    with java.util.NavigableSet[E]
    with java.lang.Cloneable
    with java.io.Serializable {

  def this() =
    this(null, asJavaCollection(List[E]()))

}*/
