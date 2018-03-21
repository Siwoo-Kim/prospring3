
drop table client if exists;

create table client(
  id varchar(255) not null auto_increment,
  name varchar(255),
  age integer not null,
  primary key (id)
);