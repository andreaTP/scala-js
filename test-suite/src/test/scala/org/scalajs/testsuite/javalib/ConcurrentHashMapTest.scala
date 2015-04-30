package org.scalajs.testsuite.javalib

import language.implicitConversions

import scala.language.implicitConversions

import org.scalajs.jasminetest.JasmineTest


object ConcurrentHashMapTest extends JasmineTest {

  // Just in here, we allow ourselves to do this
  //implicit def array2jsArray[T](arr: Array[T]): js.Array[T] = arr.toJSArray

  /** Overridden by typedarray tests */
  //def Array[T : ClassTag](v: T*): scala.Array[T] = scala.Array(v: _*)

  /** Overridden by typedarray tests */
  def testBody(suite: => Unit) = describe("java.util.concurrent.ConcurrentHashMap")(suite)


  testBody {

    it("should store elements") {
      val chm = new java.util.concurrent.ConcurrentHashMap[String,String]()

	  assert{chm != null}

	  assert{chm.TEST != null }

	  expect(chm.size()).toEqual(0)
    }

   }

}