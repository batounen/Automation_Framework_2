package com.example.step_definitions;

import com.example.pages.Contacts;
import io.cucumber.java.en.Then;

public class US13 {

    Contacts contacts = new Contacts();

    @Then("verify the contact names are in the list")
    public void verifyTheContactNamesAreInTheList() {
        contacts.verifyContacts();
    }

}