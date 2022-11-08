package com.example.pages;

import com.example.utils.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class Files_Favorites {

    public Files_Favorites() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindAll(@FindBy(css = "#app-content-favorites tbody tr .innernametext"))
    private List<WebElement> favoriteFiles;

    public List<String> favFileList() {
        List<String> favFileNames = new ArrayList<>();
        for (WebElement eachFile : favoriteFiles) {
            favFileNames.add(eachFile.getText());
        }
        return favFileNames;
    }

}