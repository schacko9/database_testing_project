-- Creating  Credit Limit fucntion
delimiter //
create function CustomerLevel(credit Decimal(10,2)) returns varchar(20)
deterministic
begin
	declare customerLevel varchar(20);
    if credit > 50000 then
		set customerlevel = 'Platinum';
	elseif (credit >= 10000 and credit <= 50000) then
		set customerlevel = 'Gold';
	elseif credit < 10000 then
		set customerlevel = 'Silver';
	end if;
    return customerLevel;
end //
delimiter ;


-- Creating Procedure to call Credit Limit fuunction
delimiter //
create procedure getCustomerLevel(IN customerNo int, out customerLevel varchar(50))
Begin
	declare credit decimal(10,2) default 0;
    
	-- get credit limit of a customer
    select creditLimit into credit from customers
		where customerNumber = customerNo;
	
    -- call Credit Limit Function
	set customerLevel = CustomerLevel(credit);
End //
delimiter ;



