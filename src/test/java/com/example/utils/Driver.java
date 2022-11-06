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
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
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

    private static final ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();
    private static final Properties properties = new Properties();
    private static final String browser;
    private static final String defaultBrowser;
    private static final int defaultWaitTime;
    private static final boolean enableGrid;
    private static final String gridUrl;

    static {
        try {
            FileInputStream file = new FileInputStream("config.properties");
            properties.load(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        defaultBrowser = getProperty("browserDefault").toLowerCase();
        defaultWaitTime = Integer.parseInt(getProperty("defaultWaitTime"));
        browser = getProperty("browser");
        enableGrid = Boolean.parseBoolean(getProperty("gridEnable").toLowerCase());
        gridUrl = getProperty("gridHubUrl");
    }

    /**
     * Private constructor to restrict access of object creation
     */
    private Driver() {
    }

    /**
     * Method to return value for given key from .properties file
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Method to create WebDriver object
     *
     * @throws Exception
     */
//    public static void createDriver() throws Exception {
//        switch (browser.toLowerCase()) {
//            case "chrome":
//                driverPool.set(createChromeDriver());
//                break;
//            case "firefox":
//                driverPool.set(createFirefoxDriver());
//                break;
//            default:
//                throw new Exception("Unknown browser type entered.");
//        }
//    }
    public static void createDriver() throws Exception {
        if (enableGrid) {
            createRemoteDriver();
        } else {
            createLocalDriver();
        }
    }

    /**
     * Method to do the initial setup of remote WebDriver
     *
     * @throws MalformedURLException
     */
    private static void createRemoteDriver() throws MalformedURLException {
        String remoteUrl = gridUrl;
        RemoteWebDriver driver = null;
        if (browser.isBlank()) {
            remoteUrl = defaultBrowser;
            ChromeOptions chromeOptions = new ChromeOptions();
            driver = new RemoteWebDriver(new URL(remoteUrl), chromeOptions);
            driver.setFileDetector(new LocalFileDetector());
        } else {
            switch (browser) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    driver = new RemoteWebDriver(new URL(remoteUrl), chromeOptions);
                    driver.setFileDetector(new LocalFileDetector());
                    break;
                case "firefox":
                    FirefoxOptions fireFoxOptions = new FirefoxOptions();
                    driver = new RemoteWebDriver(new URL(remoteUrl), fireFoxOptions);
                    driver.setFileDetector(new LocalFileDetector());
                    break;
                case "safari":
                    SafariOptions safariOptions = new SafariOptions();
                    driver = new RemoteWebDriver(new URL(remoteUrl), safariOptions);
                    driver.setFileDetector(new LocalFileDetector());
                    break;
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    driver = new RemoteWebDriver(new URL(remoteUrl), edgeOptions);
                    driver.setFileDetector(new LocalFileDetector());
                    break;
                case "opera":
                    OperaOptions operaOptions = new OperaOptions();
                    driver = new RemoteWebDriver(new URL(remoteUrl), operaOptions);
                    driver.setFileDetector(new LocalFileDetector());
                    break;
            }
        }
        if (driver != null) {
            maxWindow(driver);
        }
    }

    /**
     * Method to do the initial setup of local WebDriver
     * @throws Exception
     */
    private static WebDriver createLocalDriver() throws Exception {
        if (driverPool.get() == null) {
            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    driverPool.set(new ChromeDriver(chromeOptions));
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    driverPool.set(new FirefoxDriver(firefoxOptions));
                    break;
                case "safari":
                    WebDriverManager.safaridriver().setup();
                    SafariOptions safariOptions = new SafariOptions();
                    driverPool.set(new SafariDriver(safariOptions));
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    driverPool.set(new EdgeDriver(edgeOptions));
                    break;
                case "opera":
                    WebDriverManager.operadriver().setup();
                    OperaOptions operaOptions = new OperaOptions();
                    driverPool.set(new OperaDriver(operaOptions));
                    break;
                case "chromeheadless":
                    WebDriverManager.chromedriver().setup();
                    driverPool.set(new ChromeDriver(new ChromeOptions().setHeadless(true)));
                    break;
            }
            maxWindow(driverPool.get());
        }
        return driverPool.get();
    }

