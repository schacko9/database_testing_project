package utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;


public class DB_Operations extends DB_Class{
	Connection con = null;
	
	public DB_Operations(String database, String username, String password){
		try {
			this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database+"",""+username+"", ""+password+"");
		}catch(SQLException e ) {
			e.printStackTrace();
		}
	}

	
	public String[][] get_DB_Values(String sql) throws SQLException, ClassNotFoundException{
		PreparedStatement pst = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = pst.executeQuery();
		rs.next();
		
		rs.last();
		int rows = rs.getRow();
		int col = rs.getMetaData().getColumnCount();
		String data[][] = new String [rows][col];
		
		rs.beforeFirst();
	
		int i=0;
		while(rs.next()) {
			for(int j=0; j<col; j++) {
				data[i][j] = rs.getString(j+1);
				System.out.println(data[i][j]);
			}
			i++;
		}
		
		return data;
	}
	
	public void setup_SLDB_update(String id) throws SQLException, ClassNotFoundException{ 
		int i = Integer.parseInt(id);  
		
		String sql = "UPDATE saucelabs SET total = 0 WHERE id = ?";
		PreparedStatement pst = con.prepareStatement(sql);

		pst.setInt(1, i);

		int value = pst.executeUpdate();
		Assert.assertEquals(value, 1);
		System.out.println("Successful Setup with "+id);

	}
	
	public void update_Total_SLDB(String total,String id) throws SQLException, ClassNotFoundException{
		float f = Float.parseFloat(total); 
		int i = Integer.parseInt(id);  

		String sql = "UPDATE saucelabs SET total = ? WHERE id = ?;";
		PreparedStatement pst = con.prepareStatement(sql);

		pst.setFloat(1, f);
		pst.setInt(2, i);

		int value = pst.executeUpdate();
		Assert.assertEquals(value, 1);
		System.out.println("Successful entry with: { ID: "+ id+", TOTAL: "+total+"}");

	}

	public boolean compareResultSet(ResultSet rs1, ResultSet rs2) throws SQLException {
		while(rs1.next()){
			rs2.next();
			int count = rs1.getMetaData().getColumnCount();
			for(int i=1; i<count; i++) {
				if(!StringUtils.equals(rs1.getString(i), rs2.getString(i))) {
					return false;
				}
			}
		}
		return true;
	}

	
	//  ------------------------------------------------------------------------------------------------------------------------------- //	
	// Procedure Functions //
	public void procedure_Exists(String table) throws SQLException {
		String sql = "show procedure status where Name = ?";

		CallableStatement cst = con.prepareCall(sql);
		cst.setString(1, table);
		ResultSet rs = cst.executeQuery();
		rs.next();
		
		Assert.assertEquals(rs.getString("Name"), table);
	}
	
	public void procedure_SelectAllCustomers() throws SQLException {
		String sql = "{call selectallCustomers()}";

		CallableStatement cst = con.prepareCall(sql);
		ResultSet rs1 = cst.executeQuery();
		
		Statement st = con.createStatement();
		ResultSet rs2 = st.executeQuery("select * from customers");

		Assert.assertEquals(compareResultSet(rs1,rs2), true);
	}
	
	public void procedure_SelectAllCustomers_City(String city) throws SQLException {
		String sql = "{call selectallCustomersbyCity(?)}";
		String sql2 = "select * from customers where city = ?";

		CallableStatement cst = con.prepareCall(sql);
		cst.setString(1, city);
		ResultSet rs1 = cst.executeQuery();
		
		PreparedStatement pst = con.prepareStatement(sql2);
		pst.setString(1, city);
		ResultSet rs2 = pst.executeQuery();

		Assert.assertEquals(compareResultSet(rs1,rs2), true);
	}
	
	public void procedure_SelectAllCustomers_City_Pin(String city, String code) throws SQLException {
		String sql = "{call selectallCustomersbyCityandPin(?,?)}";
		String sql2 = "select * from customers where city = ? and postalcode=?";

		CallableStatement cst = con.prepareCall(sql);
		cst.setString(1, city);
		cst.setString(2, code);
		ResultSet rs1 = cst.executeQuery();
		
		PreparedStatement pst = con.prepareStatement(sql2);
		pst.setString(1, city);
		pst.setString(2, code);
		ResultSet rs2 = pst.executeQuery();

		Assert.assertEquals(compareResultSet(rs1,rs2), true);
	}
	
	public void procedure_GetOrderByCustomer(int num) throws SQLException {
		String sql = "{call getorderbycustomer(?,?,?,?,?)}";
		String sql2 = "select\r\n"
				+ "(select count(*) as 'shipped' from orders where customerNumber=? and status = 'Shipped') as Shipped,\r\n"
				+ "(select count(*) as 'canceled' from orders where customerNumber=? and status = 'Canceled') as Canceled,\r\n"
				+ "(select count(*) as 'resolved' from orders where customerNumber=? and status = 'Resolved') as Resolved,\r\n"
				+ "(select count(*) as 'disputed' from orders where customerNumber=? and status = 'Disputed') as Disputed";

		CallableStatement cst = con.prepareCall(sql);
		cst.setInt(1, num);
		cst.registerOutParameter(2, Types.INTEGER);
		cst.registerOutParameter(3, Types.INTEGER);
		cst.registerOutParameter(4, Types.INTEGER);
		cst.registerOutParameter(5, Types.INTEGER);
		
		ResultSet rs1 = cst.executeQuery();
		int shipped = cst.getInt(2);
		int canceled = cst.getInt(3);
		int resolved = cst.getInt(4);
		int disputed = cst.getInt(5);

		
		PreparedStatement pst = con.prepareStatement(sql2);
		pst.setInt(1, num);
		pst.setInt(2, num);
		pst.setInt(3, num);
		pst.setInt(4, num);
		ResultSet rs = pst.executeQuery();

		rs.next();
		int exp_shipped = rs.getInt("shipped");
		int exp_canceled = rs.getInt("canceled");
		int exp_resolved = rs.getInt("resolved");
		int exp_disputed = rs.getInt("disputed");

		
		if(shipped==exp_shipped && canceled==exp_canceled && resolved==exp_resolved && disputed==exp_disputed) {
			Assert.assertTrue(true);
	
		} else {
			Assert.assertTrue(false);
		}
	}
	
	public void procedure_GetCustomerShipping(int num) throws SQLException {
		String sql = "{call GetCustomerShipping(?,?)}";
		String sql2 = "select country,\r\n"
				+ "case \r\n"
				+ "	when country='USA' then '2-Day Shipping'\r\n"
				+ "	when country='Canada' then '3-Day Shipping'\r\n"
				+ "	else'5-Day Shipping'\r\n"
				+ "end as ShippingTime\r\n"
				+ "from customers where customerNumber=?";

		CallableStatement cst = con.prepareCall(sql);
		cst.setInt(1, num);
		cst.registerOutParameter(2, Types.VARCHAR);
		
		ResultSet rs1 = cst.executeQuery();
		String shippingTime = cst.getString(2);
		
		PreparedStatement pst = con.prepareStatement(sql2);
		pst.setInt(1, num);
		ResultSet rs = pst.executeQuery();

		rs.next();
		String exp_shippingTime = rs.getString("ShippingTime");
		
		Assert.assertEquals(shippingTime, exp_shippingTime);
	}
	
	
	//  ------------------------------------------------------------------------------------------------------------------------------- //	
	// Function Functions //
	public void function_Exists(String func) throws SQLException {
		String sql = "show function status where Name = ?";

		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, func);
		ResultSet rs = pst.executeQuery();
		rs.next();
		
		Assert.assertEquals(rs.getString("Name"), func);
	}
	
	public void function_CustomerLevel() throws SQLException {
		String sql = "select customerName, CustomerLevel(creditLimit) as CreditLimit from customers";
		String sql2 = "select customerName,\r\n"
				+ "Case\r\n"
				+ "	When creditLimit > 50000 then 'Platium'\r\n"
				+ "	When (creditLimit >= 10000 and creditLimit <= 50000) then 'Gold'\r\n"
				+ "	When creditLimit < 10000 then 'Silver'\r\n"
				+ "End as customerLevel From customers";

		PreparedStatement pst = con.prepareStatement(sql);
		ResultSet rs1 = pst.executeQuery();
		
		Statement st = con.createStatement();
		ResultSet rs2 = st.executeQuery(sql);

		Assert.assertEquals(compareResultSet(rs1,rs2), true);
	}
	
	public void function_CustomerLevel_With_Procedure(int num) throws SQLException {
		String sql = "{call getCustomerLevel(?, ?)}";
		String sql2 = "select customerName,\r\n"
				+ "Case\r\n"
				+ "	When creditLimit > 50000 then 'Platinum'\r\n"
				+ "	When (creditLimit >= 10000 and creditLimit <= 50000) then 'Gold'\r\n"
				+ "	When creditLimit < 10000 then 'Silver'\r\n"
				+ "End as customerLevel from customers where customerNumber=?";

		CallableStatement cst = con.prepareCall(sql);
		cst.setInt(1, num);
		cst.registerOutParameter(2, Types.VARCHAR);
	
		ResultSet rs1 = cst.executeQuery();
		String credit_Limit = cst.getString(2);
		
		
		PreparedStatement pst = con.prepareStatement(sql2);
		pst.setInt(1, num);
		ResultSet rs2 = pst.executeQuery();
		rs2.next();
		String exp_credit_Limit = rs2.getString("customerLevel");
		
		
		Assert.assertEquals(credit_Limit, exp_credit_Limit);
	}
	
	
	//  ------------------------------------------------------------------------------------------------------------------------------- //	
	// Trigger Functions //
		public void trigger_Before_Workcenters_Insert(String name, int capacity) throws SQLException {
			String sql = "insert into WorkCenters(name, capacity) values(?, ?)";
			String sql2 = "select * from workcenters";
			String sql3 = "select * from workcenterstats";
			String sql4 = "delete from workcenters where id = ?";

			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, name);
			pst.setInt(2, capacity);
			int value = pst.executeUpdate();
			Assert.assertEquals(value, 1);
			
			PreparedStatement pst2 = con.prepareStatement(sql2);
			ResultSet rs = pst2.executeQuery();
			
			int cap = 0;
			String id = null;
			while(rs.next()) {
				String capS = rs.getString("capacity");
				int capN = Integer.parseInt(capS);
				cap += capN;
				String Rname = rs.getString("name");
				if(Rname.equalsIgnoreCase(name)){
					id = rs.getString("id");
				} else {
					assert true;
				}
			}

			
			Statement st2 = con.createStatement();
			ResultSet rs2 = st2.executeQuery(sql3);
			rs2.next();
			String exp_cap = rs2.getString("totalCapacity");
			int exp_capN = Integer.parseInt(exp_cap);
			
			Assert.assertEquals(cap, exp_capN);
			
			int idNum = Integer.parseInt(id);
			PreparedStatement pst4 = con.prepareStatement(sql4);
			pst4.setInt(1, idNum);
			int value2 = pst4.executeUpdate();
			Assert.assertEquals(value2, 1);
			
		}
		
		public void trigger_After_Member_Insert(String name, String email,  String date) throws SQLException {
			String sql = "insert into members(name, email, birthdate) values(?, ?, ?)";
			String sql2 = "select * from members";
			String sql3 = "delete from members where id = ?";
			String sql4 = "delete from reminders where memberId = ?";

			PreparedStatement pst = con.prepareStatement(sql);
			pst.setString(1, name);
			pst.setString(2, email);
			if(date.equalsIgnoreCase("NULL")){
			    pst.setNull(3, Types.NULL);
			} else {
				pst.setString(3, date);
			}
		
			int value = pst.executeUpdate();
			Assert.assertEquals(value, 1);
			
			Statement st2 = con.createStatement();
			ResultSet rs = st2.executeQuery(sql2);
			rs.next();
			
			String exp_name = null;
			String id = null;
			while(rs.next()){
				String row_email = rs.getString("email");
				if(row_email.equalsIgnoreCase(email)) {
					exp_name = rs.getString("name");
					id = rs.getString("id");
				} else {
					assert true;
				}
			}
			
			Assert.assertEquals(name, exp_name);
			
			int idNum = Integer.parseInt(id);
			PreparedStatement pst2 = con.prepareStatement(sql3);
			pst2.setInt(1, idNum);
			int value2 = pst2.executeUpdate();
			Assert.assertEquals(value2, 1);

			PreparedStatement pst3 = con.prepareStatement(sql4);
			pst3.setInt(1, idNum);
			int value3 = pst3.executeUpdate();
			Assert.assertEquals(value3, 1);
		}
		
		public void trigger_Before_Sales_Update(int id) throws SQLException {
			String sql = "select * from sales WHERE id = ?";
			String sql2 = "Update sales SET quantity = ? WHERE id = ?";
		
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			rs.next();
			String set_q = rs.getString("quantity");
			int set_quantity = Integer.parseInt(set_q);
			int error_quaniity = (set_quantity * 3) + 1;
			
			
			PreparedStatement pst2 = con.prepareStatement(sql2);
			pst2.setInt(1, error_quaniity);
			pst2.setInt(2, id);
			
			try {
				int value = pst2.executeUpdate();
				Assert.assertTrue(false);
			} catch(SQLException e) {
				Assert.assertTrue(true);
			}
		}
		
		public void trigger_After_Sales_Update(int id) throws SQLException {
			String sql = "select * from sales WHERE id = ?";
			String sql2 = "Update sales SET quantity = ? WHERE id = ?";
			String sql3 = "select * from saleschanges WHERE salesId = ?";
	
		
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			rs.next();
			String set_q = rs.getString("quantity");
			int set_quantity = Integer.parseInt(set_q);
			int input_quantity = set_quantity + 10;
			
			PreparedStatement pst2 = con.prepareStatement(sql2);
			pst2.setInt(1, input_quantity);
			pst2.setInt(2, id);
			int value = pst2.executeUpdate();
			Assert.assertEquals(value, 1);
		
			PreparedStatement pst3 = con.prepareStatement(sql3);
			pst3.setInt(1, id);
			ResultSet rs2 = pst3.executeQuery();
			rs2.next();
			
			boolean flag = false;
			while(rs2.next()) {
				String bf_q = rs2.getString("beforeQuantity");
				int bf_quantity = Integer.parseInt(bf_q);
				if(bf_quantity == set_quantity){
					String af_q = rs2.getString("afterQuantity");
					int af_quantity = Integer.parseInt(af_q);
					if(af_quantity == input_quantity) {
						flag=true;
						break;
					}
				}
			}
			
			Assert.assertTrue(flag);
		}
		
		public void trigger_Before_Salaries_Delete() throws SQLException {
			String sql = "Insert Into salaries(validFrom, salary) Values (?,?)";
			String sql2 = "select * from salaries WHERE validFrom = ? AND salary = ?";
			String sql3 = "DELETE FROM salaries WHERE employeeNumber = ?";
			String sql4 = "select * from salariesarchive";
	
			String date = "2222-08-01";
			int salary = 99999;
		
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setDate(1, Date.valueOf(date));
			pst.setInt(2, salary);
			int value = pst.executeUpdate();
			Assert.assertEquals(value, 1);
		
			PreparedStatement pst2 = con.prepareStatement(sql2);
			pst2.setDate(1, Date.valueOf(date));
			pst2.setInt(2, salary);
			ResultSet rs = pst2.executeQuery();
			rs.next();
			String eN = rs.getString("employeeNumber");
			int eNum = Integer.parseInt(eN);
			
			PreparedStatement pst3 = con.prepareStatement(sql3);
			pst3.setInt(1, eNum);
			int value2 = pst3.executeUpdate();
			Assert.assertEquals(value2, 1);
			
			PreparedStatement pst4 = con.prepareStatement(sql4);
			ResultSet rs2 = pst4.executeQuery();
			
			boolean flag = false;
			while(rs2.next()) {
				String recieved_eN = rs2.getString("employeeNumber");
				int recieved_eNum = Integer.parseInt(recieved_eN);
				if(recieved_eNum == eNum){
					flag=true;
					break;
					}
			}
			
			Assert.assertTrue(flag);
		}
			
	
		public void trigger_After_Salaries_Delete() throws SQLException {
			String sql = "Insert Into salaries(validFrom, salary) Values (?,?)";
			String sql2 = "select * from salaries WHERE validFrom = ? AND salary = ?";
			String sql3 = "select * from salariesbudget";
			String sql4 = "DELETE FROM salaries WHERE employeeNumber = ?";
			
	
			String date = "2222-08-01";
			int salary = 99999;
			float salaryf = salary;
			
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setDate(1, Date.valueOf(date));
			pst.setInt(2, salary);
			int value = pst.executeUpdate();
			Assert.assertEquals(value, 1);
		
			PreparedStatement pst2 = con.prepareStatement(sql2);
			pst2.setDate(1, Date.valueOf(date));
			pst2.setInt(2, salary);
			ResultSet rs = pst2.executeQuery();
			rs.next();
			String eN = rs.getString("employeeNumber");
			int eNum = Integer.parseInt(eN);
			
			PreparedStatement pst3 = con.prepareStatement(sql3);
			ResultSet rs2 = pst3.executeQuery();
			rs2.next();
			String originalN = rs2.getString("total");
			float originalNum = Float.parseFloat(originalN);
			
			PreparedStatement pst4 = con.prepareStatement(sql4);
			pst4.setInt(1, eNum);
			int value2 = pst4.executeUpdate();
			Assert.assertEquals(value2, 1);
			
			PreparedStatement pst5 = con.prepareStatement(sql3);
			ResultSet rs3 = pst5.executeQuery();
			rs3.next();
			String finalN = rs3.getString("total");
			float finalNum = Float.parseFloat(finalN);
			
			float finalNumSal= finalNum + salaryf;
			Assert.assertEquals(finalNumSal, originalNum);
		}

		
	//  ------------------------------------------------------------------------------------------------------------------------------- //	
	//	Data Validation Functions 	//
		public int course_Insert(int id, String name, int duration, int fee, String expect) throws SQLException {
			String sql = "insert into courses values(?, ?, ?, ?)";
			
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setInt(1,id);
			pst.setString(2, name);
			pst.setInt(3, duration);
			pst.setInt(4, fee);
			
			positive_Negative_Test(pst, expect);
	
			return id;
		}
		
		public void course_Insert_NULL(String id, String name, int duration, int fee, String expect) throws SQLException {
			String sql = "insert into student(studentId, studentName, age, courseId) Values(?, ?, ?, ?)";
			
			PreparedStatement pst = con.prepareStatement(sql);
			if(id.equalsIgnoreCase("NULL")){
			    pst.setNull(2, Types.NULL);
			} else {
				assert false;
			}
			pst.setString(2, name);
			pst.setInt(3, duration);
			pst.setInt(4, fee);
			
			positive_Negative_Test(pst, expect);
		}
		
		public void course_Insert_NULLDuration(int id, String name, String duration, int fee, String expect) throws SQLException {
			String sql = "insert into student(studentId, studentName, age, courseId) Values(?, ?, ?, ?)";
			
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1,  id);
			pst.setString(2, name);
			if(duration.equalsIgnoreCase("NULL")){
			    pst.setNull(3, Types.NULL);
			} else {
				assert false;
			}
			pst.setInt(4, fee);
			
			
			positive_Negative_Test(pst, expect);
		}
		
		public void course_Delete(int id) throws SQLException {
			String sql2 = "delete from courses where courseId = ?";
			
			PreparedStatement pst2 = con.prepareStatement(sql2);
			pst2.setInt(1, id);

			int value2 = pst2.executeUpdate();
			Assert.assertEquals(value2, 1);
		}
		
		public int student_Insert(int id, String name, int age, int courseId, String expect) throws SQLException {
			String sql = "insert into student(studentId, studentName, age, courseId) Values(?, ?, ?, ?)";
			
			PreparedStatement pst = con.prepareStatement(sql);
			
			pst.setInt(1,id);
			pst.setString(2, name);
			pst.setInt(3, age);
			pst.setInt(4, courseId);
			
			positive_Negative_Test(pst, expect);
	
			return id;
		}
		
		public void student_Insert_NULL(int id, String name, int age, int courseId, String expect) throws SQLException {
			String sql = "insert into student(studentId, studentName, age, courseId) Values(?, ?, ?, ?)";
			
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			if(name.equalsIgnoreCase("NULL")){
			    pst.setNull(2, Types.NULL);
			} else {
				assert false;
			}
			pst.setInt(3, age);
			pst.setInt(4, courseId);
			
			positive_Negative_Test(pst, expect);
		}
		
		public void student_Delete(int id) throws SQLException {
			String sql = "delete from student where studentId = ?";
			
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);

			int value = pst.executeUpdate();
			Assert.assertEquals(value, 1);
		}	
}