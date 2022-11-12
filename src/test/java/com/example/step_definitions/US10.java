package com.example.step_definitions;

import com.example.pages.Files;
import com.example.utils.Driver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class US10 {

    Files files = new Files();

    @And("user clicks Settings on the left bottom corner")
    public void userClicksSettingsOnTheLeftBottomCorner() {
        files.settingsClick();
    }

    @Then("the user should be able to click any buttons")
    public void theUserShouldBeAbleToClickAnyButtons() {
        files.settingsCheckBoxSelect();
    }

    @And("user checks the current storage usage")
    public void userChecksTheCurrentStorageUsage() {
        files.getCurrentStorage();
    }

    @And("user uploads file with the upload file option")
    public void userUploadsFileWithTheUploadFileOption() {
        files.directFileUpload();
        files.getStorageBeforeUpload();
    }

    @And("user refresh the page")
    public void userRefreshThePage() {
        Driver.getDriver().navigate().refresh();
        Driver.sleep(5);
        files.getStorageAfterUpload();
    }

    @Then("the user should be able to see storage usage is increased")
    public void theUserShouldBeAbleToSeeStorageUsageIsIncreased() {
        files.verifyStorageIncrease();
    }

}