package com.example.step_definitions;

import com.example.pages.Files;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US8 {

    Files files = new Files();

    @And("user click action-icon from any file on the page")
    public void userClickActionIconFromAnyFileOnThePage() {
        files.randomActionDotsClick();
    }

    @And("user choose the {string} option")
    public void userChooseTheOption(String command) {
        files.actionsMenuClick(command);
    }

    @When("the user clicks the {string} sub-module on the left side")
    public void theUserClicksTheSubModuleOnTheLeftSide(String command) {
        files.appNavigation(command);
    }

    @Then("Verify the deleted file is displayed on the page.")
    public void verifyTheDeletedFileIsDisplayedOnThePage() {
        files.verifyDeletedFile();
    }

}