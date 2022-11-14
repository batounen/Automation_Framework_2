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

    private final String newFolderName = "TestFolder" + Driver.randomNumberGenerator(0, 1000);
    private String randomlySelectedFileName;
    private int randomNumFiles;
    private String comment;
    private double storageBeforeUpload;
    private double storageAfterUpload;

//    Use below 2 lines if the test file is located somewhere outside the project folder
    private final String uploadFilePath = Driver.getProperty("uploadTestFilePath");
    private final String uploadFileName = Driver.getProperty("uploadTestFileName");

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

    @FindBy(css = "a[id='commentsTabView']")
    private WebElement commentsSection;

    @FindBy(css = "div[data-placeholder='New comment …']")
    private WebElement newCommentInput;

    @FindBy(css = "input[class$='has-tooltip']")
    private WebElement commentSubmitBtn;

    @FindAll(@FindBy(css = "li[class='comment'] div[class='message']"))
    private List<WebElement> allPostedComments;

    @FindAll(@FindBy(css = "div[id='app-settings-content'] label[for$='Toggle']"))
    private List<WebElement> settingsCommands;

    @FindAll(@FindBy(css = "#app-settings-content input[type='checkbox']"))
    private List<WebElement> settingsCheckBoxes;

    @FindBy(css = "#app-settings-header > button")
    private WebElement settingsBtn;

    @FindBy(css = "div[id='editor-container']")
    private WebElement richWorkspace;

    @FindBy(css = "div[id='recommendations']")
    private WebElement recommendations;

    @FindBy(css = "#app-content-files")
    private WebElement pageContents;

    @FindBy(css = "#content #app-navigation li a p")
    private WebElement currentStorageSize;

    @FindBy(css = "#oc-dialog-fileexists-content")
    private WebElement fileUploadConflictAlert;

    @FindBy(css = "#checkbox_original_0")
    private WebElement originalFileCheckbox;

    @FindBy(css = "#checkbox_replacement_0")
    private WebElement newFileCheckbox;

    @FindBy(css = ".continue")
    private WebElement continueBtn;

    @FindAll(@FindBy(css = "tr th input[type='checkbox'][id^='checkbox']"))
    private List<WebElement> conflictCheckboxes;

    @FindBy(css = "label[for='select_all_trash']")
    private WebElement checkAllTrash;

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
        System.out.println("Clicked Files = " + clickedFiles);
        System.out.println("Files in Favorites = " + files_favorites.favFileList());
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
        System.out.println("Upload File Name = " + uploadFileName);
        for (int i = 0; i < fileNames.size(); i++) {
            if (fileNames.get(i).getText().strip().equals(uploadFileName)) {
                actionDots.get(i).click();
                actionsMenuClick("Delete");
                Driver.getDriver().navigate().refresh();
                Driver.waitUntilClickable(uploadFileButton);
                break;
            }
        }
        uploadBtnClick();
        hiddenUploadField.sendKeys(uploadFilePath);
        Driver.waitUntilInvisible(uploadLoadingBar);
    }

    public void verifyFileUpload() {
        List<String> allVisibleFileNames = new ArrayList<>();
        for (WebElement fileName : fileNames) {
            allVisibleFileNames.add(fileName.getText());
        }
        System.out.println("All Visible Files = " + allVisibleFileNames);
        System.out.println("Uploaded File name = " + uploadFileName);
        assertTrue(allVisibleFileNames.contains(uploadFileName));
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
        System.out.println("All Visible File Names = " + allVisibleFileNames);
        System.out.println("New Folder Name = " + newFolderName);
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
        randomNumFiles = Driver.randomNumberGenerator(0, actionDots.size());
        randomlySelectedFileName = fileNames.get(randomNumFiles).getText();
        actionDots.get(randomNumFiles).click();
    }

    public void appNavigation(String module) {
        for (WebElement eachModule : appNavigation) {
            if (eachModule.getText().strip().equalsIgnoreCase(module)) {
                eachModule.click();
            }
        }
    }

    public void verifyDeletedFile() {
        Driver.waitUntilClickable(checkAllTrash);
        List<String> deletedFileNames = new ArrayList<>();
        for (WebElement eachFile : deletedFiles) {
            deletedFileNames.add(eachFile.getText());
        }
        System.out.println("All Deleted Files = " + deletedFileNames);
        System.out.println("Deleted File = " + randomlySelectedFileName);
        assertTrue(deletedFileNames.contains(randomlySelectedFileName));
    }

    public void goToComments() {
        actionsMenuClick("Details");
        Driver.waitUntilClickable(commentsSection);
        commentsSection.click();
        Driver.waitUntilVisible(newCommentInput);
    }

    public void postNewComment() {
        comment = "TestComment" + Driver.randomNumberGenerator(0, 1000);
        Driver.waitUntilClickable(commentsSection);
        commentsSection.click();
        Driver.waitUntilVisible(newCommentInput);
        newCommentInput.sendKeys(comment);
    }

    public void submitNewComment() {
        commentSubmitBtn.click();
    }

    public void verifyNewComment() {
        for (int i = 0; i < fileNames.size(); i++) {
            if (fileNames.get(i).getText().equals(randomlySelectedFileName)) {
                actionDots.get(i).click();
            }
        }
        goToComments();
        List<String> postedComments = new ArrayList<>();
        for (WebElement eachComment : allPostedComments) {
            postedComments.add(eachComment.getText());
        }
        System.out.println("All Posted Comments = " + postedComments);
        System.out.println("New Comment = " + comment);
        assertTrue(postedComments.contains(comment));
    }

    public void settingsCheckBoxSelect() {
        for (int i = 0; i < settingsCommands.size(); i++) {
            if (settingsCheckBoxes.get(i).isSelected()) {
                settingsCommands.get(i).click();
            }
            if (!settingsCheckBoxes.get(i).isSelected()) {
                settingsCommands.get(i).click();
                assertTrue(settingsCheckBoxes.get(i).isSelected());
            }
        }
    }

    public void settingsClick() {
        Driver.waitUntilClickable(settingsBtn);
        settingsBtn.click();
        Driver.sleep(1);
    }

    public double getCurrentStorage() {
        String storage = currentStorageSize.getText();
        StringBuilder temp = new StringBuilder();
        double storageSize = 0;
        for (int i = 0; i < storage.length(); i++) {
            if (Character.isDigit(storage.charAt(i)) || storage.charAt(i) == '.') {
                temp.append(storage.charAt(i));
            }
        }
        storageSize = Double.parseDouble(temp.toString());
        return storageSize;
    }

    public void getStorageBeforeUpload() {
        storageBeforeUpload = getCurrentStorage();
        System.out.println("Storage Before Upload = " + storageBeforeUpload);
    }

    public void getStorageAfterUpload() {
        storageAfterUpload = getCurrentStorage();
        System.out.println("Storage After Upload = " + storageAfterUpload);
    }

    public void verifyStorageIncrease() {
        assertTrue(storageBeforeUpload < storageAfterUpload);
    }

}