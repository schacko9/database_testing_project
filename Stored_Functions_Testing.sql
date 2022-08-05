-- Test Case 1 --
show function status where db = 'classicmodels';
show function status where Name = 'CustomerLevel';

-- Test Case 2 -- 
select customerName, CustomerLevel(creditLimit) as CreditLimit from customers;
select customerName,
Case
	When creditLimit > 50000 then 'Platinum'
	When (creditLimit >= 10000 and creditLimit <= 50000) then 'Gold'
	When creditLimit < 10000 then 'Silver'
End as customerLevel From customers;



-- Test Case 3 --
call getCustomerLevel(131, @customerLevel);
select @customerLevel;

select customerName,
Case
	When creditLimit > 50000 then 'Platinum'
	When (creditLimit >= 10000 and creditLimit <= 50000) then 'Gold'
	When creditLimit < 10000 then 'Silver'
End as customerLevel from customers where customerNumber=131;

