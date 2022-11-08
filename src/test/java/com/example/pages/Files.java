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

    //    @FindBy(css = "a[data-action=\"Favorite\"] span")
    @FindBy(css = ".action-favorite-container")
    private WebElement firstActionsCommand;

    @FindBy(css = ".action-favorite > span:last-of-type")
    private WebElement firstActionsCommandText;

    @FindAll(@FindBy(css = "div[class*=\"fileActionsMenu\"]>ul>li span:last-of-type"))
    private List<WebElement> actionsCommandNames;

    @FindAll(@FindBy(css = "div[class*=\"fileActionsMenu\"]>ul>li"))
    private List<WebElement> actionsCommandButtons;

    @FindBy(css = "li[data-id=\"favorites\"]")
    private WebElement favoritesModule;

    @FindBy(css = "#emptycontent>div[class=\"icon-starred\"]")
    private WebElement noFavoritesYetStar;

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

    public void commandAllFiles(String command) {
        if (fileNames.size() == actionDots.size()) {
            if (command.equals("Remove from favorites")) {
                removeFromFavorite();
            } else if (command.equals("Add to favorites")) {
                addToFavorites();
            }
        }
    }

    public void addToFavorites() {
        for (int i = 0; i < fileNames.size(); i++) {
            clickedFiles.add(fileNames.get(i).getText());
            Driver.waitUntilClickable(actionDots.get(i));
            actionDots.get(i).click();
            Driver.waitUntilClickable(firstActionsCommand);
            actionsMenuClick("Add to favorites");
        }
    }

    public void removeFromFavorite() {
        int i = 0;
        while (true) {
            Driver.waitUntilClickable(actionDots.get(0));
            actionDots.get(0).click();
            Driver.waitUntilClickable(firstActionsCommand);
            if (!firstActionsCommandText.getText().equals("Remove from favorites")) {
                break;
            } else {
                actionsMenuClick("Remove from favorites");
                Driver.sleep(2);
            }

        }
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

    public void actionsMenuClick(String command) {
        for (int i = 0; i < actionsCommandNames.size(); i++) {
            if (!actionsCommandNames.get(i).getText().isBlank()) {
                if (actionsCommandNames.get(i).getText().strip().equals(command)) {
                    actionsCommandButtons.get(i).click();
                }
            }
        }
    }

    public void verifyNoFavorites() {
        assertTrue(noFavoritesYetStar.isDisplayed());
    }

}