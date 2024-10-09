package com.example.TravelList;

import com.example.Modules.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.tuple.Pair;

public class AdminTravelListTest {

    // The Chrome WebDriver
    WebDriver driver;

    // The WebDriverWait object
    WebDriverWait wait;

    @Test
    public void testAdminTravelList() throws InterruptedException {

        // Maximize window
        driver.manage().window().maximize();

        // Navigate to the login screen for an admin
        AdminLogin.Execute(driver, wait);
        AdminClickLogin.Execute(wait);
        assertFalse(driver.getPageSource().contains("admin-panel"));

        // Wait for alert
        Thread.sleep(500);

        //FIRST ADD A ROUTE
        // Navigate to RouteList
        driver.findElement(By.className("am-icon-line-chart")).click();
        try {//handle optional not content alert
            Thread.sleep(100);
            Alert alt = driver.switchTo().alert();
            alt.accept();
        } catch(NoAlertPresentException noe) {
            // no alert mean there was content
        }
        assertTrue(driver.findElement(By.className("portlet-title")).getText().contains("Route"));
        String inputStations = "shanghai,suzhou";
        String inputStationsSearch = "[\"shanghai\",\"suzhou\"]";
        String inputDistances = "0,50";
        String inputStart = "shanghai";
        String inputTerminal = "suzhou";
        try {//handle optional not content alert
            Thread.sleep(500);
            driver.switchTo().alert().accept();
        } catch(NoAlertPresentException noe) {/* no alert mean there was content */}
        // Add route
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div[1]/div/div/div/button")).click();
        driver.findElement(By.id("add_route_stations")).sendKeys(inputStations);
        driver.findElement(By.id("add_route_distances")).sendKeys(inputDistances);
        driver.findElement(By.id("add_route_start_station")).sendKeys(inputStart);
        driver.findElement(By.id("add_route_terminal_station")).sendKeys(inputTerminal);
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/div[3]/div/div[3]/span[2]")).click();
        // Record the route's ID
        int routeRowNumber;
        routeRowNumber = SearchTable.Execute(wait, "/html/body/div/div[2]/div/div[2]/div[2]/div/form/table", inputStationsSearch, false, driver);
        assertNotEquals(-1, routeRowNumber);
        //HERE PLEASE ADD THE CODE TO GET THE ID which is the first item in this row
        String rowXPath = "/html/body/div/div[2]/div/div[2]/div[2]/div/form/table/tbody/tr[" + routeRowNumber + "]/td[2]";
        String sampleRouteID = driver.findElement(By.xpath(rowXPath)).getText();

        //Second add train
        driver.findElement(By.className("am-icon-table")).click();
        driver.findElement(By.className("am-icon-train")).click();
        try {//handle optional not content alert
            Thread.sleep(100);
            Alert alt = driver.switchTo().alert();
            alt.accept();
        } catch(NoAlertPresentException noe) {/*no alert mean there was content*/ }
        assertTrue(driver.findElement(By.className("portlet-title")).getText().contains("Train"));
        String sampleTrainType = "ChooChoo";
        String sampleCap = "123456789";
        String sampleSpeed = "1234";

        //Delete train if already exists
        int rowNumber;
        while ((rowNumber = SearchTable.Execute(wait, "/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/table", sampleTrainType, true, driver)) != -1){
            DeleteRecord.Execute(wait, rowNumber, "/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/table/tbody/tr[", "]/td[5]/div/div/button[2]", "/html/body/div[2]/div/div[3]/span[2]");
            DismissAlert.Execute(wait);
        }
        // Add train
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div[1]/div/div/div/button")).click();
        driver.findElement(By.id("add-train-type-name")).sendKeys(sampleTrainType);
        driver.findElement(By.id("add-train-economy-class")).sendKeys(sampleCap);
        driver.findElement(By.id("add-train-confort-class")).sendKeys(sampleCap);
        driver.findElement(By.id("add-train-average-speed")).sendKeys(sampleSpeed);
        driver.findElement(By.xpath("/html/body/div[4]/div/div[6]/span[2]")).click();
        DismissAlert.Execute(wait);


        //NOW ADD TRAVEL
        // Navigate to Contact List
        driver.findElement(By.className("am-icon-globe")).click();
        try {//handle optional not content alert
            Thread.sleep(500);
            driver.switchTo().alert().accept();
        } catch(NoAlertPresentException noe) {/* no alert mean there was content */}
        assertTrue(driver.findElement(By.className("portlet-title")).getText().contains("Travel"));

        String sampleTravelID = "G18005882300";
        String sampleStartStation = "shanghai";
        String sampleEndStation = "suzhou";

        // Add travel
        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div[1]/div/div/div/button")).click();

        driver.findElement(By.id("add_travel_id")).sendKeys(sampleTravelID);

        Select routes = new Select(driver.findElement(By.name("travel_route_id")));
        Select startStation = new Select(driver.findElement(By.name("travel_start_station")));
        Select stationName = new Select(driver.findElement(By.name("travel_station_name")));
        Select endStation = new Select(driver.findElement(By.name("travel_terminal_station")));
        Select train = new Select(driver.findElement(By.name("train_type_id")));

        routes.selectByValue(sampleRouteID);
        startStation.selectByValue(sampleStartStation);
        stationName.selectByValue(sampleEndStation);
        endStation.selectByValue(sampleEndStation);
        train.selectByValue(sampleTrainType);

        driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/div[3]/div/div[3]/span[2]")).click();
        DismissAlert.Execute(wait);
        Thread.sleep(3000);

        // Check for travel id
        rowNumber = SearchTable.Execute(wait, "/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/table", sampleTravelID, true);
        assertNotEquals(-1, rowNumber);

        // Test Delete Travel
        DeleteRecord.Execute(wait, rowNumber, "/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/table/tbody/tr[", "]/td[1]/div/div/button[2]", "/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/div[2]/div/div[3]/span[2]");

        DismissAlert.Execute(wait);
        driver.navigate().refresh();

        // Check for deleted record
        rowNumber = SearchTable.Execute(wait, "/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/table", sampleTravelID, false);
        assertEquals(-1, rowNumber);

        //NOW DELETE ROUTE FROM EARLIER
        driver.findElement(By.className("am-icon-line-chart")).click();
        assertTrue(driver.findElement(By.className("portlet-title")).getText().contains("Route"));
        DeleteRecord.Execute(wait, routeRowNumber, "/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/table/tbody/tr[", "]/td[1]/div/div/button[2]", "/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/div[2]/div/div[3]/span[2]");
        DismissAlert.Execute(wait);
        try {//handle optional no content alert
            Thread.sleep(100);
            Alert alt = driver.switchTo().alert();
            alt.accept();
        } catch(NoAlertPresentException noe) {/*no alert mean there was content*/ }

        //Now delete train from earlier
        driver.findElement(By.className("am-icon-table")).click();
        driver.findElement(By.className("am-icon-train")).click();
        rowNumber = SearchTable.Execute(wait, "/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/table", sampleTrainType, true, driver);
        assertNotEquals(-1, rowNumber);
        DeleteRecord.Execute(wait, rowNumber, "/html/body/div[1]/div[2]/div/div[2]/div[2]/div/form/table/tbody/tr[", "]/td[5]/div/div/button[2]", "/html/body/div[2]/div/div[3]/span[2]");
        DismissAlert.Execute(wait);
        try {//handle optional no content alert
            Thread.sleep(100);
            Alert alt = driver.switchTo().alert();
            alt.accept();
        } catch(NoAlertPresentException noe) {/*no alert mean there was content*/ }


        // Logout as an admin
        logout();
        assertEquals(GlobalVariables.getAdminLoginUrl(), driver.getCurrentUrl());
    }

    @BeforeEach
    public void setUpDriver(){
        Pair<WebDriver, WebDriverWait> pair = SetUpDriverChrome.Execute();
        driver = pair.getLeft();
        wait = pair.getRight();
    }

    @AfterEach
    public void tearDown() {
        TearDownDriver.Execute(driver);
    }

    /**
     * Logs out of TrainTicket
     */
    private void logout() {
        driver.findElement(By.className("am-icon-sign-out")).click();
    }
}

