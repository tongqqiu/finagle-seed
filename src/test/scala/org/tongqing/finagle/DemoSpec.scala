package org.tongqing.finagle

/**
 * Author: TQui 
 * CreatedDate: 10/20/14.
 */

import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.MustMatchers
import org.tongqing.thriftscala.User

@RunWith(classOf[JUnitRunner])
class DemoSpec extends WordSpec with MustMatchers {
  def printUser(user: User) {println("User %s, id %d".format(user.name, user.id))}

  "generated finagle service" should {
    "server and client" in {
      val server = DemoServer.buildServer()
      val client = DemoClient.buildClient(server.localAddress)
      client.createUser("Tyrion")().name must be("Tyrion")
      client.createUser("Jon")().name must be("Jon")

    }

  }
}

