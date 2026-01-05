package com.carlopolancos.playwright._18_toolshop.cucumber.stepdefinitions;

import com.microsoft.playwright.Tracing;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.nio.file.Paths;

public class ScenarioTracingFixtures {

    @Before
    public void setupTracing() {
        PlaywrightCucumberFixtures.getBrowserContext().tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );
    }

    @After
    public void cleanupTracing(Scenario scenario) {
        String traceName = scenario.getName()
                .replace(" ","-")
                .replace("(","")
                .replace(")", "")
                .toLowerCase();
        PlaywrightCucumberFixtures.getBrowserContext().tracing().stop(
                new Tracing.StopOptions()
                        .setPath(Paths.get("target/traces/trace-" + traceName + ".zip"))
        );
    }
}
