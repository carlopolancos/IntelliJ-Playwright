package com.carlopolancos.playwright._01_17exercisefiles;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;

import static com.microsoft.playwright.options.AriaRole.BUTTON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class _01_ASimplePlaywrightTest extends _13_PlaywrightTestCase {

//    private static Playwright pw;
//    private static Browser br;
//    private static BrowserContext ctx;
//    Page page;
//
//    @BeforeAll
//    public static void setupBrowser() {
//        pw = Playwright.create();
////        br = pw.chromium().launch();
//        br = pw.chromium().launch(new BrowserType.LaunchOptions()
////                .setHeadless(false)
////                .setSlowMo(3_000)
//                .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
//        );
//        ctx = br.newContext();
//    }
//
//    @BeforeEach
//    public void setup(){
//        page = ctx.newPage();
//    }
//
//    @AfterAll
//    public static void cleanupBrowser() {
//        br.close();
//        pw.close();
//    }

    @Test
    void shouldShowThePageTitle(){
        page.navigate("https://practicesoftwaretesting.com/");
        String title = page.title();
        System.out.println(title);
        assertTrue(title.contains("Practice Software Testing"));
    }

    @Test
    void shouldSearchByKeyboard(){
        page.navigate("https://practicesoftwaretesting.com/");

        page.waitForResponse(res -> res.url().contains("/products/search?q=pliers")
                && res.status() == 200, () -> {
            //        page.locator("[data-test=\"search-query\"]").fill("pliers");
            //        page.locator("[data-test=\"search-submit\"]").click();
            //        page.locator("[placeholder='Search']").fill("pliers");
            page.getByPlaceholder("Search").fill("pliers");
            //        page.locator("[placeholder=Search]").fill("pliers");
            //        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
            //        page.locator("button:has-text('Search')").click();
            page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        });
        Locator products = page.getByTestId("product-name");
        try {
            products.first().waitFor(new Locator.WaitForOptions().setTimeout(3000));
        } catch (com.microsoft.playwright.TimeoutError e) {
            new ArrayList<>();
        }

        int matchingSearchResults = page.locator(".card").count();
        System.out.println(matchingSearchResults);
        assertEquals(4, matchingSearchResults);
    }
}
