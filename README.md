# ubirch-cassandra-eval
Evaluation of Cassandra and Scala

## Getting Started

* [Apache Cassandra](http://cassandra.apache.org/)

    Make sure you have a running instance of [Apache Cassandra](http://cassandra.apache.org/). You can follow the install instructions [here](http://cassandra.apache.org/doc/latest/getting_started/installing.html).
    At the time of this writing, the version being used is _3.11.3_ 
    
* [SPT](https://www.scala-sbt.org/)

    Make sure you have installed _sbt_    


Once you have cloned the project, you can follow the instructions below:

```
sbt compile
```

This command should compile all the sub-projects.
 
You can also run all the tests by running 

```
sbt test
``` 

But if you do so, make sure the prerequisites of all the subprojects are met, otherwise, you will see a lot of failed tests.

Alternatively, you can dive into the project you would like to run tests for or run its examples by doing. Don't forget the project's requisites.

```
sbt 
sbt:ubirch-cassandra-eval> project  PROJECT_NAME
test or run 

``` 

## Alpakka-Cassandra Connector

**Page**: https://developer.lightbend.com/docs/alpakka/current/cassandra.html

**Description**: The Cassandra connector allows you to read and write to Cassandra. You can query a stream of rows from CassandraSource or use prepared statements to insert or update with CassandraFlow or CassandraSink.

**Prerequisites:** A running instance of [Apache Cassandra](http://cassandra.apache.org/)

**Notes:**

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

```
1. Start your Cassandra.
2. run sbt test
```

## Quill

**Page**: http://getquill.io/

**Description**: Free/Libre Compile-time Language Integrated Queries for Scala

**Prerequisites:** 

* A running instance of [Apache Cassandra](http://cassandra.apache.org/)
* Make sure you have run this on your db.

```
CREATE KEYSPACE IF NOT EXISTS db
WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE IF NOT EXISTS db.weather_station (
  country TEXT,
  city TEXT,
  station_id TEXT,
  entry INT,
  value INT,
  PRIMARY KEY (country, city, station_id)
);
```

**Notes:**

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

**Questions:**

1. How is the connection pool handled and how configurations are available.?
2. How can different versions of the same table be handled?
3. What is the recommended way to manage the cassandra context throughout your models.
4. Does Quill take care of closing the connections or does an explicit close needs to be done?

### How to run

#####Examples

```
1. Start your Cassandra.
2. Run 'sbt run' in your console
3. Select the example you think can be intersting.
```

#####Tests

```
1. Start your Cassandra.
2. Run 'sbt test'
```
  









 
    


