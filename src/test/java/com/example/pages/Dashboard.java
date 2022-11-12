package com.example.pages;

import com.example.utils.Driver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Dashboard {

    public Dashboard() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    String searchFileName;

    @FindAll(@FindBy(css = "#appmenu  a"))
    private List<WebElement> menuList;

    @FindBy(css = ".avatardiv-shown")
    private WebElement userInfo;

    @FindBy(css = "a[class='header-menu__trigger']")
    private WebElement searchBtn;

    @FindBy(css = ".icon-search")
    private WebElement searchIcon;

    @FindAll(@FindBy(css = ".panel--content li span[class='name']"))
    private List<WebElement> allRecommendedFiles;

    @FindBy(css = "input[type='search']")
    private WebElement searchInput;

    @FindAll(@FindBy(css = ".unified-search__result-content h3 strong"))
    private List<WebElement> searchResult;

    public WebElement getUserInfo() {
        return userInfo;
    }

    public List<String> actualMenuList() {
        List<String> actualMenu = new ArrayList<>();
        for (WebElement eachMenu : menuList) {
            if (!eachMenu.getAttribute("aria-label").strip().isBlank()) {
                actualMenu.add(eachMenu.getAttribute("aria-label").strip());
            }
        }
        return actualMenu;
    }

    public void moduleClicker(String moduleName) {
        for (WebElement eachMenu : menuList) {
            if (eachMenu.getAttribute("aria-label").strip().equalsIgnoreCase(moduleName)) {
                eachMenu.click();
                break;
            }
        }
        Driver.sleep(2);
    }

    public void searchBtnClick() {
        searchBtn.click();
        Driver.waitUntilVisible(searchIcon);
    }

    public void searchRandomFile() {
        List<String> filesVisibleOnDashboard = new ArrayList<>();
        for (WebElement eachFile : allRecommendedFiles) {
            filesVisibleOnDashboard.add(eachFile.getText());
        }
        int randomNum = Driver.randomNumberGenerator(0, filesVisibleOnDashboard.size());
        searchFileName = filesVisibleOnDashboard.get(randomNum);
        searchInput.sendKeys(searchFileName + Keys.ENTER);
        Driver.sleep(3);
    }

    public void verifySearchResult() {
        List<String> searchResults = new ArrayList<>();
        for (WebElement eachResult : searchResult) {
            searchResults.add(eachResult.getText());
        }
        System.out.println("Search File Name = " + searchFileName);
        System.out.println("Search Results = " + searchResults);
        assertTrue(searchResults.contains(searchFileName));
    }

}