select * from courses;
select * from student;


-- Test Case 1 --
insert into courses values(333, 'SQL', 2, 300);
insert into courses values(333, 'SQL', 3, 300);


-- Test Case 2 --
insert into courses values(null, 'SQL', 2, 300);


-- Test Case 3 --
insert into courses values(444, "Java", 2, 300);
insert into courses values(555, "Java", 2, 300);


-- Test Case 4 --
insert into courses values(666, 'VB', null, 100);


-- Test Case 5 --
insert into courses values(777, "Javascript", 2, 50);
insert into courses values(888, "Typescript", 2, 500);


-- Test Case 6 --
insert into student(studentId, studentName, age, courseId) Values(101, 'Josh Thompson', 20, 111);
insert into student(studentId, studentName, age, courseId) Values(101, 'Josh Thompson', 20, 111);

-- Test Case 7 --
insert into student(studentId, studentName, age, courseId) Values(101, null, 20, 111);

-- Test Case 8 --
insert into student(studentId, studentName, age, courseId) Values(102, 'Tom Ford', 20, 111);
insert into student(studentId, studentName, age, courseId) Values(103, 'John Fried', 21, 111);
insert into student(studentId, studentName, age, courseId) Values(104, 'Sally True', 10, 111);
insert into student(studentId, studentName, age, courseId) Values(105, 'Sarah True', 40, 111);

-- Test Case 9 --
insert into student(studentId, studentName, age, courseId) Values(106, 'Jake Bangs', 20, 333);
insert into student(studentId, studentName, age, courseId) Values(107, 'Jesscia Hurts', 20, 999);
