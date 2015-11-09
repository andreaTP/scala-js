/*                     __                                               *\
**     ________ ___   / /  ___      __ ____  Scala.js Test Suite        **
**    / __/ __// _ | / /  / _ | __ / // __/  (c) 2013, LAMP/EPFL        **
**  __\ \/ /__/ __ |/ /__/ __ |/_// /_\ \    http://scala-js.org/       **
** /____/\___/_/ |_/____/_/ | |__/ /____/                               **
**                          |/____/                                     **
\*                                                                      */
package org.scalajs.testsuite.javalib.util.concurrent.locks

import scala.scalajs.js

import org.scalajs.jasminetest.JasmineTest

object ReentrantLockTest extends JasmineTest {

  describe("java.util.concurrent.locks.ReentrantLock") {

    it("should have all the normal operations") {
      val lock = new java.util.concurrent.locks.ReentrantLock()
      expect(lock.isFair).toBeFalsy
      expect(lock.isLocked).toBeFalsy
      lock.lock()
      expect(lock.isLocked).toBeTruthy
      lock.unlock()
      expect(lock.isLocked).toBeFalsy
      val lock2 = new java.util.concurrent.locks.ReentrantLock(true)
      expect(lock2.isFair).toBeTruthy
    }
  }
}
