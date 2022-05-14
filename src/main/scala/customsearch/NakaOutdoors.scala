package customsearch

import lookup.{LookupCriteria, LookupResult, ProductLookup}
import net.ruippeixotog.scalascraper.model.{Document, Element, TextNode}
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList
import net.ruippeixotog.scalascraper.dsl.DSL.deepFunctorOps
import net.ruippeixotog.scalascraper.dsl.DSL._

object NakaOutdoors {
  val boulderShoeSizeCriteria: NakaOutDoorsSearchBySize = NakaOutDoorsSearchBySize(size => size >= 39.5 && size <= 40.5)

  val interestedProducts = List(
    ProductLookup("kubo", "https://www.nakaoutdoors.com.ar/8242-la-sportiva-kubo", boulderShoeSizeCriteria),
    ProductLookup("solution", "https://www.nakaoutdoors.com.ar/5772-la-sportiva-solution", boulderShoeSizeCriteria),
    ProductLookup("skwama", "https://www.nakaoutdoors.com.ar/6887-la-sportiva-skwama", boulderShoeSizeCriteria),
    ProductLookup("theory", "https://www.nakaoutdoors.com.ar/7873-la-sportiva-theory", boulderShoeSizeCriteria)
  )
}

case class NakaOutDoorsSearchBySize(criteria: Double => Boolean) extends LookupCriteria {
  override def search(doc: Document): LookupResult = {
    val elements: List[Element] = doc >> elementList(".ns-cart-select-txt-option")
    val availableSizes: List[Double] = elements.flatMap { elem =>
      elem.childNodes.collectFirst { case TextNode(content) => content.trim.toDoubleOption }.flatten
    }
    LookupResult(availableSizes.exists(criteria), s"Available sizes: ${availableSizes.mkString(", ")}")
  }
}