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

public class TestSet {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private ArrayList<Coupon> pageCoupons = new ArrayList<Coupon>();
  private ArrayList<Coupon> aggregateCoupons = new ArrayList<Coupon>();
  private int couponIndex = -1;
  
  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://coupons.walmart.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testGotoPage2() throws Exception {
	  // open the base url
	  driver.get(baseUrl + "/");
	  // click each link
	  for (int index = 0; index < 100; index++) {
		
	  	for (WebElement h4 : driver.findElements(By.className("offer-product"))) {
			pageCoupons.add(new Coupon(h4.getText()));
			couponIndex++;
			print("title", h4.getText());
		
		}
		couponIndex = 0;
		for (WebElement h3 : driver.findElements(By.className("offer-value"))) {
			pageCoupons.get(couponIndex).setDiscount(h3.getText());
			couponIndex++;
			print("value", h3.getText());
		
		}
		couponIndex = 0;
		for (WebElement p : driver.findElements(By.className("offer-details"))) {
			pageCoupons.get(couponIndex).setDesc(p.getText());
			couponIndex++;
			print("details", p.getText());
		}
		couponIndex = -1;
		
		aggregateCoupons.addAll(pageCoupons);
		pageCoupons = new ArrayList<Coupon>();
		driver.findElement(By.linkText("Next")).click();
	  }
		  
		Gson gson = new Gson();
		//GsonBuilder gsonBuilder = new GsonBuilder();
		
		FileWriter writer = new FileWriter("walmartCoupons.json");
		
		writer.write(gson.toJson(aggregateCoupons));
		
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
