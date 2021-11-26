create table Tasks
(
    TaskId          SERIAL primary key,
    ListId          integer      not null REFERENCES Lists (ListId) ON DELETE CASCADE,
    TaskDescription varchar(100) not null,
    TaskCompleted   boolean      not null
);

create table Lists
(
    ListId   SERIAL primary key,
    ListName varchar(50) not null
);