package lookup

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document


case class ProductLookupRunner(products: List[ProductLookup], notifier: Notifier) {
  private val browser = JsoupBrowser()

  def run(): Unit = {
    products.foreach { lookup =>
      val doc: Document = browser.get(lookup.url)
      val result = lookup.criteria.search(doc)
      notifier.notify(result, lookup)
    }
  }
}

trait Notifier {
  def notify(result: LookupResult, product: ProductLookup): Unit
}
