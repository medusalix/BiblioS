/*
 * Copyright (C) 2017 Medusalix
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.medusalix.biblios;

import javafx.scene.image.Image;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Consts
{
    public static final String ADMINISTRATION_PASSWORD = "biblios" + LocalDate.now().getDayOfWeek().getValue();

    public static class Paths
    {
        private static final String FXML_FOLDER = "/fxml/";
        private static final String IMAGE_FOLDER = "images/";

        private static final String WINDOW_FOLDER = FXML_FOLDER + "windows/";
        private static final String DIALOG_FOLDER = FXML_FOLDER + "dialogs/";

        private static final String MENU_ITEM_IMAGE_FOLDER = IMAGE_FOLDER + "menu_items/";
        private static final String DIALOG_HEADER_IMAGE_FOLDER = IMAGE_FOLDER + "dialogs/headers/";

        public static final String STYLESHEET = "/style.css";

        public static final String MAIN_WINDOW = WINDOW_FOLDER + "MainWindow.fxml";

        public static final String ABOUT_WINDOW = WINDOW_FOLDER + "AboutWindow.fxml";
        public static final String ADMINISTRATION_WINDOW = WINDOW_FOLDER + "AdministrationWindow.fxml";
        public static final String STATS_WINDOW = WINDOW_FOLDER + "StatsWindow.fxml";
        public static final String BORROW_LIST_WINDOW = WINDOW_FOLDER + "BorrowListWindow.fxml";
        public static final String HISTORY_WINDOW = WINDOW_FOLDER + "HistoryWindow.fxml";

        public static final String PASSWORD_DIALOG = DIALOG_FOLDER + "PasswordDialog.fxml";
        public static final String STUDENT_DIALOG = DIALOG_FOLDER + "StudentDialog.fxml";
        public static final String BOOK_DIALOG = DIALOG_FOLDER + "BookDialog.fxml";
        public static final String ISBN_DIALOG = DIALOG_FOLDER + "IsbnDialog.fxml";

        public static final Path DATA_FOLDER = java.nio.file.Paths.get("BiblioS");
        public static final Path BACKUP_FOLDER = DATA_FOLDER.resolve("Backups");

        public static final Path DATABASE_NO_EXT = DATA_FOLDER.resolve(Database.NAME);
        public static final Path DATABASE = DATA_FOLDER.resolve(Database.NAME + Database.EXTENSION);

        public static final Path LOCK = DATA_FOLDER.resolve("BiblioS.lock");
        public static final Path API_KEY = DATA_FOLDER.resolve("Api-Key.txt");
    }

    public static class Database
    {
        public static final String NAME = "BiblioS";
        public static final String EXTENSION = ".mv.db";

        public static final String CONNECTION_URL = "jdbc:h2:file:./"
            + Paths.DATABASE_NO_EXT
            + ";DATABASE_TO_UPPER=FALSE;TRACE_LEVEL_FILE=0";

        public static final String BACKUP_PREFIX = "Backup-";
        public static final String MANUAL_BACKUP_SUFFIX = "-Manual";
        public static final String START_OF_SCHOOL_BACKUP_SUFFIX = "-StartOfSchool";

        public static final int RETURNED_BOOKS_LIMIT = 3;
        public static final int BACKUP_LIMIT = 10;
    }

    public static class Dialogs
    {
        public static final String ADD_STUDENT_TEXT = "Schüler hinzufügen";
        public static final String ADD_BOOK_TEXT = "Buch hinzufügen";
        
        public static final String CHANGE_STUDENT_TEXT = "Schüler bearbeiten";
        public static final String CHANGE_BOOK_TEXT = "Buch bearbeiten";
        
        public static final String DELETE_STUDENT_TITLE = "Schüler löschen";
        public static final String DELETE_STUDENT_MESSAGE = "Soll dieser Schüler wirklich gelöscht werden?";
    
        public static final String DELETE_BOOK_TITLE = "Buch löschen";
        public static final String DELETE_BOOK_MESSAGE = "Soll dieses Buch wirklich gelöscht werden?";

        public static final String PASSWORD_REQUIRED_TEXT = "Passwort erforderlich";

        public static final String DELETE_ALL_BACKUPS_TITLE = "Alle Backups löschen";
        public static final String DELETE_ALL_BACKUPS_MESSAGE = "Sollen wirklich ALLE Backups gelöscht werden?"
            + System.lineSeparator()
            + "Diese Änderung kann NICHT RÜCKGÄNGIG gemacht werden!";

        public static final String RESET_STATS_TITLE = "Statistik zurücksetzen";
        public static final String RESET_STATS_MESSAGE = "Soll wirklich die komplette Statistik zurückgesetzt werden?";

        public static final String START_OF_SCHOOL_TITLE = "Schulbeginn durchführen";
        public static final String START_OF_SCHOOL_MESSAGE = "Sollen wirklich alle Schüller in eine Klasse höher versetzt werden?";

        public static final String RESTART_TITLE = "Programm wird geschlossen";
        public static final String RESTART_MESSAGE = "Bitte starten Sie das Programm neu, da es sonst zu Fehlern kommen kann!";
    }

    public static class Strings
    {
        public static final String STUDENT_LIST_VIEW_PLACEHOLDER = "Keine Schüler vorhanden";
        public static final String BOOK_TABLE_VIEW_PLACEHOLDER = "Keine Bücher vorhanden";
        public static final String BORROWED_BOOK_TABLE_VIEW_PLACEHOLDER = "Keine ausgeliehenen Bücher vorhanden";
        public static final String HISTORY_TABLE_VIEW_PLACEHOLDER = "Kein Verlauf vorhanden";
        public static final String STAT_CHART_PLACEHOLDER = "Keine Bücher in der Statistik";

        public static final String CHANGE_MENU_ITEM_TEXT = "Bearbeiten";
        public static final String DELETE_MENU_ITEM_TEXT = "Löschen";
        public static final String SHOW_STUDENT_MENU_ITEM_TEXT = "Schüler anzeigen";
        public static final String SHOW_HISTORY_MENU_ITEM_TEXT = "Verlauf öffnen";
        public static final String EXTEND_MENU_ITEM_TEXT = "Verlängern";

        public static final String MAIN_WINDOW_TITLE = "BiblioS v1.2.1 ©Severin v. W.";
        public static final String HISTORY_WINDOW_TITLE = "Verlauf für '%s'";

        public static final String CHOOSE_BACKUP_TEXT = "Backup auswählen";

        public static final String NO_BACKUPS_EXISTING_TEXT = "Keine Backups vorhanden";
    }

    public static class Images
    {
        public static final Image ICON = new Image(Paths.IMAGE_FOLDER + "icon.png");

        public static final Image FULLSCREEN_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "fullscreen.png");
        public static final Image ABOUT_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "about.png");
        public static final Image CHANGE_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "change.png");
        public static final Image DELETE_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "delete.png");
        public static final Image SHOW_STUDENT_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "student.png");
        public static final Image SHOW_HISTORY_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "history.png");
        public static final Image EXTEND_MENU_ITEM = new Image(Paths.MENU_ITEM_IMAGE_FOLDER + "extend.png");

        public static final Image PASSWORD_DIALOG_HEADER = new Image(Paths.DIALOG_HEADER_IMAGE_FOLDER + "password.png");
        public static final Image CHANGE_DIALOG_HEADER = new Image(Paths.DIALOG_HEADER_IMAGE_FOLDER + "change.png");
        public static final Image ADD_DIALOG_HEADER = new Image(Paths.DIALOG_HEADER_IMAGE_FOLDER + "add.png");
        public static final Image FETCH_DIALOG_HEADER = new Image(Paths.DIALOG_HEADER_IMAGE_FOLDER + "fetch.png");

        public static final Image EXCEEDED_COLUMN = new Image(Paths.IMAGE_FOLDER + "exceeded_column.png");
        public static final Image NOT_EXCEEDED = new Image(Paths.IMAGE_FOLDER + "not_exceeded.png");
        public static final Image EXCEEDED = new Image(Paths.IMAGE_FOLDER + "exceeded.png");

        public static final Image BORROW_BOOK = new Image(Paths.IMAGE_FOLDER + "borrow.png");
        public static final Image RETURN_BOOK = new Image(Paths.IMAGE_FOLDER + "return.png");
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
        public static final String VOLUMES_BY_ISBN_URL = "https://www.googleapis.com/books/v1/volumes?q=ISBN:%s&fields=items/selfLink&key=%s";
        public static final String VOLUME_INFO_FIELDS_URL = "%s?fields=volumeInfo(title,authors,publisher,publishedDate)&key=%s";
    }

    public static class Misc
    {
        public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy'-'HH-mm-ss");

        public static final String BOOK_NOT_BORROWED_CLASS = "book-not-borrowed";
        public static final String BOOK_BORROWED_CLASS = "book-borrowed";

        // Used when there is no ISBN for a book
        public static final String ISBN_PLACEHOLDER = "0";

        public static final int DAYS_TO_EXTEND = 14;
        public static final int MAX_STATS_TO_DISPLAY = 10;
    }
}
