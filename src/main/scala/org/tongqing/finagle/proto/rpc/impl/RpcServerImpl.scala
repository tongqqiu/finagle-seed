package org.tongqing.finagle.proto.rpc.impl

import java.net.InetSocketAddress
import java.util.concurrent.ExecutorService

import com.google.protobuf.{Message, RpcCallback, Service}
import com.twitter.finagle.builder.{Server, ServerBuilder}
import com.twitter.util.{Duration, FuturePool}
import org.tongqing.finagle.proto.rpc.{RpcServer, channel}

class RpcServerImpl(sb: ServerBuilder[(String, Message), (String, Message), Any, Any, Any], port: Int, service: Service, executorService: ExecutorService) extends RpcServer {

   private val futurePool = FuturePool(executorService)

   private val server: Server = ServerBuilder.safeBuild(ServiceDispatcher(service, futurePool),
     sb.codec(new channel.ProtoBufCodec(service))
       .name(getClass().getName())
       .bindTo(new InetSocketAddress(port)))

   def close(d: Duration) = {
     server.close(d)
   }
 }

class ServiceDispatcher(service: com.google.protobuf.Service, futurePool: FuturePool) extends com.twitter.finagle.Service[(String, Message), (String, Message)] {

   //private val log = LoggerFactory.getLogger(getClass)

   def apply(request: (String, Message)) = {

     val methodName = request._1
     val reqMessage = request._2

     val m = service.getDescriptorForType().findMethodByName(methodName);
     if (m == null) {
       throw new java.lang.AssertionError("Should never happen, we already decoded " + methodName)
     }

     // dispatch to the service method
     val task = {
       var respMessage: Message = null
       service.callMethod(m, null, reqMessage, new RpcCallback[Message]() {

         def run(msg: Message) = {
           respMessage = msg;
         }
       })
       if (respMessage == null) throw new RuntimeException("Service Response message is required.")
       (methodName, respMessage)
     }
     futurePool(task)
   }
 }

object ServiceDispatcher {
   def apply(service: com.google.protobuf.Service, futurePool: FuturePool): ServiceDispatcher = { new ServiceDispatcher(service, futurePool) }
 }
