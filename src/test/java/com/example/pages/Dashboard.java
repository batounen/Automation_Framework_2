package com.example.pages;

import com.example.utils.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Dashboard {

    public Dashboard() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindAll(@FindBy(css = "#appmenu  a"))
    private List<WebElement> menuList;

    @FindBy(css = ".avatardiv-shown")
    private WebElement userInfo;

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

}