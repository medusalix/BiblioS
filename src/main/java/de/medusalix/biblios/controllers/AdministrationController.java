package de.medusalix.biblios.controllers;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.database.access.BorrowedBooks;
import de.medusalix.biblios.database.access.Stats;
import de.medusalix.biblios.database.access.Students;
import de.medusalix.biblios.utils.DialogUtils;
import de.medusalix.biblios.core.GoogleBooks;
import de.medusalix.biblios.utils.NodeUtils;
import de.medusalix.biblios.utils.ThreadUtils;
import de.medusalix.biblios.managers.BackupManager;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.utils.ExceptionUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.exceptions.DBIException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class AdministrationController
{
    private static final String PASSWORD = "severin" + LocalDate.now().getDayOfWeek().getValue();

    private Logger logger = LogManager.getLogger(AdministrationController.class);

    @FXML
    private ComboBox<String> backupBox;
    
    @FXML
    private Button createBackupButton, loadBackupButton;

    @FXML
    private TextField apiKeyField;

    private Students students = DatabaseManager.createDao(Students.class);
    private BorrowedBooks borrowedBooks = DatabaseManager.createDao(BorrowedBooks.class);
    private Stats stats = DatabaseManager.createDao(Stats.class);

    @FXML
    private void initialize()
    {
        initApiKeyField();
        updateBackups();
    }

    private void initApiKeyField()
    {
        apiKeyField.setText(GoogleBooks.readApiKey());
        apiKeyField.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue)
            {
                GoogleBooks.saveApiKey(apiKeyField.getText());
            }
        });
    }

	private void updateBackups()
	{
        backupBox.getItems().clear();

        List<String> backups = BackupManager.getBackups();

        if (backups != null)
        {
            Collections.reverse(backups);

            backupBox.getItems().addAll(backups);

            if (backupBox.getItems().size() > 0)
            {
                backupBox.setDisable(false);
                backupBox.setPromptText(Consts.Strings.CHOOSE_BACKUP_TEXT);
            }

            else
            {
                backupBox.setDisable(true);
                backupBox.setPromptText(Consts.Strings.NO_BACKUP_EXISTING_TEXT);
            }
        }
	}

    @FXML
    private void onCreateBackupClick()
    {
        BackupManager.createBackup(Consts.Database.MANUAL_BACKUP_SUFFIX);

        NodeUtils.blinkGreen(createBackupButton);

        updateBackups();
    }
    
    @FXML
    private void onDeleteAllBackupsClick(ActionEvent event)
    {
        Alert alert = DialogUtils.createAlert(Alert.AlertType.CONFIRMATION, Consts.Dialogs.DELETE_ALL_BACKUPS_TITLE, Consts.Dialogs.DELETE_ALL_BACKUPS_MESSAGE);

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            ThreadUtils.runThreadAsDaemon(() ->
            {
                BackupManager.deleteAllBackups();

                Platform.runLater(() ->
                {
                    updateBackups();

                    NodeUtils.blinkGreen((Node)event.getSource());
                });
            });
        }
    }

    @FXML
    private void onLoadBackupClick()
    {
        if (backupBox.getSelectionModel().getSelectedItem() != null)
        {
            try
            {
                Path backup = Files.list(java.nio.file.Paths.get(Consts.Paths.BACKUP_FOLDER)).filter(backup2 -> backup2.getFileName().toString().contains(backupBox.getSelectionModel().getSelectedItem())).findFirst().get();

                Files.move(backup, java.nio.file.Paths.get(Consts.Paths.DATABASE_FULL), StandardCopyOption.REPLACE_EXISTING);

                updateBackups();

                NodeUtils.blinkGreen(loadBackupButton);

                DialogUtils.createAlert(Alert.AlertType.WARNING, Consts.Dialogs.RESTART_TITLE, Consts.Dialogs.RESTART_MESSAGE).showAndWait();
            }

            catch (IOException e)
            {
                ExceptionUtils.log(e);
            }
        }

        else
        {
            NodeUtils.blinkRed(backupBox, backupBox);
        }
    }

    @FXML
    private void onOpenDataFolderClick()
    {
    	try
        {
            Desktop.getDesktop().open(new File(Consts.Paths.DATA_FOLDER));
        }

        catch (IOException e)
        {
            ExceptionUtils.log(e);
        }
    }

    @FXML
    private void onResetStatsClick(ActionEvent event)
    {
        Alert alert = DialogUtils.createAlert(Alert.AlertType.CONFIRMATION, Consts.Dialogs.RESET_STATS_TITLE, Consts.Dialogs.RESET_STATS_MESSAGE);

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            try
            {
                stats.deleteAll();
                stats.createTable();

                NodeUtils.blinkGreen((Node)event.getSource());
            }

            catch (DBIException e)
            {
                ExceptionUtils.log(e);
            }
        }
    }

    @FXML
    private void onStartOfSchoolClick(ActionEvent event)
	{
        Alert alert = DialogUtils.createAlert(Alert.AlertType.CONFIRMATION, Consts.Dialogs.START_OF_SCHOOL_TITLE, Consts.Dialogs.START_OF_SCHOOL_MESSAGE);

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            try
            {
                BackupManager.createBackup(Consts.Database.START_OF_SCHOOL_BACKUP_SUFFIX);

                borrowedBooks.deleteWhereStudentGrade12();

                students.deleteWhereGrade12();
                students.updateIncrementGrade();

                NodeUtils.blinkGreen((Node)event.getSource());

                DialogUtils.createAlert(Alert.AlertType.WARNING, Consts.Dialogs.RESTART_TITLE, Consts.Dialogs.RESTART_MESSAGE).showAndWait();
            }

            catch (DBIException e)
            {
                ExceptionUtils.log(e);
            }
        }
    }
}
