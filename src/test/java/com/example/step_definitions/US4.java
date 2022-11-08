package com.example.step_definitions;

import com.example.pages.Dashboard;
import com.example.pages.Files;
import com.example.pages.Login;
import com.example.utils.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

public class US4 {

    private final Login login = new Login();
    private final Dashboard dashboard = new Dashboard();
    private final Files files = new Files();

    @Given("user on the dashboard page")
    public void user_on_the_dashboard_page() {
        Driver.getDriver().get(Driver.getProperty("url"));
        login.login_positive(Driver.getProperty("validUsername"), Driver.getProperty("validPassword"));
        Driver.waitUntilClickable(dashboard.getUserInfo());
    }

    @When("the user clicks the {string} module")
    public void the_user_clicks_the_module(String moduleName) {
        dashboard.moduleClicker(moduleName);
    }

    @Then("verify the page title is {string}")
    public void verify_the_page_title(String expectedTitle) {
        Driver.verifyTitle(expectedTitle);
    }

    @When("user click the top-left checkbox of the table")
    public void user_click_the_top_left_checkbox_of_the_table() {
        files.selectAllBoxClick();
    }

    @Then("verify all the files are selected")
    public void verify_all_the_files_are_selected() {
        files.verifySelectAllBox();
    }

}
