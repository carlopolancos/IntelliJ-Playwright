package com.carlopolancos.playwright._18_toolshop.catalog.pageObjects;

import com.microsoft.playwright.Page;

import java.util.List;

public class CheckoutCart {
    private final Page page;

    public CheckoutCart(Page page){
        this.page = page;
    }

    public List<CartLineItem> getLineItems(){
        page.waitForSelector("app-cart tbody tr");
        return page.locator("app-cart div tbody tr")
                .all().stream().map(row -> {
                            String title = trimmed(row.getByTestId("product-title").innerText());
                            int quantity = Integer.parseInt(row.getByTestId("product-quantity").inputValue());
                            double price = price(row.getByTestId("product-price").innerText());
                            double total = total(row.getByTestId("line-price").innerText());
                            return new CartLineItem(title, quantity, price, total);
                        }
                ).toList();
    }

    private String trimmed(String s) {
        return s.replace(" ","");
    }

    public double price(String s){
        return Double.parseDouble(s.replace("$", ""));
    }

    public double total (String s){
        return Double.parseDouble(s.replace("$", ""));
    }
}
