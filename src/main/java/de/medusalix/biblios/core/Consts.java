package de.medusalix.biblios.core;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Consts
{
    public static final String PROGRAM_NAME = "BiblioS";
    public static final String CURRENT_VERSION = "1.1.3";
    public static final String TITLE = String.format("%s ©Severin v. W. | Version %s", PROGRAM_NAME, CURRENT_VERSION);
    public static final String ADMINISTRATION_PASSWORD = "severin" + LocalDate.now().getDayOfWeek().getValue();

    public static class Resources
    {
        public static final String BASE_FXML_PATH = "/fxml/";
        public static final String DATA_FOLDER_PATH = PROGRAM_NAME + "/";

        public static final String API_KEY_PATH = DATA_FOLDER_PATH + "Api-Key.txt";

        public static final String ICON_PACKAGE_SOURCE_URL = "http://paomedia.github.io/small-n-flat/";

        public static final String STYLESHEET_PATH = "/style.css";

        public static final String BASE_WINDOW_PATH = BASE_FXML_PATH + "windows/";
        public static final String BASE_DIALOG_PATH = BASE_FXML_PATH + "dialogs/";

        public static final String MAIN_WINDOW_PATH = BASE_WINDOW_PATH + "MainWindow.fxml";
        public static final String ABOUT_WINDOW_PATH = BASE_WINDOW_PATH + "AboutWindow.fxml";
        public static final String ADMINISTRATION_WINDOW_PATH = BASE_WINDOW_PATH + "AdministrationWindow.fxml";
        public static final String STATS_WINDOW_PATH = BASE_WINDOW_PATH + "StatsWindow.fxml";
        public static final String BORROW_LIST_WINDOW_PATH = BASE_WINDOW_PATH + "BorrowListWindow.fxml";

        public static final String REPORT_PROBLEM_DIALOG_PATH = BASE_DIALOG_PATH + "ReportProblemDialog.fxml";
        public static final String SUGGEST_FEATURE_DIALOG_PATH = BASE_DIALOG_PATH + "SuggestFeatureDialog.fxml";
        public static final String PASSWORD_DIALOG_PATH = BASE_DIALOG_PATH + "PasswordDialog.fxml";
        public static final String PROGRESS_DIALOG_PATH = BASE_DIALOG_PATH + "ProgressDialog.fxml";
        public static final String STUDENT_DIALOG_PATH = BASE_DIALOG_PATH + "StudentDialog.fxml";
        public static final String BOOK_DIALOG_PATH = BASE_DIALOG_PATH + "BookDialog.fxml";
        public static final String SCAN_ISBN_DIALOG_PATH = BASE_DIALOG_PATH + "ScanIsbnDialog.fxml";
        public static final String BOOK_ISBN_DIALOG_PATH = BASE_DIALOG_PATH + "BookIsbnDialog.fxml";
    }

    public static class Network
    {
        public static final String SERVER_HOSTNAME = "wnuck.ddns.net";
        public static final int SERVER_PORT = 50000;
    }

    public static class Database
    {
        public static final String DATABASE_PATH = Resources.DATA_FOLDER_PATH + "Database.db";

        public static final String BACKUP_FOLDER_PATH = Resources.DATA_FOLDER_PATH + "Backups";

        public static final String CONNECTION_URL = "jdbc:sqlite:" + DATABASE_PATH;

        public static final String STUDENTS_TABLE_NAME = "Students";
        public static final String BOOKS_TABLE_NAME = "Books";
        public static final String BORROWED_BOOKS_TABLE_NAME = "BorrowedBooks";
        public static final String STATS_TABLE_NAME = "Stats";

        public static final String CREATE_STUDENTS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS Students(Id INTEGER PRIMARY KEY, Name TEXT UNIQUE, Grade TEXT)";
        public static final String CREATE_BOOKS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS Books(Id INTEGER PRIMARY KEY, Title TEXT UNIQUE, Author TEXT, Isbn TEXT, Publisher TEXT, PublishedDate TEXT, AdditionalInfo TEXT)";
        public static final String CREATE_BORROWED_BOOKS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS BorrowedBooks(StudentId INTEGER, BookId INTEGER UNIQUE, BorrowDate TEXT, ReturnDate TEXT, FOREIGN KEY(StudentId) REFERENCES Students(Id), FOREIGN KEY(BookId) REFERENCES Books(Id))";
        public static final String CREATE_STATS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS Stats(BookId INTEGER UNIQUE, NumberOfBorrows INTEGER, FOREIGN KEY(BookId) REFERENCES Books(Id))";

        public static final String COUNT_COLUMN_NAME = "COUNT(*)";

        public static final String ID_COLUMN_NAME = "Id";
        public static final String BOOK_ID_COLUMN_NAME = "BookId";
        public static final String STUDENT_ID_COLUMN_NAME = "StudentId";

        public static final String NAME_COLUMN_NAME = "Name";
        public static final String GRADE_COLUMN_NAME = "Grade";

        public static final String TITLE_COLUMN_NAME = "Title";
        public static final String AUTHOR_COLUMN_NAME = "Author";
        public static final String ISBN_COLUMN_NAME = "Isbn";
        public static final String PUBLISHER_COLUMN_NAME = "Publisher";
        public static final String PUBLISHED_DATE_COLUMN_NAME = "PublishedDate";
        public static final String ADDITIONAL_INFO_COLUMN_NAME = "AdditionalInfo";

        public static final String BORROW_DATE_COLUMN_NAME = "BorrowDate";
        public static final String RETURN_DATE_COLUMN_NAME = "ReturnDate";

        public static final String NUMBER_OF_BORROWS_COLUMN_NAME = "NumberOfBorrows";

        public static final int MAX_NUMBER_OF_BACKUPS = 10;

        public static final String BACKUP_PREFIX = "Backup-";
        public static final String MANUAL_BACKUP_SUFFIX = "-Manual";
        public static final String START_OF_SCHOOL_BACKUP_SUFFIX = "-StartOfSchool";
    }

    public static class Messages
    {
        public static final String REPORT_PROBLEM_TEXT = "Problem melden";
        public static final String SUGGEST_FEATURE_TEXT = "Funktion vorschlagen";

        public static final String PASSWORD_REQUIRED_TEXT = "Passwort erforderlich";

        public static final String PROGRESS_LOADING_DATABASE_TITLE = "Lade Datenbank";
        public static final String PROGRESS_FETCH_BOOK_INFO_TITLE = "Informationen abrufen";

        public static final String BORROWED_BOOK_TABLE_VIEW_PLACEHOLDER = "Keine ausgeliehenen Bücher vorhanden";
        public static final String BOOK_TABLE_VIEW_PLACEHOLDER = "Keine Bücher vorhanden";

        public static final String ADD_STUDENT_TEXT = "Schüler hinzufügen";
        public static final String ADD_BOOK_TEXT = "Buch hinzufügen";
        public static final String CHANGE_STUDENT_TEXT = "Schüler bearbeiten";
        public static final String CHANGE_BOOK_TEXT = "Buch bearbeiten";

        public static final String CHANGE_MENU_ITEM_TEXT = "Bearbeiten";
        public static final String DELETE_MENU_ITEM_TEXT = "Löschen";
        public static final String SHOW_STUDENT_MENU_ITEM_TEXT = "Schüler anzeigen";
        public static final String EXTEND_MENU_ITEM_TEXT = "Verlängern";

        public static final String CHOOSE_BACKUP_TEXT = "Backup auswählen";

        public static final String NO_BACKUP_EXISTING_TEXT = "Kein Backup vorhanden";

        public static final String DELETE_ALL_BACKUPS_TITLE = "Alle Backups löschen";
        public static final String DELETE_ALL_BACKUPS_MESSAGE = "Sollen wirklich ALLE Backups gelöscht werden?" + System.lineSeparator() + "Diese Änderung kann NICHT RÜCKGÄNGIG gemacht werden!";

        public static final String RESET_STATS_TITLE = "Statistik zurücksetzen";
        public static final String RESET_STATS_MESSAGE = "Soll wirklich die komplette Statistik zurückgesetzt werden?";

        public static final String START_OF_SCHOOL_TITLE = "Schulbeginn durchführen";
        public static final String START_OF_SCHOOL_MESSAGE = "Sollen wirklich alle Schüller in eine Klasse höher versetzt werden?";

        public static final String STAT_CHART_PLACEHOLDER = "Keine Bücher in der Statistik";
    }

    public static class Images
    {
        public static final String BASE_IMAGE_PATH = "images/";

        public static final String FAVICON_IMAGE_PATH = BASE_IMAGE_PATH + "favicon.png";

        public static final String MENU_ITEM_BASE_IMAGE_PATH = BASE_IMAGE_PATH + "menuItems/";

        public static final Image FULLSCREEN_MENU_ITEM_IMAGE = new Image(MENU_ITEM_BASE_IMAGE_PATH + "fullscreen.png");
        public static final Image REPORT_PROBLEM_MENU_ITEM_IMAGE = new Image(MENU_ITEM_BASE_IMAGE_PATH + "reportProblem.png");
        public static final Image SUGGEST_FEATURE_MENU_ITEM_IMAGE = new Image(MENU_ITEM_BASE_IMAGE_PATH + "suggestFeature.png");
        public static final Image ABOUT_MENU_ITEM_IMAGE = new Image(MENU_ITEM_BASE_IMAGE_PATH + "about.png");
        public static final Image CHANGE_MENU_ITEM_IMAGE = new Image(MENU_ITEM_BASE_IMAGE_PATH + "change.png");
        public static final Image DELETE_MENU_ITEM_IMAGE = new Image(MENU_ITEM_BASE_IMAGE_PATH + "delete.png");
        public static final Image SHOW_STUDENT_MENU_ITEM_IMAGE = new Image(MENU_ITEM_BASE_IMAGE_PATH + "showStudent.png");
        public static final Image EXTEND_MENU_ITEM_IMAGE = new Image(MENU_ITEM_BASE_IMAGE_PATH + "extend.png");

        public static final String DIALOG_HEADER_BASE_PATH = BASE_IMAGE_PATH + "dialogs/headers/";

        public static final Image REPORT_PROBLEM_HEADER_IMAGE = new Image(DIALOG_HEADER_BASE_PATH + "reportProblem.png");
        public static final Image SUGGEST_FEATURE_HEADER_IMAGE = new Image(DIALOG_HEADER_BASE_PATH + "suggestFeature.png");
        public static final Image PASSWORD_HEADER_IMAGE = new Image(DIALOG_HEADER_BASE_PATH + "password.png");
        public static final Image CHANGE_HEADER_IMAGE = new Image(DIALOG_HEADER_BASE_PATH + "change.png");
        public static final Image ADD_HEADER_IMAGE = new Image(DIALOG_HEADER_BASE_PATH + "add.png");
        public static final Image FETCH_HEADER_IMAGE = new Image(DIALOG_HEADER_BASE_PATH + "fetch.png");

        public static final Image EXCEEDED_COLUMN_IMAGE = new Image(BASE_IMAGE_PATH + "exceededColumn.png");
        public static final Image NOT_EXCEEDED_IMAGE = new Image(BASE_IMAGE_PATH + "notExceeded.png");
        public static final Image EXCEEDED_IMAGE = new Image(BASE_IMAGE_PATH + "exceeded.png");
    }

    public static class FxIds
    {
        public static final String PASSWORD_FIELD_ID = "#passwordField";

        public static final String PROBLEM_AREA_ID = "#problemArea";
        public static final String FEATURE_AREA_ID = "#featureArea";

        public static final String NAME_FIELD_ID = "#nameField";
        public static final String GRADE_FIELD_ID = "#gradeField";

        public static final String TITLE_FIELD_ID = "#titleField";
        public static final String AUTHOR_FIELD_ID = "#authorField";
        public static final String ISBN_FIELD_ID = "#isbnField";
        public static final String PUBLISHER_FIELD_ID = "#publisherField";
        public static final String PUBLISHED_DATE_FIELD_ID = "#publishedDateField";
        public static final String ADDITIONAL_INFO_FIELD_ID = "#additionalInfoField";
    }

    public static class GoogleBooks
    {
        public static final String GET_VOLUMES_BY_ISBN_URL = "https://www.googleapis.com/books/v1/volumes?q=ISBN:%s&fields=items/selfLink&key=%s";
        public static final String VOLUME_INFO_FIELDS = "%s?fields=volumeInfo(title,subtitle,authors,publisher,publishedDate)&key=%s";
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
