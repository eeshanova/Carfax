package io.duotech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;


public class CarfaxUsedCarSearch {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\SeleniumFiles\\BrowserDrivers\\chromedriver.exe");
		
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		
		//Step 1 - Go  to carfax.com
		driver.get("https://www.carfax.com/");
		
		//Step 2 - Click on Find a Used Car
		driver.findElement(By.linkText("Find a Used Car")).click();
		
		//Step 3 - Verify the page title contains the word “Used Cars”
		if (driver.getTitle().contains("Used Cars")) {
			System.out.println("PASS");
		}
		else {
			System.err.println("FAIL");
		}
		
		Thread.sleep(1000);
		
		//Step 4 - Choose “Tesla” for  Make.
		Select make = new Select(driver.findElement(By.name("make")));
		make.selectByValue("Tesla");
		
		//Step 5 - Verify that the Select Model dropdown box contains 3 current Tesla models - (Model 3, Model S, Model X).
		Thread.sleep(1000);
		Select model = new Select(driver.findElement(By.name("model")));
		List<String> expectedOptions = Arrays.asList("Model 3", "Model S", "Model X");
		
		List<WebElement> options = driver.findElements(By.xpath("//select[@name='model']//optgroup[@label='Current Models']"));
		ArrayList<String> currentOptions = new ArrayList<>();
		for (WebElement element: options) {
			String finalElements = String.valueOf((element.getText().trim()));
			currentOptions.add(finalElements);
		}
	
		boolean pass = true;
		
		for (int i = 0; i < options.size(); i++) {
			
			if(!options.get(i).getText().contains(expectedOptions.get(i))) {
				pass = false;
			}
			
		}
		
		if(pass) {
			System.out.println("PASS");
		}else {
			System.err.println("FAIL");
		}

		//Step 6 - Choose “Model S” for Model.
		model.selectByValue("Model S");
		
		//Step 7 - Enter the zipcode as 22182 and click Next
		driver.findElement(By.xpath("//*[@id=\"makeModelPanel\"]/form/div[3]/div/div[4]/div/input")).sendKeys("22182");
				
		driver.findElement(By.id("make-model-form-submit")).click();
		
		//Step 8 - Verify that the page has “Step 2 – Show me cars with” text
		if (driver.getPageSource().contains("Step 2 - Show me cars with")) {
			System.out.println("PASS");
		}
		else {
			System.err.println("FAIL");
		}
		
		//Step 9 - Click on all 4 checkboxes.
		WebElement checkboxOption = driver.findElement(By.xpath("//ul[@class = 'checkbox-list checkbox-list--left checkbox-list--large list-unstyled']"));
		List<WebElement> checkbox = checkboxOption.findElements(By.tagName("li"));
		for (WebElement webElements : checkbox) {
			if(!webElements.isSelected())	{
				Thread.sleep(1000);
				webElements.click();
			}
		}

		//Step 10 - Save the result of “Show me X Results” button to a variable. 
		String result = driver.findElement(By.xpath("//span[@class='totalRecordsText']")).getText();
		System.out.println(result);
		
		//Step 11 - Click on “Show me x Results” button. 
		driver.findElement(By.xpath("//button[@class = 'button large primary-green']")).click();
		Thread.sleep(1000);
		
		//Step 12 - On the next page, verify that the results page has the same number of results as indicated in Step 10 
		//by extracting the number and comparing the result
		String confirmResult = driver.findElement(By.id("totalResultCount")).getText();
		System.out.println(confirmResult);
		
		if(result.equals(confirmResult)) {
			System.out.println("PASS");
		}
		else {
			System.err.println("FAIL");
		
		}
		
		//Step 13 - Verify the results also by getting the actual number of results displayed in the page with the number in the Step 10. 
		//For this step get the count the number of WebElements related to each result.
		
		WebElement elementOptions = driver.findElement(By.xpath("//div[@class='srp-list-container']"));
		List<WebElement> numberOfElements = elementOptions.findElements(By.className("srp-list-item"));
		int countElements = numberOfElements.size();
		
		if (countElements == Integer.parseInt(result)) {
			System.out.println("PASS");
		}
		else {
			System.err.println("FAIL");
		}
		
		//Step 14 - Verify that each result contains String “Tesla Model S”.
		String verify = "Tesla Model S";
//		for (WebElement element : numberOfElements) {
//			System.out.println(element.getText());
//		}
		
