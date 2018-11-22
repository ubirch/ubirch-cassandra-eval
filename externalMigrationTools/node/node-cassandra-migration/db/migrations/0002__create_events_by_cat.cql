drop table if exists events_by_cat;

---

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