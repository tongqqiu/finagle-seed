package org.tongqing.finagle.proto.rpc.channel

import com.google.protobuf.{Service, Message}
import com.twitter.finagle.{Codec, CodecFactory}
import org.jboss.netty.channel.{ChannelPipelineFactory, Channels}

class ProtoBufCodec(val service: Service) extends CodecFactory[(String, Message), (String, Message)] {

  val maxFrameSize = 1024 * 1024

  val repo = SimpleMethodLookup(service)

  def server = Function.const {
    new Codec[(String, Message), (String, Message)] {
      def pipelineFactory = new ChannelPipelineFactory {
        def getPipeline = {
          val pipeline = Channels.pipeline()
          pipeline.addLast("decoder", new ServerSideDecoder(repo, service))
          pipeline.addLast("encoder", new CustomProtobufEncoder(repo));
          pipeline
        }
      }
    }
  }

  def client = Function.const {
    new Codec[(String, Message), (String, Message)] {
      def pipelineFactory = new ChannelPipelineFactory {
        def getPipeline = {
          val pipeline = Channels.pipeline()
          pipeline.addLast("encoder", new CustomProtobufEncoder(repo))
          pipeline.addLast("decoder",
            new ClientSideDecoder(repo, service))
          pipeline
        }
      }
    }
  }

}
