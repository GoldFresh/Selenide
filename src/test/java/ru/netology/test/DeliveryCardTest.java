package ru.netology.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DeliveryCardTest {
    LocalDate today = LocalDate.now();
   // LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @BeforeEach
    void Setup() {
        open("http://localhost:9999");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    }

    @Test
    void shouldSendFormWithValidData() {
        $("[data-test-id=city] input").setValue("Ростов-на-Дону");
        $("[data-test-id=date] input").sendKeys(formatter.format(today.plusDays(3)));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79034444444");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $(withText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15000));
        $(".notification__content")
                .shouldHave(exactText("Встреча успешно забронирована на " + today.plusDays(3).format(formatter)));
    }

    @Test
    void shouldSendFormWithInvalidSurname() {
        $("[data-test-id=city] input").setValue("Ростов-на-Дону");
        $("[data-test-id=date] input").sendKeys(formatter.format(today.plusDays(3)));
        $("[data-test-id=name] input").setValue("Ivanov Иван");
        $("[data-test-id=phone] input").setValue("+79034444444");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSendFormWithInvalidPhoneNumber() {
        $("[data-test-id=city] input").setValue("Ростов-на-Дону");
        $("[data-test-id=date] input").sendKeys(formatter.format(today.plusDays(3)));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+790344444441");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldSendFormWithInvalidCity() {
        $("[data-test-id=city] input").setValue("Азов");
        $("[data-test-id=date] input").sendKeys(formatter.format(today.plusDays(3)));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79034444444");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=city] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldSendFormWithInvalidDate() {
        $("[data-test-id=city] input").setValue("Ростов-на-Дону");
        $("[data-test-id=date] input").doubleClick().sendKeys("09.04.2023");
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79034444444");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=date] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldSendFormWithEmptyName() {
        $("[data-test-id=city] input").setValue("Ростов-на-Дону");
        $("[data-test-id=date] input").sendKeys(formatter.format(today.plusDays(3)));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79034444444");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithEmptyNumber() {
        $("[data-test-id=city] input").setValue("Ростов-на-Дону");
        $("[data-test-id=date] input").sendKeys(formatter.format(today.plusDays(3)));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $(".button").click();
        $("[data-test-id=phone] .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSendFormWithoutCheckbox() {
        $("[data-test-id=city] input").setValue("Ростов-на-Дону");
        $("[data-test-id=date] input").sendKeys(formatter.format(today.plusDays(3)));
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79034444444");
        $(".button").click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text").shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}
