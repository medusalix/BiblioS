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
import de.medusalix.biblios.database.access.BookDatabase;
import de.medusalix.biblios.database.access.BorrowedBookDatabase;
import de.medusalix.biblios.database.access.StatDatabase;
import de.medusalix.biblios.database.access.StudentDatabase;
import de.medusalix.biblios.database.Database;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;

public class StatsController
{
    private static final Logger logger = LogManager.getLogger(StatsController.class);

    @FXML
    private TitledPane chartPane;

    @FXML
    private PieChart chart;

    @FXML
    private Label studentCountLabel, bookCountLabel, borrowedBookCountLabel, backupCountLabel;

    private StudentDatabase studentDatabase = Database.get(StudentDatabase.class);
    private BookDatabase bookDatabase = Database.get(BookDatabase.class);
    private BorrowedBookDatabase borrowedBookDatabase = Database.get(BorrowedBookDatabase.class);
    private StatDatabase statDatabase = Database.get(StatDatabase.class);

    @FXML
    private void initialize()
    {
        Platform.runLater(() -> chartPane.setExpanded(true));

        updateChart();
        updateStats();
    }

    private void updateChart()
    {
        chart.getData().clear();

        statDatabase.findAllWithBookTitle(Consts.Misc.MAX_STATS_TO_DISPLAY)
            .stream()
            .map(stat -> new PieChart.Data(stat.getBookTitle(), stat.getNumberOfBorrows()))
            .forEach(data -> chart.getData().add(data));

        chart.getData()
            .forEach(data ->
            {
                // Add the number of borrows to the legend
                chart.lookupAll(".label.chart-legend-item")
                    .stream()
                    .map(node -> (Label)node)
                    .filter(label -> label.getText().equals(data.getName()))
                    .findFirst()
                    .ifPresent(label -> label.setText((int)data.getPieValue() + "   " + label.getText()));

                // Convert the number of borrows into percentage values
                data.setPieValue(data.getPieValue() / chart.getData().size() * 100);
            });

        if (chart.getData().isEmpty())
        {
            chart.getData().add(new PieChart.Data(Consts.Strings.STAT_CHART_PLACEHOLDER, 100));
        }
    }

    private void updateStats()
    {
        try
        {
            studentCountLabel.setText(String.valueOf(studentDatabase.count()));
            bookCountLabel.setText(String.valueOf(bookDatabase.count()));
            borrowedBookCountLabel.setText(String.valueOf(borrowedBookDatabase.count()));
            backupCountLabel.setText(String.valueOf(Files.list(Consts.Paths.BACKUP_FOLDER).count()));
        }

        catch (IOException e)
        {
            logger.error("", e);
        }
    }
}
