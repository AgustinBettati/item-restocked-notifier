package customnotifier

import lookup.{LookupResult, Notifier, ProductLookup}
import courier._
import akka.Done
import utils.AppConfig

import javax.mail.internet.InternetAddress
import scala.concurrent.{ExecutionContext, Future}

object SuccessEmailNotifier extends Notifier {

  override def notify(result: LookupResult, product: ProductLookup)(implicit ec: ExecutionContext): Future[Done] = {
    if (result.wasFound) {
      val mailer = Mailer("smtp.gmail.com", 587)
        .auth(true)
        .as(AppConfig.gmailSenderAccount, AppConfig.gmailSenderPass)
        .startTls(true)()
      mailer(Envelope.from(new InternetAddress(s"item-restocked@gmail.com"))
        .to(AppConfig.emailReceivers.map(new InternetAddress(_)):_*)
        .subject(s"[item-restocked-notifier] ${product.name} has arrived!")
        .content(Text(s"${product.url} - sizes: ${result.details}"))).map(_ => Done)
    }
    else Future.successful(Done)
  }
}
