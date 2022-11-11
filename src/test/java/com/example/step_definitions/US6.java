package com.example.step_definitions;

import com.example.pages.Files;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US6 {

    Files files = new Files();

    @When("user choose the {string} option from action-icon")
    public void user_choose_the_option(String command) {
        files.commandAllFiles(command);
    }

    @Then("Verify that the file is removed from the Favorites sub-module’s table")
    public void verify_that_the_file_is_removed_from_the_favorites_sub_module_s_table() {
        files.clickFavorites();
        files.verifyNoFavorites();
    }

    @When("the user clicks the add icon on the top")
    public void the_user_clicks_the_add_icon_on_the_top() {
        files.uploadBtnClick();
    }

    @When("users uploads file with the {string} option")
    public void users_uploads_file_with_the_upload_file_option(String command) {
        files.uploadCommandSelector(command);
    }

    @Then("verify the file is displayed on the page")
    public void verify_the_file_is_displayed_on_the_page() {
        files.verifyFileUpload();
    }
}