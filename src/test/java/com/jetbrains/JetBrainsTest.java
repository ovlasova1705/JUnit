package com.jetbrains;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class JetBrainsTest {

    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = "1920x1080";
        Configuration.holdBrowserOpen = true;
    }

    @DisplayName("Verify that website can be opened")
    @Test
    void firstTest() {
        Selenide.open("https://www.jetbrains.com/");
        $(".rs-h1.rs-h1_theme_dark.home-page__title").shouldHave(text("Essential tools for software developers and teams"))
                .shouldBe(visible);
    }

    @ValueSource(strings = {"java", "kotlin"})
    @ParameterizedTest(name = "Showing results for {0}")
    void valueSourceCommon(String testData) {
        Selenide.open("https://www.jetbrains.com/");
        $("button[aria-label='Open search']").click();
        $("input[placeholder='Ctrl+K for advanced search']").setValue(testData);
        $(".quick-search__results-query").shouldHave(text(testData)).shouldBe(visible);
    }
    @Disabled
    @CsvSource(value = {
            "java, IntelliJ IDEA – the Leading Java and Kotlin IDE",
            "kotlin, IntelliJ IDEA – the Leading Java and Kotlin IDE",
    })
    @ParameterizedTest(name = "first result for {0} should contain {1}")
    void csvSourceParamTest(String testData, String expectedResult) {
        Selenide.open("https://www.jetbrains.com/");
        $("button[aria-label='Open search']").click();
        $("input[placeholder='Ctrl+K for advanced search']").setValue(testData);
        $$("[data-test='result-title']").find(text(expectedResult)).shouldBe(visible);

    }

    @CsvFileSource(resources = {"/testData/TestDataJet.csv"})
    @ParameterizedTest(name = "first result for {0} should contain {1}")
    void csvFileSourceParametrizedTest(String testData, String expectedResult) {
        Selenide.open("https://www.jetbrains.com/");
        $("button[aria-label='Open search']").click();
        $("input[placeholder='Ctrl+K for advanced search']").setValue(testData);
        $$("[data-test='result-title']").find(text(expectedResult)).shouldBe(visible);
    }



    static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of(EnumLang.English, List.of("Developer Tools","Team Tools","Education","Solutions","Support","Store")),
                Arguments.of(EnumLang.Deutsch, List.of("Entwicklungstools","Team-Tools","Bildung","Lösungen","Support","Store")),
                Arguments.of(EnumLang.Español, List.of("Para desarrolladores","Para equipos","Educación","Soluciones","Asistencia","Tienda"))
        );
    }

    @MethodSource(value = "dataProvider")
    @ParameterizedTest(name = "List of buttons (1) should show up for language {0} ")
    void siteShouldContainsAllOfGivenButtonsForGivenLanguage(EnumLang enumLang, List <String> expectedResult) {
        Selenide.open("https://www.jetbrains.com/");
        $("[data-test='language-picker']").click();
        $$(".wt-list-item__content").find(text(enumLang.name())).click();
        //$$("[data-test='main-menu']").filter(visible).shouldHave(texts(expectedResult));
        $$("._mainMenuItem_wvleql").filter(visible).shouldHave(texts(expectedResult));
    }
}

