package org.tongqing.finagle.proto.rpc.impl

import java.util.concurrent.ExecutorService

import com.google.protobuf.{Message, RpcChannel, RpcController, Service}
import com.twitter.finagle.builder.{ClientBuilder, ServerBuilder}
import org.tongqing.finagle.proto.rpc.{RpcServer, RpcFactory}
import scala.language.reflectiveCalls

class RpcFactoryImpl extends RpcFactory {

   def createServer(sb: ServerBuilder[(String, Message), (String, Message), Any, Any, Any], port: Int, service: Service, executorService: ExecutorService): RpcServer =
     new RpcServerImpl(sb, port, service, executorService)

   def createStub[T <: Service](cb: ClientBuilder[(String, Message), (String, Message), Any, Any, Any], service: { def newStub(c: RpcChannel): T }, executorService: ExecutorService): T = {
     service.newStub(new RpcChannelImpl(cb, service.asInstanceOf[T], executorService))
   }

   def createController(): RpcController = { new org.tongqing.finagle.proto.rpc.RpcControllerWithOnFailureCallback() }

   def release(stub: { def getChannel(): RpcChannel }) {
   stub.getChannel().asInstanceOf[RpcChannelImpl].release()
   }
 }
