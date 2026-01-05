package com.carlopolancos.playwright._18_toolshop.fixtures;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.AfterEach;

@UsePlaywright
public interface TakesFinalScreenshot {

    @AfterEach
    default void takeScreenshot(Page page)  {
        ScreenshotManager.takeScreenshot(page, "Final screenshot");
    }
}