@us6 @smoke
Feature: As a user, I should be able to remove files from favorites and upload a file directly

  Background:
    Given user on the dashboard page
    When the user clicks the "Files" module

  Scenario: verify users to remove files to Favorites
    When  user choose the "Remove from favorites" option from action-icon
    And user click the "Favorites" sub-module on the left side
    Then Verify that the file is removed from the Favorites sub-module’s table
@wip
  Scenario: verify users to upload a file from Files
    When the user clicks the add icon on the top
    And users uploads file with the "Upload file" option
    Then verify the file is displayed on the page