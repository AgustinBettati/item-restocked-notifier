import akka.actor.typed.{ActorSystem, Behavior, SupervisorStrategy}
import akka.actor.typed.scaladsl.Behaviors
import customnotifier.{MultipleNotifiers, SuccessEmailNotifier, TelegramNotifier}
import customsearch.NakaOutdoors
import lookup.ProductLookupRunner.{RunnerCommand, StartLookupCommand}
import lookup.{ProductLookup, ProductLookupRunner}
import org.slf4j.event.Level
import utils.AppConfig

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt

object Main {
  def main(args: Array[String]): Unit = schedulePeriodicExecution()

  def schedulePeriodicExecution(): Unit = {
    val actor = Behaviors.supervise[RunnerCommand] {
      val notifier = MultipleNotifiers(List(TelegramNotifier, SuccessEmailNotifier))
      ProductLookupRunner(notifier)
    }.onFailure(SupervisorStrategy.restart.withLogLevel(Level.ERROR)) // make sure main actor does not terminate on error.

    val system: ActorSystem[StartLookupCommand] = ActorSystem(actor, "product-lookup-actor")
    implicit val ec: ExecutionContextExecutor = system.executionContext

    val nakaProducts: List[ProductLookup] = NakaOutdoors.interestedProducts
    system.scheduler.scheduleAtFixedRate(0.seconds, AppConfig.lookupInterval)(() => {
      nakaProducts.foreach{ productLookup =>
        system ! StartLookupCommand(productLookup)
      }
    })
  }
}



