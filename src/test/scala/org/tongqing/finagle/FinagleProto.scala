import java.util.concurrent._
import java.util.concurrent.atomic._

import Protouser.ProtoUserService
import com.google.protobuf.{Message, RpcController, RpcCallback, RpcChannel}
import com.twitter.finagle.builder.{ClientBuilder, ServerBuilder}
import com.twitter.util.Duration
import org.tongqing.finagle.proto.rpc.RpcControllerWithOnFailureCallback
import org.tongqing.finagle.proto.rpc.impl.RpcFactoryImpl

class RpcProtobufSpec {
  def CLIENT_TIMEOUT_SECONDS = 1
  def THREAD_COUNT = 40
  def REQ_PER_THREAD = 100
  def port = 8080
  def executorService = Executors.newFixedThreadPool(4)
  def factory = new RpcFactoryImpl()

  def serverBuilder = ServerBuilder.get().maxConcurrentRequests(10)

  def clientBuilder = ClientBuilder
    .get()
    .hosts(String.format("localhost:%s", port.toString()))
    .hostConnectionLimit(1)
    .retries(2)
    .requestTimeout(Duration(CLIENT_TIMEOUT_SECONDS, TimeUnit.SECONDS))
    val totalRequests = new AtomicInteger()
    val service = new SampleUserServiceImpl(80, null)
    val server = factory.createServer(serverBuilder.asInstanceOf[ServerBuilder[(String, Message), (String, Message), Any, Any, Any]],
      port,
      service,
      executorService)
    val stub = factory.createStub(clientBuilder.asInstanceOf[ClientBuilder[(String, Message), (String, Message), Any, Any, Any]],
      ProtoUserService.newStub(null).asInstanceOf[ {def newStub(c: RpcChannel): ProtoUserService}],
      executorService)

    val finishBarrier = new CyclicBarrier(THREAD_COUNT + 1)
    val startBarrier = new CyclicBarrier(THREAD_COUNT)

    for (i <- 0 until THREAD_COUNT) {
      new Thread(new Runnable() {
        def run() {
          startBarrier.await();
          try {
            for (k <- 0 until REQ_PER_THREAD) {
              makeRequest(service, stub, totalRequests)
            }
          }
          finally {
            finishBarrier.await(60l, TimeUnit.SECONDS)
          }
        }
      }).start()
    }

      finishBarrier.await(60l, TimeUnit.SECONDS)
      server.close(Duration(1, TimeUnit.SECONDS))
      executorService.shutdown()
      println("Total requests" + totalRequests.get())


  def makeRequest(service: SampleUserServiceImpl, stub: ProtoUserService, totalRequests: AtomicInteger) {
    val controller = factory.createController().asInstanceOf[RpcControllerWithOnFailureCallback]

    val l = new java.util.concurrent.CountDownLatch(1);
    val request = Protouser.ProtoUser.newBuilder().setName("Tony").build()
    stub.createUser(controller.onFailure(new RpcCallback[Throwable]() {

      def run(e: Throwable) {
        l.countDown()
      }
    }), request, new RpcCallback[Protouser.ProtoUser]() {

      def run(response: Protouser.ProtoUser) {
        totalRequests.incrementAndGet()
        println(response.getId + "|" + response.getName)
        l.countDown()
      }
    });

    l.await(CLIENT_TIMEOUT_SECONDS + 2, TimeUnit.SECONDS)
  }
}

class SampleUserServiceImpl(val name: Int, val getHistoricWeather: Callable[Any]) extends ProtoUserService {

  private val userIdCounter: AtomicInteger = new AtomicInteger(0)

  def createUser(controller: RpcController, request: Protouser.ProtoUser, done:
  RpcCallback[Protouser.ProtoUser]) {
    done.run(request.newBuilderForType().setId( userIdCounter.incrementAndGet()).setName(request.getName).build())
  }

}

object Test {
  def main(args: Array[String]) {
    val spec = new RpcProtobufSpec()
    System.exit(1)
  }
}