/*                     __                                               *\
**     ________ ___   / /  ___      __ ____  Scala.js Test Suite        **
**    / __/ __// _ | / /  / _ | __ / // __/  (c) 2013-2015, LAMP/EPFL   **
**  __\ \/ /__/ __ |/ /__/ __ |/_// /_\ \    http://scala-js.org/       **
** /____/\___/_/ |_/____/_/ | |__/ /____/                               **
**                          |/____/                                     **
\*                                                                      */
package org.scalajs.testsuite.javalib.util

import java.{util => ju}

import scala.reflect.ClassTag

class IdentityHashMapTest extends LinkedHashMapTest {
  override def factory(): IdentityHashMapFactory = new IdentityHashMapFactory
}

object IdentityHashMapFactory {
  def allFactories: Iterator[MapFactory] =
    Iterator(new IdentityHashMapFactory) ++ LinkedHashMapFactory.allFactories
}

class IdentityHashMapFactory extends AbstractMapFactory {
  override def implementationName: String =
    "java.util.IdentityHashMap"

  override def empty[K: ClassTag, V: ClassTag]: ju.IdentityHashMap[K, V] =
    new ju.IdentityHashMap[K, V]

  def allowsNullKeys: Boolean = true
  def allowsNullValues: Boolean = true
}
