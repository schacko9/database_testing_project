package pageObjects;

import java.util.List;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utils.BaseClass;


public class PCPage extends BaseClass{

	
	@FindBy(css=".inventory_item_name") List<WebElement> productsTitle;
	@FindBy(css=".btn_inventory") List<WebElement> add;
	@FindBy(css=".shopping_cart_link") WebElement cart;
	@FindBy(css=".checkout_button") WebElement checkout;
	
	@FindBy(css="input[placeholder='First Name']") WebElement first;
	@FindBy(css="input[placeholder='Last Name']") WebElement last;
	@FindBy(css="input[placeholder='Zip/Postal Code']") WebElement zip;
	@FindBy(id="continue") WebElement cont;

	@FindBy(css=".summary_total_label") WebElement total;

	
	
	public PCPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	

	public void getProduct(String item){
		for(int i=0; i<productsTitle.size(); i++) {
			WebElement product = productsTitle.get(i);
			String productText = get_Text(product);
			if(productText.equalsIgnoreCase(item)){
				 click(add.get(i));
			}
		}
	}
	
	public void getCart(){
		click(cart);
	}
	
	public void getCheckout(){
		click(checkout);
	}
	
	public void getFirstName(String text){
		sendKeys(first, text);
	}
	
	public void getLastName(String text){
		sendKeys(last, text);
	}
	
	public void getZIP(String text){
		sendKeys(zip, text);
	}
	
	public void getContinue(){
		click(cont);
	}
	
	public String getTotal(){
		String totalText = get_Text(total);
		String t = totalText.split(Pattern.quote("$"))[1];
		return t;
	}
	
}
