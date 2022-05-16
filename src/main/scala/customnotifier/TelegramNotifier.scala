package customnotifier

import akka.Done
import lookup.{LookupResult, Notifier, ProductLookup}
import utils.AppConfig

import java.io.{BufferedInputStream, IOException}
import java.net.URL
import scala.concurrent.{ExecutionContext, Future}

object TelegramNotifier extends Notifier {

  private lazy val apiToken = AppConfig.telegramApiToken

  override def notify(result: LookupResult, product: ProductLookup)(implicit ec: ExecutionContext): Future[Done] = {
    var urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s"
    val msg: String = s"[Searched ${product.name} ${product.url} - has size: ${result.wasFound}] ${result.details}"

    urlString = String.format(urlString, apiToken, AppConfig.telegramChannel, msg)

    try {
      val url = new URL(urlString)
      val conn = url.openConnection
      Future {
        new BufferedInputStream(conn.getInputStream)
      }.map(_ => Done)
    }
  }
}
