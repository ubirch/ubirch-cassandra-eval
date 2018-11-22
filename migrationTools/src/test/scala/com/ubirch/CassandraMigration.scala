package com.ubirch

import com.datastax.driver.core.{ Cluster, PoolingOptions, SimpleStatement }
import org.scalatest.{ BeforeAndAfterAll, BeforeAndAfterEach, MustMatchers, WordSpec }
import org.scalatest.concurrent.ScalaFutures
import org.cognitor.cassandra.migration.Database
import org.cognitor.cassandra.migration.MigrationRepository
import org.cognitor.cassandra.migration.MigrationTask

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.collection.JavaConverters._

class CassandraMigration extends WordSpec
  with ScalaFutures
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with MustMatchers {

  //Config Object can be passed in to the Context object which is a good thing as specific configs could be used.
  //https://docs.datastax.com/en/developer/java-driver/2.1/manual/pooling/

  implicit val defaultPatience =
    PatienceConfig(timeout = 2.seconds, interval = 50.millis)

  val poolingOptions = new PoolingOptions

  val cluster = Cluster.builder
    .addContactPoint("127.0.0.1")
    .withPort(9042)
    .withPoolingOptions(poolingOptions)
    .build

  val session = cluster.connect()

  override protected def afterAll(): Unit = {
    session.close()
    cluster.close()
  }

  var keyspaceName: String = _

  override def beforeEach(): Unit = {
    keyspaceName = s"migration_tools_${System.nanoTime()}"
    session.execute(
      s"""
         |CREATE KEYSPACE $keyspaceName WITH replication = {
         |  'class': 'SimpleStrategy',
         |  'replication_factor': '1'
         |};
      """.stripMargin)
  }

  override protected def afterEach(): Unit = {
    session.execute(new SimpleStatement(s"DROP KEYSPACE IF EXISTS $keyspaceName;").setReadTimeoutMillis(75000))
  }

  "CassandraMigration" must {

    "run the evolutions in cassandra/migration folder" in {

      val database = new Database(cluster, keyspaceName)
      val migration = new MigrationTask(database, new MigrationRepository)

      migration.migrate()

      val resultSet = session.execute(s"SELECT * FROM ${keyspaceName}.schema_migration;")

      resultSet.asScala
        .zipWithIndex
        .map(x => (x._1, x._2 + 1))
        .foreach {
          case (row, i) =>

            val appliedSuccessfulColumnVal = row.getBool("applied_successful")
            val versionColumnVal = row.getInt("version")

            assert(appliedSuccessfulColumnVal)
            assert(versionColumnVal == i)
        }

    }

  }

}
