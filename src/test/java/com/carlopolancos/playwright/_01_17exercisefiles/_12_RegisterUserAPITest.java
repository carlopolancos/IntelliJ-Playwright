package com.carlopolancos.playwright._01_17exercisefiles;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.*;

@UsePlaywright
public class _12_RegisterUserAPITest extends _13_PlaywrightTestCase{

//    private static Playwright pw;
//    private static Browser br;
//    private static BrowserContext ctx;
//    private static Page page;
//
//    private Gson gson = new Gson();
//    private APIRequestContext reqCtx;
//
//    @BeforeAll
//    public static void setupAll(){
//        pw = Playwright.create();
//        pw.selectors().setTestIdAttribute("data-test");
//        br = pw.chromium().launch(new BrowserType.LaunchOptions()
////                .setHeadless(false)
////                .setSlowMo(0)
//                .setChannel("chrome")
//                .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu")));
//        ctx = br.newContext(new Browser.NewContextOptions()
//                .setUserAgent("Mozilla/5.0 (Windows NT 11.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.6998.166 Safari/537.36"));
//        page = ctx.newPage();
//    }
//
//    @BeforeEach
//    void setup(){
//        reqCtx = pw.request().newContext(
//                new APIRequest.NewContextOptions()
//                        .setBaseURL("https://api.practicesoftwaretesting.com")
//        );
//    }
//
//    @AfterEach
//    void cleanup() {
//        if (reqCtx != null){
//            reqCtx.dispose();
//        }
//    }

    @Test
    void shouldRegisterUser(){
        _11_User validUser = _11_User.randomUser();
        var response = reqCtx.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(validUser)
        );

        String responseBody  = response.text();
        _11_User createdUser = gson.fromJson(responseBody, _11_User.class);

        JsonObject responseObject = gson.fromJson(responseBody,JsonObject.class);

        assertSoftly(softly -> {
            softly.assertThat(response.status())
                    .as("Registration should return 201 status code")
                    .isEqualTo(201);
            softly.assertThat(createdUser)
                    .as("Created user should match the specified user without the password")
                    .isEqualTo(validUser.withPassword(null));
            softly.assertThat(responseObject.has("password"))
                            .as("No password should be returned")
                            .isFalse();
            softly.assertThat(responseObject.get("id").getAsString())
                    .as("Registered user should have an ID")
                    .isNotNull();
            softly.assertThat(response.headers().get("content-type")).contains("application/json");
        });
    }

    @Test
    void firstNameIsMandatory(){
        _11_User userWithNoName = new _11_User(
                null,
                "Smith",
                new Address(
                        "Some Street",
                        "Some City",
                        "Some State",
                        "Some Country",
                        "Some PostCode"
                ),
                "091234567890",
                "1990-01-01",
                "Az123!xyz",
                "some@email.com"
        );

        var response = reqCtx.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(userWithNoName)
        );

        String responseBody  = response.text();
        JsonObject responseObject = gson.fromJson(responseBody,JsonObject.class);

        assertSoftly(softly -> {
            softly.assertThat(response.status()).isEqualTo(422);
            softly.assertThat(responseObject.has("first_name")).isTrue();
        });
        String errorMessage = responseObject.get("first_name").getAsString();

        assertThat(errorMessage).isEqualTo("The first name field is required.");
    }
}
