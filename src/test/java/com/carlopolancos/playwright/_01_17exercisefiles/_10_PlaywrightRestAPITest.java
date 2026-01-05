package com.carlopolancos.playwright._01_17exercisefiles;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.playwright.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.*;

public class _10_PlaywrightRestAPITest extends _13_PlaywrightTestCase {

//    private static Playwright pw;
//    private static Browser br;
//    private static BrowserContext ctx;
//    static Page page;
//
//    @BeforeAll
//    public static void setupBrowser(){
//        pw = Playwright.create();
//        pw.selectors().setTestIdAttribute("data-test");
//        br = pw.chromium().launch(new BrowserType.LaunchOptions()
////                .setHeadless(false)
//                .setChannel("chrome")
//                .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu")));
//        ctx = br.newContext(new Browser.NewContextOptions()
//                .setUserAgent("Mozilla/5.0 (Windows NT 11.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.6998.166 Safari/537.36"));
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

    @Test
    void shouldShowAllProductNames() {
        List<String> productNames = page.getByTestId("product-name").allInnerTexts();
        Assertions.assertThat(productNames).contains("Pliers", "Bolt Cutters", "Hammer");
    }

    @DisplayName("Playwright allows us to mock out API responses")
    @Nested
    class MockingAPIResponses{

        @DisplayName("When a search returns a single product")
        @Test
        void whenASingleItemIsFound(){
            // /products/search?q=pliers
            page.route("**/products/search?q=Pliers", route -> {
                route.fulfill(new Route.FulfillOptions()
                        .setBody(_09_MockSearchResponses.RESPONSE_WITH_A_SINGLE_ENTRY)
                        .setStatus(200));
            });

            page.getByPlaceholder("Search").fill("Pliers");
            page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

            assertThat(page.getByTestId("product-name")).hasCount(1);
            assertThat(page.getByTestId("product-name")).hasText("Super Pliers");
        }

        @DisplayName("When a search returns no product")
        @Test
        void whenNoItemsAreFound(){
            // /products/search?q=pliers
            page.route("**/products/search?q=Pliers", route -> {
                route.fulfill(new Route.FulfillOptions()
                        .setBody(_09_MockSearchResponses.RESPONSE_WITH_NO_ENTRIES)
                        .setStatus(200));
            });

            page.getByPlaceholder("Search").fill("Pliers");
            page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

            assertThat(page.getByTestId("product-name")).hasCount(0);
            assertThat(page.getByTestId("search_completed")).hasText("There are no products found.");
        }
    }

    @Nested
    class MakingAPICallsP{

        record Product (String name, Double price) {}

        private static APIRequestContext reqCtx;

        @BeforeAll
        public static void setupRequestContext() {
            reqCtx = pw.get().request().newContext(
                    new APIRequest.NewContextOptions()
                            .setBaseURL("https://api.practicesoftwaretesting.com")
                            .setExtraHTTPHeaders(new HashMap<>() {{
                                put("Accept", "application/json");
                            }})
            );
        }

        static Stream<Product> products(){
            APIResponse response = reqCtx.get("/products?page=2");
            Assertions.assertThat(response.status()).isEqualTo(200);

            JsonObject jsonObject = new Gson().fromJson(response.text(), JsonObject.class);
            JsonArray data = jsonObject.getAsJsonArray("data");

            return data.asList().stream().map(jsonElement -> {
                JsonObject productJson = jsonElement.getAsJsonObject();
                return new Product(
                        productJson.get("name").getAsString(),
                        productJson.get("price").getAsDouble()
                );
            });
        }

        @DisplayName("Check presence of known products")
        @ParameterizedTest(name = "Checking product {0}")
        @MethodSource("products")
        void checkKnownProduct(Product product){

//            page.fill("[placeholder='Search']", product.name);
//            page.getByPlaceholder("Search").fill(product.name);
            page.getByTestId("search-query").fill(product.name);
            page.click("button:has-text('Search')");
//            page.locator("button[has-text='Search']");
//            page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

            Locator productCard = page.locator(".card")
                    .filter(
                            new Locator.FilterOptions()
                                    .setHasText(product.name)
                                    .setHasText(Double.toString(product.price))
                    );
            assertThat(productCard).isVisible();
        }
    }
}
