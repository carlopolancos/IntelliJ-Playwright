package com.carlopolancos.playwright._18_toolshop.cucumber.stepdefinitions;

import com.carlopolancos.playwright._18_toolshop.fixtures.ScreenshotManager;
import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class PlaywrightCucumberFixtures {

    private static final ThreadLocal<Playwright> pw
            = ThreadLocal.withInitial(() -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );

    private static final ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            pw.get().chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );

    private static final ThreadLocal<BrowserContext> ctx = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    public APIRequestContext reqCtx;

    @Before(order = 100)
    public void setupContext(){
        String threadId = String.valueOf(Thread.currentThread().threadId());

        ctx.set(pw.get().chromium().launchPersistentContext(Paths.get("user-data-" + threadId),
                new BrowserType.LaunchPersistentContextOptions()
//                    .setHeadless(false)
//                    .setSlowMo(1000)
//                    .setTimeout(5000)
                        .setChannel("chrome")
                        .setViewportSize(1280,720)
                        .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36")
//                    .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
                        .setArgs(List.of("--disable-blink-features=AutomationControlled"))));

        reqCtx = pw.get().request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://api.practicesoftwaretesting.com")
        );
        page.set(ctx.get().pages().isEmpty() ? ctx.get().newPage() : ctx.get().pages().getFirst());
        page.get().navigate("https://practicesoftwaretesting.com/");
        page.get().waitForSelector(".card-img-top");
    }

    @After
    public void cleanup() {
        ScreenshotManager.takeScreenshot(page.get(), "End of test");
        if (reqCtx != null){
            reqCtx.dispose();
        }
        ctx.get().close();
    }

    @AfterAll
    public static void cleanupAll() {
        pw.get().close();
        pw.remove();
    }

    public static Page getPage() {
        return page.get();
    }

    public static BrowserContext getBrowserContext() { return ctx.get(); }
}
