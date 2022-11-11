package com.example.step_definitions;

import com.example.pages.Files;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;

public class US9 {

    Files files = new Files();

    @And("user write a comment inside the input box")
    public void userWriteACommentInsideTheInputBox() {
        files.postNewComment();
    }

    @And("user click the submit button to post it")
    public void userClickTheSubmitButtonToPostIt() {
        files.submitNewComment();
    }

    @Then("Verify the comment is displayed in the comment section.")
    public void verifyTheCommentIsDisplayedInTheCommentSection() {
        files.verifyNewComment();
    }

}