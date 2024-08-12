use employees;

select * from departments;
select * from dept_emp;
select * from dept_manager;
select * from employees;
select * from salaries;
select * from titles;

-- bai 1
-- cau 1.1

select e.dept_no, employees.emp_no, first_name, last_name, e.salary from employees
join salaries on employees.emp_no=salaries.emp_no
join dept_emp on employees.emp_no=dept_emp.emp_no
join
(
	select dept_no, min(salary) as salary from employees
	join salaries on employees.emp_no=salaries.emp_no
	join dept_emp on employees.emp_no=dept_emp.emp_no
	where hire_date > '1995-01-01' and ("1996-07-25" between salaries.from_date and salaries.to_date)
	group by dept_no
) e
on dept_emp.dept_no=e.dept_no
where salaries.salary=e.salary and hire_date > '1995-01-01' and ("1996-07-25" between salaries.from_date and salaries.to_date);

-- cau 1.2

select departments.dept_no, dept_name, avgsalary from departments
right join 
(
	select dept_no, avg(salary) as avgsalary from employees
	join salaries on employees.emp_no = salaries.emp_no
	join dept_emp on employees.emp_no = dept_emp.emp_no
	group by dept_no
    order by avgsalary desc
    limit 1
) e
on e.dept_no = departments.dept_no;

-- cau 1.3

SELECT 
    e.first_name,
    e.last_name,
    e.hire_date,
    SUM(s.salary * 12) AS salary_total
FROM
    employees e
JOIN
    salaries s ON e.emp_no = s.emp_no
JOIN
    titles t ON e.emp_no = t.emp_no
WHERE
    e.emp_no = 10005
    AND t.title = 'Staff'
    AND s.from_date >= t.from_date
    AND s.to_date <= t.to_date
GROUP BY 
    e.first_name,
    e.last_name,
    e.hire_date;

-- cau 1.4

SELECT 
    COUNT(DISTINCT m.emp_no) AS number_of_employees
FROM 
    employees e
JOIN 
    dept_manager dm ON dm.emp_no = e.emp_no
JOIN 
    dept_emp de ON de.dept_no = dm.dept_no
JOIN 
    employees m ON de.emp_no = m.emp_no
WHERE 
    e.first_name = 'Margareta' 
    AND e.last_name = 'Markovitch'
    and de.to_date >= dm.from_date 
    and de.from_date <= dm.to_date;
    
-- cau 1.5

select title, avg(salary) as avg_salary from salaries
join titles on salaries.emp_no = titles.emp_no
where ("1996-07-25" between salaries.from_date and salaries.to_date) and ("1996-07-25" between titles.from_date and titles.to_date)
group by title;

-- cau 1.6

select sum(fee) from
(
	select emp_no,
	case
		when salary <= 40000 then 0
		when salary <= 60000 then (salary - 40000) * 5 / 100
		when salary <= 90000 then 20000 * 5 / 100 + (salary - 60000) * 10 / 100
		else 20000 * 5 / 100 + 30000 * 10 / 100 + (salary - 90000) * 15 / 100
	end as fee
	from
	(
		select employees.emp_no, salary from employees
		join salaries on salaries.emp_no = employees.emp_no
		where '1996-07-25' between salaries.from_date and salaries.to_date
	) as salary_table
) as temp_table;

-- cau 1.7

select dept_no, sum(sumsalary2) as sumsalary
from dept_emp
join
(
	select emp_no, first_name, last_name, sum(sumsalary1) as sumsalary2
	from
	(
		select employees.emp_no, first_name, last_name, from_date, to_date,
		case
			when year(to_date) = 1996 then salary * (month(to_date) - 1)
			when year(from_date) = 1996 then salary * (12 - month(from_date) + 1)
		end as sumsalary1
		from employees
		join salaries on employees.emp_no = salaries.emp_no
		where 1996 between year(salaries.from_date) and year(salaries.to_date)
	) as temp_tb1
	group by employees.emp_no, first_name, last_name
) as temp_tb2
on dept_emp.emp_no = temp_tb2.emp_no
where 1996 between year(dept_emp.from_date) and year(dept_emp.to_date)
group by dept_no;

-- cau 1.8

SELECT e.emp_no, e.first_name, e.last_name, d.dept_no, d.dept_name
FROM employees e
JOIN dept_emp de ON e.emp_no = de.emp_no
JOIN departments d ON de.dept_no = d.dept_no
WHERE e.gender = 'M'
  AND (d.dept_no = 'd003' OR d.dept_no = 'd002')
  AND e.emp_no NOT IN (
      SELECT de1.emp_no
      FROM dept_emp de1
      JOIN dept_emp de2 ON de1.emp_no = de2.emp_no
      WHERE de1.dept_no = 'd003' AND de2.dept_no = 'd002'
  );

-- cau 1.9

SELECT
    d.dept_no,
    d.dept_name,
    SUM(s.salary) AS total_salary
FROM
    salaries s
JOIN
    dept_emp de ON s.emp_no = de.emp_no
JOIN
    departments d ON de.dept_no = d.dept_no
WHERE
    '1996-07-25' BETWEEN s.from_date AND s.to_date
    AND '1996-07-25' BETWEEN de.from_date AND de.to_date
