package com.organization.name.myProject1;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;



import java.text.SimpleDateFormat;
import java.util.Calendar;


public class RoundTripTest {

    // Use the application driver
    WebDriver driver;
    LandingPage onLandingPage;
    FlightsSearchPage onFlightsSearchPage;
    SearchResultsPage onResultsPage;
    BookingDetailsPage bookingpage;


    
    @BeforeMethod
    
      public void setup() throws InterruptedException{
        //launch the application under test
        System.setProperty("webdriver.gecko.driver","/Users/srnvam/Downloads/geckodriver");
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);
        driver = new FirefoxDriver(capabilities);
        driver.get("http://www.cleartrip.com/");
        Thread.sleep(30000);
        onLandingPage = new LandingPage(driver);
        onFlightsSearchPage = onLandingPage.goToFlightsSearchPage();
        

    }

    


    
	@Test
    public void testRoundjourney() throws InterruptedException{

        onFlightsSearchPage.chooseToHaveAReturnJourney();
        onFlightsSearchPage.enterOriginAs("Bangalore");
        onFlightsSearchPage.enterDestinationAs("New Delhi");
        onFlightsSearchPage.enterDepartureDateAs(tomorrow());
        onFlightsSearchPage.enterReturnDateAs(dayAfterTomorrow());
        onFlightsSearchPage.selectAdult("1");
        onResultsPage = onFlightsSearchPage.searchForTheJourney();
        SearchResultsPage searchresults= new SearchResultsPage(driver);
        
        Assert.assertEquals("Cleartrip | Bangalore ⇄ New Delhi", driver.getTitle().toString());
        
       
       Assert.assertTrue(searchresults.getBookingDetails().contains("Bangalore ⇄ New Delhi"));
        Assert.assertTrue(searchresults.getFromDetails().contains("Bangalore → New Delhi"));
        Assert.assertTrue(searchresults.gettodetails().contains("New Delhi → Bangalore"));
        String amount=searchresults.getAmount();
        
        bookingpage= searchresults.clickBookLink();
        BookingDetailsPage bookingpage= new BookingDetailsPage(driver);
       
        Assert.assertEquals("Cleartrip | Book your flight securely in simple steps", driver.getTitle().toString());
        Assert.assertTrue(bookingpage.getRefund().contains("REFUNDABLE"));
        
        bookingpage.selectCheckbox();
        bookingpage.clickBookingButton();
        Thread.sleep(10000);
        bookingpage.enterEmailAddress("cnuchella2@gmail.com");
        bookingpage.emailContinueClick();
        Thread.sleep(10000);
        bookingpage.enterTravllerDetails("Mr", "srinu", "rahul", "9886199969");
        bookingpage.clikTravelerContinue();
        
   }


    @AfterMethod
    public void teardown(){
        //close the browser
        driver.quit();

    }


    public String tomorrow(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        return new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
    }

    public String dayAfterTomorrow(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 2);
        return new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
    }

    private String currentDatePlus(int numberOfDays) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, numberOfDays);
        return new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
    }


}
