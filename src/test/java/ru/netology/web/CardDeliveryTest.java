package ru.netology.web;

import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import io.qameta.allure.selenide.AllureSelenide;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

class CardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldHappyPath() {
        UserData user = DataGenerator.generateUser(4);
        open("http://localhost:9999");
        $(" [data-test-id='city'] input").setValue(user.getCity());
        $(" [data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $(" [data-test-id='date'] input").setValue(user.getDate());
        $("[data-test-id='name'] inputx").setValue(user.getName());
        $("[name=\"phone\"]").setValue(user.getPhone());
        $("[data-test-id=\"agreement\"]").click();
        $$("[type='button']").findBy(cssClass("button")).click();
        $(withText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] > .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + user.getDate()));

        // повторная попытка с другой датой
        $(" [data-test-id='date'] input")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $(" [data-test-id='date'] input").setValue(DataGenerator.generateDate(6));
        $$("[type='button']").findBy(cssClass("button")).click();
        $(withText("Перепланировать?")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content > button").click();
        $("[data-test-id='success-notification'] > .notification__content")
                .shouldHave(text("Встреча успешно запланирована на " + DataGenerator.generateDate(6)));
    }
}
