package com.example.step_definitions;

import com.example.pages.Files;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US5 {

    private final Files files = new Files();

    @When("the user clicks action-icon from any file on the page and choose the {string} option")
    public void theUserClicksActionIconFromAnyFileOnThePageAndChooseTheAddToFavoritesOption(String command) {
        files.commandAllFiles(command);
    }
    @When("user click the {string} sub-module on the left side")
    public void user_click_the_sub_module_on_the_left_side(String string) {
        files.clickFavorites();
    }
    @Then("Verify the chosen file is listed on the table")
    public void verify_the_chosen_file_is_listed_on_the_table() {
        files.verifyFavFiles();
    }


}