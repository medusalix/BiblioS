package de.medusalix.biblios.controllers;

import de.medusalix.biblios.core.Consts;
import de.medusalix.biblios.database.access.Books;
import de.medusalix.biblios.database.access.BorrowedBooks;
import de.medusalix.biblios.database.access.Stats;
import de.medusalix.biblios.database.access.Students;
import de.medusalix.biblios.managers.DatabaseManager;
import de.medusalix.biblios.utils.ExceptionUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skife.jdbi.v2.exceptions.DBIException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class StatsController
{
    private Logger logger = LogManager.getLogger(StatsController.class);

	@FXML
	private TitledPane chartPane;
	
    @FXML
    private PieChart chart;

    @FXML
    private Label studentCountLabel, bookCountLabel, borrowedBookCountLabel, backupCountLabel;

    private Students students = DatabaseManager.createDao(Students.class);
    private Books books = DatabaseManager.createDao(Books.class);
    private BorrowedBooks borrowedBooks = DatabaseManager.createDao(BorrowedBooks.class);
    private Stats stats = DatabaseManager.createDao(Stats.class);

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
        try
        {
            chart.getData().setAll(stats.findAllWithBookTitle()
                                        .stream()
                                        .map(stat -> new PieChart.Data(stat.getBookTitle(), stat.getNumberOfBorrows()))
                                        .collect(Collectors.toList()));
        }

        catch (DBIException e)
        {
            ExceptionUtils.log(e);
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
            chart.getData().add(new PieChart.Data(Consts.Strings.STAT_CHART_PLACEHOLDER, 100));
        }
	}
	
	private void updateStats()
	{
        try
        {
            studentCountLabel.setText(String.valueOf(students.count()));
            bookCountLabel.setText(String.valueOf(books.count()));
            borrowedBookCountLabel.setText(String.valueOf(borrowedBooks.count()));
            backupCountLabel.setText(String.valueOf(Files.list(java.nio.file.Paths.get(Consts.Paths.BACKUP_FOLDER)).count()));
        }

        catch (DBIException | IOException e)
        {
            ExceptionUtils.log(e);
        }
	}
}
