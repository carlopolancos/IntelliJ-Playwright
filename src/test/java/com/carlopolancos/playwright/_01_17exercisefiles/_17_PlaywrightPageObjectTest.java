package com.carlopolancos.playwright._01_17exercisefiles;

import com.carlopolancos.playwright._18_toolshop.catalog.pageObjects.*;
import com.microsoft.playwright.Page;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.*;

public class _17_PlaywrightPageObjectTest extends _13_PlaywrightTestCase {

    @BeforeEach
    void setupPage() {
        page.navigate("https://practicesoftwaretesting.com/");
        page.waitForSelector(".card-img-top");
    }

    @DisplayName("When searching products by keywords")
    @Nested
    class WhenSearchingProductsByKeywords{

        @DisplayName("Without Page Objects")
        @Test
        void withoutPageObjects() {
            page.waitForResponse("**/products/search?q=tape", () -> {
//                page.getByTestId("search-query").fill("tape");
                page.getByPlaceholder("Search").fill("tape");
                page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
            });
            List<String> matchingProducts = page.getByTestId("product-name").allInnerTexts();

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(matchingProducts).contains("Tape Measure 7.5m",
                        "Measuring Tape", "Tape Measure 5m");
            });
        }

        @DisplayName("With Page Objects")
        @Test
        void withPageObjects() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);
            String product = "tape";

//            searchComponent.searchBy(product);
            var matchingProducts = productList.getProductNames(page, searchComponent, product);

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(matchingProducts).contains("Tape Measure 7.5m",
                        "Measuring Tape", "Tape Measure 5m");
            });
        }
    }

    @DisplayName("When adding items to the cart")
    @Nested
    class WhenAddingItemsToTheCart{
        SearchComponent searchComponent;
        ProductList productList;
        ProductDetails productDetails;
        NavBar navBar;
        CheckoutCart checkoutCart;

        @BeforeEach
        void setup(){
            searchComponent = new SearchComponent(page);
            productList = new ProductList(page);
            productDetails = new ProductDetails(page);
            navBar = new NavBar(page);
            checkoutCart = new CheckoutCart(page);
        }

        @DisplayName("Without Page Objects")
        @Test
        void withoutPageObjects(){
            page.waitForResponse("**/products/search?q=pliers", () -> {
                page.getByPlaceholder("Search").fill("pliers");
                page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
            });

            page.locator(".card").getByText("Combination Pliers").click();

            page.getByTestId("increase-quantity").click();
            page.getByTestId("increase-quantity").click();
            page.getByText("Add to cart").click();
            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("3"));

            page.getByTestId("nav-cart").click();

            assertThat(page.locator(".product-title").getByText("Combination Pliers")).isVisible();
            assertThat(page.getByTestId("cart-quantity").getByText("3")).isVisible();
        }

        @DisplayName("With Page Objects")
        @Test
        void withPageObjects(){
            String productName = "Combination Pliers";
            int orders = 3;

            searchComponent.searchBy("pliers");
            productList.viewProductDetails(productName);
            productDetails.increaseQuantityTo(orders);
            productDetails.addToCart();
            navBar.openCart(orders);

            List<CartLineItem> lineItems = checkoutCart.getLineItems();

            Assertions.assertThat(lineItems).hasSize(1).first().satisfies(item -> {
                Assertions.assertThat(item.title()).contains(productName);
                Assertions.assertThat(item.quantity()).isEqualTo(orders);
                Assertions.assertThat(item.total()).isEqualTo(orders* item.price());
                    }
            );
        }

        @DisplayName("When checking out multiple items")
        @Test
        void whenCheckingOutMultipleItems(){
            String productName1 = "Bolt Cutters";
            String productName2 = "Slip Joint Pliers";
            int order1 = 3;
            int order2 = 1;
            int totalOrders = order1 + order2;

            navBar.openHomePage();
            productList.viewProductDetails(productName1);
            productDetails.increaseQuantityTo(order1);
            productDetails.addToCart();

            navBar.openHomePage();
            productList.viewProductDetails(productName2);
            productDetails.increaseQuantityTo(order2);
            productDetails.addToCart();

            navBar.openCart(totalOrders);

            List<CartLineItem> lineItems = checkoutCart.getLineItems();
            Assertions.assertThat(lineItems).hasSize(2).allSatisfy(item -> {
                        Assertions.assertThat(item.total()).isEqualTo(item.quantity()* item.price());
                    }
            );

            List<String> productNames = lineItems.stream().map(CartLineItem::title).toList();
            Assertions.assertThat(productNames).contains("Bolt Cutters","Slip Joint Pliers");
        }
    }


}
