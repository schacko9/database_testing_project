create database classicmodels;

use classicmodels;
describe information_schema.columns;


-- Test Case 1 & 2 --
show tables;

-- Test Case 3 --
select count(*) as numberofcolumns FROM information_schema.columns WHERE table_name="customers";

-- Test Case 4 --
select column_name FROM information_schema.columns WHERE table_name="customers";

-- Test Case 5 --
select column_name, data_type FROM information_schema.columns WHERE table_name="customers";

-- Test Case 6 --
select column_name, column_type FROM information_schema.columns WHERE table_name="customers";

-- Test Case 7 --
select column_name, is_nullable FROM information_schema.columns WHERE table_name="customers";

-- Test Case 8 --
select column_name, column_key FROM information_schema.columns WHERE table_name="customers";

