package com.carlopolancos.playwright._01_17exercisefiles;


import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class _03_PlaywrightLocatorsTest extends _13_PlaywrightTestCase {

//    private static Playwright pw;
//    private static Browser br;
//    private static BrowserContext ctx;
//    Page page;
//
//    @BeforeAll
//    public static void setupBrowser(){
//        pw = Playwright.create();
//        br = pw.chromium().launch();
//        ctx = br.newContext();
//    }
//
//    @BeforeEach
//    public void setup(){
//        page = ctx.newPage();
//    }
//
//    @AfterAll
//    public static void cleanupBrowser(){
//        br.close(); pw.close();
//    }

    @DisplayName("Locating elements using CSS")
    @Nested
    class LocatingElementsUsingCSS{
        @BeforeEach
        void openContactPage(){
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("By id")
        @Test
        void locateTheFirstNameFieldByID(){
            String name = "Sarah-Jane";
            var firstName = page.locator("#first_name");
            firstName.fill(name);
            assertThat(firstName).hasValue(name);
        }

        @DisplayName("By CSS class")
        @Test
        void locateTheSendButtonByCssClass(){
            page.locator("#first_name").fill("Sarah-Jane");
            page.locator(".btnSubmit").click();
            List<String> alertMessages = page.locator(".alert").allTextContents();
            assertFalse(alertMessages.isEmpty());
        }

        @DisplayName("By attribute")
        @Test
        void locateTheSendButtonByAttribute(){
            page.locator("[placeholder='Your last name *']").fill("Smith");
            assertThat(page.locator("#last_name")).hasValue("Smith");
        }
    }

}
