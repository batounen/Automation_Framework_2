package com.example.step_definitions;

import com.example.pages.Files;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US7 {

    Files files = new Files();

    @When("user clicks the add icon on the top")
    public void user_clicks_the_add_icon_on_the_top() {
        files.uploadBtnClick();
    }

    @And("user click {string} and write a folder name")
    public void userClick(String commandName) {
        files.uploadCommandSelector(commandName);
    }

    @When("the user click submit icon")
    public void the_user_click_submit_icon() {
        files.newFolderNameSubmit();
    }

    @Then("Verify the folder is displayed on the page")
    public void verify_the_folder_is_displayed_on_the_page() {
        files.verifyNewFolder();
    }

    @When("user choose a folder from the page")
    public void user_choose_a_folder_from_the_page() {
        files.folderSelect();
    }

    @When("the user uploads a file with the upload file option")
    public void the_user_uploads_a_file_with_the_upload_file_option() {
        files.directFileUpload();
    }

    @Then("Verify the file is displayed on the page")
    public void verify_the_file_is_displayed_on_the_page() {
        files.verifyFileUpload();
    }


}