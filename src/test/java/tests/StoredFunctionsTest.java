package tests;

import java.sql.SQLException;

import org.testng.annotations.Test;

import utils.BaseTest;

public final class StoredFunctionsTest extends BaseTest{
	
	private StoredFunctionsTest() {
		
	}

	
	@Test(priority=1)
	void test_FunctionExists() throws SQLException{
		db.function_Exists("CustomerLevel");
	}
	
	@Test(priority=2)
	void test_CustomerLevel_Function() throws SQLException{
		db.function_CustomerLevel();
	}
	
	@Test(priority=3)
	void test_CustomerLevel_With_Procedure() throws SQLException{
		db.function_CustomerLevel_With_Procedure(131);
	}
	
	
}
