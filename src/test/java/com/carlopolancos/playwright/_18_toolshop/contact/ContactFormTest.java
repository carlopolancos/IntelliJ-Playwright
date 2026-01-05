package com.carlopolancos.playwright._18_toolshop.contact;

import com.carlopolancos.playwright._01_17exercisefiles._13_PlaywrightTestCase;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.*;

@DisplayName("Contact Form")
@Feature("Contacts")
public class ContactFormTest extends _13_PlaywrightTestCase {

    ContactForm contactForm;

    @DisplayName("When submitting a request")
    @BeforeEach
    void openContactPage(){
//        ctx.tracing().start(new Tracing.StartOptions()
//                .setScreenshots(true)
//                .setSnapshots(true)
//                .setSources(true));

        contactForm = new ContactForm(page);
        page.navigate("https://practicesoftwaretesting.com/contact");
    }
//
//    @AfterEach
//    void stopTracing() {
//        ctx.tracing().stop(new Tracing.StopOptions()
//                .setPath(Paths.get("trace.zip")));
//    }

    @Test
    @Story("Contact Form")
    @DisplayName("Customers can use the contact form to contact us")
    void completeForm() throws URISyntaxException {
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah-jane@example.com");
        contactForm.setMessage("Hello, world!Hello, world!Hello, world!Hello, world!Hello, world!Hello, world!Hello, world!Hello, world!");
        contactForm.setSubject("Warranty");
//        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
//        contactForm.setAttachment(fileToUpload);

        contactForm.submitForm();

        Assertions.assertThat(contactForm.getAlertMessage())
                .contains("Thanks for your message! We will contact you shortly.");
    }

    @ParameterizedTest
    @Story("Contact Form")
    @DisplayName("First Name, last name, email, and message are mandatory")
    @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
    void mandatoryFields(String fieldName){
        page.navigate("https://practicesoftwaretesting.com/contact");

        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah-jane@example.com");
        contactForm.setMessage("Hello, world!Hello, world!Hello, world!Hello, world!Hello, world!Hello, world!Hello, world!Hello, world!");
        contactForm.setSubject("Warranty");


        contactForm.clearField(fieldName);
        page.getByRole(HEADING, new Page.GetByRoleOptions().setName("Contact")).click();
        assertThat(page.getByLabel(fieldName)).isEmpty();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        contactForm.submitForm();

        assertThat(page.locator(".alert-danger")).containsText(fieldName + " is required");
    }

    @Test
    @Story("Contact Form")
    @DisplayName("The message should be about 50 characters long")
    void messageField() {
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah-jane@example.com");
        contactForm.setMessage("Short message man");
        contactForm.setSubject("Warranty");

        contactForm.submitForm();

        assertThat(page.getByRole(ALERT)).hasText("Message must be minimal 50 characters");
    }
}
