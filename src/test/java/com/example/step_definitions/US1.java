package com.example.step_definitions;

import com.example.pages.Login;
import com.example.utils.Driver;
import com.example.utils.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US1 extends TestBase {

    @Given("user on the login page")
    public void user_on_the_login_page() {
        login = new Login();
        login.goLoginPage();
    }
    @When("user use username {string} and passcode {string}")
    public void user_use_username_and_passcode_userpass123(String username, String password) {
        login = new Login();
        login.usernamePassword(username, password);
    }
    @When("user click the login button")
    public void user_click_the_login_button() {
        login = new Login();
        login.loginButtonClick();
    }
    @Then("verify the user should be at the dashboard page")
    public void verify_the_user_should_be_at_the_dashboard_page() {
        Driver.verifyTitle(Driver.getProperty("homePageTitle"));
    }

}