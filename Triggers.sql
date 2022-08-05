SET SQL_SAFE_UPDATES = 0;

---------------------------------- INSERT ----------------------------------
DELIMITER //
CREATE TRIGGER before_workcenters_insert BEFORE INSERT ON WorkCenters FOR EACH ROW
BEGIN
	DECLARE rowcount INT;
    
    SELECT COUNT(*) into rowcount from workcenterstats;
    
    If rowcount > 0 THEN
		UPDATE workcenterstats SET totalCapacity = totalCapacity + new.capacity;
	ELSE
		INSERT INTO workcenterstats(totalCapacity) VALUES(new.capacity);
	END IF;
END //
DELIMITER ;


DELIMITER //
CREATE TRIGGER after_members_insert AFTER INSERT ON members FOR EACH ROW
BEGIN
	IF NEW.birthdate IS NULL THEN INSERT INTO reminders(memberId, name, message)
		VALUES(new.id, NEW.name, CONCAT('Hi ', NEW.name, ', please update your date of birth.'));
	END IF;
END //
DELIMITER ;




---------------------------------- UPDATE ----------------------------------
DELIMITER //
CREATE TRIGGER before_sales_update BEFORE UPDATE ON sales FOR EACH ROW
BEGIN
	DECLARE errorMessage VARCHAR(255);
    SET errorMessage = CONCAT('The new quantity ', NEW.quantity, ' cannot be 3 times greater than the current quanitty ', OLD.quantity);
    
    IF NEW.quantity > OLD.quantity * 3 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = errorMessage;
	END IF;
END //
DELIMITER ;


DELIMITER //
CREATE TRIGGER after_sales_update AFTER UPDATE ON sales FOR EACH ROW
BEGIN
    IF OLD.quantity <> NEW.quantity THEN
		INSERT INTO saleschanges(salesId, beforeQuantity, afterQuantity) VALUES (OLD.id, old.quantity, NEW.quantity);
	END IF;
END //
DELIMITER ;



---------------------------------- DELETE ----------------------------------
DELIMITER //
CREATE TRIGGER before_salaries_delete BEFORE DELETE ON salaries FOR EACH ROW
BEGIN
	INSERT INTO salariesarchive(employeeNumber, validFrom, salary) VALUES(OLD.employeeNumber, OLD.validFrom, OLD.salary);
END //
DELIMITER ;


DELIMITER //
CREATE TRIGGER after_salaries_delete BEFORE DELETE ON salaries FOR EACH ROW
BEGIN
	UPDATE salariesbudget SET total = total - old.salary;
END //
DELIMITER ;