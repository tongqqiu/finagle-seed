package org.tongqing.finagle

import com.twitter.finagle.Thrift
import com.twitter.util.{Await, Future}
import org.tongqing.thriftscala.Hello

/**
 * Author: TQui 
 * CreatedDate: 10/20/14.
 */
object ThriftServer {
  def main(args: Array[String]) {
    //#thriftserverapi
    val server = Thrift.serveIface("localhost:8081", new Hello[Future] {
      def hi() = {
        println("server hi called")
        Future.value("hi")
      }
    })
    Await.ready(server)
    //#thriftserverapi
  }
}
