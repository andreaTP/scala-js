package java.util

trait Map[K, V] {
  def clear(): Unit
  def containsKey(key: Any): Boolean
  def containsValue(value: Any): Boolean
  def entrySet(): Set[Map.Entry[K, V]]
  def equals(o: Any): Boolean
  def get(key: Any): V
  def hashCode(): Int
  def isEmpty(): Boolean
  def keySet(): java.util.Set[K]
  def put(key: K, value: V): V
  def putAll[K2 <: K, V2 <: V](m: Map[K2, V2]): Unit
  def remove(key: Any): V
  def size(): Int
  def values(): Collection[V]
}

object Map {

  trait Entry[K, V] {
    def equals(o: Any): Boolean
    def getKey(): K
    def getValue(): V
    def hashCode(): Int
    def setValue(value: V): V
  }

}
