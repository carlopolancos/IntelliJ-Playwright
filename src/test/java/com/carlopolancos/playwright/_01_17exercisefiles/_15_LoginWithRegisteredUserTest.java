package com.carlopolancos.playwright._01_17exercisefiles;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class _15_LoginWithRegisteredUserTest extends _13_PlaywrightTestCase {



    @DisplayName("Should be able to login with a registered user")
    @Test
    void shouldLoginWithRegisteredUser(){
        //Register a user via API
        _11_User randomUser = _11_User.randomUser();
        _16_UserAPIClient userAPIClient = new _16_UserAPIClient(page);
        userAPIClient.registerUser(randomUser);

        //Login via login page
        _14_LoginPage loginPage = new _14_LoginPage(page);
        loginPage.open();
        loginPage.loginAs(randomUser);

        //Check that we are on the right account page

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(loginPage.title()).isEqualTo("My account");
            softly.assertThat(loginPage.tabTitle()).isEqualTo("Overview - Practice Software Testing - Toolshop - v5.0");
        });
    }

    @DisplayName("Should not be able to login with an invalid password")
    @Test
    void shouldNotLoginWithInvalidPassword() {
        //Register a user via API
        _11_User randomUser = _11_User.randomUser();
        _16_UserAPIClient userAPIClient = new _16_UserAPIClient(page);
        userAPIClient.registerUser(randomUser);

        //Login via login page
        _14_LoginPage loginPage = new _14_LoginPage(page);
        loginPage.open();
        loginPage.loginAs(randomUser.withPassword("wrong-password"));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(loginPage.loginErroMessage()).isEqualTo("Invalid email or password");
        });
    }
}
