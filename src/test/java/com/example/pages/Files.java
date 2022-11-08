package com.example.pages;

import com.example.utils.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Files {

    public Files() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(css = "label[for=\"select_all_files\"]")
    private WebElement selectAllBox;

    @FindAll(@FindBy(css = "#fileList tr"))
    List<WebElement> checkBoxes;

    @FindAll(@FindBy(css = ".innernametext"))
    private List<WebElement> fileNames;

    @FindAll(@FindBy(css = ".permanent .icon-more"))
    private List<WebElement> actionDots;

    @FindBy(css = "a[data-action=\"Favorite\"] span")
    private WebElement addToFavorite;

    @FindBy(css = "li[data-id=\"favorites\"]")
    private WebElement favoritesModule;

    public void selectAllBoxClick() {
        selectAllBox.click();
    }

    public void verifySelectAllBox() {
        Driver.sleep(1);
        for (WebElement checkBox : checkBoxes) {
            assertTrue(checkBox.getAttribute("class").endsWith("selected"));
        }
    }

    public void clickActionDots() {
        for (WebElement actionDot : actionDots) {
            actionDot.click();
            break;
        }
    }
    List<String> clickedFiles = new ArrayList<>();
    public void addAllFilesToFavorites() {
        if (fileNames.size() == actionDots.size()) {
            for (int i = 0; i < 2; i++) {
                clickedFiles.add(fileNames.get(i).getText());
                Driver.waitUntilClickable(actionDots.get(i));
                actionDots.get(i).click();
                Driver.waitUntilClickable(addToFavorite);
                clickAddToFavorite();
            }
        }
    }

    public void clickAddToFavorite() {
        addToFavorite.click();
    }

    public void clickFavorites() {
        favoritesModule.click();
        Driver.sleep(3);
    }

    public void verifyFavFiles() {
        Files_Favorites files_favorites = new Files_Favorites();
        System.out.println("clickedFiles = " + clickedFiles);
        System.out.println("files_favorites.favFileList() = " + files_favorites.favFileList());
        assertTrue(files_favorites.favFileList().containsAll(clickedFiles));
    }
}