package java.util.concurrent

import scala.collection.JavaConversions.{setAsJavaSet, asScalaSet, asJavaCollection}

trait ConcurrentMap[K,V] extends java.util.Map[K,V] {

	def putIfAbsent(key: K, value: V): V = 
		if (!containsKey(key))
       		put(key, value)
   		else
       		get(key)

	def remove(key: Any, value: Any): Boolean = 
 		if (containsKey(key) && get(key).equals(value)) {
       		remove(key)
       		true
   		} else false

	def replace(key: K, value: V): V = 
		if (containsKey(key)) {
       		put(key, value)
   		} else get(key)
		
	def replace(key: K, oldValue: V, newValue: V): Boolean =
		if (containsKey(key) && get(key).equals(oldValue)) {
       		put(key, newValue)
       		true
   		} else false

}

class ConcurrentHashMap[K >: Null, V >: Null](initialCapacity: Int = 16,
                 			loadFactor: Float = 0.75F,
                 			concurrencyLevel: Int = 16) 
							extends java.util.AbstractMap[K,V] 
							with java.util.concurrent.ConcurrentMap[K,V] 
							with java.io.Serializable {

	private val inner = new scala.collection.mutable.HashMap[K,V]()

	def TEST(): scala.collection.mutable.HashMap[K,V] = inner

	override def clear(): Unit = 
		inner.clear
	
	override def containsKey(key: Any): Boolean =
		try {
			inner.contains(key.asInstanceOf[K]) 
		 } catch {
		   case e: Throwable => false
		 } 
		
	override def containsValue(value: Any): Boolean =
		try { 
		   inner.exists{case (ik, iv) => value.equals(iv)}
		 } catch {
		   case e: Throwable => false
		 } 
		
	def elements(): Iterable[V] = inner.values
	
	def entrySet() = 
		setAsJavaSet(
			inner.map{case (k,v) => new java.util.AbstractMap.SimpleImmutableEntry(k,v)}.toSet
		)

	override def get(key: Any): V =
		inner(key.asInstanceOf[K])
		
	override def isEmpty(): Boolean = inner.isEmpty

	def keys(): Iterable[K] = inner.keys

	override def keySet(): java.util.Set[K] = setAsJavaSet(inner.keySet)
	
	override def put(key: K, value: V): V =
		try {
			inner.put(key,value)
			value
		} catch {
			case _ : Throwable => null
		}

	override def putAll[K1 <:K, V1 <:V](m: java.util.Map[K1,V1]): Unit =
		for {
			e <- asScalaSet(m.entrySet())
		} yield {
			put(e.getKey, e.getValue)
		}

	override def putIfAbsent(key: K, value: V): V =
		inner.get(key) match {
			case Some(actual) => actual
			case _ => inner.put(key, value); value
		}
	
	override def remove(key: Any): V = 
		inner.remove(key.asInstanceOf[K]).get

	override def remove(key: Any, value: Any): Boolean = 
		try {
			val old = inner(key.asInstanceOf[K])
			if (value.equals(old)) {
				inner.remove(key.asInstanceOf[K]).isDefined
			} else false
		} catch {
			case _ : Throwable => false
		}

	override def replace(key: K, value: V): V =
		try {
			inner(key)
			put(key, value)
			value
		} catch {
			case err : Throwable => null
		}
	
	override def replace(key: K, oldValue: V, newValue: V): Boolean =
		try {
			val old = inner(key)
			assert(oldValue.equals(old))
			put(key, newValue)
			true
		} catch {
			case err : Throwable => false
		}

	override def size() = {
		inner.size
	}

	override def values: java.util.Collection[V] = asJavaCollection(inner.values)

}