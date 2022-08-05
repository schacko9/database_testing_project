show triggers;


-- Test Case 1 --
insert into WorkCenters(name, capacity) values('Moldy Machine 2', 700);
select * from workcenters;
select * from workcenterstats;
delete from workcenters where id =4; 


-- Test Case 2 --
insert into members(name, email, birthdate) values('Sarah', 'sarah@example.com', null);
select * from members;
delete from members where id=12;
delete from reminders where memberId=12;


-- Test Case 3 --
select * from sales;
Update sales SET quantity = 1000 WHERE id = 1; 


-- Test Case 4 --
select * from sales;
Update sales SET quantity = 200 WHERE id = 1;
select * from saleschanges;


-- Test Case 5 --
Insert Into salaries(validFrom, salary) Values ('2222-08-01',99999);
select * from salaries WHERE validFrom = 2222-08-01 AND salary = 99999;
DELETE FROM salaries WHERE employeeNumber = 1005;
select * from salariesarchive;
    

-- Test Case 6 --
Insert Into salaries(validFrom, salary) Values ('2222-08-01',99999);
select * from salaries WHERE validFrom = 2222-08-01 AND salary = 99999;
select * from salariesbudget;
DELETE FROM salaries WHERE employeeNumber = 1005;
			