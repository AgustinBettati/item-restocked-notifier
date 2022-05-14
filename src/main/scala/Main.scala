import ExecutionActor.ProductsLookupCommand
import akka.actor.typed.{ActorSystem, Behavior, SupervisorStrategy}
import akka.actor.typed.scaladsl.Behaviors
import customnotifier.TelegramNotifier
import customsearch.NakaOutdoors
import lookup.{ProductLookup, ProductLookupRunner}
import org.slf4j.event.Level
import utils.AppConfig

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt

object Main {
  def main(args: Array[String]): Unit = {
    val telegramApiToken: String = args(0)
    schedulePeriodicExecution(telegramApiToken)
  }

  def schedulePeriodicExecution(telegramApiToken: String): Unit = {
    val system: ActorSystem[ProductsLookupCommand] = ActorSystem(ExecutionActor(telegramApiToken), "product-lookup-actor")
    implicit val ec: ExecutionContextExecutor = system.executionContext

    val nakaProducts: List[ProductLookup] = NakaOutdoors.interestedProducts
    system.scheduler.scheduleAtFixedRate(0.seconds, AppConfig.lookupInterval)(() => {
      system.log.info("sending scheduled lookup command")
      system ! ProductsLookupCommand(nakaProducts)
    })
  }
}

object ExecutionActor {

  case class ProductsLookupCommand(products: List[ProductLookup])

  def apply(telegramApiToken: String): Behavior[ProductsLookupCommand] = {
    Behaviors.supervise[ProductsLookupCommand](
      Behaviors.receiveMessage {
        c: ProductsLookupCommand =>
          ProductLookupRunner(c.products, TelegramNotifier(telegramApiToken)).run()
          Behaviors.same
      }
    ).onFailure(SupervisorStrategy.restart.withLogLevel(Level.ERROR)) // make sure application does not terminate on error.
  }
}


