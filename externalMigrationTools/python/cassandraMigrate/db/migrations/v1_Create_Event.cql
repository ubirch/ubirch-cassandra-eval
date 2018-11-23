/* Cassandra migration for keyspace python_cassandra_migration_test.
   Version 1 - 2018-11-23T04:19:03.806129+00:00

   Create Event */

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

