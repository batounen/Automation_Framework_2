package com.example.step_definitions;

import com.example.pages.Login;
import com.example.utils.TestBase;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class US2 extends TestBase {

    @When("user enter invalid {string} and {string}")
    public void user_enter_invalid_and(String username, String password) {
        login = new Login();
        login.usernamePassword(username, password);
    }
    @Then("verify {string} message should be displayed")
    public void verify_message_should_be_displayed(String expectedErrorMsg) {
        login = new Login();
        login.errorMsgVerification(expectedErrorMsg);
    }

}
