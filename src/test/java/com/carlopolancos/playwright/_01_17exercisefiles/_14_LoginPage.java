package com.carlopolancos.playwright._01_17exercisefiles;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.options.AriaRole.*;

public class _14_LoginPage {

    private final Page page;

    public _14_LoginPage(Page page){
        this.page = page;
    }

    public void open(){
        page.navigate("https://practicesoftwaretesting.com/auth/login");
    }

    public void loginAs(_11_User randomUser) {
        page.getByText("Email address *").fill(randomUser.email());
        page.getByText("Password *").fill(randomUser.password());
        page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
    }

    public String title() {
        return page.getByTestId("page-title").textContent();
    }
    public String tabTitle() {
        return page.title();
    }

    public String loginErroMessage() {
        return page.getByTestId("login-error").textContent();
    }
}
