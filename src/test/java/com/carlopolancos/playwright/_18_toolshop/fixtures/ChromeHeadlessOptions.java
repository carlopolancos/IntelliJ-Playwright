package com.carlopolancos.playwright._18_toolshop.fixtures;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;

import java.util.Arrays;

public class ChromeHeadlessOptions implements OptionsFactory {
    @Override
    public Options getOptions() {
        return new Options().setLaunchOptions(
                        new BrowserType.LaunchOptions()
                                .setArgs(Arrays.asList("--no-sandbox",
                                        "--disable-extensions", "--disable-gpu", "--start-maximized", "--disable-blink-features=AutomationControlled"
                                , "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36"))
                ).setHeadless(true)
                .setTestIdAttribute("data-test")
                .setChannel("chrome");
    }

}
