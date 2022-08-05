package utils;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import pageObjects.LoginPage;
import pageObjects.PCPage;

public class BaseTest {

	protected BaseTest() {
		
	}
	
	protected LoginPage login;
	protected PCPage pc;
	protected DB_Operations db = new DB_Operations("classicmodels", "root", "root");
	
	
	@BeforeMethod(onlyForGroups = {"database"})
	public void setup() throws Exception{
		DriverEngine.initDriver();
		login = new LoginPage(DriverEngine.driver);
		pc = new PCPage(DriverEngine.driver);
	}
	
	@AfterMethod(onlyForGroups = {"database"})
	public void teardown(){
		DriverEngine.quitDriver();
	}
}
