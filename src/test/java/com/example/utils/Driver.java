package com.example.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;


public final class Driver {

    private static final ThreadLocal<WebDriver> DRIVER_POOL = new ThreadLocal<>();
    private static final Properties PROPERTIES = new Properties();
    private static final String BROWSER = getProperty("test.browser");
    private static final int DEFAULT_WAIT_TIME = Integer.parseInt(getProperty("default.wait.time"));
    private static final String DEFAULT_BROWSER = getProperty("test.browser.default");
    private static final boolean ENABLE_GRID = Boolean.parseBoolean(getProperty("test.grid.enable"));
    private static final String GRID_URL = getProperty("test.grid.hub.url");

    static {
        try {
            FileInputStream file = new FileInputStream("config.properties");
            PROPERTIES.load(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to load information from .properties file
     */
    public static void loadProperties(String fileName) {
        try {
            FileInputStream file = new FileInputStream(fileName);
            PROPERTIES.load(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Private constructor to restrict access of object creation
     */
    private Driver() {
    }

    /**
     * Method to load .properties file to Properties object
     */
    public static void readDataFromProperties(String fileName) {
        try {
            FileInputStream file = new FileInputStream(fileName);
            PROPERTIES.load(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to return value for given key from .properties file
     */
    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }


    public static WebDriver createDriver() throws Exception {
        return ENABLE_GRID ? createRemoteDriver() : createLocalDriver();
    }

    /**
     * Method to do the initial setup of remote WebDriver
     */
    private static WebDriver createRemoteDriver() throws MalformedURLException {
        String remoteUrl = GRID_URL;
        RemoteWebDriver webDriver = null;
        if (BROWSER.isBlank()) {
            remoteUrl = DEFAULT_BROWSER;
            ChromeOptions chromeOptions = new ChromeOptions();
            webDriver = new RemoteWebDriver(new URL(remoteUrl), chromeOptions);
            webDriver.setFileDetector(new LocalFileDetector());
        } else {
            switch (BROWSER.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    webDriver = new RemoteWebDriver(new URL(remoteUrl), chromeOptions);
                    webDriver.setFileDetector(new LocalFileDetector());
                    break;
                case "firefox":
                    FirefoxOptions fireFoxOptions = new FirefoxOptions();
                    webDriver = new RemoteWebDriver(new URL(remoteUrl), fireFoxOptions);
                    webDriver.setFileDetector(new LocalFileDetector());
                    break;
                case "safari":
                    SafariOptions safariOptions = new SafariOptions();
                    webDriver = new RemoteWebDriver(new URL(remoteUrl), safariOptions);
                    webDriver.setFileDetector(new LocalFileDetector());
                    break;
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    webDriver = new RemoteWebDriver(new URL(remoteUrl), edgeOptions);
                    webDriver.setFileDetector(new LocalFileDetector());
                    break;
                case "opera":
                    OperaOptions operaOptions = new OperaOptions();
                    webDriver = new RemoteWebDriver(new URL(remoteUrl), operaOptions);
                    webDriver.setFileDetector(new LocalFileDetector());
                    break;

            }
        }
        if (webDriver != null) {
            maxWindow(webDriver);
        }
        return webDriver;
    }

    /**
     * Method to do the initial setup of local WebDriver
     */
    private static WebDriver createLocalDriver() throws Exception {
        if (DRIVER_POOL.get() == null) {
            switch (BROWSER.toLowerCase()) {
                case "chrome":
                    DRIVER_POOL.set(WebDriverManager.chromedriver().create());
                    break;
                case "firefox":
                    DRIVER_POOL.set(WebDriverManager.firefoxdriver().create());
                    break;
                case "safari":
                    DRIVER_POOL.set(WebDriverManager.safaridriver().create());
                    break;
                case "edge":
                    DRIVER_POOL.set(WebDriverManager.edgedriver().create());
                    break;
                case "opera":
                    DRIVER_POOL.set(WebDriverManager.operadriver().create());
                    break;
                case "chromeheadless":
                    WebDriverManager.chromedriver().setup();
                    DRIVER_POOL.set(new ChromeDriver(new ChromeOptions().setHeadless(true)));
                    break;
            }
            maxWindow(DRIVER_POOL.get());
        }
        return DRIVER_POOL.get();
    }

    /**
     * Method to return driver
     */
    public static WebDriver getDriver() {
        if (DRIVER_POOL.get() != null) {
            return DRIVER_POOL.get();
        } else {
            throw new RuntimeException("Webdriver is null.");
        }
    }

    /**
     * Method to do maximize the browser window
     */
    private static void maxWindow(WebDriver driver) {
        driver.manage().window().maximize();
    }

    /**
     * Method to read data from Excel file and return Object[][]
     */
    public static Object[][] readXLSX() {
        Object[][] testData = null;
        try {
            File file = new File(getProperty("excelFilePath"));
            FileInputStream fis = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(fis);
            Sheet sheet = wb.getSheet(getProperty("excelSheetName"));
            int totalRows = sheet.getLastRowNum() + 1;
            Row rowCells = sheet.getRow(0);
            int totalColumns = rowCells.getLastCellNum();
            DataFormatter format = new DataFormatter();
            testData = new Object[totalRows][totalColumns];
            for (int i = 0; i < totalRows; i++) {
                for (int j = 0; j < totalColumns; j++) {
                    testData[i][j] = format.formatCellValue(sheet.getRow(i).getCell(j));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testData;
    }

    /**
     * Method that accepts sheet name as an argument to read data from Excel file and return Object[][]
     */
    public static Object[][] readXLSX(String sheetName) {
        Object[][] testData = null;
        try {
            File file = new File(getProperty("excelFilePath"));
            FileInputStream fis = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(fis);
            Sheet sheet = wb.getSheet(sheetName);
            int totalRows = sheet.getLastRowNum() + 1;
            Row rowCells = sheet.getRow(0);
            int totalColumns = rowCells.getLastCellNum();
            DataFormatter format = new DataFormatter();
            testData = new Object[totalRows][totalColumns];
            for (int i = 0; i < totalRows; i++) {
                for (int j = 0; j < totalColumns; j++) {
                    testData[i][j] = format.formatCellValue(sheet.getRow(i).getCell(j));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testData;
    }

    /**
     * Method that accepts File path and Sheet name as arguments to read data from Excel file and return Object[][]
     */
    public static Object[][] readXLSX(String filePath, String sheetName) {
        Object[][] testData = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(fis);
            Sheet sheet = wb.getSheet(sheetName);
            int totalRows = sheet.getLastRowNum() + 1;
            Row rowCells = sheet.getRow(0);
            int totalColumns = rowCells.getLastCellNum();
            DataFormatter format = new DataFormatter();
            testData = new Object[totalRows][totalColumns];
            for (int i = 0; i < totalRows; i++) {
                for (int j = 0; j < totalColumns; j++) {
                    testData[i][j] = format.formatCellValue(sheet.getRow(i).getCell(j));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testData;
    }

    /**
     * Method to read data from CSV file and return ArrayList
     */
    public static List<String> readCSV(int dataColumn) {
        List<String> testData = new ArrayList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(getProperty("csvFilePath")));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                testData.add(values[dataColumn]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testData;
    }

    /**
     * Method to read data from CSV file and return HashMap
     */
    public static Map<String, String> readCSV(int keyColumn, int valueColumn) {
        Map<String, String> testData = new HashMap<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(getProperty("csvFilePath")));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                testData.put(values[keyColumn], values[valueColumn]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testData;
    }

    /**
     * Method to take full screenshot
     */
    public static void captureScreen() {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
            LocalTime localTime = LocalTime.now();
            String minSec = dtf.format(localTime).replace(":", "");
            File file = ((TakesScreenshot) DRIVER_POOL.get()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, new File(getProperty("screenShotSavePath") + minSec + getProperty("screenShotExtension")));
            sleep(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to take screenshot of Entire page
     */
    public static void captureEntirePage() {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
            LocalTime localTime = LocalTime.now();
            String minSec = dtf.format(localTime).replace(":", "");
            Screenshot ss = new AShot().takeScreenshot(DRIVER_POOL.get());
            ImageIO.write(ss.getImage(), getProperty("screenShotExtension"), new File(getProperty("screenShotSavePath") + minSec));
            sleep(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to take screenshot of WebElement
     */
    public static void captureElement(WebElement element) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
            LocalTime localTime = LocalTime.now();
            String minSec = dtf.format(localTime).replace(":", "");
            Screenshot ss = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(DRIVER_POOL.get(), element);
            ImageIO.write(ss.getImage(), getProperty("screenShotExtension"), new File(getProperty("screenShotSavePath") + minSec));
            sleep(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to highlight a WebElement and take screenshot
     */
    public static void captureHighlighted(WebElement element) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
            LocalTime localTime = LocalTime.now();
            String minSec = dtf.format(localTime).replace(":", "");
            JavascriptExecutor jse = (JavascriptExecutor) DRIVER_POOL.get();
            if (getProperty("needBackground").equalsIgnoreCase("yes")) {
                jse.executeScript("arguments[0].setAttribute('style', 'border:" + getProperty("highlightBorderSize") + "px solid " + getProperty("highlightBorderColor") + "; background:" + getProperty("backgroundColor") + "')", element);
            } else if (getProperty("needBackground").equalsIgnoreCase("no")) {
                jse.executeScript("arguments[0].style.border='" + getProperty("highlightBorderSize") + "px solid " + getProperty("highlightBorderColor") + "'", element);
            }
            sleep(1);
            File file = ((TakesScreenshot) DRIVER_POOL.get()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, new File(getProperty("screenShotSavePath") + minSec + getProperty("screenShotExtension")), true);
            sleep(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to verify the Title of the page
     */
    public static boolean verifyTitle(String expected) {
        return DRIVER_POOL.get().getTitle().equals(expected);
    }

    /**
     * Method to select all options
     */
    public static void selectAll(Select dropDown) {
        for (WebElement each : dropDown.getOptions()) {
            each.click();
        }
    }

    /**
     * Method to adjust implicit wait time
     */
    public static void waitImplicit() {
        DRIVER_POOL.get().manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIME, TimeUnit.SECONDS);
    }

    /**
     * Method to wait explicitly until the Title loads
     */
    public static void waitForTitle(String expectedTitle) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), DEFAULT_WAIT_TIME);
        wait.until(ExpectedConditions.titleIs(expectedTitle));
    }

    /**
     * Method to wait explicitly until the WebElement is visible
     */
    public static void waitUntilVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), DEFAULT_WAIT_TIME);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Method to wait explicitly until the WebElement is invisible
     */
    public static void waitUntilInvisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), DEFAULT_WAIT_TIME);
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Method to wait explicitly until the WebElement is clickable
     */
    public static void waitUntilClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), DEFAULT_WAIT_TIME);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Method to wait until the page is loaded
     */
    public static void waitPageLoad() {
        if (Driver.getDriver() instanceof ChromeDriver) {
            ChromeOptions options = new ChromeOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        } else if (Driver.getDriver() instanceof FirefoxDriver) {
            FirefoxOptions options = new FirefoxOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        } else if (Driver.getDriver() instanceof EdgeDriver) {
            FirefoxOptions options = new FirefoxOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        }
    }

    /**
     * Method to adjust implicit wait time
     */
    public static void waitPageLoadDefaultTime() {
        DRIVER_POOL.get().manage().timeouts().pageLoadTimeout(DEFAULT_WAIT_TIME, TimeUnit.SECONDS);
    }

    /**
     * Method to adjust thread sleep time
     */
    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to perform blank click on webpage
     */
    public static void blankClick() {
        Driver.getDriver().findElement(By.xpath("//html")).click();
    }

    /**
     * Method to terminate the WebDriver instance
     */
    public static void quitDriver() {
        if (DRIVER_POOL.get() != null) {
            DRIVER_POOL.get().quit();
        }
    }

    /**
     * Method to remove value from ThreadLocal
     */
    public static void removeDriver() {
        if (DRIVER_POOL.get() != null) {
            DRIVER_POOL.remove();
        }
    }

    public static void cleanUpDriver() {
        quitDriver();
        removeDriver();
    }

    /**
     * Method to kill session after test
     */
    public static void closeDriver() {
        if (DRIVER_POOL.get() != null) {
            DRIVER_POOL.get().quit();
            DRIVER_POOL.remove();
        }
    }

}