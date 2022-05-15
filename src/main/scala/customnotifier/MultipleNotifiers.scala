package customnotifier

import lookup.{LookupResult, Notifier, ProductLookup}

case class MultipleNotifiers(notifiers: List[Notifier]) extends Notifier {
  override def notify(result: LookupResult, product: ProductLookup): Unit = {
    notifiers.foreach(_.notify(result, product))
  }
}
