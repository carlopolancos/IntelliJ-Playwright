package com.carlopolancos.playwright._18_toolshop.catalog.pageObjects;

import com.carlopolancos.playwright._18_toolshop.fixtures.ProductSummary;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductList {
    private final Page page;

    public ProductList (Page page){
        this.page = page;
    }

    public List<String> getProductNames(Page page, SearchComponent searchComponent, String product) {
        String finalProduct = product.replace(" ", "%20");
        page.waitForResponse(
                res -> res.url().contains("/products/search?q=" + finalProduct) && res.status() == 200,
                () -> searchComponent.searchBy(product)
        );

        Locator productLabels = page.getByTestId("product-name");
        try {
            productLabels.first().waitFor(new Locator.WaitForOptions().setTimeout(3000));
        } catch (com.microsoft.playwright.TimeoutError e) {
            return new ArrayList<>();
        }

        return productLabels.allInnerTexts();
    }

    public List<String> getProductNames(Page page) {
        Locator productLabels = page.getByTestId("product-name");
        try {
            productLabels.first().waitFor(new Locator.WaitForOptions().setTimeout(3000));
        } catch (com.microsoft.playwright.TimeoutError e) {
            return new ArrayList<>();
        }

        return productLabels.allInnerTexts();
    }

    public List<ProductSummary> getProductSummaries() {
        return page.locator(".card").all()
                .stream()
                .map(productCard -> {
                    String productName = productCard.getByTestId("product-name").innerText().trim();
                    String productPrice = productCard.getByTestId("product-price").innerText();
                    return new ProductSummary(productName,productPrice);
                }).toList();
    }

    public List<String> getProductNamesDefault(Page page, SearchComponent searchComponent) {
        page.waitForResponse(
                res -> res.url().contains("/products?page=0&between=price,1,100&is_rental=false") && res.status() == 200,
                searchComponent::clearSearch
        );
        Locator productLabels = page.getByTestId("product-name");
        try {
            productLabels.first().waitFor(new Locator.WaitForOptions().setTimeout(3000));
        } catch (com.microsoft.playwright.TimeoutError e) {
            return new ArrayList<>();
        }
        return productLabels.allInnerTexts();
    }

    @Step("View product details")
    public void viewProductDetails(String productName) {
        page.locator(".card").getByText(productName).click();
    }

    public String getSearchCompletedMessage() {
        return page.getByTestId("search_completed").textContent();
    }
}
