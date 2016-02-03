package de.medusalix.biblios.controllers;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.helpers.GoogleBooksHelper;
import de.medusalix.biblios.managers.BackupManager;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.managers.ReportManager;
import de.medusalix.biblios.sql.operator.WhereLikeOperator;
import de.medusalix.biblios.sql.operator.WhereNotOperator;
import de.medusalix.biblios.sql.operator.WhereOperator;
import de.medusalix.biblios.sql.query.base.ActionQuery;
import de.medusalix.biblios.sql.query.base.ResultQuery;
import de.medusalix.biblios.sql.query.general.DeleteQuery;
import de.medusalix.biblios.sql.query.general.SelectQuery;
import de.medusalix.biblios.sql.query.general.UpdateQuery;
import de.medusalix.biblios.sql.query.specific.TableCreationQuery;
import de.medusalix.biblios.helpers.DialogHelper;
import de.medusalix.biblios.helpers.NodeHelper;
import de.medusalix.biblios.helpers.ThreadHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdministrationController
{
    @FXML
    private ComboBox<String> backupBox;
    
    @FXML
    private Button createBackupButton, loadBackupButton;

    @FXML
    private TextField apiKeyField;

    @FXML
    private void initialize()
    {
        initApiKeyField();
        updateBackups();
    }

    private void initApiKeyField()
    {
        apiKeyField.setText(GoogleBooksHelper.readApiKey());
        apiKeyField.focusedProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue)
                GoogleBooksHelper.saveApiKey(apiKeyField.getText());
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
                backupBox.setPromptText(Consts.Messages.CHOOSE_BACKUP_TEXT);
            }

            else
            {
                backupBox.setDisable(true);
                backupBox.setPromptText(Consts.Messages.NO_BACKUP_EXISTING_TEXT);
            }
        }
	}

    @FXML
    private void onCreateBackupClick()
    {
        BackupManager.createBackup(Consts.Database.MANUAL_BACKUP_SUFFIX);

        NodeHelper.blinkGreen(createBackupButton);

        updateBackups();
    }
    
    @FXML
    private void onDeleteAllBackupsClick(ActionEvent event)
    {
        Alert alert = DialogHelper.createAlert(Alert.AlertType.CONFIRMATION, Consts.Messages.DELETE_ALL_BACKUPS_TITLE, Consts.Messages.DELETE_ALL_BACKUPS_MESSAGE);

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            ThreadHelper.runThreadAsDaemon(() ->
            {
                BackupManager.deleteAllBackups();

                Platform.runLater(() ->
                {
                    updateBackups();

                    NodeHelper.blinkGreen((Node)event.getSource());
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
                Path backup = Files.list(Paths.get(Consts.Database.BACKUP_FOLDER_PATH)).filter(backup2 -> backup2.getFileName().toString().contains(backupBox.getSelectionModel().getSelectedItem())).findFirst().get();

                Files.move(backup, Paths.get(Consts.Database.DATABASE_PATH), StandardCopyOption.REPLACE_EXISTING);

                updateBackups();

                NodeHelper.blinkGreen(loadBackupButton);
            }

            catch (IOException e)
            {
                ReportManager.reportException(e);
            }
        }

        else
        {
            NodeHelper.blinkRed(backupBox, backupBox);
        }
    }

    @FXML
    private void onOpenDataFolderClick()
    {
    	try
        {
            Desktop.getDesktop().open(new File(Consts.Resources.DATA_FOLDER_PATH));
        }

        catch (IOException e)
        {
            ReportManager.reportException(e);
        }
    }

    @FXML
    private void onResetStatsClick(ActionEvent event)
    {
        Alert alert = DialogHelper.createAlert(Alert.AlertType.CONFIRMATION, Consts.Messages.RESET_STATS_TITLE, Consts.Messages.RESET_STATS_MESSAGE);

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            try (Connection connection = DatabaseManager.openConnection())
            {
                ActionQuery deleteQuery = new DeleteQuery(Consts.Database.STATS_TABLE_NAME);

                ActionQuery createTableQuery = new TableCreationQuery(Consts.Database.CREATE_STATS_TABLE_QUERY);

                deleteQuery.executeBatch(connection, createTableQuery);

                NodeHelper.blinkGreen((Node)event.getSource());
            }

            catch (SQLException e)
            {
                ReportManager.reportException(e);
            }
        }
    }

    @FXML
    private void onStartOfSchoolClick(ActionEvent event)
	{
        Alert alert = DialogHelper.createAlert(Alert.AlertType.CONFIRMATION, Consts.Messages.START_OF_SCHOOL_TITLE, Consts.Messages.START_OF_SCHOOL_MESSAGE);

        if (alert.showAndWait().get() == ButtonType.OK)
        {
            try (Connection connection = DatabaseManager.openConnection())
            {
                BackupManager.createBackup(Consts.Database.START_OF_SCHOOL_BACKUP_SUFFIX);

                ActionQuery deleteQuery = new DeleteQuery(Consts.Database.STUDENTS_TABLE_NAME);

                deleteQuery.addOperator(new WhereLikeOperator(Consts.Database.GRADE_COLUMN_NAME, "12_"));
                deleteQuery.execute(connection);

                ResultQuery selectQuery = new SelectQuery(Consts.Database.STUDENTS_TABLE_NAME, Consts.Database.ID_COLUMN_NAME, Consts.Database.GRADE_COLUMN_NAME);

                selectQuery.addOperator(new WhereNotOperator(Consts.Database.GRADE_COLUMN_NAME, ""));

                List<HashMap<String, Object>> students = selectQuery.execute(connection);

                List<ActionQuery> queries = new ArrayList<>();

                for (HashMap<String, Object> result : students)
                {
                    String grade = result.get(Consts.Database.GRADE_COLUMN_NAME).toString();

                    Matcher matcher = Pattern.compile("\\d*").matcher(grade);

                    if (matcher.find())
                    {
                        String match = matcher.group(matcher.groupCount());

                        ActionQuery query = new UpdateQuery(Consts.Database.STUDENTS_TABLE_NAME, Consts.Database.GRADE_COLUMN_NAME, Integer.parseInt(match) + 1 + grade.substring(match.length()));

                        query.addOperator(new WhereOperator(Consts.Database.ID_COLUMN_NAME, result.get(Consts.Database.ID_COLUMN_NAME)));

                        queries.add(query);
                    }
                }

                queries.get(0).executeBatch(connection, queries.toArray(new ActionQuery[queries.size()]));

                NodeHelper.blinkGreen((Node)event.getSource());
            }

            catch (SQLException e)
            {
                ReportManager.reportException(e);
            }
        }
    }
}
