package com.carlopolancos.playwright._18_toolshop.catalog;

import com.carlopolancos.playwright._01_17exercisefiles._13_PlaywrightTestCase;
import com.carlopolancos.playwright._18_toolshop.catalog.pageObjects.*;
import com.carlopolancos.playwright._18_toolshop.fixtures.ScreenshotManager;
import com.carlopolancos.playwright._18_toolshop.fixtures.TakesFinalScreenshot;
import com.microsoft.playwright.Page;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.BUTTON;

@DisplayName("Shopping Cart")
@Feature("Shopping Cart")
public class AddToCartTest extends _13_PlaywrightTestCase implements TakesFinalScreenshot {
    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    CheckoutCart checkoutCart;

    @BeforeEach
    void initializePages(){
        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        checkoutCart = new CheckoutCart(page);
        navBar.openHomePage();
    }

//    @DisplayName("Without Page Objects")
//    @Test
//    void withoutPageObjects(){
//        page.waitForResponse("**/products/search?q=pliers", () -> {
//            page.getByPlaceholder("Search").fill("pliers");
//            page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
//        });
//
//        page.locator(".card").getByText("Combination Pliers").click();
//
//        page.getByTestId("increase-quantity").click();
//        page.getByTestId("increase-quantity").click();
//        page.getByText("Add to cart").click();
//        page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("3"));
//
//        page.getByTestId("nav-cart").click();
//
//        assertThat(page.locator(".product-title").getByText("Combination Pliers")).isVisible();
//        assertThat(page.getByTestId("cart-quantity").getByText("3")).isVisible();
//    }
//
//    @DisplayName("With Page Objects")
//    @Test
//    void withPageObjects(){
//        String productName = "Combination Pliers";
//        int orders = 3;
//
//        searchComponent.searchBy("pliers");
//        productList.viewProductDetails(productName);
//        productDetails.increaseQuantityTo(orders);
//        productDetails.addToCart();
//        navBar.openCart(orders);
//
//        List<CartLineItem> lineItems = checkoutCart.getLineItems();
//
//        Assertions.assertThat(lineItems).hasSize(1).first().satisfies(item -> {
//                    Assertions.assertThat(item.title()).contains(productName);
//                    Assertions.assertThat(item.quantity()).isEqualTo(orders);
//                    Assertions.assertThat(item.total()).isEqualTo(orders* item.price());
//                }
//        );
//    }

    @Test
    @Story("Checking out a product")
    @DisplayName("Checking out a single item")
    void whenCheckingOutASingleItem(){
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
    @Story("Checking out a product")
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