GROUP BY
    d.dept_no, d.dept_name
HAVING
    SUM(s.salary) > 300000;

-- cau 1.10

select t1.emp_no, first_name, last_name
from titles t1
join employees e on e.emp_no = t1.emp_no
where t1.to_date = '9999-01-01'
and t1.emp_no in (
	select t2.emp_no 
    from titles t2
	group by t2.emp_no
	having count(t2.title) > 1
);

-- cau 1.11

WITH RankedSalaries AS (
    SELECT
        e.emp_no,
        e.first_name,
        e.last_name,
        d.dept_no,
        d.dept_name,
        s.salary,
        t.title,
        ROW_NUMBER() OVER (PARTITION BY d.dept_no ORDER BY s.salary) as ranks
    FROM
        employees e
    JOIN
        dept_emp de ON e.emp_no = de.emp_no
    JOIN
        departments d ON de.dept_no = d.dept_no
    JOIN
        salaries s ON e.emp_no = s.emp_no
    JOIN
        titles t ON e.emp_no = t.emp_no
    WHERE
        t.title = 'Staff'
        AND de.to_date = '9999-01-01'
        AND s.to_date = '9999-01-01'
)
SELECT
    emp_no,
    first_name,
    last_name,
    dept_no,
    dept_name,
    salary,
    title
FROM
    RankedSalaries
WHERE
    ranks <= 5;
    
    -- cau 1.12
    
SELECT
    e.emp_no,
    e.first_name,
    e.last_name,
    de1.dept_no AS dept_no1,
    de2.dept_no AS dept_no2,
    t1.title AS title1,
    t2.title AS title2
FROM
    employees e
JOIN
    dept_emp de1 ON e.emp_no = de1.emp_no
JOIN
    dept_emp de2 ON e.emp_no = de2.emp_no
JOIN
    titles t1 ON e.emp_no = t1.emp_no AND de1.from_date = t1.from_date
JOIN
    titles t2 ON e.emp_no = t2.emp_no AND de2.from_date = t2.from_date
WHERE
    de1.dept_no != de2.dept_no
    AND t1.title != t2.title
    AND de1.to_date > de2.from_date
    AND de2.to_date > de1.from_date;

-- bai 2
-- cau 2.1

start transaction;
	update titles 
	set to_date = now()
    where emp_no = 10002 and to_date = '9999-01-01';
    
    insert into titles 
    values (10002, 'Senior Staff', now(), '9999-01-01');
commit;

delete from titles where emp_no = 10002 and title = 'Senior Staff';

-- cau 2.2

start transaction;
	select @deptid := dept_no from departments 
    where dept_name = 'Production'; 
    
    delete from employees 
    where employees.emp_no in (
		select emp_no from
        (
			select * from dept_emp
			union
			select * from dept_manager
        ) as temp_table
        where temp_table.dept_no = @deptid
    );

	delete from departments 
    where dept_name = 'Production';
commit;

-- cau 2.3

start transaction;
	insert into departments
    values ('d004', 'Bigdata & ML');
    
    insert into dept_manager
    values ('d004', 10173, now(), '9999-01-01');
commit;

-- bai 3 
delimiter //

CREATE PROCEDURE spud_employees_getData(in ename varchar(30))
BEGIN
    select tb.id, tb.full_name, tb.gender, tb.title, tb.dept_name, sum(salary) as sum_salary from salaries
    join 
    (
		select employees.emp_no as id, CONCAT(first_name, ' ', last_name) AS full_name, gender, title, dept_name
		from employees
		join dept_emp on employees.emp_no = dept_emp.emp_no
		join departments on dept_emp.dept_no = departments.dept_no
		join titles on employees.emp_no = titles.emp_no
        where titles.to_date = '9999-01-01' and dept_emp.to_date = '9999-01-01'
		having full_name = ename
    ) as tb
    on tb.id = salaries.emp_no
    where salaries.to_date < now()
    group by tb.id, tb.full_name, tb.gender, tb.title, tb.dept_name;
END//

delimiter ;

-- bai 4

delimiter //

CREATE PROCEDURE spud_dept_emp_changeDepartment(
	in empid int,  
    in newDeptID char(4),
    in newTitle varchar(50)
)
BEGIN
    update dept_emp set to_date = now()
    where emp_no = empid and to_date = '9999-01-01';
    
    insert into dept_emp 
    values (empid, newDeptID, now(), '9999-01-01');
    
    update titles set to_date = now()
    where emp_no = empid and to_date = '9999-01-01';
    
    insert into titles 
    values (empid, newTitle, now(), '9999-01-01');
    
    select employees.emp_no as id, CONCAT(first_name, ' ', last_name) AS full_name, gender, title, dept_name
		from employees
		join dept_emp on employees.emp_no = dept_emp.emp_no
		join departments on dept_emp.dept_no = departments.dept_no
		join titles on employees.emp_no = titles.emp_no
        where employees.emp_no = empid and titles.to_date = '9999-01-01' and dept_emp.to_date = '9999-01-01';
END//

delimiter ;

call spud_dept_emp_changeDepartment(10001, 'd003', 'Senior Staff');