//    private static WebDriver createChromeDriver() throws MalformedURLException {
//        return enableGrid ? createRemoteChromeDriver() : createLocalChromeDriver();
//    }
//
//    private static WebDriver createFirefoxDriver() throws MalformedURLException {
//        return enableGrid ? createRemoteFirefoxDriver() : createLocalFirefoxDriver();
//    }
//
//    private static WebDriver createLocalChromeDriver() {
//        WebDriverManager.chromedriver().setup();
//        ChromeOptions chromeOptions = new ChromeOptions();
//        WebDriver driver = new ChromeDriver(chromeOptions);
//        maxWindow(driver);
//        return driver;
//    }
//
//    private static WebDriver createLocalFirefoxDriver() {
//        WebDriverManager.firefoxdriver().setup();
//        FirefoxOptions fireFoxOptions = new FirefoxOptions();
//        WebDriver driver = new FirefoxDriver(fireFoxOptions);
//        maxWindow(driver);
//        return driver;
//    }
//
//    private static WebDriver createRemoteChromeDriver() throws MalformedURLException {
//        ChromeOptions chromeOptions = new ChromeOptions();
//        RemoteWebDriver driver = new RemoteWebDriver(new URL(gridUrl), chromeOptions);
//        maxWindow(driver);
//        driver.setFileDetector(new LocalFileDetector());
//        return driver;
//    }
//
//    private static WebDriver createRemoteFirefoxDriver() throws MalformedURLException {
//        FirefoxOptions fireFoxOptions = new FirefoxOptions();
//        RemoteWebDriver driver = new RemoteWebDriver(new URL(gridUrl), fireFoxOptions);
//        maxWindow(driver);
//        driver.setFileDetector(new LocalFileDetector());
//        return driver;
//    }

    /**
     * Method to return driver
     *
     * @return
     */
    public static WebDriver getDriver() {
        if (driverPool.get() != null) {
            return driverPool.get();
        } else {
            throw new RuntimeException("Webdriver is null.");
        }
    }

    /**
     * Method to do maximize the browser window
     * @param driver
     */
    private static void maxWindow(WebDriver driver) {
        driver.manage().window().maximize();
    }

    /**
     * Method to read data from Excel file and return Object[][]
     * @return
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
     * @param sheetName
     * @return
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
     * @param filePath
     * @param sheetName
     * @return
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
     * @param dataColumn
     * @return
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
     * @param keyColumn
     * @param valueColumn
     * @return
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
            File file = ((TakesScreenshot) driverPool.get()).getScreenshotAs(OutputType.FILE);
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
            Screenshot ss = new AShot().takeScreenshot(driverPool.get());
            ImageIO.write(ss.getImage(), getProperty("screenShotExtension"), new File(getProperty("screenShotSavePath") + minSec));
            sleep(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to take screenshot of WebElement
     * @param element
     */
    public static void captureElement(WebElement element) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
            LocalTime localTime = LocalTime.now();
            String minSec = dtf.format(localTime).replace(":", "");
            Screenshot ss = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(100)).takeScreenshot(driverPool.get(), element);
            ImageIO.write(ss.getImage(), getProperty("screenShotExtension"), new File(getProperty("screenShotSavePath") + minSec));
            sleep(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to highlight a WebElement and take screenshot
     * @param element
     */
    public static void captureHighlighted(WebElement element) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
            LocalTime localTime = LocalTime.now();
            String minSec = dtf.format(localTime).replace(":", "");
            JavascriptExecutor jse = (JavascriptExecutor) driverPool.get();
            if (getProperty("needBackground").equalsIgnoreCase("yes")) {
                jse.executeScript("arguments[0].setAttribute('style', 'border:" + getProperty("highlightBorderSize") + "px solid " + getProperty("highlightBorderColor") + "; background:" + getProperty("backgroundColor") + "')", element);
            } else if (getProperty("needBackground").equalsIgnoreCase("no")) {
                jse.executeScript("arguments[0].style.border='" + getProperty("highlightBorderSize") + "px solid " + getProperty("highlightBorderColor") + "'", element);
            }
            sleep(1);
            File file = ((TakesScreenshot) driverPool.get()).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, new File(getProperty("screenShotSavePath") + minSec + getProperty("screenShotExtension")), true);
            sleep(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to verify the Title of the page
     * @param expected
     * @return
     */
    public static boolean verifyTitle(String expected) {
        return driverPool.get().getTitle().equals(expected);
    }

    /**
     * Method to select all options
     * @param dropDown
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
        driverPool.get().manage().timeouts().implicitlyWait(defaultWaitTime, TimeUnit.SECONDS);
    }

    /**
     * Method to wait explicitly until the Title loads
     * @param expectedTitle
     */
    public static void waitForTitle(String expectedTitle) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), defaultWaitTime);
        wait.until(ExpectedConditions.titleIs(expectedTitle));
    }

    /**
     * Method to wait explicitly until the WebElement is visible
     * @param element
     */
    public static void waitUntilVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), defaultWaitTime);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Method to wait explicitly until the WebElement is invisible
     * @param element
     */
    public static void waitUntilInvisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), defaultWaitTime);
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Method to wait explicitly until the WebElement is clickable
     * @param element
     */
    public static void waitUntilClickable(WebElement element) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), defaultWaitTime);
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
        driverPool.get().manage().timeouts().pageLoadTimeout(defaultWaitTime, TimeUnit.SECONDS);
    }

    /**
     * Method to adjust thread sleep time
     * @param seconds
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
        if (driverPool.get() != null) {
            driverPool.get().quit();
        }
    }

    /**
     * Method to remove value from ThreadLocal
     */
    public static void removeDriver() {
        if (driverPool.get() != null) {
            driverPool.remove();
        }
    }

    /**
     * Method to kill session after test
     */
    public static void cleanUpDriver() {
        quitDriver();
        removeDriver();
    }

}