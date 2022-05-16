package customnotifier

import akka.Done
import lookup.{LookupResult, Notifier, ProductLookup}

import scala.concurrent.{ExecutionContext, Future}

case class MultipleNotifiers(notifiers: List[Notifier]) extends Notifier {
  override def notify(result: LookupResult, product: ProductLookup)(implicit ec: ExecutionContext): Future[Done] = {
    Future.sequence(notifiers.map(_.notify(result, product))).map(_ => Done)
  }
}
