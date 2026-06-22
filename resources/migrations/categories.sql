create table if not exists categories (
	id serial primary key,
	name varchar (100) not null unique
);