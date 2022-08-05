package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utils.BaseClass;

public class LoginPage extends BaseClass{
	
	//private WebDriver driver;

	@FindBy(css="input[placeholder='Username']") WebElement username;
	@FindBy(css="input[placeholder='Password']") WebElement password;
	@FindBy(id="login-button") WebElement login;
	
	
	public LoginPage(WebDriver driver) {
		//this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	
	public void getLogin(){
		 click(login);
	}
	
	
	public void getUsername(String user){
		sendKeys(username, user);	
	}
	

	public void getPassword(String pass){
		sendKeys(password, pass);
	}
	
}
