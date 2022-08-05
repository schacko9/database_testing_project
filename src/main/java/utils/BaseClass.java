package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BaseClass{

	public void click(WebElement locator) {
		WebDriverWait wait = new WebDriverWait(DriverEngine.driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}
	
	public void sendKeys(WebElement locator, String text) {
		WebDriverWait wait = new WebDriverWait(DriverEngine.driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(locator)).sendKeys(text);
	}
	
	public String get_Text(WebElement locator) {
		WebDriverWait wait = new WebDriverWait(DriverEngine.driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOf(locator));
        return locator.getText();
	}
	public int get_Size(List<WebElement> locator) {
		WebDriverWait wait = new WebDriverWait(DriverEngine.driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfAllElements(locator));
        return locator.size();
	}

	
	public static String getProperty(String key) throws Exception {
		String src = System.getProperty("user.dir")+"\\src\\main\\resources\\resources\\data.properties";
		FileInputStream fis = new FileInputStream(src);
		Properties prop = new Properties();

		prop.load(fis);
		String value = prop.get(key).toString().toLowerCase();
		
		if(StringUtils.isEmpty(value)) {
			throw new Exception("Value is not present: "+ key + ", in data property file");
		}
		
		return value;
	}
	
	public String getScreenShotPath(String testCaseName, WebDriver driver) throws IOException{
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destinationFile = System.getProperty("user.dir")+"\\reports\\"+testCaseName+".png";
		FileUtils.copyFile(source,new File(destinationFile));
		
		return destinationFile;
	}
}
