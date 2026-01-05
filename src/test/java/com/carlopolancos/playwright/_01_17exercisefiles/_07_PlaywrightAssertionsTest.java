package com.carlopolancos.playwright._01_17exercisefiles;

import com.microsoft.playwright.options.LoadState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class _07_PlaywrightAssertionsTest extends _13_PlaywrightTestCase {

    @DisplayName("Making assertions about the content of a field")
    @Nested
    class LocatingElementsUsingCss{
        @BeforeEach
        void openContactPage(){
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Checking the value of a field")
        @Test
        void fieldValues(){
            var firstNameField = page.getByLabel("First name");
            firstNameField.fill("Sarah-Jane");
            assertThat(firstNameField).hasValue("Sarah-Jane");
            assertThat(firstNameField).not().isDisabled();
            assertThat(firstNameField).isVisible();
            assertThat(firstNameField).isEditable();
        }
    }

    @DisplayName("Making assertions about data values")
    @Nested
    class MakingAssertionsAboutDataValues{
        @BeforeEach
        void openHomePage(){
            page.navigate("https://practicesoftwaretesting.com");
        }

        @Test
        void allProductPricesShouldBeCorrectValues(){
            page.getByTestId("product-price").first().waitFor();
            List<Double> prices = page.getByTestId("product-price")
                    .allInnerTexts().stream()
                    .map(price -> Double.parseDouble(price.replace("$","")))
                    .toList();

            Assertions.assertThat(prices)
                    .isNotEmpty()
                    .allMatch(p -> p > 0)
                    .allMatch(p -> p < 1000)
                    .allSatisfy(p ->
                            Assertions.assertThat(p)
                                    .isGreaterThan(0)
                                    .isLessThan(1000));
        }

        // SELECT element is broken
        @Test
        void shouldSortInAlphabeticalOrder() {
            page.getByTestId("sort").selectOption("Name (A - Z)");
            page.waitForLoadState(LoadState.NETWORKIDLE);

            List<String> productNames = page.getByTestId("product-name").allTextContents();
            Assertions.assertThat(productNames).isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);
        }

        @Test
        void shouldSortInReverseAlphabeticalOrder() {
            page.locator(".form-select").selectOption("Name (Z - A)");
            page.waitForLoadState(LoadState.NETWORKIDLE);

            List<String> productNames = page.getByTestId("product-name").allTextContents();
            Assertions.assertThat(productNames).isSortedAccordingTo(Comparator.reverseOrder());
        }
    }
}
