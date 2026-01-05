package com.carlopolancos.playwright._01_17exercisefiles;


import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static com.microsoft.playwright.options.AriaRole.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class _06_PlaywrightFormsTest extends _13_PlaywrightTestCase {

    @DisplayName("Interacting with text fields")
    @Nested
    class WhenInteractingWithTextFields{
        @BeforeEach
        void openContactPage(){
            page.navigate("https://practicesoftwaretesting.com/contact");
            page.waitForLoadState(LoadState.NETWORKIDLE);
        }

        @DisplayName("Complete the form")
        @Test
        void completeForm() throws URISyntaxException  {
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailField = page.getByLabel("Email address");
            var messageField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var uploadField = page.getByLabel("Attachment");

            firstNameField.fill("Sarah-Jane");
            lastNameField.fill("Smith");
            emailField.fill("sarah-jane@example.com");
            messageField.fill("Hello, world!");
            subjectField.selectOption(new SelectOption().setIndex(5));

            Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());

            page.setInputFiles("#attachment", fileToUpload);

            assertThat(firstNameField).hasValue("Sarah-Jane");
            assertThat(lastNameField).hasValue("Smith");
            assertThat(emailField).hasValue("sarah-jane@example.com");
            assertThat(messageField).hasValue("Hello, world!");
            assertThat(subjectField).hasValue("warranty");
            String uploadedFile = uploadField.inputValue();
            Assertions.assertThat(uploadedFile).endsWith("sample-data.txt");
        }

        @DisplayName("Mandatory Fields")
        @ParameterizedTest
        @ValueSource(strings = {"First name", "Last name", "Email", "Message",})
        void mandatoryFields(String fieldName) {

            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailField = page.getByLabel("Email address");
            var messageField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var sendButton = page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Send"));

            firstNameField.fill("Sarah-Jane");
            lastNameField.fill("Smith");
            emailField.fill("sarah-jane@example.com");
            messageField.fill("Hello, world!");
            subjectField.selectOption("Warranty");

            page.getByLabel(fieldName).clear();


            sendButton.click();

            var errorMessage = page.getByRole(ALERT).getByText(fieldName + " is required");

            assertThat(errorMessage).isVisible();
        }
    }

}
