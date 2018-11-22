# ubirch-cassandra-eval
Evaluation of Cassandra and Scala

Integration Tools

* [Getting Started](#getting-started)

* [Alpakka](#alpakka-cassandra-connector)

* [Quill](#quill)

* [Phantom](#phantom)

[DB migrations management](#db-migrations-management)


### TL;TR

Technology | Documentation  | Licence | Case Class Support | Streams Support | Async Support | Scala-like Idioms
------------ | ------------- | ------------- | ------------- | ------------- | -------------  | -------------  
Alpakka | OK | Open Source: Apache 2 License | Needs explicit binding | Built on top of Akka-Streams | Yes | Yes
Quill | OK | Open Source: Apache 2 License | Out of the box support | Through Monix Observables | Yes | Yes
Phantom | OK |  Apache V2 License. Other Components are also [licenced](https://github.com/outworkers/phantom#license-and-copyright) (phantom-pro) | Supports it but needs explicit table class definition |  play-iteratees | Yes | Almost

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
2. run 'sbt'
3. Select project by running 'project alpakka' 
4. run test
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

CREATE TABLE IF NOT EXISTS db.traffic_light_sensor (
  country TEXT,
  city TEXT,
  sensor_id TEXT,
  entry INT,
  value INT,
  PRIMARY KEY (country, city, sensor_id)
);

CREATE TABLE sensor_failures_count (
  id UUID PRIMARY KEY,
  failures counter
  );
  
  
drop table if exists events;

create table if not exists events (
    id timeuuid ,
    principal text ,
    category text ,
    event_source_service text ,
    device_id UUID,
    year int ,
    month int ,
    day int ,
    hour int ,
    minute int ,
    second int,
    milli int,
    created timestamp,
    updated timestamp, 
    PRIMARY KEY ((principal, category), year, month, day, hour, device_id)
) WITH CLUSTERING ORDER BY (year desc, month DESC, day DESC);

drop table if exists events_by_cat;

create table if not exists events_by_cat (
    id timeuuid ,
    principal text ,
    category text ,
    event_source_service text ,
    device_id UUID,
    year int ,
    month int ,
    day int ,
    hour int ,
    minute int ,
    second int,
    milli int,
    created timestamp,
    updated timestamp, 
    PRIMARY KEY ((category, event_source_service, year, month), day, hour, device_id)
);

insert into events (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'Regio IT', 'Validate', 'Avatar-Service', 522f3e64-6ee5-470c-8b66-9edb0cfbf3b1, 2018, 11, 1, 7, 15, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'Regio IT', 'Anchor', 'Avatar-Service', 522f3e64-6ee5-470c-8b66-9edb0cfbf3b1, 2018, 11, 1, 7, 17, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'Regio IT', 'Validate', 'Avatar-Service', 522f3e64-6ee5-470c-8b66-9edb0cfbf3b1, 2018, 11, 2, 8, 15, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'Regio IT', 'Anchor', 'Avatar-Service', 522f3e64-6ee5-470c-8b66-9edb0cfbf3b1, 2018, 11, 2, 8, 17, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));

insert into events (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'MunichRE', 'Validate', 'Avatar-Service', 41245902-69a0-450c-8d37-78e34f0e6760, 2018, 10, 1, 9, 15, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'MunichRE', 'Anchor', 'Avatar-Service', 41245902-69a0-450c-8d37-78e34f0e6760, 2018, 11, 1, 9, 17, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'MunichRE', 'Validate', 'Avatar-Service', 41245902-69a0-450c-8d37-78e34f0e6760, 2018, 11, 2, 11, 15, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'RMunichRET', 'Anchor', 'Avatar-Service', 41245902-69a0-450c-8d37-78e34f0e6760, 2018, 11, 2, 11, 17, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));

insert into events_by_cat (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'Regio IT', 'Validate', 'Avatar-Service', 522f3e64-6ee5-470c-8b66-9edb0cfbf3b1, 2018, 11, 1, 7, 15, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events_by_cat (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'Regio IT', 'Anchor', 'Avatar-Service', 522f3e64-6ee5-470c-8b66-9edb0cfbf3b1, 2018, 11, 1, 7, 17, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events_by_cat (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'Regio IT', 'Validate', 'Avatar-Service', 522f3e64-6ee5-470c-8b66-9edb0cfbf3b1, 2018, 11, 2, 8, 15, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events_by_cat (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'Regio IT', 'Anchor', 'Avatar-Service', 522f3e64-6ee5-470c-8b66-9edb0cfbf3b1, 2018, 11, 2, 8, 17, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));

insert into events_by_cat (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'MunichRE', 'Validate', 'Avatar-Service', 41245902-69a0-450c-8d37-78e34f0e6760, 2018, 10, 1, 9, 15, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events_by_cat (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'MunichRE', 'Anchor', 'Avatar-Service', 41245902-69a0-450c-8d37-78e34f0e6760, 2018, 11, 1, 9, 17, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events_by_cat (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'MunichRE', 'Validate', 'Avatar-Service', 41245902-69a0-450c-8d37-78e34f0e6760, 2018, 11, 2, 11, 15, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));
insert into events_by_cat (id, principal, category, event_source_service, device_id, year, month, day, hour, minute, second, milli, created, updated) values (now(), 'RMunichRET', 'Anchor', 'Avatar-Service', 41245902-69a0-450c-8d37-78e34f0e6760, 2018, 11, 2, 11, 17, 0, 0, toUnixTimestamp(now()), toUnixTimestamp(now()));  
```

**Notes:**

* It supports case classes very nicely. In the case con composed/nested case classes, it offers a simple way to add from/to functions that allow the mapping.
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

    It looks like a config object can be passed into the context?
    
    The context object supports the following constructors:
    
    ```
      def this(naming: N, config: CassandraContextConfig) = this(naming, config.cluster, config.keyspace, config.preparedStatementCacheSize)
      def this(naming: N, config: Config) = this(naming, CassandraContextConfig(config))
      def this(naming: N, configPrefix: String) = this(naming, LoadConfig(configPrefix))
    ```
      
    This is a good thing since we can pass (build) in special config direct onto driver, like pooling.       
    
    [Datastax Pooling](https://docs.datastax.com/en/developer/java-driver/2.1/manual/pooling/)
    
    A first try looks like this:
    
    ```
      val poolingOptions = new PoolingOptions
    
      val cluster = Cluster.builder
        .addContactPoint("127.0.0.1")
        .withPort(9042)
        .withPoolingOptions(poolingOptions)
        .build
    
      val db = new CassandraAsyncContext(SnakeCase, cluster, "db", 1000)
    ```
    
    This means that we could probably use the same options as supported in the [Cluster Builder](https://docs.datastax.com/en/drivers/java/3.1/com/datastax/driver/core/Cluster.Builder.html).
    
2. How can different versions of the same table be handled?
   
   The way we believe could be a viable option to handle different version of the same table can be found in the test _com.ubirch.QuillDifferentTableVersionsSpec_
   
   The general idea is:
   
   * There's a general case class that represents the table.
   
   ```
   case class Event(
       id: UUID,
       principal: String,
       category: String,
       eventSourceService: String,
       deviceId: UUID,
       year: Int,
       month: Int,
       day: Int,
       hour: Int,
       minute: Int,
       second: Int,
       milli: Int,
       created: Date,
       updated: Date)
   ```  
   
   * We define a trait with one value that represents the table schema where we point at the table we need.
  
   ```
     trait TablePointer[T] {
       implicit val eventSchemaMeta: SchemaMeta[T]
     }
   ```
 
   * Then for every specific table (version), there's an object with its own queries, inserts, etc. that extends from the trait above mentioned
   
   ```
     object EventsByCat extends TablePointer[Event] {
   
       implicit val eventSchemaMeta = schemaMeta[Event]("events_by_cat")
   
       def selectAll(implicit sm: SchemaMeta[Event]) = quote(query[Event])
   
       def byCatAndEventSourceAndYearAndMonth(category: String, eventSourceService: String, date: DateTime) = quote {
         query[Event]
           .filter(_.category == lift(category))
           .filter(_.eventSourceService == lift(eventSourceService))
           .filter(_.year == lift(date.year().get()))
           .filter(_.month == lift(date.monthOfYear().get()))
       }
   
       def byCatAndEventSourceAndYearAndMonthAndDay(category: String, eventSourceService: String, date: DateTime) = quote {
         query[Event]
           .filter(_.category == lift(category))
           .filter(_.eventSourceService == lift(eventSourceService))
           .filter(_.year == lift(date.year().get()))
           .filter(_.month == lift(date.monthOfYear().get()))
           .filter(_.day == lift(date.dayOfMonth().get()))
       }
   
       def byCatAndEventSourceAndYearAndMonthAndDayAndDeviceId(category: String, eventSourceService: String, date: DateTime, deviceId: UUID) = quote {
         query[Event]
           .filter(_.category == lift(category))
           .filter(_.eventSourceService == lift(eventSourceService))
           .filter(_.year == lift(date.year().get()))
           .filter(_.month == lift(date.monthOfYear().get()))
           .filter(_.day == lift(date.dayOfMonth().get()))
           .filter(_.hour == lift(date.hourOfDay().get()))
           .filter(_.deviceId == lift(deviceId))
   
       }
   
     }
   ```    
   
3. What is the recommended way to manage the cassandra context throughout your models.?
    
    An interesting option is probably DI.
    
4. Does Quill take care of closing the connections or does an explicit close needs to be done?

### How to run

_Examples_

```
1. Start your Cassandra.
2. Run 'sbt'
3. Select project by running 'project quill' 
4. Run 'run' in your console
5. Select the example you think can be intersting.
```

_Tests_

You can run all tests by following the next instructions:

```
1. Start your Cassandra.
2. Run 'sbt'
3. Select project by running 'project quill'
4. Run 'sbt test'
```
  
Or you can run a specific test by doing:

```
1. Start your Cassandra.
2. Run 'sbt'
3. Select project by running 'project quill'
4. Run 'sbt testOnly TEST_CLASS'
```  

The available test classes are:

1. _com.ubirch.QuillOpsSpec_: Plays with basic algebraic operators.   
2. _com.ubirch.QuillSpec_: Something like above, but simpler.
3. _com.ubirch.QuillDifferentTableVersionsSpec_: Shows how to have different versions of the same table

## Phantom

**Page**: https://outworkers.github.io/phantom/

**Description**: Reactive type-safe Scala driver for Apache Cassandra/Datastax Enterprise

**Prerequisites:** None

* A running instance of [Apache Cassandra](http://cassandra.apache.org/)
* Make sure you have run this on your db.

**Notes**

* It supports explicit configs as per the options supported in the [Cluster Builder](https://docs.datastax.com/en/drivers/java/3.1/com/datastax/driver/core/Cluster.Builder.html).
* Table/Keyspace creation commands can be passed through to initialized session.
* UDT types are on the pro version only.
* Connectors configs [here](https://outworkers.github.io/phantom/basics/connectors.html).
* Customization of the schema names is also supported.
* The definition of the tables has to be explicitly done and special care should be taken into account when defining your column mapping, especially the partition and clustering keys.

https://github.com/outworkers/phantom/wiki/Indexes

### How to run

_Tests_

You can run all tests by following the next instructions:

```
1. Start your Cassandra.
2. Run 'sbt'
3. Select project by running 'project phantom'
4. Run 'sbt test'
```

## DB migrations management

1. https://medium.com/cobli/the-best-way-to-manage-schema-migrations-in-cassandra-92a34c834824

2. https://github.com/patka/cassandra-migration

    Technology | Execution Mode | Has Migration folder | Incremental File Nomenclature (e.g 1.cql, 2.cql, ..., n.cql) | Filename-based order | cql statements support  
    ---------- | -------------- | ---------------------| ------------------------------------------------------------ | -------------------- | ---------------------- |     
    https://github.com/patka/cassandra-migration |  Migration Execution code needs to be put somewhere in the app when it boots | Yes | Yes | Yes | Yes
    https://github.com/Contrast-Security-OSS/cassandra-migration |
    https://github.com/smartcat-labs/cassandra-migration-tool-java |









 
    


