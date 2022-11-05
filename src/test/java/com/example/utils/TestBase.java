package com.example.utils;


import org.junit.After;
import org.junit.Before;

public class TestBase {

    @Before
    public void setup() throws Exception {
        Driver.createDriver();
    }

    @After
    public void tearDown() {
        Driver.cleanUpDriver();
    }
}