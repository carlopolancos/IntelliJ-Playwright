package com.carlopolancos.playwright._01_17exercisefiles;

import com.carlopolancos.playwright._18_toolshop.fixtures.ScreenshotManager;
import com.google.gson.Gson;
import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class _13_PlaywrightTestCase {

    protected static ThreadLocal<Playwright> pw
            = ThreadLocal.withInitial(() -> {
                Playwright playwright = Playwright.create();
                playwright.selectors().setTestIdAttribute("data-test");
                return playwright;
            }
    );

    protected static ThreadLocal<Browser> browser = ThreadLocal.withInitial(() ->
            pw.get().chromium().launch(
                    new BrowserType.LaunchOptions().setHeadless(true)
                            .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
            )
    );

    public BrowserContext ctx;
    protected Page page;

    public static Gson gson = new Gson();
    public APIRequestContext reqCtx;

    @BeforeEach
    void setupContext(){
        String threadId = String.valueOf(Thread.currentThread().threadId());

        ctx = pw.get().chromium().launchPersistentContext(Paths.get("user-data-" + threadId),
            new BrowserType.LaunchPersistentContextOptions()
//                    .setHeadless(false)
//                    .setSlowMo(1000)
//                    .setTimeout(5000)
                    .setChannel("chrome")
                    .setViewportSize(1280,720)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36")
//                    .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
                    .setArgs(List.of("--disable-blink-features=AutomationControlled"))
        );
        reqCtx = pw.get().request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://api.practicesoftwaretesting.com")
        );
        page = ctx.pages().isEmpty() ? ctx.newPage() : ctx.pages().getFirst();
        page.navigate("https://practicesoftwaretesting.com/");
        page.waitForSelector(".card-img-top");
    }

    @AfterEach
    void cleanup() {
        ScreenshotManager.takeScreenshot(page, "End of test");
        if (reqCtx != null){
            reqCtx.dispose();
        }
        ctx.close();
    }

    @AfterAll
    public static void cleanupAll() {
        pw.get().close();
        pw.remove();
    }
}
