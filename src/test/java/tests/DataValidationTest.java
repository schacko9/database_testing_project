package tests;

import java.sql.SQLException;
import org.testng.annotations.Test;

import utils.BaseTest;

public final class DataValidationTest extends BaseTest{

	private DataValidationTest(){
		
	}
	
	@Test(priority=1)
	void test_Course_ID_PrimaryKey() throws SQLException{
		int id = db.course_Insert(333, "SQL", 2, 300, "PASS");
		db.course_Insert(333, "SQL", 3, 300, "FAIL");
		db.course_Delete(id);
	}
	
	@Test(priority=2)
	void test_Course_ID_NULL() throws SQLException{
		db.course_Insert_NULL("NULL", "SQL", 2, 300, "FAIL");
	}
	
	@Test(priority=3)
	void test_Course_Name_Unique() throws SQLException{
		int id = db.course_Insert(444, "C#", 2, 300, "PASS");
		db.course_Insert(555, "C#", 2, 300, "FAIL");
		db.course_Delete(id);
	}
	 
	@Test(priority=4)
	void test_Course_Duration_NULL() throws SQLException{
		db.course_Insert_NULLDuration(666, "VB", "NULL", 100, "FAIL");
	}
	
	@Test(priority=5)
	void test_Course_Fee_Check() throws SQLException{
		db.course_Insert(777, "Javascript", 2, 50, "FAIL");
		db.course_Insert(888, "Typescript", 2, 500, "FAIL");
	}
	
	
	@Test(priority=6)
	void test_Student_ID_PrimaryKey() throws SQLException{
		int id = db.student_Insert(101, "Josh Thompson", 20, 111, "PASS");
		db.student_Insert(101, "Josh Thompson", 20, 111, "FAIL");
		db.student_Delete(id);
	}
	
	@Test(priority=7)
	void test_Student_Name_NULL() throws SQLException{
		db.student_Insert_NULL(101, "NULL", 20, 111, "FAIL");
	}
	
	@Test(priority=8)
	void test_Student_Age_Check() throws SQLException{
		int id = db.student_Insert(102, "Tom Ford", 20, 111, "PASS");
		int id2 = db.student_Insert(103, "John Fried", 21, 111, "PASS");
		db.student_Insert(104, "Sally True", 10, 111, "FAIL");
		db.student_Insert(105, "Sarah True", 40, 111, "FAIL");
		
		db.student_Delete(id);
		db.student_Delete(id2);
	}
	
	@Test(priority=9)
	void test_Student_CourseID_ForeignKey() throws SQLException{
		db.student_Insert(106, "Jake Bangs", 20, 333, "FAIL");
		db.student_Insert(107, "Jesscia Hurts", 20, 999, "FAIL");
	}
}
