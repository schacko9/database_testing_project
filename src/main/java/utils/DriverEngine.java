package utils;

import java.util.Objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverEngine {
	
	public static WebDriver driver;
	
	public static void initDriver() throws Exception {
		if(Objects.isNull(driver)) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.get(BaseClass.getProperty("url"));
		}	
	}
	
	public static void quitDriver() {
		if(Objects.nonNull(driver)) {
			driver.quit();
			driver=null;
		}
	}	
}