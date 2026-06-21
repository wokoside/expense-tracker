create table expenses(
	id serial primary key,
	amount numeric(12,2) not null,
	description text,
	category_id int references categories(id) on delete cascade,
	expense_date date not null default now()
);