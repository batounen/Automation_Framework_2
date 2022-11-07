package com.example.step_definitions;

import com.example.pages.Dashboard;
import com.example.pages.Login;
import com.example.utils.Driver;
import com.example.utils.TestBase;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class US3 extends TestBase {

    @When("the users log in with valid credentials")
    public void the_users_log_in_with_valid_credentials() {
        login = new Login();
        dashboard = new Dashboard();
        login.login_positive(Driver.getProperty("validUsername"), Driver.getProperty("validPassword"));
        Driver.waitUntilClickable(dashboard.getUserInfo());
    }
    @Then("Verify the user see the following modules")
    public void verify_the_user_see_the_following_modules(List<String> list) {
        assertEquals(list, dashboard.actualMenuList().stream().limit(list.size()).collect(Collectors.toList()));
    }

}