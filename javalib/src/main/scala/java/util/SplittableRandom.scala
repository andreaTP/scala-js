package java.util

/*
 * This is a clean room implementation derived from the original paper
 * and Java implementation mentioned there:
 *
 *Fast Splittable Pseudorandom Number Generators
 * by Guy L. Steele Jr., Doug Lea, Christine H. Flood
 * http://gee.cs.oswego.edu/dl/papers/oopsla14.pdf
 *
 */
private object SplittableRandom {

  private final val DOUBLE_ULP = 1.0 / (1L << 53)
  private final val GOLDEN_GAMMA = 0x9e3779b97f4a7c15L // odd

  //switched from original comparing result of JVM std lib
  private final def mix64variant13(_z: Long): Long = {
  //def mix64(_z: Long): Long = {
    var z = _z
    z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL
    z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L
    z ^ (z >>> 33)
  }

  // changed from original trying and comparing result on the JVM for all the numbers mentioned here:
  // http://zimbry.blogspot.pt/2011/09/better-bit-mixing-improving-on.html
  // reference was:
  //def mix32(_z: Long): Int = {
  //  var z = _z
  //  z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL
  //  z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L
  //  (z >>> 32).toInt
  //}
  // constant here comes from Mix04
  private final def mix32(_z: Long): Int = {
    var z = _z
    z = (z ^ (z >>> 33)) * 0x62a9d9ed799705f5L
    z = (z ^ (z >>> 28)) * 0xcb24d0a5c88c35b3L
    (z >>> 32).toInt
  }

  def mix64(_z: Long): Long = {
  //def mix64variant13(_z: Long): Long = {
    var z = _z
    z = (z ^ (z >>> 30)) * 0xbf58476d1ce4e5b9L
    z = (z ^ (z >>> 27)) * 0x94d049bb133111ebL
    z ^ (z >>> 31)
  }

  private final def mix64variantMurmurHash3(_z: Long): Long = {
    var z = _z
    z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL
    z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L
    z ^ (z >>> 33)
  }

  private final def mixGamma(_z: Long) = {
    var z = _z
    // original: z = mix64variant13(z) | 1L
    // changed from original version
    z = mix64variant13(z) | 1L
    val n = java.lang.Long.bitCount(z ^ (z >>> 1));
    // Reference implementation is wrong since after they mention:
    //
    // ... Therefore we require that the number of such
    // pairs, as computed by Long.bitCount(z ^ (z >>> 1)),
    // exceed 24; if it does not, then the candidate z is replaced by
    // the XOR of z and 0xaaaaaaaaaaaaaaaaL ...
    //
    // so we invert the condition:
    //if (n >= 24) z ^= 0xaaaaaaaaaaaaaaaaL
    if (n < 24) z ^= 0xaaaaaaaaaaaaaaaaL

    z // This result is always odd.
  }

}

final class SplittableRandom private (var seed: Long, gamma: Long) {
  import SplittableRandom._

  private lazy val defaultGen = new Random()

  def this(seed: Long) = {
    this(seed, SplittableRandom.GOLDEN_GAMMA)
  }

  private def this(ll: (Long, Long)) = this(ll._1, ll._2)

  def this() = { // Preferred
    this({
      val s =
        new Random().nextLong() + (2 * SplittableRandom.GOLDEN_GAMMA)

        (SplittableRandom.mix64(s),
        SplittableRandom.mixGamma(s + SplittableRandom.GOLDEN_GAMMA))
    })
  }

  def split(): SplittableRandom = {
    new SplittableRandom(
      mix64(nextSeed()),
      mixGamma(nextSeed())
    )
  }

  private def nextSeed() = {
    seed += gamma
    seed
  }

  def nextInt(): Int = mix32(nextSeed())

  //def nextInt(bound: Int): Int

  //def nextInt(origin: Int, bound: Int): Int

  def nextLong(): Long = mix64(nextSeed())

  //def nextLong(bound: Long): Long

  //def nextLong(origin: Long, bound: Long): Long

  def nextDouble(): Double =
    (nextLong() >>> 11).toDouble * DOUBLE_ULP

  //def nextDouble(bound: Double): Double

  //def nextDouble(origin: Double, bound: Double): Double =

  // this should be properly tested
  // looks to work but just by chance maybe
  def nextBoolean(): Boolean = nextInt < 0

}
