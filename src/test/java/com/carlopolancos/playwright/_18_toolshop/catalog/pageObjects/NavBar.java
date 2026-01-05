package com.carlopolancos.playwright._18_toolshop.catalog.pageObjects;

import com.carlopolancos.playwright._18_toolshop.fixtures.ScreenshotManager;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class NavBar{
    private final Page page;

    public NavBar(Page page){
        this.page = page;
    }

    @Step("Open the shopping cart")
    public void openCart(int orders){
        page.getByTestId("nav-cart").click();
//            page.navigate("https://practicesoftwaretesting.com/checkout");
        ScreenshotManager.takeScreenshot(page, "Shopping Cart");
    }

    @Step("Open home page")
    public void openHomePage(){
        page.navigate("https://practicesoftwaretesting.com/");
        page.waitForSelector(".card-img-top");
        ScreenshotManager.takeScreenshot(page, "Home page");
    }

    @Step("Open contact page")
    public void openContactPage(){
        page.navigate("https://practicesoftwaretesting.com/contact");
        ScreenshotManager.takeScreenshot(page, "Contact page");
    }
}
