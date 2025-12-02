package ru.netology.graphics.image;

public class tCS implements TextColorSchema {

    private static final String SYMBOLS = "#$@%*+-'";

    @Override
    public char convert(int color) {
        int index = color * (SYMBOLS.length() - 1) / 255;
        return SYMBOLS.charAt(index);
    }
}
