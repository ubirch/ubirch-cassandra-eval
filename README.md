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
* It is not clear if it supports statement caching.
* The composability of queries is almost none existing as queries are raw queries.

### How to run

TODO

## Quill Notes

Page: http://getquill.io/

Description: Free/Libre Compile-time Language Integrated Queries for Scala

* It support case classes very nicely. In the case con composed/nested case classes, it offers a simple way to add from/to functions that allow the mapping.
* The schema fields can be customized to support different table names or field names. For example if a field in your case class is name, you can 
customize it to use "_name" against the db. This is a plus.
* Values from the db can be lifted to case classes.
* It support algebraic operators like map, filter, flatMap, etc., which is very nice too.
* Another interesting characteristic which is part of supporting case classes is that returning values can be ad-hoc case classes, this means that 
queries can return values that are not part of the db.
* It supports compiled queries, which could be a factor in performance.
* Something that looks very amazing is the IO Monad. I need to run some examples. But this is definitely, an interesting functional programming support.
* It also support UDTs.
* Something that is important to see is how you can organize your model so that you each model doesn't repeat definition of the Casssandra Context.
  An interesting question is, should we handle the queries inside the case class companion object or inside a DAO class or object.  Both options seem plausible.
* Might be worth looking at runtime or compile DI to handle the Quill Context. In some aspects this is related to a connection pool.

### How to run

TODO
  









 
    


