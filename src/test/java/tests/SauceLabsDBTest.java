package tests;

import java.sql.SQLException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import utils.BaseTest;


public final class SauceLabsDBTest extends BaseTest {

	private SauceLabsDBTest() {
		
	}
	
	String total;	
	

	@DataProvider(name="DP")
	public String[][] feed_test() throws Exception{
		String data[][] = db.get_DB_Values("select id, username, password, product from saucelabs");
		return data;
	}
	
	
	@Test(dataProvider="DP", groups = {"database"})
	public void test_SauceLabs(String id, String user, String pass, String product) throws Exception{
		System.out.println(id+", "+user+", "+pass+", "+product);
		db.setup_SLDB_update(id);
		
		login.getUsername(user);
		login.getPassword(pass);
		login.getLogin();
		
		pc.getProduct(product);
		pc.getCart();
		pc.getCheckout();
		
		pc.getFirstName(user);
		pc.getLastName(user);
		pc.getZIP(user);
		pc.getContinue();
		
		total = pc.getTotal();
		db.update_Total_SLDB(total, id);
	}
}

