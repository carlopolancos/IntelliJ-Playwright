package com.carlopolancos.playwright._01_17exercisefiles;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.options.AriaRole.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UsePlaywright
public class _02_AnAnnotatedPlaywrightTest extends _13_PlaywrightTestCase{

//    public static class MyOptions implements OptionsFactory {
//
//        @Override
//        public Options getOptions() {
//            return new Options()
////                    .setHeadless(false)
//                    .setLaunchOptions(new BrowserType.LaunchOptions()
//                            .setTimeout(5000)
//                            .setChannel("chrome")
////                            .setViewportSize(1280,720)
////                            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36")
//                            .setArgs(List.of("--disable-blink-features=AutomationControlled"))
//                    );
//        }
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

        int matchingSearchResults = page.locator(".card").count();
        System.out.println(matchingSearchResults);
        assertEquals(4, matchingSearchResults);
    }
}
