package com.carlopolancos.playwright._18_toolshop.catalog.pageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.SelectOption;
import io.qameta.allure.Step;

import java.util.ArrayList;

import static com.microsoft.playwright.options.AriaRole.BUTTON;

public class SearchComponent {
    private final Page page;

    public SearchComponent (Page page){
        this.page = page;
    }

    @Step("Search for keyword")
    public void searchBy(String keyword) {
        page.getByPlaceholder("Search").fill(keyword);
        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
    }

    @Step("Search for keyword")
    public void searchByWithResponse(String keyword) {
        String finalProduct = keyword.replace(" ", "%20");
        page.waitForResponse(
                res -> res.url().contains("/search?q=" + finalProduct) && res.status() == 200,
                () -> searchBy(keyword)
        );
    }

    @Step("Clear the search criteria")
    public void clearSearch() {
        page.getByTestId("search-reset").click();
    }

    public void filterBy(String filterCategory) {
//        https://api.practicesoftwaretesting.com/products?page=0&q=saw&between=price,1,100&by_category=01KE5Y4G55PPQB3NWFVZCA1PJB&is_rental=false
        page.waitForResponse(res -> res.url()
                .contains("/products?page=0&q=saw&between=price,1,100&by_category=01KE5Y4G55PPQB3NWFVZCA1PJB&is_rental=false")
                && res.status() == 200, () -> {
            page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName(filterCategory)).click();
        });
    }

    public void sortBy(String sortFilter) {
        https://api.practicesoftwaretesting.com/products?page=0&sort=name,asc&between=price,1,100&is_rental=false
        page.waitForResponse(res -> res.url()
                .contains("/products?page=0&sort=")
                && res.status() == 200, () -> {
            page.getByTestId("sort").selectOption(new SelectOption().setLabel(sortFilter));
        });
    }
}
