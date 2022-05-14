package lookup

import net.ruippeixotog.scalascraper.model.Document

case class ProductLookup(name: String, url: String, criteria: LookupCriteria)

trait LookupCriteria {
  def search(doc: Document): LookupResult
}
case class LookupResult(wasFound: Boolean, details: String)