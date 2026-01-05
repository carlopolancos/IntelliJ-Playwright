package com.carlopolancos.playwright._01_17exercisefiles;


import com.microsoft.playwright.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.*;

public class _04_AddingItemsToTheCartTest extends _13_PlaywrightTestCase {

    @DisplayName("Search for pliers")
    @Test
    void searchForPliers(){
        page.waitForResponse(res -> res.url().contains("/products/search?q=Pliers")
                && res.status() == 200, () -> {
                    page.getByPlaceholder("Search").fill("Pliers");
                    page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
        });
        assertThat(page.locator(".card")).hasCount(4);

        List<String> productNames = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));

        Locator outOfStockItems = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name");
        assertThat(outOfStockItems).hasCount(1);
        assertThat(outOfStockItems).hasText("Long Nose Pliers");
    }
}
