package org.tongqing.finagle.proto.rpc.channel

import com.google.protobuf.{Message, Service}
import org.jboss.netty.buffer.ChannelBuffer
import org.jboss.netty.channel.{Channel, ChannelHandlerContext}
import org.jboss.netty.handler.codec.frame.FrameDecoder

class ServerSideDecoder(val repo: MethodLookup, val service: Service) extends FrameDecoder with ProtobufDecoder {

  @throws(classOf[Exception])
  def decode(ctx: ChannelHandlerContext, channel: Channel, buf: ChannelBuffer): Object = {
    decode(ctx, channel, buf, repo)
  }

  def getPrototype(methodName: String): Message = {
    val m = service.getDescriptorForType().findMethodByName(methodName)
    service.getRequestPrototype(m)
  }
}
