package com.alec.solenium;


import static org.junit.Assert.fail;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.google.gson.Gson;

public class neweggScraper {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private ArrayList<Item> items = new ArrayList<Item>();
  private int itemIndex = -1; 		// replace with iterator
  
  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://promotions.newegg.com/neemail/latest/index-landing.aspx";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void test() throws Exception {
	  // open the base url
	  driver.get(baseUrl + "/");
	  
	  // degbug
	  int titleCount = 0;
	  int regPriceCount = 0;
	  int newPriceCount = 0;
	  System.out.println("Start scraping titles " + System.currentTimeMillis() / 1000);
	  // scrape each title
	  for (WebElement element : driver.findElements(By.cssSelector("a[rel=nofollow]"))) {
		  if (element.getAttribute("target").contains("_blank")
				  && !element.getAttribute("style").contains("text-decoration")
				  && !element.getAttribute("style").contains("background")
				  && !element.getText().equals("")
				  && element.getText() != null
				  && element.getText().length() > 4) {	// find a better test 
			  items.add(new Item(element.getText()));
			  itemIndex++;
		  }
	  }
	  System.out.println("Done scraping titles " + System.currentTimeMillis() / 1000);
	  titleCount = itemIndex;
	  itemIndex = 0;
	  System.out.println("titles found: " + items.size());
	  // scrape each rpice
	  for (WebElement element : driver.findElements(By.className("RegularPrice"))) {
		  items.get(itemIndex).setRegPrice(element.getText());
		  itemIndex++;
	  }
	  System.out.println("Done scraping regPrices " + System.currentTimeMillis() / 1000);
	  regPriceCount = itemIndex;
	  itemIndex = 0;
	  // scrape each new price
	  for (WebElement element : driver.findElements(By.className("YourPrice"))) {
		  items.get(itemIndex).setNewPrice(element.getText());
		  itemIndex++;
	  }
	  System.out.println("Done scraping newPrices " + System.currentTimeMillis() / 1000);
	  newPriceCount = itemIndex;
	  itemIndex = 0;
	  System.out.println("Counts: " + titleCount + " " + regPriceCount + " " + newPriceCount);
	  
	  // display each item
	  for (Item item : items) {
		  System.out.println("title: " + item.getTitle());
		  System.out.println("regPrice: " + item.getRegPrice());
		  System.out.println("newPrice: " + item.getNewPrice());
	  }
	  
		  
		Gson gson = new Gson();
		//GsonBuilder gsonBuilder = new GsonBuilder();
		
		FileWriter writer = new FileWriter("walmartCoupons.json");
		
		//writer.write(gson.toJson(aggregateCoupons));
		
		writer.close();
  }

  private void print(String desc, String text) {
	System.out.println(desc + ": " + text);
}

@After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
