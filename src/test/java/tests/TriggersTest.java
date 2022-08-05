package tests;

import java.sql.SQLException;

import org.testng.annotations.Test;

import utils.BaseTest;

public final class TriggersTest extends BaseTest{

	private TriggersTest() {
		
	}
	
	
	@Test(priority=1)
	void test_Before_Workcenters_Insert() throws SQLException{
		db.trigger_Before_Workcenters_Insert("Moldy Machine 2", 700);
	}
	
	@Test(priority=2)
	void test_After_Member_Insert() throws SQLException{
		db.trigger_After_Member_Insert("John", "john2@example.com", "NULL");
	}
	
	@Test(priority=3)
	void test_Before_Sales_Update() throws SQLException{
		db.trigger_Before_Sales_Update(1);
	}
	
	@Test(priority=4)
	void test_After_Sales_Update() throws SQLException{
		db.trigger_After_Sales_Update(1);
	}
	
	@Test(priority=5)
	void test_Before_Salaries_Delete() throws SQLException{
		db.trigger_Before_Salaries_Delete();
	}
	
	@Test(priority=6)
	void test_After_Salaries_Delete() throws SQLException{
		db.trigger_After_Salaries_Delete();
	}
	
}