		boolean check = true;
		for (int i = 0; i < numberOfElements.size(); i++) {
			
			if(!numberOfElements.get(i).getText().contains(verify)) {
				check = false;
			}
		}
		
		if(check) {
			System.out.println("PASS");
		}else {
			System.err.println("FAIL");
		}
		
		//Step 15 - Get the price of each result and save them into a list in the order of their appearance.
		List<WebElement> price = driver.findElements(By.xpath("//span[@class='srp-list-item-price']"));
		ArrayList<Integer> carPrices = new ArrayList<>();
		for (WebElement element : price) {
			int finalPrice = Integer.parseInt((element.getText().replaceAll("[Price , $ :]", "")).trim());
//			System.out.println(finalPrice);
			carPrices.add(finalPrice);					
		}
		Collections.sort(carPrices);
		Collections.reverse(carPrices);
		System.out.println(carPrices);
		
		//Step 16 - Choose “Price - High to Low” option from Sort menu
		Select selection = new Select(driver.findElement(By.xpath("//select[@class = 'srp-header-sort-select']")));
		selection.selectByVisibleText("Price - High to Low ");
		Thread.sleep(1000);
		//Step 17 - Verify that the results are displayed from high to low price. 
		ArrayList<Integer> obtainedPrices = new ArrayList<>();
		List<WebElement> elementList = driver.findElements(By.xpath("//span[@class='srp-list-item-price']"));
		for(WebElement element : elementList){
			int finalPrice = Integer.parseInt((element.getText().replaceAll("[Price , $ :]", "")).trim());
			obtainedPrices.add(finalPrice);
			}
		System.out.println(obtainedPrices);

		if (carPrices.equals(obtainedPrices)) {
			System.out.println("PASS");
		}else {
			System.err.println("FAIL");
		}

		//Step 18 - Choose “Mileage - Low to High” option from Sort menu

		selection.selectByVisibleText("Mileage - Low to High ");
		Thread.sleep(1000);
		List<WebElement> milage = driver.findElements(By.cssSelector("*> div.srp-list-item__mainInfo > div.srp-list-item-basic-info.srp-list-item-special-features > span:nth-child(1)"));
		List<Integer> milages = new ArrayList<Integer>();
		for (WebElement element : milage) {
//			String milageInResultOrder = element.getText();
//			String finalResult = milageInResultOrder.substring(9, milageInResultOrder.length()-6);
//			int finalMilage = Integer.parseInt(finalResult.replaceAll(",", ""));
			int finalMilage = Integer.parseInt((element.getText().replaceAll("[Mileage , : miles]", "")).trim());
			milages.add(finalMilage);
		}

		System.out.println(milages);
		
		//Step 19 - Verify that the results are displayed from low to high mileage. 
		ArrayList<Integer> milageResult = new ArrayList<>();
		for (WebElement element : milage) {
			int finalMilage = Integer.parseInt((element.getText().replaceAll("[Mileage , : miles]", "")).trim());
			milageResult.add(finalMilage);
		}
		Collections.sort(milageResult);
		System.out.println(milageResult);
		if (milageResult.equals(milages)) {
			System.out.println("PASS");
		}else {
			System.err.println("FAIL");
		}
				
		//Step 20 - Choose “Year - New to Old” option from Sort menu 
		
		selection.selectByVisibleText("Year - New to Old ");
		Thread.sleep(1000);
		List<WebElement> year = driver.findElements(By.className("srp-list-item-basic-info-model"));
		List<Integer> years = new ArrayList<>();
		for (WebElement element : year) {
			String modifiedYear = element.getText();
			int finalResult = Integer.parseInt(modifiedYear.substring(0,4));
			years.add(finalResult);
		}
		System.out.println(years);
		
		//Step 21 - Verify that the results are displayed from new to old year.
		ArrayList<Integer> yearResult = new ArrayList<>();
		for (WebElement element : year) {
			String yearInResultOrder = element.getText();
			int finalResult = Integer.parseInt(yearInResultOrder.substring(0,4));
			yearResult.add(finalResult);
		}
			
		Collections.sort(yearResult);
		Collections.reverse(yearResult);
		System.out.println(yearResult);

		if (yearResult.equals(years)) {
			System.out.println("PASS");
		}else {
			System.err.println("FAIL");
		}
			
	}

}
