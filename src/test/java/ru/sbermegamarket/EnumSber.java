package ru.sbermegamarket;

public enum EnumSber {
    SMARTFONE("смартфон в стразах", "По запросу «смартфон в стразах» найдено: 255 товаров"),
    EARPHONES("наушники золотые", "По запросу «наушники золотые» в категории Наушники найден: 181 товар");

    public final String enumData;
    public final String result;


    EnumSber(String enumData, String result) {
        this.enumData = enumData;
        this.result = result;
    }

}