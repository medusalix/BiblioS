/*
 * Copyright (C) 2016 Medusalix
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

package de.medusalix.biblios.controllers;

import de.medusalix.biblios.Consts;
import de.medusalix.biblios.database.access.BorrowedBookDatabase;
import de.medusalix.biblios.database.access.StatDatabase;
import de.medusalix.biblios.database.access.StudentDatabase;
import de.medusalix.biblios.database.Database;
import de.medusalix.biblios.utils.AlertUtils;
import de.medusalix.biblios.utils.BackupUtils;
import de.medusalix.biblios.utils.NodeUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;

public class AdministrationController
{
    private static final Logger logger = LogManager.getLogger(AdministrationController.class);
    
    @FXML
    private ComboBox<BackupUtils.Backup> backupBox;
    
    @FXML
    private Button createBackupButton, loadBackupButton;

    @FXML
    private TextField apiKeyField;

    private StudentDatabase studentDatabase = Database.get(StudentDatabase.class);
    private BorrowedBookDatabase borrowedBookDatabase = Database.get(BorrowedBookDatabase.class);
    private StatDatabase statDatabase = Database.get(StatDatabase.class);

    @FXML
    private void initialize()
    {
        initApiKeyField();
        updateBackups();
    }
    
    private void initApiKeyField()
    {
        try
        {
            List<String> lines = Files.readAllLines(Consts.Paths.API_KEY);

            apiKeyField.setText(lines.get(0));
        }
        
        catch (IOException e)
        {
            logger.error("", e);
        }
        
        apiKeyField.focusedProperty().addListener((observable, oldValue, focused) ->
        {
            if (focused)
            {
                return;
            }

            try
            {
                Files.write(Consts.Paths.API_KEY, apiKeyField.getText().getBytes());
            }

            catch (IOException e)
            {
                logger.error("", e);
            }
        });
    }

	private void updateBackups()
	{
        List<BackupUtils.Backup> backups;
        
        try
        {
            backups = BackupUtils.findBackups();
        }
        
        catch (IOException e)
        {
            logger.error("", e);

            return;
        }
        
        backups.sort(Comparator.comparing(BackupUtils.Backup::getTime).reversed());
        
        backupBox.getItems().setAll(backups);

        if (backupBox.getItems().size() > 0)
        {
            backupBox.setDisable(false);
            backupBox.setPromptText(Consts.Strings.CHOOSE_BACKUP_TEXT);
        }

        else
        {
            backupBox.setDisable(true);
            backupBox.setPromptText(Consts.Strings.NO_BACKUPS_EXISTING_TEXT);
        }
	}

    @FXML
    private void onCreateBackupClick()
    {
        try
        {
            BackupUtils.createBackup(Consts.Database.MANUAL_BACKUP_SUFFIX);
    
            NodeUtils.blinkGreen(createBackupButton);
        }
        
        catch (IOException e)
        {
            logger.error("", e);

            NodeUtils.blinkRed(createBackupButton);
        }

        updateBackups();
    }
    
    @FXML
    private void onDeleteAllBackupsClick(ActionEvent event)
    {
        AlertUtils.showConfirmation(
            Consts.Dialogs.DELETE_ALL_BACKUPS_TITLE,
            Consts.Dialogs.DELETE_ALL_BACKUPS_MESSAGE,
            () ->
            {
                try
                {
                    BackupUtils.deleteAllBackups();

                    NodeUtils.blinkGreen((Node)event.getSource());
                }

                catch (IOException e)
                {
                    logger.error("", e);

                    NodeUtils.blinkRed((Node)event.getSource());
                }

                updateBackups();
            }
        );
    }

    @FXML
    private void onLoadBackupClick()
    {
        BackupUtils.Backup selectedBackup = backupBox.getSelectionModel()
            .getSelectedItem();
        
        if (selectedBackup == null)
        {
            NodeUtils.blinkRed(backupBox, backupBox);
            
            return;
        }
        
        try
        {
            selectedBackup.restoreDatabase();

            NodeUtils.blinkGreen(loadBackupButton);
        }
        
        catch (IOException e)
        {
            logger.error("", e);

            NodeUtils.blinkRed(loadBackupButton);
        }

        AlertUtils.showWarning(
            Consts.Dialogs.RESTART_TITLE,
            Consts.Dialogs.RESTART_MESSAGE
        );

        Platform.exit();
    }

    @FXML
    private void onOpenDataFolderClick(ActionEvent event)
    {
    	try
        {
            Desktop.getDesktop().open(Consts.Paths.DATA_FOLDER.toFile());
        }

        catch (IOException e)
        {
            logger.error("", e);

            NodeUtils.blinkRed((Node)event.getSource());
        }
    }

    @FXML
    private void onResetStatsClick(ActionEvent event)
    {
        AlertUtils.showConfirmation(
            Consts.Dialogs.RESET_STATS_TITLE,
            Consts.Dialogs.RESET_STATS_MESSAGE,
            () ->
            {
                statDatabase.deleteAll();
                statDatabase.createTable();

                NodeUtils.blinkGreen((Node)event.getSource());
            }
        );
    }

    @FXML
    private void onStartOfSchoolClick(ActionEvent event)
	{
        AlertUtils.showConfirmation(
            Consts.Dialogs.START_OF_SCHOOL_TITLE,
            Consts.Dialogs.START_OF_SCHOOL_MESSAGE,
            () ->
            {
                try
                {
                    BackupUtils.createBackup(Consts.Database.START_OF_SCHOOL_BACKUP_SUFFIX);

                    borrowedBookDatabase.deleteGraduatedStudents();

                    studentDatabase.deleteWhereGrade12();
                    studentDatabase.updateIncrementGradeSimple();
                    studentDatabase.updateIncrementGradeComplex();

                    NodeUtils.blinkGreen((Node)event.getSource());
                }

                catch (IOException e)
                {
                    logger.error("", e);

                    NodeUtils.blinkRed((Node)event.getSource());
                }

                AlertUtils.showWarning(
                    Consts.Dialogs.RESTART_TITLE,
                    Consts.Dialogs.RESTART_MESSAGE
                );

                Platform.exit();
            }
        );
    }
}
