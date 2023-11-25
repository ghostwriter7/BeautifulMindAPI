create table if not exists event (
    id int auto_increment primary key,
    title varchar(100) not null,
    description varchar(500),
    dueDate timestamp not null,
    location varchar(100)
);
