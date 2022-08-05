use classicmodels;

-- Table Set 1 --
CREATE TABLE SupplierProducts (
    supplierId int,
    productId int,
    CONSTRAINT Relation PRIMARY KEY (supplierId, productId)
);
CREATE TABLE WorkCenters(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name varchar(100) NOT NULL,
    capacity INT NOT NULL
);
CREATE TABLE WorkCenterStats(
	totalCapacity INT NOT NULL
);

DELIMITER //
CREATE TRIGGER before_workcenters_delete BEFORE DELETE ON workcenters FOR EACH ROW
BEGIN
	UPDATE WorkCenterStats SET totalCapacity = totalCapacity - old.capacity;
END //
DELIMITER ;

drop table WorkCenterStats;

-- Table Set 2 --
CREATE TABLE members(
	id INT AUTO_INCREMENT,
	name varchar(100) NOT NULL,
    email varchar(255),
    birthdate DATE,
    PRIMARY KEY (id)
);
CREATE TABLE reminders(
	id INT AUTO_INCREMENT,
	memberId INT,
    name varchar(100) NOT NULL,
    message varchar(255) NOT NULL,
    PRIMARY KEY (id, memberId)
);



-- Table Set 3 --
CREATE TABLE sales(
	id INT AUTO_INCREMENT,
	product varchar(100) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    fiscalYear SMALLINT NOT NULL,
	fiscalMonth TINYINT NOT NULL,
    CHECK(fiscalMonth >= 1 and fiscalMonth <= 12), 
    CHECK( fiscalYear between 2000 and 2050),
    CHECK (quantity >= 0),
    UNIQUE( product, fiscalYear, fiscalMonth),
    PRIMARY KEY (id)
);
Insert Into sales(product, quantity, fiscalYear, fiscalMonth) Values
	('2003 Harley-Davidson Eagle Drag Bike', 120, 2020, 1),
    ('1969 Corvair Monza', 150, 2020, 1),
    ('1970 Plymonth Hemi Cuda', 200, 2020, 1);

CREATE TABLE saleschanges(
	id INT AUTO_INCREMENT PRIMARY KEY,
	salesId INT,
    beforeQuantity INT,
    afterQuantity INT,
    changedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- Table Set 4 -- 
CREATE TABLE salaries(
	employeeNumber INT AUTO_INCREMENT PRIMARY KEY,
	validFrom DATE NOT NULL,
    salary DECIMAL(12,2) NOT NULL DEFAULT 0
);
ALTER TABLE salaries AUTO_INCREMENT = 1000;
Insert Into salaries(validFrom, salary) Values
	('2000-01-01', 75000),
	('2000-01-02', 90000),
	('2000-01-03', 100000);
CREATE TABLE salariesarchive(
	id INT AUTO_INCREMENT PRIMARY KEY,
	employeeNumber INT,
	validFrom DATE NOT NULL,
    salary DECIMAL(12,2) NOT NULL DEFAULT 0,
    deletedAt TIMESTAMP DEFAULT NOW()
);
CREATE TABLE salariesbudget(
	total DECIMAL(12,2) DEFAULT 0
); 
INSERT INTO salariesbudget(total) SELECT SUM(salary) FROM salaries;


-- Table Set 5 -- 
CREATE TABLE courses(
	courseId INT PRIMARY KEY,
	courseName VARCHAR(20) UNIQUE,
    duration INT NOT NULL,
    fee INT check(fee between 100 and 300)
);
insert into courses values(111, 'Python', 2, 100);
insert into courses values(222, 'Java', 3, 200);

CREATE TABLE student(
	studentId INT PRIMARY KEY,
	studentName VARCHAR(20) NOT NULL,
	age INT check(age between 15 and 30),
	doj DATETIME DEFAULT NOW(),
    courseId INT,
    FOREIGN KEY (courseId) references courses(courseId) ON DELETE CASCADE
);



---------------------------------- E2E ----------------------------------
CREATE TABLE saucelabs2(
	id INT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(50) NOT NULL,
	password VARCHAR(50) NOT NULL,
	product  VARCHAR(100),
    total INT DEFAULT 0
);

CREATE TABLE sl_total_track(
	tid INT AUTO_INCREMENT PRIMARY KEY,
    id INT NOT NULL,
	username VARCHAR(50) NOT NULL,
	total INT DEFAULT 0,
    FOREIGN KEY (id) references saucelabs2(id) ON DELETE CASCADE
);


DELIMITER //
CREATE TRIGGER after_saucelabs_insert AFTER INSERT ON saucelabs2 FOR EACH ROW
BEGIN
    IF NEW.total = 0 THEN 
		INSERT INTO sl_total_track(id, username) VALUES (NEW.id, NEW.username);
	END IF;
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER after_saucelabs_update AFTER UPDATE ON saucelabs2 FOR EACH ROW
BEGIN
    IF OLD.total = 0 AND NEW.total > OLD.total THEN 
     	DELETE FROM sl_total_track WHERE id = NEW.id;
	END IF;
END //
DELIMITER ;


select * from saucelabs;
select * from sl_total_track;

INSERT INTO saucelabs(username, password, product) VALUES ("standard_user", "secret_sauce", "Sauce Labs Onesie");
INSERT INTO saucelabs(username, password, product) VALUES ("standard_user", "secret_sauce", "Sauce Labs Bolt T-Shirt");

UPDATE saucelabs SET total = 10 WHERE id = 1;

DELETE FROM saucelabs WHERE id =4;