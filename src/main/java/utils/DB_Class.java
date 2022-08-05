package utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.testng.Assert;

public class DB_Class {

	public void positive_Negative_Test(PreparedStatement pst, String expect) {
		if(expect.equalsIgnoreCase("PASS")){
			try {
				int value = pst.executeUpdate();
				Assert.assertEquals(value, 1);	
			} catch(SQLException e) {
				Assert.assertTrue(false);
			}
		} else{
			try {
				int value3 = pst.executeUpdate();
				Assert.assertEquals(value3, 1);
			} catch(SQLException e) {
				Assert.assertTrue(true);
			}
		}
	}
	
}
