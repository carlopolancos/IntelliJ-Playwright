package com.carlopolancos.playwright._18_toolshop.catalog.pageObjects;

import com.carlopolancos.playwright._18_toolshop.fixtures.ScreenshotManager;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

public class ProductDetails {
    private final Page page;
    private Integer addedCartItems;

    public ProductDetails(Page page) {
        this.page = page;
        addedCartItems = 0;
    }

    @Step("Increase quantity")
    public void increaseQuantityTo(int orders) {
        for (int i = 1; i <= orders-1; i++){
            page.getByTestId("increase-quantity").click();
        }
        addedCartItems += orders;
        ScreenshotManager.takeScreenshot(page, "Quantity increased by " + orders);
    }

    @Step("Add to cart")
    public void addToCart() {
        page.waitForResponse(
                response -> response.url().contains("/carts") && response.request().method().equals("POST"),
                () -> page.getByText("Add to cart").click()
        );
        page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals(String.valueOf(addedCartItems)));
        ScreenshotManager.takeScreenshot(page, "Added to cart");
    }
}
