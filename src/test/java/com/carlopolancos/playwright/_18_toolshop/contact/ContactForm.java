package com.carlopolancos.playwright._18_toolshop.contact;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.options.AriaRole.ALERT;
import static com.microsoft.playwright.options.AriaRole.BUTTON;

public class ContactForm {
    private final Page page;

    private Locator firstNameField;
    private Locator lastNameField;
    private Locator emailField;
    private Locator messageField;
    private Locator subjectField;
    private Locator uploadField;
    private Locator sendButton;
    private Locator alertMessage;

    public ContactForm(Page page) {
        this.page = page;
        firstNameField = page.getByLabel("First name");
        lastNameField = page.getByLabel("Last name");
        emailField = page.getByLabel("Email address");
        messageField = page.getByLabel("Message");
        subjectField = page.getByLabel("Subject");
        uploadField = page.getByLabel("Attachment");
        sendButton = page.getByRole(BUTTON, new Page.GetByRoleOptions().setName("Send"));
        alertMessage = page.getByRole(ALERT);
    }


    public void setFirstName(String firstName) {
        firstNameField.fill(firstName);
    }

    public void setLastName(String lastName) {
        lastNameField.fill(lastName);
    }

    public void setEmail(String email) {
        emailField.fill(email);
    }

    public void setMessage(String message) {
        messageField.fill(message);
    }

    public void setSubject(String subject){
        subjectField.selectOption(new SelectOption().setLabel(subject));
    }

    public void setAttachment(Path fileToUpload){
        page.setInputFiles("#attachment", fileToUpload);
    }

    public void submitForm() {
        sendButton.scrollIntoViewIfNeeded();
        sendButton.click();
    }

    public String getAlertMessage() {
        alertMessage.waitFor();
        return alertMessage.innerText();
    }

    public void clearField(String fieldName) {
        Locator field = page.getByLabel(fieldName);
        page.getByLabel(fieldName).focus();
        page.keyboard().press("Control+A");
        page.keyboard().press("Backspace");
        page.getByLabel(fieldName).fill("");
        field.blur();
    }

    public List<String> getAllContent (){
        List<String> data = new ArrayList<>();
        data.add(firstNameField.inputValue());
        data.add(lastNameField.inputValue());
        data.add(emailField.inputValue());
        data.add(messageField.inputValue());
        data.add(subjectField.inputValue());
        return data;
    }
}
