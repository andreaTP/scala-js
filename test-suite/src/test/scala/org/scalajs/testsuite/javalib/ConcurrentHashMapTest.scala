package org.scalajs.testsuite.javalib

import language.implicitConversions

import scala.language.implicitConversions

import org.scalajs.jasminetest.JasmineTest

object ConcurrentHashMapTest extends JasmineTest {

  describe("java.util.concurrent.ConcurrentHashMap") {

    it("should store strings") {
      val chm = new java.util.concurrent.ConcurrentHashMap[String, String]()
      assert { chm != null }

      expect(chm.size()).toEqual(0)
      chm.put("ONE", "one")
      expect(chm.size()).toEqual(1)
      expect(chm.get("ONE")).toEqual("one")
      chm.put("TWO", "two")
      expect(chm.size()).toEqual(2)
      expect(chm.get("TWO")).toEqual("two")
    }

    it("should store integers") {
      val chm = new java.util.concurrent.ConcurrentHashMap[Int, Int]()
      assert { chm != null }

      chm.put(1, 1)
      expect(chm.size()).toEqual(1)
      val one = chm.get(1)
      expect(one).toEqual(1)
    }

    it("should store custom objects") {
      case class TestObj(num: Int)

      val chm = new java.util.concurrent.ConcurrentHashMap[TestObj, TestObj]()
      assert { chm != null }

      chm.put(TestObj(1), TestObj(1))
      expect(chm.size()).toEqual(1)
      val one = chm.get(TestObj(1))
      expect(one.num).toEqual(1)
    }

    it("should remove stored elements") {
      val chm = new java.util.concurrent.ConcurrentHashMap[String, String]()
      assert { chm != null }

      chm.put("ONE", "one")
      expect(chm.size()).toEqual(1)
      expect(chm.remove("ONE")).toEqual("one")
      val newOne = chm.get("ONE")
      expect(chm.get("ONE")).toBeNull
    }

    it("should behave like expected on every method") {
      val chm = new java.util.concurrent.ConcurrentHashMap[String, String]()
      assert { chm != null }
      chm.put("ONE", "one")
      expect(chm.size()).toEqual(1)
      chm.clear
      expect(chm.size()).toEqual(0)
      expect(chm.isEmpty()).toEqual(true)

      chm.put("ONE", "one")
      expect(chm.containsKey("ONE")).toEqual(true)
      expect(chm.containsKey("TWO")).toEqual(false)

      expect(chm.containsValue("one")).toEqual(true)
      expect(chm.containsValue("two")).toEqual(false)

      expect(chm.elements.nextElement()).toEqual("one")

      expect(chm.values.iterator.next()).toEqual("one")
      expect(chm.values.size).toEqual(1)

      expect(chm.keys().nextElement()).toEqual("ONE")

      expect({
        val entry = chm.entrySet().iterator.next()
        entry.getKey + entry.getValue
      }).toEqual("ONEone")

      import scala.collection.JavaConversions._
      import scala.collection.mutable
      try {
        val m = mutable.Map[String, String](
          "X" -> "y"
        )
        chm.putAll(mutableMapAsJavaMap(m))
      } catch {
        case err: Throwable => err.printStackTrace
      }

      expect(chm.get("X")).toEqual("y")

      expect(chm.replace("ONE", "two")).toEqual("two")
      expect(chm.get("ONE")).toEqual("two")

      expect(chm.replace("ONE", "one", "two")).toEqual(false)
      expect(chm.replace("ONE", "two", "one")).toEqual(true)
      expect(chm.get("ONE")).toEqual("one")

    }
  }

}
