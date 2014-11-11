package org.tongqing.finagle.proto.rpc

import java.util.concurrent.ExecutorService

import com.google.protobuf.{Message, RpcChannel, RpcController, Service}
import com.twitter.finagle.builder.{ClientBuilder, ServerBuilder}

trait RpcFactory {

  def createServer(sb: ServerBuilder[(String, Message), (String, Message), Any, Any, Any], port: Int, service: Service, executorService: ExecutorService): RpcServer

  def createStub[T <: Service](cb: ClientBuilder[(String, Message), (String, Message), Any, Any, Any], service: { def newStub(c: RpcChannel): T }, executorService: ExecutorService): T

  def createController(): RpcController

  def release(stub: { def getChannel(): RpcChannel })

}
