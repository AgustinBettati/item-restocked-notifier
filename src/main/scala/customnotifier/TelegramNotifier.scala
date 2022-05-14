package customnotifier

import lookup.{LookupResult, Notifier, ProductLookup}
import utils.AppConfig

import java.io.{BufferedInputStream, IOException}
import java.net.URL

case class TelegramNotifier(apiToken: String) extends Notifier {


  override def notify(result: LookupResult, product: ProductLookup): Unit = {
    var urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s"
    val msg: String = s"[Searched ${product.name} ${product.url} - has size: ${result.wasFound}] ${result.details}"

    urlString = String.format(urlString, apiToken, AppConfig.telegramChannel, msg)

    try {
      val url = new URL(urlString)
      val conn = url.openConnection
      new BufferedInputStream(conn.getInputStream)
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }
}
