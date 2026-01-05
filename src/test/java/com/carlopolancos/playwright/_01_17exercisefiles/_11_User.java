package com.carlopolancos.playwright._01_17exercisefiles;

//{
//        "first_name": "John",
//        "last_name": "Doe",
//        "address":
    //        {
    //        "street": "Street 1",
    //        "city": "City",
    //        "state": "State",
    //        "country": "Country",
    //        "postal_code": "1234AA"
    //        },
//        "phone": "0987654321",
//        "dob": "1970-01-01",
//        "password": "SuperSecure@123",
//        "email": "john@doe.example"
//        }

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

record Address(
        String street,
        String city,
        String state,
        String country,
        String postal_code
) {}

public record _11_User(
        String first_name,
        String last_name,
        Address address,
        String phone,
        String dob,
        String password,
        String email) {

    public static _11_User randomUser() {
        Faker fake = new Faker();
        int year = fake.number().numberBetween(1970,2000);
        int month = fake.number().numberBetween(1,12);
        int day = fake.number().numberBetween(1,28);
        LocalDate date = LocalDate.of(year,month,day);
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Address randomAddress = new Address(
                fake.address().streetAddress(),
                fake.address().city(),
                fake.address().state(),
                fake.address().country(),
                fake.address().postcode()
        );

        return new _11_User(
            fake.name().firstName(),
            fake.name().lastName(),
            randomAddress,
            fake.phoneNumber().phoneNumber(),
            formattedDate,
            "Az123!&xyz",
            fake.internet().emailAddress()
        );
    }

    public _11_User withPassword(String password) {
        return new _11_User(
                first_name,
                last_name,
                address,
                phone,
                dob,
                password,
                email
        );
    }
}
