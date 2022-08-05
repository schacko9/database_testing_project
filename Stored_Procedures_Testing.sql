use classicmodels;

-- Test Case 1
show procedure status where db = 'classicmodels';
show procedure status where Name = 'SelectAllCustomers';

-- Test Case 2
call selectallCustomers();
select * from customers;


-- Test Case 3
call selectallCustomersbyCity('Singapore');
select * from customers where city = 'Singapore';


-- Test Case 4
call selectallCustomersbyCityandPin('Singapore', '079903');
select * from customers where city = 'Singapore' and postalcode='079903';


-- Test Case 5
call getorderbycustomer(141, @shipped, @canceled, @resolved, @disputed);
select @shipped, @canceled, @resolved, @disputed;

select
(select count(*) as 'shipped' from orders where customerNumber=141 and status = 'Shipped') as Shipped,
(select count(*) as 'canceled' from orders where customerNumber=141 and status = 'Canceled') as Canceled,
(select count(*) as 'resolved' from orders where customerNumber=141 and status = 'Resolved') as Resolved,
(select count(*) as 'disputed' from orders where customerNumber=141 and status = 'Disputed') as Disputed;


-- Test Case 6
call GetCustomerShipping(112, @pshipping);
select @pshipping as ShippingTime;

call GetCustomerShipping(260, @pshipping);
select @pshipping as ShippingTime;

call GetCustomerShipping(353, @pshipping);
select @pshipping as ShippingTime;


select country,
case 
	when country='USA' then '2-Day Shipping'
	when country='Canada' then '3-Day Shipping'
	else'5-Day Shipping'
end as ShippingTime
from customers where customerNumber=112;

select country,
case 
	when country='USA' then '2-Day Shipping'
	when country='Canada' then '3-Day Shipping'
	else'5-Day Shipping'
end as ShippingTime
from customers where customerNumber=260;

select country,
case 
	when country='USA' then '2-Day Shipping'
	when country='Canada' then '3-Day Shipping'
	else'5-Day Shipping'
end as ShippingTime
from customers where customerNumber=353;




