package com.carlopolancos.playwright._18_toolshop.cucumber.stepdefinitions;

import com.carlopolancos.playwright._18_toolshop.catalog.pageObjects.NavBar;
import com.carlopolancos.playwright._18_toolshop.catalog.pageObjects.ProductList;
import com.carlopolancos.playwright._18_toolshop.catalog.pageObjects.SearchComponent;
import com.carlopolancos.playwright._18_toolshop.fixtures.ProductSummary;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Map;

public class ProductCatalogStepDefinitions {

    NavBar navbar;
    SearchComponent searchComponent;
    ProductList productList;

    @Before
    public void setupPageObjects() {
        navbar = new NavBar(PlaywrightCucumberFixtures.getPage());
        searchComponent = new SearchComponent(PlaywrightCucumberFixtures.getPage());
        productList = new ProductList(PlaywrightCucumberFixtures.getPage());
    }

    @Given("Sally is on the home page")
    public void sally_is_on_the_home_page() {
        navbar.openHomePage();
    }

    @When("she searches for {string}")
    public void she_searches_for(String searchTerm) {
        searchComponent.searchByWithResponse(searchTerm);
    }

    @Then("the {string} product should be displayed")
    public void the_product_should_be_displayed(String productName) {
        var matchingProducts = productList.getProductNames(PlaywrightCucumberFixtures.getPage(),
                searchComponent, productName);
        Assertions.assertThat(matchingProducts).contains(productName);
    }

//    @Then("the following products should be displayed:")
//    public void theFollowingProductsShouldBeDisplayed(List<String> expectedProducts) {
//        var matchingProducts = productList.getProductNames(PlaywrightCucumberFixtures.getPage());
//        Assertions.assertThat(matchingProducts).containsAll(expectedProducts);
//    }

    @DataTableType
    public ProductSummary productSummaryRow(Map<String, String> productData) {
        return new ProductSummary(
            productData.get("Product"),
            productData.get("Price")
        );
    }
    @Then("the following products should be displayed:")
    public void theFollowingProductsShouldBeDisplayed(List<ProductSummary> expectedProductSummaries) {
        List<ProductSummary> matchingProducts = productList.getProductSummaries();
//        List<Map<String, String>> expectedProductData = expectedProducts.asMaps();
//        List<ProductSummary> expectedProductSummaries =
//                expectedProductData.stream().map(productData -> {
//                    return new ProductSummary(
//                            productData.get("Product"),
//                            productData.get("Price")
//                    );
//                }).toList();

        Assertions.assertThat(matchingProducts).containsExactlyInAnyOrderElementsOf(expectedProductSummaries);
    }

    @Then("no products should be displayed")
    public void noProductsShouldBeDisplayed() {
        var matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts).isEmpty();
    }

    @And("the message {string} should be displayed.")
    public void theMessageShouldBeDisplayed(String messageText) {
        String completionMessage = productList.getSearchCompletedMessage();
        Assertions.assertThat(completionMessage).isEqualTo(messageText);
    }

    @And("she filters by {string}")
    public void sheFiltersBy(String filterCategory) {
        searchComponent.filterBy(filterCategory);
    }

    @When("she sorts by {string}")
    public void sheSortsBy(String sortFilter) {
        searchComponent.sortBy(sortFilter);
    }

    @Then("the first product displayed should be {string}")
    public void theFirstProductDisplayedShouldBe(String firstProduct) {
        var matchingProducts = productList.getProductNames(PlaywrightCucumberFixtures.getPage());
        Assertions.assertThat(matchingProducts).startsWith(firstProduct);
    }
}
