package hu.nje.mozifxml.util;

public class Constant {

    public static final String NUMBER_REGEX = "\\d+";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_MOVIE_TITLE = "Film cím";
    public static final String COLUMN_CINEMA_NAME = "Mozi neve (város)";
    public static final String COLUMN_DATE = "Előadás dátuma";
    public static final String COLUMN_VIEWER = "Nézők száma";
    public static final String COLUMN_INCOME = "Bevétel";
    public static final String ERROR_TITLE = "Hiba történt";
    public static final String ERROR_MSG = "Hiba történt a tranzakció során";
    public static final String SUCCESSFULLY_SAVED = "Sikeres mentés";
    public static final String SUCCESSFULLY_SAVED_MSG = "Az adatok sikeresen mentésre kerültek";
    public static final String EMPTY = "";
    public static final String LIKE_PER_CENT = "%";

    public static boolean isNotEmpty(final String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isEmpty(final String s) {
        return s == null || s.trim().isEmpty();
    }
}
