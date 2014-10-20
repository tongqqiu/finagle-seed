package org.tongqing.finagle

import com.twitter.finagle.Thrift
import org.tongqing.thriftscala.Hello

/**
 * Author: TQui 
 * CreatedDate: 10/20/14.
 */
object ThriftClient {
  def main(args: Array[String]) {
    //#thriftclientapi
    val client = Thrift.newIface[Hello.FutureIface]("localhost:8081")
    client.hi() onSuccess { response =>
      println("Received response: " + response)
    }
    //#thriftclientapi
  }
}
