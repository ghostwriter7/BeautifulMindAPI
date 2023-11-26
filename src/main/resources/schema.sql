drop table event;

create table if not exists event (
	id int auto_increment primary key,
    date_id date not null,
    title varchar(100) not null,
    description varchar(1024),
    start_datetime datetime not null,
    end_datetime datetime not null,
    location varchar(100)
);

create index idx_date_id on event(date_id);