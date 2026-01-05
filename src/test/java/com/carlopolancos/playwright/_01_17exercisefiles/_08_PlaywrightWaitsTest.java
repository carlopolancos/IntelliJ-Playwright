package com.carlopolancos.playwright._01_17exercisefiles;

import com.microsoft.playwright.*;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.*;

@UsePlaywright
public class _08_PlaywrightWaitsTest extends _13_PlaywrightTestCase {

//    private static Playwright pw;
//    private static Browser br;
//    private static BrowserContext ctx;
//    static Page page;
//
//    @BeforeAll
//    public static void setupBrowser(){
//        pw = Playwright.create();
//        pw.selectors().setTestIdAttribute("data-test");
//        br = pw.chromium().launch();
//        ctx = br.newContext();
//        page = ctx.newPage();
//    }
//
//    @BeforeEach
//    public void setup(){
//        page.navigate("https://practicesoftwaretesting.com/");
//        page.waitForSelector(".card-img-top");
//    }
//
//    @AfterAll
//    public static void cleanupBrowser(){
//        br.close(); pw.close();
//    }

    @Nested
    class WaitingState{
        @Test
        void shouldShowAllProductNames() {
            List<String> productNames = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(productNames).contains("Pliers","Bolt Cutters", "Hammer");
        }

        @Test
        void shouldShowAllProductImages(){
            List<String> productImageTitles = page.locator(".card-img-top").all()
                    .stream().map(img -> img.getAttribute("alt")).toList();
            Assertions.assertThat(productImageTitles).contains("Pliers","Bolt Cutters", "Hammer");
        }
    }

    @Nested
    class AutomaticWaits {
        @DisplayName("Should wait for the filter checkbox options to appear before clicking")
        @Test
        void shouldWaitForTheFilterCheckboxes() {
            var screwdriverFilter = page.getByLabel("Screwdriver");
            screwdriverFilter.click();
            assertThat(screwdriverFilter).isChecked();
        }

        @DisplayName("Should filter products by category")
        @Test
        void shouldFilterProductsByCategory(){
            page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Categories")).click();
            page.getByTestId("nav-power-tools").click();

            page.waitForSelector(".card");

            var filteredProducts = page.getByTestId("product-name").allInnerTexts();
            Assertions.assertThat(filteredProducts).contains("Sheet Sander", "Belt Sander", "Random Orbit Sander");
        }
    }

    @Nested
    class WaitingForElementsToAppearAndDisappear {
        @DisplayName("It should display a toaster message when an item is added to the cart")
        @Test
        void shouldDisplayToasterMessage() {
//            page.waitForResponse(res -> res.url().contains("/carts/01kdybncnr8wsxxs71jkz57jw8")
//                    && res.status() == 200 && res.request().method().equalsIgnoreCase("POST"), () -> {
                page.getByText("Bolt Cutters").click();
                page.getByText("Add to cart").click();
//            });

            assertThat(page.getByRole(ALERT)).isVisible();
            assertThat(page.getByRole(ALERT)).hasText("Product added to shopping cart.");

            page.waitForCondition( () -> page.getByRole(ALERT).isHidden());
        }

        @DisplayName("Should update the cart item count")
        @Test
        void shouldUpdateTheCartItemCount(){
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            page.waitForCondition( () -> page.getByTestId("cart-quantity").textContent().equals("1"));

            page.waitForSelector("[data-test='cart-quantity']:has-text('1')");
        }
    }

    @Nested
    class WaitForAPICalls{

        @DisplayName("Prices must be sorted in descending order")
        @Test
        void sortByDescendingPrice(){
            //https://api.practicesoftwaretesting.com/products?page=0&sort=price,desc&between=price,1,100&is_rental=false
            page.waitForResponse("**/products?page=0&sort=price,desc**",
                    () -> {
                        page.getByTestId("sort").selectOption("Price (High - Low)");
                    });
            page.getByTestId("product-price").first().waitFor();


            var productPrices = page.getByTestId("product-price")
                    .allInnerTexts()
                    .stream().map(WaitForAPICalls::extractPrice).toList();

            System.out.println("Product Prices: " + productPrices);
            Assertions.assertThat(productPrices)
                    .isNotEmpty()
                    .isSortedAccordingTo(Comparator.reverseOrder());
        }

        private static double extractPrice(String price){
            return Double.parseDouble(price.replace("$",""));
        }

    }
}
