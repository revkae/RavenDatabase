create table if not exists USERS (surname varchar(255) not null, name varchar(255) not null);
truncate table USERS;

insert into USERS (surname, name) values ('Mamiyev', 'Ravan');
insert into USERS (surname, name) values ('Mamiyeva', 'Fidan');