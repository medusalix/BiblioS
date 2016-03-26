package de.medusalix.biblios.core;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reference
{
    public static final String WINDOW_TITLE = "BiblioS ©Severin v. W. | Version 1.1.9";
    public static final String ADMINISTRATION_PASSWORD = "medusalix" + LocalDate.now().getDayOfWeek().getValue();

    public static class Paths
    {
        private static final String FXML_FOLDER = "/fxml/";
        private static final String IMAGE_FOLDER = "images/";

        private static final String WINDOW_FOLDER = FXML_FOLDER + "windows/";
        private static final String DIALOG_FOLDER = FXML_FOLDER + "dialogs/";

        private static final String MENU_ITEM_IMAGE_FOLDER = IMAGE_FOLDER + "menuItems/";
        private static final String DIALOG_HEADER_IMAGE_FOLDER = IMAGE_FOLDER + "dialogs/headers/";

        public static final String STYLESHEET = "/style.css";

        public static final String MAIN_WINDOW = WINDOW_FOLDER + "MainWindow.fxml";

        public static final String ABOUT_WINDOW = WINDOW_FOLDER + "AboutWindow.fxml";
        public static final String ADMINISTRATION_WINDOW = WINDOW_FOLDER + "AdministrationWindow.fxml";
        public static final String STATS_WINDOW = WINDOW_FOLDER + "StatsWindow.fxml";
        public static final String BORROW_LIST_WINDOW = WINDOW_FOLDER + "BorrowListWindow.fxml";
        public static final String PASSWORD_DIALOG = DIALOG_FOLDER + "PasswordDialog.fxml";

        public static final String PROGRESS_DIALOG = DIALOG_FOLDER + "ProgressDialog.fxml";
        public static final String STUDENT_DIALOG = DIALOG_FOLDER + "StudentDialog.fxml";
        public static final String BOOK_DIALOG = DIALOG_FOLDER + "BookDialog.fxml";
        public static final String SCAN_ISBN_DIALOG = DIALOG_FOLDER + "ScanIsbnDialog.fxml";
        public static final String BOOK_ISBN_DIALOG = DIALOG_FOLDER + "BookIsbnDialog.fxml";

        public static final String DATA_FOLDER = "BiblioS/";
        public static final String BACKUP_FOLDER = DATA_FOLDER + "Backups";

        public static final String DATABASE = DATA_FOLDER + Database.NAME;
        public static final String DATABASE_FULL = DATABASE + Database.SUFFIX;

        public static final String API_KEY = DATA_FOLDER + "Api-Key.txt";

        public static final String ICON_PACKAGE_URL = "http://paomedia.github.io/small-n-flat/";
    }

    public static class Database
    {
        public static final String NAME = "BiblioS";
        public static final String SUFFIX = ".mv.db";

        public static final String CONNECTION_URL = "jdbc:h2:file:./" + Paths.DATABASE + ";DATABASE_TO_UPPER=FALSE;TRACE_LEVEL_FILE=0";

        public static final String BACKUP_PREFIX = "Backup-";
        public static final String MANUAL_BACKUP_SUFFIX = "-Manual";
        public static final String START_OF_SCHOOL_BACKUP_SUFFIX = "-StartOfSchool";

        public static final int MAX_NUMBER_OF_BACKUPS = 10;
    }

    public static class Dialogs
    {
        public static final String LOADING_DATABASE_TITLE = "Lade Datenbank";
        public static final String FETCHING_INFORMATION_TITLE = "Informationen abrufen";

        public static final String ADD_STUDENT_TEXT = "Schüler hinzufügen";
        public static final String ADD_BOOK_TEXT = "Buch hinzufügen";
        public static final String CHANGE_STUDENT_TEXT = "Schüler bearbeiten";
        public static final String CHANGE_BOOK_TEXT = "Buch bearbeiten";

        public static final String PASSWORD_REQUIRED_TEXT = "Passwort erforderlich";

        public static final String DELETE_ALL_BACKUPS_TITLE = "Alle Backups löschen";
        public static final String DELETE_ALL_BACKUPS_MESSAGE = "Sollen wirklich ALLE Backups gelöscht werden?" + System.lineSeparator() + "Diese Änderung kann NICHT RÜCKGÄNGIG gemacht werden!";

        public static final String RESET_STATS_TITLE = "Statistik zurücksetzen";
        public static final String RESET_STATS_MESSAGE = "Soll wirklich die komplette Statistik zurückgesetzt werden?";

        public static final String START_OF_SCHOOL_TITLE = "Schulbeginn durchführen";
        public static final String START_OF_SCHOOL_MESSAGE = "Sollen wirklich alle Schüller in eine Klasse höher versetzt werden?";

        public static final String RESTART_TITLE = "Programm neustarten";
        public static final String RESTART_MESSAGE = "Bitte starten Sie das Programm neu, da es sonst zu Fehlern kommen kann!";
    }

    public static class Strings
    {
        public static final String STUDENT_LIST_VIEW_PLACEHOLDER = "Keine Schüler vorhanden";
        public static final String BORROWED_BOOK_TABLE_VIEW_PLACEHOLDER = "Keine ausgeliehenen Bücher vorhanden";
        public static final String BOOK_TABLE_VIEW_PLACEHOLDER = "Keine Bücher vorhanden";
        public static final String STAT_CHART_PLACEHOLDER = "Keine Bücher in der Statistik";

        public static final String CHANGE_MENU_ITEM_TEXT = "Bearbeiten";
        public static final String DELETE_MENU_ITEM_TEXT = "Löschen";
        public static final String SHOW_STUDENT_MENU_ITEM_TEXT = "Schüler anzeigen";
        public static final String EXTEND_MENU_ITEM_TEXT = "Verlängern";

        public static final String CHOOSE_BACKUP_TEXT = "Backup auswählen";

        public static final String NO_BACKUP_EXISTING_TEXT = "Kein Backup vorhanden";
    }

    public static class Images
    {
        public static final Image FAVICON = new Image(Paths.IMAGE_FOLDER + "favicon.png");

        public static final Image FULLSCREEN_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "fullscreen.png");
        public static final Image ABOUT_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "about.png");
        public static final Image CHANGE_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "change.png");
        public static final Image DELETE_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "delete.png");
        public static final Image SHOW_STUDENT_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "showStudent.png");
        public static final Image EXTEND_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "extend.png");

        public static final Image PASSWORD_DIALOG_HEADER = new Image(Paths.DIALOG_HEADER_IMAGE_FOLDER + "password.png");
        public static final Image CHANGE_DIALOG_HEADER = new Image(Paths.DIALOG_HEADER_IMAGE_FOLDER + "change.png");
        public static final Image ADD_DIALOG_HEADER = new Image(Paths.DIALOG_HEADER_IMAGE_FOLDER + "add.png");
        public static final Image FETCH_DIALOG_HEADER = new Image(Paths.DIALOG_HEADER_IMAGE_FOLDER + "fetch.png");

        public static final Image EXCEEDED_COLUMN = new Image(Paths.IMAGE_FOLDER + "exceededColumn.png");
        public static final Image NOT_EXCEEDED = new Image(Paths.IMAGE_FOLDER + "notExceeded.png");
        public static final Image EXCEEDED = new Image(Paths.IMAGE_FOLDER + "exceeded.png");
    }

    public static class FxIds
    {
        public static final String PASSWORD_FIELD = "#passwordField";

        public static final String NAME_FIELD = "#nameField";
        public static final String GRADE_FIELD = "#gradeField";

        public static final String TITLE_FIELD = "#titleField";
        public static final String AUTHOR_FIELD = "#authorField";
        public static final String ISBN_FIELD = "#isbnField";
        public static final String PUBLISHER_FIELD = "#publisherField";
        public static final String PUBLISHED_DATE_FIELD = "#publishedDateField";
        public static final String ADDITIONAL_INFO_FIELD = "#additionalInfoField";
    }

    public static class GoogleBooks
    {
        public static final String GET_VOLUMES_BY_ISBN_URL = "https://www.googleapis.com/books/v1/volumes?q=ISBN:%s&fields=items/selfLink&key=%s";
        public static final String VOLUME_INFO_FIELDS_URL = "%s?fields=volumeInfo(title,subtitle,authors,publisher,publishedDate)&key=%s";
    }

    public static class Misc
    {
        public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy'-'HH-mm-ss");

        public static final Background BOOK_NOT_BORROWED_BACKGROUND = new Background(new BackgroundFill(Color.valueOf("#99FF99"), CornerRadii.EMPTY, Insets.EMPTY));
        public static final Background BOOK_BORROWED_BACKGROUND = new Background(new BackgroundFill(Color.valueOf("#FF7777"), CornerRadii.EMPTY, Insets.EMPTY));

        // Used when there is no ISBN for a book
        public static final String ISBN_PLACEHOLDER = "0";

        public static final int MAX_BOOKS_IN_STATS = 10;
    }
}
