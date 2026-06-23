create table if not exists expenses (
	id integer generated always as identity primary key,
	amount numeric(12,2) not null,
	description text,
	category_id int references categories(id) not null,
	expense_date date not null default now()

	constraint chk_expenses_amount
	 check (amount > 0)
);

create index if not exists idx_expenses_category_id on expenses(category_id);

