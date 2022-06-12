package ru.sbermegamarket;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SberTest {

    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = "1263x601";
        Configuration.holdBrowserOpen = true;
    }



    @DisplayName("Первый тест открытия сайта ")
    @Test
    void firstTest() {
        Selenide.open("https://sbermegamarket.ru/");
        //$("[data-widget='searchBarDesktop']").shouldHave(text(""));
        $("input[placeholder='Искать товары']").shouldBe(empty);
    }


    @ValueSource(strings = {"Смартфон", "Наушники"})
    @ParameterizedTest(name = "При поиске {0} в выдаче есть {0}")
    void valueSourceCommon(String testData){
        Selenide.open("https://sbermegamarket.ru/");
        $("input[placeholder='Искать товары']").click();
        $("input[placeholder='Искать товары']").setValue(testData).pressEnter();
        $(".catalog-listing__items").shouldHave(text(testData)).shouldBe(visible);
    }
    @CsvSource(value = {
            "Смартфон в стразах, По запросу «смартфон в стразах» найдено: 255 товаров",
            "Наушники золотые, По запросу «наушники золотые» в категории Наушники найден: 181 товар"
    })
    @ParameterizedTest(name = "При поиске {0} на cтранице есть {1}")
    void csvSourceParamTest(String testData, String expectedResult) {
        Selenide.open("https://sbermegamarket.ru/");
        $("input[placeholder='Искать товары']").click();
        $("input[placeholder='Искать товары']").setValue(testData).pressEnter();
        $$(".catalog-default__department-header-container").findBy(text(expectedResult)).shouldBe(visible);

    }

    @CsvFileSource(resources = {"/test_data/sber.csv"})
    @ParameterizedTest(name = "При поиске {0} на cтранице есть {1}")
    void csvFileSourceParametrizedTest(String testData, String expectedResult) {
        Selenide.open("https://sbermegamarket.ru/");
        $("input[placeholder='Искать товары']").click();
        $("input[placeholder='Искать товары']").setValue(testData).pressEnter();
        $$(".catalog-default__department-header-container").find(text(expectedResult)).shouldBe(visible);
    }

    @EnumSource(EnumSber.class)
    @ParameterizedTest(name = "Check {0}")
    void enumTest(EnumSber enumData) {
        Selenide.open("https://sbermegamarket.ru/");
        $("input[placeholder='Искать товары']").click();
        $("input[placeholder='Искать товары']").setValue(enumData.enumData).pressEnter();
        $$(".catalog-default__department-header-container").find(text(enumData.result)).shouldBe(visible);
    }
    static Stream<Arguments> sberTestComplexProvider() {
        return Stream.of(
                Arguments.of("еда", List.of("Продукты питания")),
                Arguments.of("продукты", List.of("Продукты питания"))
        );
    }

    @MethodSource(value = "sberTestComplexProvider")
    @ParameterizedTest(name = "При поиске {0} в результатах есть {1}")
    void sberTestComplex(String testData, List <String> expectedResult) {
        Selenide.open("https://sbermegamarket.ru/");
        $("input[placeholder='Искать товары']").click();
        $("input[placeholder='Искать товары']").setValue(testData).pressEnter();
        $$(".catalog-department-header__title").shouldHave(CollectionCondition.texts(expectedResult));
    }
}

