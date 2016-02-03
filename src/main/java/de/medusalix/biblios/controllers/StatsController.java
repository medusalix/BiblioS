package de.medusalix.biblios.controllers;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.managers.ReportManager;
import de.medusalix.biblios.sql.operator.JoinOnOperator;
import de.medusalix.biblios.sql.operator.LimitOperator;
import de.medusalix.biblios.sql.operator.OrderByOperator;
import de.medusalix.biblios.sql.query.base.ResultQuery;
import de.medusalix.biblios.sql.query.general.SelectQuery;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.sql.operator.LimitOperator;
import de.medusalix.biblios.sql.query.base.ResultQuery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StatsController
{
	@FXML
	private TitledPane chartPane;
	
    @FXML
    private PieChart chart;

    @FXML
    private Label studentCountLabel, bookCountLabel, borrowedBookCountLabel, mostBorrowedLabel, backupCountLabel;
    
    @FXML
    private void initialize()
    {
        updateData();
    }

	private void updateData()
    {
        Platform.runLater(() -> chartPane.setExpanded(true));
		
		updateChart();
		updateStats();
	}
	
	private void updateChart()
    {
        try (Connection connection = DatabaseManager.openConnection())
        {
            ResultQuery selectQuery = new SelectQuery(Consts.Database.STATS_TABLE_NAME, Consts.Database.TITLE_COLUMN_NAME, Consts.Database.NUMBER_OF_BORROWS_COLUMN_NAME);

            selectQuery.addOperator(new JoinOnOperator(Consts.Database.BOOKS_TABLE_NAME, Consts.Database.BOOK_ID_COLUMN_NAME, Consts.Database.ID_COLUMN_NAME));
            selectQuery.addOperator(new OrderByOperator(Consts.Database.NUMBER_OF_BORROWS_COLUMN_NAME, OrderByOperator.Order.DESCENDING));
            selectQuery.addOperator(new LimitOperator(Consts.Misc.MAX_BOOKS_IN_STATS));

            List<HashMap<String, Object>> stats = selectQuery.execute(connection);

            chart.getData().setAll(stats.stream().map(stat -> new PieChart.Data(stat.get(Consts.Database.TITLE_COLUMN_NAME).toString(), (int)stat.get(Consts.Database.NUMBER_OF_BORROWS_COLUMN_NAME))).collect(Collectors.toList()));
        }

        catch (SQLException e)
        {
            ReportManager.reportException(e);
        }

        chart.getData().forEach(stat -> stat.setPieValue(stat.getPieValue() / chart.getData().size() * 100));
        
        for (Node node : chart.lookupAll(".text.chart-pie-label"))
        {
        	Text text = (Text)node;
        	
        	PieChart.Data value = chart.getData().stream().filter(value2 -> value2.getName().equals(text.getText())).findFirst().get();
        	
        	text.setText(String.valueOf(Math.round(value.getPieValue() / 100 * chart.getData().size())));
        }
        
        for (Node node : chart.lookupAll(".label.chart-legend-item"))
        {
        	Label label = (Label)node;
        	
        	PieChart.Data value = chart.getData().stream().filter(value2 -> value2.getName().equals(label.getText())).findFirst().get();
        	
        	label.setText(Math.round(value.getPieValue() / 100 * chart.getData().size()) + "   " + label.getText());
        }
        
        if (chart.getData().isEmpty())
        {
            chart.getData().add(new PieChart.Data(Consts.Messages.STAT_CHART_PLACEHOLDER, 100));
        }
	}
	
	private void updateStats()
	{
        try (Connection connection = DatabaseManager.openConnection())
        {
            ResultQuery studentCountQuery = new SelectQuery(Consts.Database.STUDENTS_TABLE_NAME, Consts.Database.COUNT_COLUMN_NAME);

            studentCountLabel.setText(studentCountQuery.execute(connection).get(0).get(Consts.Database.COUNT_COLUMN_NAME).toString());

            ResultQuery bookCountQuery = new SelectQuery(Consts.Database.BOOKS_TABLE_NAME, Consts.Database.COUNT_COLUMN_NAME);

            bookCountLabel.setText(bookCountQuery.execute(connection).get(0).get(Consts.Database.COUNT_COLUMN_NAME).toString());

            ResultQuery borrowedBookCountQuery = new SelectQuery(Consts.Database.BORROWED_BOOKS_TABLE_NAME, Consts.Database.COUNT_COLUMN_NAME);

            borrowedBookCountLabel.setText(borrowedBookCountQuery.execute(connection).get(0).get(Consts.Database.COUNT_COLUMN_NAME).toString());

            ResultQuery mostBorrowedQuery = new SelectQuery(Consts.Database.BORROWED_BOOKS_TABLE_NAME, Consts.Database.NAME_COLUMN_NAME, Consts.Database.COUNT_COLUMN_NAME);

            mostBorrowedQuery.addOperator(new JoinOnOperator(Consts.Database.STUDENTS_TABLE_NAME, Consts.Database.STUDENT_ID_COLUMN_NAME, Consts.Database.ID_COLUMN_NAME));
            mostBorrowedQuery.addOperator(new OrderByOperator(Consts.Database.COUNT_COLUMN_NAME, OrderByOperator.Order.DESCENDING));

            List<HashMap<String, Object>> mostBorrowedResults = mostBorrowedQuery.execute(connection);

            mostBorrowedLabel.setText(!mostBorrowedResults.isEmpty() ? mostBorrowedResults.get(0).get(Consts.Database.NAME_COLUMN_NAME).toString() : null);

            backupCountLabel.setText(String.valueOf(Files.list(Paths.get(Consts.Database.BACKUP_FOLDER_PATH)).count()));
        }

        catch (SQLException | IOException e)
        {
            ReportManager.reportException(e);
        }
	}
}
