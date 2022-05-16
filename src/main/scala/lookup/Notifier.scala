package lookup

import akka.Done

import scala.concurrent.{ExecutionContext, Future}

trait Notifier {
  def notify(result: LookupResult, product: ProductLookup)(implicit ec: ExecutionContext): Future[Done]
}
