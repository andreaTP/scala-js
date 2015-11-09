package java.util.concurrent.locks

class ReentrantLock(private[this] val fair: Boolean) {

  private var locked = false

  def this() = this(false)

  def lock(): Unit = locked = true

  def unlock(): Unit = locked = false

  def isLocked(): Boolean = locked

  final def isFair: Boolean = fair
}
