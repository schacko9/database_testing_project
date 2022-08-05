package tests;

import java.sql.SQLException;

import org.testng.annotations.Test;

import utils.BaseTest;



public final class StoredProceduresTest extends BaseTest{

	private StoredProceduresTest() {
		
	}

	
	@Test(priority=1)
	void test_StoreProcedureExists() throws SQLException{
		db.procedure_Exists("selectallCustomers");
	}
	
	@Test(priority=2)
	void test_SelectAllCustomers() throws SQLException{
		db.procedure_SelectAllCustomers();
	}
		
	@Test(priority=3)
	void test_SelectAllCustomers_By_City() throws SQLException{
		db.procedure_SelectAllCustomers_City("Singapore");
	}
	
	@Test(priority=4)
	void test_SelectAllCustomers_By_City_And_Pin() throws SQLException{
		db.procedure_SelectAllCustomers_City_Pin("Singapore", "079903");
	}
		
	@Test(priority=5)
	void test_GetOrderByCustomer() throws SQLException{
		db.procedure_GetOrderByCustomer(141);
	}
		
	@Test(priority=6)
	void test_GetCustomerShipping() throws SQLException{
		db.procedure_GetCustomerShipping(141);
	}	
}
