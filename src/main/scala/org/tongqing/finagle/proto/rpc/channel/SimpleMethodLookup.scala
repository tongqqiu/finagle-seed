package org.tongqing.finagle.proto.rpc.channel

import com.google.protobuf.Service
import org.tongqing.finagle.proto.rpc.Util

import scala.collection.JavaConversions._
import scala.collection.mutable.{Set,HashMap,MultiMap}

/**
 * Maps method names to short codes. This particular implementation uses the trivial String.hashCode.
 */
class SimpleMethodLookup(val methods: java.util.List[String]) extends MethodLookup {

  val codeToNameMulti = new HashMap[Int, Set[String]] with MultiMap[Int, String]
  methods.toList foreach { (it: String) => (codeToNameMulti.getOrElseUpdate(createEncoding(it), Set[String]())) += it }

  // do we have collisions?
  codeToNameMulti foreach { (it: (Int, Set[String])) =>
    if (it._2.size > 1) {
      val sb = new StringBuilder("Collision on ")
      it._2 foreach { it => sb.append(it).append(", ") }
      throw new IllegalArgumentException(sb.toString())
    }
  }

  val codeToName = new HashMap[Int, String]
  codeToNameMulti foreach { (it: (Int, Set[String])) => codeToName(it._1) = it._2.iterator.next() }
  val nameToCode = codeToName map { _.swap }

  def encode(methodName: String): Int = nameToCode.getOrElse(methodName, throw new NoSuchMethodException("Method: " + methodName))

  def lookup(code: Int): String = codeToName.getOrElse(code, throw new NoSuchMethodException("Code: " + code))

  def createEncoding(s: String): Int = {
    s.hashCode()
  }

}

object SimpleMethodLookup {
  def apply(s: Service): SimpleMethodLookup = { new SimpleMethodLookup(Util.extractMethodNames(s)) }
}

