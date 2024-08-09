create database grocery_stores;
use grocery_stores;

create table customers (
	cus_id int auto_increment,
    gender enum('M', 'F'),
    age int,
    points int,
    primary key (cus_id)
);

create table stock_staffs (
	emp_id int auto_increment,
    gender enum('M', 'F'),
    seniority int,
    primary key (emp_id)
);

create table sales_staffs (
	emp_id int auto_increment,
    gender enum('M', 'F'),
    shift enum('morning', 'afternoon', 'evening'),
    primary key (emp_id)
);

create table products (
	prod_id int auto_increment,
    prod_name varchar(40),
    category enum('food', 'pill'),
    price double,
    primary key (prod_id)
);

create table invoices (
	inv_id int auto_increment,
    sales_staff_id int,
    cus_id int default null,
    total_price double,
    purchase_date date,
    point_earned int,
    primary key (inv_id),
    foreign key (sales_staff_id) references sales_staffs(emp_id),
    foreign key (cus_id) references customers(cus_id)
);

create table invoice_details (
	inv_id int,
    prod_id int,
    quantity int,
    primary key (inv_id, prod_id),
    foreign key (inv_id) references invoices(inv_id),
    foreign key (prod_id) references products(prod_id)
);

create table work_dates (
	emp_id int,
    work_date date,
    primary key (emp_id, work_date),
    foreign key (emp_id) references stock_staffs(emp_id),
    foreign key (emp_id) references sales_staffs(emp_id)
);

create table Attendances (
	att_id int auto_increment,
    emp_id int,
    check_in_time datetime,
    att_date date,
    primary key (att_id),
    foreign key (emp_id) references stock_staffs(emp_id),
    foreign key (emp_id) references sales_staffs(emp_id)
);

create table users (
	user_id int auto_increment,
    username int,
    pass varchar(50),
    roles varchar(20),
    primary key (user_id),
    foreign key (username) references stock_staffs(emp_id),
    foreign key (username) references sales_staffs(emp_id)
);

create table imports_stock (
	entry_id int auto_increment,
    prod_id int,
    stock_staff_id int,
    quantity int,
    total_price double,
    import_date date,
    primary key (entry_id),
    foreign key (stock_staff_id) references stock_staffs(emp_id),
    foreign key (prod_id) references products(prod_id)
);

create table exports_stock (
	entry_id int auto_increment,
    prod_id int,
    stock_staff_id int,
    quantity int,
    total_price double,
    export_date date,
    primary key (entry_id),
    foreign key (stock_staff_id) references stock_staffs(emp_id),
    foreign key (prod_id) references products(prod_id)
);