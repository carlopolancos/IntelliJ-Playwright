package com.carlopolancos.playwright._01_17exercisefiles;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;

import java.util.Arrays;
import java.util.List;

public class _05_HeadlessChromeOptions implements OptionsFactory {
    @Override
    public Options getOptions() {
        return new Options()
                .setLaunchOptions(new BrowserType.LaunchOptions()
//                        .setSlowMo(1000)
//                        .setHeadless(false)
                        .setTimeout(5000)
                        .setArgs(List.of("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36"))
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu")))
                .setTestIdAttribute("data-test");
    }
}
