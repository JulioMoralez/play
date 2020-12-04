package modules

import actors.PaymentReader
import com.google.inject.AbstractModule
import controllers.ConfiguredActor
import play.api.libs.concurrent.AkkaGuiceSupport

class MyModule extends AbstractModule with AkkaGuiceSupport {
  override def configure(): Unit = {
    bindActor[ConfiguredActor]("configured-actor")
    bindActor[PaymentReader]("payment-reader")
  }
}
