package com.example.pages;

import com.example.utils.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Talk {

    private String testText;

    public Talk() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindAll(@FindBy(css = "a[id^='conversation']"))
    private List<WebElement> allConversations;

    @FindBy(css = "div[class='new-message-form__advancedinput']")
    private WebElement messageBox;

    @FindBy(css = "button[type='submit']")
    private WebElement submitBtn;

    @FindAll(@FindBy(css = ".wrapper .rich-text--wrapper"))
    private List<WebElement> allMessages;

    @FindBy(css = "button[slot='trigger']")
    private WebElement newConversationBtn;

    public void pickRandomConversation() {
        Driver.waitUntilClickable(newConversationBtn);
        int random = Driver.randomNumberGenerator(0, allConversations.size());
        allConversations.get(random).click();
        Driver.waitUntilVisible(messageBox);
    }

    public void sendMessage() {
        testText = "TestMessage" + Driver.randomNumberGenerator(0, 10000);
        messageBox.sendKeys(testText);
    }

    public void submitBtnClick() {
        submitBtn.click();
    }

    public void verifyMessage() {
        List<String> messages = new ArrayList<>();
        for (WebElement eachMessage : allMessages) {
            messages.add(eachMessage.getText());
        }
        System.out.println("All Messages = " + messages);
        System.out.println("Test Message = " + testText);
        assertTrue(messages.contains(testText));
    }

}