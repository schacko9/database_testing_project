delimiter //
create procedure selectallCustomers()
Begin
	select * from customers;
End //
delimiter ;


delimiter //
create procedure selectallCustomersbyCity(IN mycity varchar(50))
Begin
	select * from customers where city=mycity;
End //
delimiter ;


delimiter //
create procedure selectallCustomersbyCityandPin(IN mycity varchar(50), IN pcode varchar(50))
Begin
	select * from customers where city=mycity and postalCode=pcode;
End //
delimiter ;


delimiter //
create procedure getorderbycustomer(
	IN custno int, 
    OUT shipped int,
    OUT canceled int,
    OUT resolved int,
	OUT disputed int)
Begin
	-- shipped
	select count(*) into shipped from orders where customerNumber=custno and status ='Shipped';
    -- canceled
	select count(*) into canceled from orders where customerNumber=custno and status ='Canceled';
    -- resolved
	select count(*) into resolved from orders where customerNumber=custno and status ='Resolved';
    -- disputed
	select count(*) into disputed from orders where customerNumber=custno and status ='Disputed';
End //
delimiter ;


delimiter //
create procedure GetCustomerShipping(
	IN pCustomerNum int, 
    OUT pShipping VARCHAR(50))
Begin
	DECLARE customerCountry VARCHAR(100);
	
    SELECT country into customerCountry FROM customers WHERE customerNumber=pCustomerNum;
    CASE customerCountry
		WHEN 'USA' THEN
			SET pShipping = '2-Day Shipping';
		WHEN 'Canada' THEN
			SET pShipping = '3-Day Shipping';
		ElSE
			SET pShipping = '5-Day Shipping';
	END CASE;
End //
delimiter ;



delimiter //
create procedure insertSupplierProducts(IN inSupplierID int, IN inProductID int)
Begin
	-- exit if the duplicate key occurs
    DECLARE EXIT HANDLER FOR 1062 SELECT 'Duplicate keys error encounters' Message;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION SELECT 'SQLEXCEPTION encounters' Message;
    DECLARE EXIT HANDLER FOR SQLSTATE '23000' SELECT 'SQLSTATE 23000' Message;

	-- insert a new roe into the SupplierProducts
    insert into SupplierProducts(supplierId,productId) values (inSupplierID,inProductID);
    
    -- return the product supplied by the supplier id
    select count(*) from SupplierProducts where supplierId = inSupplierId;
    
End //
delimiter ;