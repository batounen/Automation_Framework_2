package com.example.step_definitions;

import com.example.pages.Dashboard;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US14 {

    Dashboard dashboard = new Dashboard();

    @When("the user clicks the magnifier icon on the right top")
    public void theUserClicksTheMagnifierIconOnTheRightTop() {
        dashboard.searchBtnClick();
    }

    @And("users search any existing file, folder, username")
    public void usersSearchAnyExistingFileFolderUserName() {
        dashboard.searchRandomFile();
    }

    @Then("verify the app displays the expected result option")
    public void verifyTheAppDisplaysTheExpectedResultOption() {
        dashboard.verifySearchResult();
    }

}