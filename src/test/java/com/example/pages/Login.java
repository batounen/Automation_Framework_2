package com.example.pages;

import com.example.utils.Driver;
import com.example.utils.TestBase;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Login extends TestBase {

    public Login() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(css = "#user")
    private WebElement usernameBox;

    @FindBy(css = "#password")
    private WebElement passwordBox;

    @FindBy(css = "#submit-form")
    private WebElement loginButton;

    @FindBy(css = "#lost-password")
    private WebElement forgotPassword;

    @FindBy(css = "div[class='v-align'] a:nth-of-type(2)")
    private WebElement loginWithDevice;

    @FindBy(css = ".wrongPasswordMsg")
    private WebElement errorMsg;

    public void goLoginPage() {
        Driver.getDriver().get(Driver.getProperty("url"));
    }

    public void usernamePassword(String username, String password) {
        usernameBox.sendKeys(username);
        passwordBox.sendKeys(password);
    }

    public void loginButtonClick() {
        loginButton.click();
    }

    public void login_positive(String username, String password) {
        usernameBox.sendKeys(username);
        passwordBox.sendKeys(password);
        loginButton.click();
    }

    public void login_negative(String username, String password) {
        usernameBox.sendKeys(username);
        passwordBox.sendKeys(password);
        loginButton.click();
        assertTrue(errorMsg.isDisplayed());
    }

    public void errorMsgVerification(String expectedErrorMsg) {
        assertEquals(expectedErrorMsg, errorMsg.getText().strip());
    }
}
