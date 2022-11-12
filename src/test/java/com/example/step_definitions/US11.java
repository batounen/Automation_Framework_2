package com.example.step_definitions;

import com.example.pages.Talk;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class US11 {

    Talk talk = new Talk();

    @And("user search user from the search box")
    public void userSearchUserFromTheSearchBox() {
        talk.pickRandomConversation();
    }

    @And("user write a message")
    public void userWriteAMessage() {
        talk.sendMessage();
    }

    @And("user clicks to submit button")
    public void userClicksToSubmitButton() {
        talk.submitBtnClick();
    }

    @Then("the user should be able to see the message is displayed on the conversation log")
    public void theUserShouldBeAbleToSeeTheMessageIsDisplayedOnTheConversationLog() {
        talk.verifyMessage();
    }

}