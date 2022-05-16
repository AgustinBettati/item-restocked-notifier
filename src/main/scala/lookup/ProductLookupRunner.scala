package lookup

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

import scala.util.{Failure, Success}

object ProductLookupRunner {

  sealed trait RunnerCommand
  case class StartLookupCommand(product: ProductLookup) extends RunnerCommand
  private case class NotificationSuccessful(product: ProductLookup) extends RunnerCommand
  private case class NotificationFailed(result: LookupResult, product: ProductLookup, lastError: Throwable, retryCount: Int = 0) extends RunnerCommand

  def apply(notifier: Notifier): Behavior[RunnerCommand] = {
    Behaviors.setup { ctx =>
      val browser = JsoupBrowser()

      Behaviors.receiveMessage {
        case StartLookupCommand(product) =>
          ctx.log.info(s"Starting lookup for ${product.name}")
          val doc: Document = browser.get(product.url)
          val result = product.criteria.search(doc)

          val notified = notifier.notify(result, product)(ctx.executionContext) // could use dedicated dispatcher for sending notifications
          ctx.pipeToSelf(notified) {
            case Success(_) => NotificationSuccessful(product)
            case Failure(e) => NotificationFailed(result, product, e)
          }
          Behaviors.same
        case NotificationSuccessful(productLookup) =>
          ctx.log.info(s"Notification successful for ${productLookup.name}")
          Behaviors.same
        case NotificationFailed(result, product, exception, retryCount) =>
          // TODO: implement retry of notification when we have to notify a successful lookup
          ctx.log.info(s"Notification failed for ${product.name}", exception)
          Behaviors.same
      }
    }
  }
}


