package com.example.pages;

import com.example.utils.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Files {

    private final String newFolderName = "TestFolder" + (int) (Math.random() * 100000);
    private String deletedFileName;

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

    @FindBy(css = ".creatable > a")
    private WebElement uploadFileButton;

    @FindAll(@FindBy(css = ".menu-left .menuitem"))
    private List<WebElement> uploadButtonCommands;

    @FindAll(@FindBy(css = ".displayname"))
    private List<WebElement> uploadButtonCommandNames;

    @FindBy(css = "#uploadprogressbar")
    private WebElement uploadLoadingBar;

    @FindBy(css = "#file_upload_start")
    private WebElement hiddenUploadField;

    @FindBy(css = "input[class='icon-confirm'][type='submit']")
    private WebElement newFolderNameSubmit;

    @FindBy(css = "#view13-input-folder")
    private WebElement newFolderNameInput;

    @FindBy(css = ".show .tooltip-inner")
    private WebElement folderAlreadyExistMsg;

    @FindAll(@FindBy(css = "#fileList tr .thumbnail"))
    private List<WebElement> allFileIcons;

    @FindAll(@FindBy(css = "#content #app-navigation li a"))
    private List<WebElement> appNavigation;

    @FindAll(@FindBy(css = ".extra-data .innernametext"))
    private List<WebElement> deletedFiles;

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
                Driver.sleep(1);
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
                    break;
                } else if (actionsCommandNames.get(i).getText().contains("Delete")) {
                    actionsCommandButtons.get(i).click();
                    break;
                }
            }
        }
    }

    public void verifyNoFavorites() {
        assertTrue(noFavoritesYetStar.isDisplayed());
    }

    public void uploadBtnClick() {
        Driver.waitUntilClickable(uploadFileButton);
        uploadFileButton.click();
    }

    public void uploadCommandSelector(String command) {
        for (int i = 0; i < uploadButtonCommands.size(); i++) {
            if (uploadButtonCommandNames.get(i).getText().equalsIgnoreCase(command) && command.equalsIgnoreCase("upload file")) {
                directFileUpload();
                break;
            } else if (uploadButtonCommandNames.get(i).getText().equalsIgnoreCase(command) && command.equalsIgnoreCase("new folder")) {
                uploadButtonCommands.get(i).click();
                newFolderNameInput.clear();
                newFolderNameInput.sendKeys(newFolderName);
//                if (folderAlreadyExistMsg.isDisplayed()) {
//                    newFolderNameInput.clear();
//                    newFolderName = newFolderName.substring(0,newFolderName.length()-3);
//                    newFolderNameInput.sendKeys(newFolderName);
//                }
            }
        }
    }

    public void directFileUpload() {
        hiddenUploadField.sendKeys(Driver.getProperty("uploadTestFile"));
    }

    public void verifyFileUpload() {
        Driver.waitUntilInvisible(uploadLoadingBar);
        String uploadedFilePath = Driver.getProperty("uploadTestFile");
        String uploadedFileName = uploadedFilePath.substring(uploadedFilePath.lastIndexOf("/") + 1, uploadedFilePath.indexOf("."));
        List<String> allVisibleFileNames = new ArrayList<>();
        for (WebElement fileName : fileNames) {
            allVisibleFileNames.add(fileName.getText());
        }
        System.out.println(allVisibleFileNames);
        System.out.println(uploadedFileName);
        assertTrue(allVisibleFileNames.contains(uploadedFileName));
    }

    public void newFolderNameSubmit() {
        Driver.waitUntilClickable(newFolderNameSubmit);
        newFolderNameSubmit.click();
    }

    public void verifyNewFolder() {
        Driver.getDriver().navigate().refresh();
        Driver.waitUntilClickable(uploadFileButton);
        List<String> allVisibleFileNames = new ArrayList<>();
        for (WebElement fileName : fileNames) {
            allVisibleFileNames.add(fileName.getText());
        }
        System.out.println("allVisibleFileNames = " + allVisibleFileNames);
        System.out.println("newFolderName = " + newFolderName);
        assertTrue(allVisibleFileNames.contains(newFolderName));
    }

    public void folderSelect() {
        for (int i = 0; i < allFileIcons.size(); i++) {
            if (allFileIcons.get(i).getAttribute("style").contains("filetypes/folder")) {
                fileNames.get(i).click();
                break;
            }
        }
    }

    public void randomActionDotsClick() {
        int min = 0, max = actionDots.size();
        int randomNum = (int) ((Math.random() * (max - min)) + min);
        actionDots.get(randomNum).click();
    }

    public void appNavigation(String module) {
        for (WebElement eachModule : appNavigation) {
            if (eachModule.getText().strip().equalsIgnoreCase(module)) {
                eachModule.click();
            }
        }
    }

    public void verifyDeletedFile() {
        String deletedFileName = "";
        List<String> deletedFileNames = new ArrayList<>();
        for (WebElement eachFile : this.deletedFiles) {
            deletedFileNames.add(eachFile.getText());
        }
        assertTrue(deletedFileNames.contains(deletedFileName));
    }

}