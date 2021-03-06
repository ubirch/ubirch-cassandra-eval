package com.ubirch

import java.net.InetSocketAddress

import com.datastax.driver.core.Cluster.Builder
import com.typesafe.config.ConfigFactory
import io.getquill.context.cassandra.cluster.ClusterBuilder
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpec }
import org.scalatest.concurrent.ScalaFutures

class ClusterBuilderSpec
  extends WordSpec
  with ScalaFutures
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with MustMatchers {

  val hosts = List("127.0.0.1", "127.0.0.2", "127.0.0.3")
  val contactPoints = hosts.map(new InetSocketAddress(_, 9042))

  "creates Builder" must {

    "with a single host" in {
      val cfgString = s"contactPoint = ${hosts.head}"
      val clusterBuilder: Builder = ClusterBuilder(ConfigFactory.parseString(cfgString))
      clusterBuilder.getContactPoints must contain theSameElementsAs (contactPoints.take(1))
    }

    "with a single host in an array" in {
      val cfgString = s"contactPoints = [${hosts.head}]"
      val clusterBuilder: Builder = ClusterBuilder(ConfigFactory.parseString(cfgString))
      clusterBuilder.getContactPoints must contain theSameElementsAs (contactPoints.take(1))

    }

    "with multiple hosts" in {
      val cfgString = s"""contactPoints = [${hosts.mkString(",")}] """
      val clusterBuilder: Builder = ClusterBuilder(ConfigFactory.parseString(cfgString))
      clusterBuilder.getContactPoints must contain theSameElementsAs (contactPoints)
    }
  }
}