package org.tongqing.finagle.proto.rpc

import java.util

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

import com.google.common.collect.Lists
import com.google.protobuf.Descriptors.MethodDescriptor
import com.google.protobuf.Service


object Util {


	def extractMethodNames(s: Service) : util.List[String] = {

		return Lists.transform(s.getDescriptorForType().getMethods(),
				new com.google.common.base.Function[MethodDescriptor, String]() {

					@Override
					def apply(d: MethodDescriptor) : String = {
						return d.getName()
					}
				})
	}

}
