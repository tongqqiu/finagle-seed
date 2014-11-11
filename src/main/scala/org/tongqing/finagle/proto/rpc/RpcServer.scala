package org.tongqing.finagle.proto.rpc

import com.twitter.util.Duration

trait RpcServer {

  def close(d: Duration): Unit;

}
