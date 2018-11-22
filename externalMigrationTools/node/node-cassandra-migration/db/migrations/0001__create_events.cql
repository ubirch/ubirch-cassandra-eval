drop table if exists events;

---

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