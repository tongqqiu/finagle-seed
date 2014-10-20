package org.tongqing.finagle

import java.net.{InetSocketAddress, SocketAddress}
import java.util.concurrent.atomic.AtomicInteger

import com.twitter.finagle.builder.{ClientBuilder, ServerBuilder}
import com.twitter.finagle.stats.DefaultStatsReceiver
import com.twitter.finagle.thrift.{ThriftClientFramedCodec, ThriftServerFramedCodec}
import com.twitter.finagle.zipkin.thrift.ZipkinTracer
import com.twitter.util.Future
import org.apache.thrift.protocol.TBinaryProtocol
import org.tongqing.thriftscala.{User, UserService}

object DemoClient {
  def buildClient(address: SocketAddress) = {
    val clientService = ClientBuilder()
      .hosts(Seq(address))
      .codec(ThriftClientFramedCodec())
      .hostConnectionLimit(1)
      .name("user_service_client")
      .tracer(ZipkinTracer.mk( host = "localhost", port = 9410, DefaultStatsReceiver, 1 ))
      .build()
    new UserService.FinagledClient(clientService)
  }
}

object DemoServer {
  private val userIdCounter: AtomicInteger = new AtomicInteger(0)
  val tracer = ZipkinTracer.mk( host = "localhost", port = 9410, DefaultStatsReceiver, 1 )
  // need to provide an implementation for the finagle service
  class MyUserImpl extends UserService.FutureIface {
    def createUser(name: String): Future[User] = {
      val id = userIdCounter.incrementAndGet()
      Future.value(User(id, name))
    }
  }

  def buildServer() = {
    val protocol = new TBinaryProtocol.Factory()
    val serverService = new UserService.FinagledService(new MyUserImpl, protocol)
    ServerBuilder()
      .codec(ThriftServerFramedCodec())
      .bindTo(new InetSocketAddress(0))
      .name("user_service")
      .tracer(tracer)
      .build(serverService)
  }
}