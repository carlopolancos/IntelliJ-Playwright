package com.carlopolancos.playwright._01_17exercisefiles;

import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class _16_UserAPIClient extends _13_PlaywrightTestCase {
    private final Page page;
    private static final String REGISTER_USER = "https://api.practicesoftwaretesting.com/users/register";

    public _16_UserAPIClient(Page page){
        this.page = page;
    }

    public void registerUser(_11_User user){
        reqCtx = pw.get().request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://api.practicesoftwaretesting.com")
        );
        var response = reqCtx.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
                        .setData(user)
        );
        if(response.status() != 201){
            throw new IllegalStateException("Could not create user: " + response.text());
        }

        String responseBody  = response.text();
        _11_User createdUser = gson.fromJson(responseBody, _11_User.class);
        JsonObject responseObject = gson.fromJson(responseBody,JsonObject.class);
        assertSoftly(softly -> {
            softly.assertThat(response.status())
                    .as("Registration should return 201 status code")
                    .isEqualTo(201);
            softly.assertThat(createdUser)
                    .as("Created user should match the specified user without the password")
                    .isEqualTo(user.withPassword(null));
            softly.assertThat(responseObject.has("password"))
                    .as("No password should be returned")
                    .isFalse();
            softly.assertThat(responseObject.get("id").getAsString())
                    .as("Registered user should have an ID")
                    .isNotNull();
            softly.assertThat(response.headers().get("content-type")).contains("application/json");
        });
    }
}
