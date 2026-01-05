package com.carlopolancos.playwright._18_toolshop.catalog;

import com.carlopolancos.playwright._01_17exercisefiles._13_PlaywrightTestCase;
import com.carlopolancos.playwright._18_toolshop.catalog.pageObjects.ProductList;
import com.carlopolancos.playwright._18_toolshop.catalog.pageObjects.SearchComponent;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Feature;
import io.qameta.allure.Stories;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.microsoft.playwright.options.AriaRole.BUTTON;

@DisplayName("Searching for products")
@Feature("Product Catalog")
public class SearchForProductsTest extends _13_PlaywrightTestCase {

//    @DisplayName("Without Page Objects")
//    @Test
//    void withoutPageObjects() {
//        page.waitForResponse("**/products/search?q=tape", () -> {
////                page.getByTestId("search-query").fill("tape");
//            page.getByPlaceholder("Search").fill("tape");
//            page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
//        });
//        Locator productNames = page.getByTestId("product-name");
//        productNames.first().waitFor();
//        List<String> matchingProducts = productNames.allInnerTexts();
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(matchingProducts).contains("Tape Measure 7.5m",
//                    "Measuring Tape", "Tape Measure 5m");
//        });
//    }
//
//    @DisplayName("With Page Objects")
//    @Test
//    void withPageObjects() {
//        SearchComponent searchComponent = new SearchComponent(page);
//        ProductList productList = new ProductList(page);
//        String product = "tape";
//
//        searchComponent.searchBy(product);
//        var matchingProducts = productList.getProductNames(page, searchComponent, product);
//
//        SoftAssertions.assertSoftly(softly -> {
//            softly.assertThat(matchingProducts).contains("Tape Measure 7.5m",
//                    "Measuring Tape", "Tape Measure 5m");
//        });
//    }

    @Test
    @Story("Searching for products")
    @DisplayName("When there are matching results")
    void whenSearchingByKeyword() {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);
        String product = "tape";

//        searchComponent.searchBy(product);

        var matchingProducts = productList.getProductNames(page, searchComponent, product);

        Assertions.assertThat(matchingProducts).contains("Tape Measure 7.5m",
                "Measuring Tape", "Tape Measure 5m");

    }

    @Test
    @Story("Searching for products")
    @DisplayName("When there are no matching results")
    void whenThereIsNoMatchingProduct() {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);
        String noProduct = "unknown";

//        searchComponent.searchBy(noProduct);

        var matchingProducts = productList.getProductNames(page, searchComponent, noProduct);

        Assertions.assertThat(matchingProducts).isEmpty();
        Assertions.assertThat(productList.getSearchCompletedMessage());
    }

    @Test
    @Story("Searching for products")
    @DisplayName("When search criteria is none")
    void clearingTheSearchResults() {
        SearchComponent searchComponent = new SearchComponent(page);
        ProductList productList = new ProductList(page);
        String product = "saw";

//        searchComponent.searchBy(product);

        var matchingFilteredProducts = productList.getProductNames(page, searchComponent, product);
        Assertions.assertThat(matchingFilteredProducts).hasSize(2);

//        searchComponent.clearSearch();

        var matchingProducts = productList.getProductNamesDefault(page, searchComponent);
        Assertions.assertThat(matchingProducts).hasSize(9);
    }
}
