package resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReporterNG {
	
	static ExtentReports extent;
	
	public static ExtentReports getReportObject()
	{
		String path = System.getProperty("user.dir")+"\\reports\\Spark.html";
		ExtentSparkReporter spark = new ExtentSparkReporter(path);
		
		spark.config().setTheme(Theme.DARK);
		spark.config().setDocumentTitle("Database JDBC Project");
		spark.config().setReportName("Database JDBC Report");
		
		extent = new ExtentReports();
		extent.attachReporter(spark);
		extent.setSystemInfo("Tester", "Slomo Chacko");
		
		return extent;	
	}
}
