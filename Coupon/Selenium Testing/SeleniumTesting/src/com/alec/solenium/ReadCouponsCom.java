package com.alec.solenium;


import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;

public class ReadCouponsCom {
  private FirefoxDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  private ArrayList<Coupon> coupons = new ArrayList<Coupon>();
  private int couponIndex = -1;
  private JavaScriptExecutor jsExec;
  
  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://www.coupons.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void test() throws Exception {
	  // open the base url
	  driver.get(baseUrl + "/");
	  System.out.println(driver.executeScript("return APP_COUPONSINC;"));
	  
	  
	  /** /
	  for (WebElement element : driver.findElements(By.tagName("script"))) {
		  System.out.println("script: " + element.getText());
	  }
	  
	  /** /
	  // display all coupons
	  while (isElementPresent(By.className("primary")) && driver.findElement(By.className("primary")).isDisplayed() ) {
			driver.findElement(By.className("primary")).click();
			System.out.println("Begin waiting:" + System.currentTimeMillis());
			WebDriverWait wait = new WebDriverWait(driver, 10);// 1 minute 
			try {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("primary")));
			} catch(Exception e) {
				e.printStackTrace();
			}
			//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			System.out.println("End waiting: " + System.currentTimeMillis());
	  }
	  	for (WebElement h5 : driver.findElements(By.className("brand"))) {
	  		coupons.add(new Coupon(h5.getText()));
			couponIndex++;
			print("title", h5.getText());
		}
		couponIndex = 0;
		for (WebElement h4 : driver.findElements(By.className("summary"))) {
			coupons.get(couponIndex).setDiscount(h4.getText());
			couponIndex++;
			print("value", h4.getText());
		}
		couponIndex = 0;
		for (WebElement p : driver.findElements(By.className("details"))) {
			coupons.get(couponIndex).setDesc(p.getText());
			couponIndex++;
			print("details", p.getText());
		}
		
		
		Gson gson = new Gson();
		//GsonBuilder gsonBuilder = new GsonBuilder();
		
		FileWriter writer = new FileWriter("couponsComCoupons.json");
		
		writer.write(gson.toJson(coupons));
		
		writer.close();
		
	  /**/
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
