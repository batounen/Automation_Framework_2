package com.example.pages;

import com.example.utils.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class Contacts {

    public Contacts() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindAll(@FindBy(css = ".vue-recycle-scroller__item-wrapper div .app-content-list-item-line-one"))
    private List<WebElement> allContacts;

    public void verifyContacts() {
        List<String> contactNames = new ArrayList<>();
        for (WebElement eachContact : allContacts) {
            contactNames.add(eachContact.getText());
        }
        System.out.println("Contact Names = " + contactNames);
        assertTrue(contactNames.size() > 0);
    }

}