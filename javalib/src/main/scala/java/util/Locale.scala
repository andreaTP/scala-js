package java.util

import scala.collection.mutable

class Locale(language: String, country: String, variant: String)
    extends Cloneable with Serializable {

  if ((language eq null) || (country eq null) || (variant eq null))
    throw new NullPointerException()

  def this(language: String, country: String) =
    this(language, country, "")

  def this(language: String) =
    this(language, "", "")

  override def toString(): String =
    Seq(language, country, variant).filterNot(_ == "").mkString("_")
}

object Locale {

  final val ENGLISH = new Locale("en")
  final val FRENCH = new Locale("fr")
  final val GERMAN = new Locale("de")
  final val ITALIAN = new Locale("it")
  final val JAPANESE = new Locale("ja")
  final val KOREAN = new Locale("ko")
  final val CHINESE = new Locale("zh")
  final val SIMPLIFIED_CHINESE = new Locale("zh", "CN")
  final val TRADITIONAL_CHINESE = new Locale("zh", "TW")
  final val FRANCE = new Locale("fr", "FR")
  final val GERMANY = new Locale("de", "DE")
  final val ITALY = new Locale("it", "IT")
  final val JAPAN = new Locale("ja", "JP")
  final val KOREA = new Locale("ko", "KR")
  final val CHINA = new Locale("zh", "CN")
  final val PRC = new Locale("zh", "CN")
  final val TAIWAN = new Locale("zh", "TW")
  final val UK = new Locale("en", "GB")
  final val US = new Locale("en", "US")
  final val CANADA = new Locale("en", "CA")
  final val CANADA_FRENCH = new Locale("fr", "CA")
  final val ROOT = new Locale("")

  final val PRIVATE_USE_EXTENSION = 'x'
  final val UNICODE_LOCALE_EXTENSION = 'u'

}
