# ubirch-cassandra-eval
Evaluation of Cassandra and Scala

## Alpakka-Cassandra Connector Notes

Page: https://developer.lightbend.com/docs/alpakka/current/cassandra.html

Description: The Cassandra connector allows you to read and write to Cassandra. You can query a stream of rows from CassandraSource or use prepared statements to insert or update with CassandraFlow or CassandraSink.

* Akka Streams are first-class citizens.
* Under the hood, it uses [https://github.com/datastax/java-driver](https://github.com/datastax/java-driver) as the driver.
* There's no implicit support for binding case classes when building the db statements. 
  This means that every case class must be explicitly mapped into the bind function.
  So, in the case of complex case classes and cassandra tables, the maintenance of the bind function can be cumbersome and brittle.
* If the principal data was in a _Json_ format, and only the fields that would be used for querying are part of the case class,
  then the process should be easier and better controlled.
* Something in line with the previous point is that the db statements are raw queries, which again, are prone to maintenance errors.



 
    


