/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.analyze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jscape.database.HistoryTable;
import jscape.database.PerformanceTable;
import jscape.database.StudentTable;
import jscape.analyze.graphs.MonthlyProgress;
import jscape.analyze.graphs.YearlyProgress;
import jscape.analyze.performance.GlobalViewStatsTable;
import jscape.analyze.performance.PerformancePieChart;
import jscape.analyze.performance.PerformanceStatsTable;

/**
 *
 * @author achantreau
 */
public class AnalyzePane extends BorderPane {

    private Label firstName;
    private Label lastName;
    private Label loginName;
    private Label className;
    private Label lastLogin;
    private Label lastExerciseAnswered;

    private ComboBox categoryBox;
    private ComboBox graphCategoryBox;
    private ComboBox graphTypesBox;
    private ComboBox monthBox;
    private ComboBox yearBox;

    private HBox graphSelectHBox;

    private PerformanceStatsTable performanceStatsTable;
    private PerformancePieChart performancePieChart;
    private MonthlyProgress monthlyProgressChart;
    private YearlyProgress yearlyProgressChart;

    private GlobalViewStatsTable globalViewStatsTable;
    private ComboBox globalViewCategoryBox;

    private Label selectClassLabel;
    private ComboBox selectClassBox;

    private ComboBox selectStudentBox;
    private Label selectStudentLabel;

    private VBox globalViewVBox;
    private ScrollPane scrollPane;

    private StackPane stackPane;

    public AnalyzePane() {
        super();

        stackPane = new StackPane();

        selectClassLabel = new Label("Select class:    ");
        selectClassLabel.setFont(new Font("Arial", 13));
        selectClassLabel.setStyle("-fx-text-fill: #2E211C;");
        selectClassLabel.setAlignment(Pos.TOP_CENTER);

        ArrayList<String> classList = StudentTable.getClassList();
        ObservableList<String> classObsList
                = FXCollections.observableArrayList(classList);

        selectClassBox = new ComboBox(classObsList);
        selectClassBox.setMinWidth(150);
        selectClassBox.setMaxWidth(Double.MAX_VALUE);
        selectClassBox.getSelectionModel().selectFirst();
        selectClassBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                ArrayList<String> studentList = StudentTable.getStudentList((String) selectClassBox.getSelectionModel().getSelectedItem());
                ObservableList<String> studentObsList = FXCollections.observableArrayList(studentList);
                studentObsList.add("Global view");
                selectStudentBox.setItems(studentObsList);
                selectStudentBox.getSelectionModel().selectFirst();
            }
        });

        HBox selectClassHBox = new HBox(5);
        selectClassHBox.getChildren().addAll(selectClassLabel, selectClassBox);

        ArrayList<String> studentList = StudentTable.getStudentList((String) selectClassBox.getSelectionModel().getSelectedItem());
        ObservableList<String> studentObsList = FXCollections.observableArrayList(studentList);
        studentObsList.add("Global view");

        selectStudentBox = new ComboBox(studentObsList);
        selectStudentBox.setMinWidth(150);
        selectStudentBox.setMaxWidth(Double.MAX_VALUE);
        selectStudentBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                String selectedItem = (String) selectStudentBox.getSelectionModel().getSelectedItem();

                if (selectedItem != null) {
                    if ("Global view".equals(selectedItem)) {
                        scrollPane.setVisible(false);
                        globalViewVBox.setVisible(true);

                        firstName.setText("");
                        lastName.setText("");
                        loginName.setText("");
                        className.setText("");
                        lastLogin.setText("");
                        lastExerciseAnswered.setText("");

                    } else {
                        globalViewVBox.setVisible(false);
                        scrollPane.setVisible(true);

                        String[] parts = selectedItem.split("-");
                        String loginId = parts[0].trim();
                        ArrayList<String> profileInfoPayload = StudentTable.getProfileInfo(loginId);

                        firstName.setText(profileInfoPayload.get(0));
                        lastName.setText(profileInfoPayload.get(1));
                        loginName.setText(loginId);
                        className.setText(profileInfoPayload.get(2));

                        if ("null".equals(profileInfoPayload.get(3))) {
                            lastLogin.setText("Last login: NO DATA YET");
                        } else {
                            lastLogin.setText("Last login: " + profileInfoPayload.get(3));
                        }

                        if ("null".equals(profileInfoPayload.get(4))) {
                            lastExerciseAnswered.setText("Last exercise answered: NO DATA YET");
                        } else {
                            lastExerciseAnswered.setText("Last exercise answered: " + profileInfoPayload.get(4));
                        }

                        ArrayList<String> performanceStatsPayload = PerformanceTable.getPerformanceStats(loginId);
                        performanceStatsTable.setItems(performanceStatsPayload);

                        /* Add categories to comboBox, select the first item which triggers
                         the listener to add the data to the pie chart. */
                        if (categoryBox.getSelectionModel().getSelectedItem() == null) {
                            ObservableList<String> graphCategories = performanceStatsTable.getExerciseCategories();
                            graphCategoryBox.setItems(graphCategories);
                            globalViewCategoryBox.setItems(graphCategories);

                            ObservableList<String> exerciseCategories = performanceStatsTable.getExerciseCategories();
                            exerciseCategories.add("-- Total answers per category --");

                            categoryBox.setItems(exerciseCategories);
                            categoryBox.getSelectionModel().selectFirst();
                        } else {
                            String selectedCategory = categoryBox.getSelectionModel().getSelectedItem().toString();
                            ObservableList items = categoryBox.getItems();

                            if (!items.contains("-- Total answers per category --")) {
                                categoryBox.getItems().add("-- Total answers per category --");
                            }

                            performancePieChart.setData(selectedCategory);
                        }

                        // Get date list
                        ArrayList<String> dateList = HistoryTable.getDateList(loginId);
                        ObservableList<String> monthsAndYears = FXCollections.observableArrayList(dateList);
                        monthBox.setItems(monthsAndYears);
                        yearBox.setItems(FXCollections.observableArrayList(getYearList(dateList)));

                        graphCategoryBox.getSelectionModel().clearSelection();
                        monthBox.getSelectionModel().clearSelection();
                        yearBox.getSelectionModel().clearSelection();
                        monthlyProgressChart.setVisible(false);
                        yearlyProgressChart.setVisible(false);
                    }
                }
            }
        });

        Separator horizontalSeparator = new Separator(Orientation.HORIZONTAL);
        horizontalSeparator.setStyle("-fx-border-style: solid;\n"
                + "-fx-border-width: 1px;\n"
                + "-fx-background-color: black;");

        selectStudentLabel = new Label("Select student:");
        selectStudentLabel.setFont(new Font("Arial", 13));
        selectStudentLabel.setStyle("-fx-text-fill: #2E211C;");
        selectStudentLabel.setAlignment(Pos.TOP_CENTER);

        HBox selectStudentHBox = new HBox(5);
        selectStudentHBox.getChildren().addAll(selectStudentLabel, selectStudentBox);

        firstName = new Label();
        lastName = new Label();
        loginName = new Label();
        className = new Label();
        lastLogin = new Label();
        lastExerciseAnswered = new Label();

        // TODO: PUT THIS IN CSS
        firstName.setFont(new Font("Arial", 18));
        firstName.setStyle("-fx-text-fill: #2E211C;");

        lastName.setFont(new Font("Arial", 20));
        lastName.setStyle("-fx-text-fill: #2E211C;");

        loginName.setFont(new Font("Arial", 14));
        loginName.setStyle("-fx-text-fill: #2E211C;");

        className.setFont(new Font("Arial", 14));
        className.setStyle("-fx-text-fill: #2E211C;");

        lastLogin.setFont(new Font("Arial", 13));
        lastLogin.setStyle("-fx-text-fill: #2E211C");

        lastExerciseAnswered.setFont(new Font("Arial", 13));
        lastExerciseAnswered.setStyle("-fx-text-fill: #2E211C;");

        BorderPane profileInfo = new BorderPane();
        profileInfo.setMinWidth(285);
        profileInfo.setMaxWidth(285);
        profileInfo.setPrefWidth(285);

        profileInfo.setId("page-tree");

        VBox profileInfoTop = new VBox();
        profileInfoTop.getChildren().addAll(selectClassHBox, selectStudentHBox, horizontalSeparator, firstName, lastName, loginName, className);
        profileInfoTop.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(selectClassHBox, new Insets(20, 0, 5, 10));
        VBox.setMargin(selectStudentHBox, new Insets(20, 0, 15, 10));
        VBox.setMargin(firstName, new Insets(20, 0, 0, 0));

        VBox profileInfoBottom = new VBox();
        profileInfoBottom.getChildren().addAll(lastLogin, lastExerciseAnswered);
        profileInfoBottom.setAlignment(Pos.BOTTOM_CENTER);
        VBox.setMargin(lastExerciseAnswered, new Insets(0, 0, 20, 0));

        profileInfo.setTop(profileInfoTop);
        profileInfo.setBottom(profileInfoBottom);

        // Create display area for profile statistics
        VBox profileStatistics = new VBox() {
            // stretch to allways fill height of scrollpane
            @Override
            protected double computePrefHeight(double width) {
                return Math.max(
                        super.computePrefHeight(width),
                        getParent().getBoundsInLocal().getHeight()
                );
            }
        };

        // Add STATISTICS title bar
        Label statistics = new Label("      STATISTICS");
        statistics.setMaxWidth(Double.MAX_VALUE);
        statistics.setMinHeight(Control.USE_PREF_SIZE);
        statistics.getStyleClass().add("category-header");
        profileStatistics.getChildren().add(statistics);

        // Create Performance Summary Label
        Label performanceSummary = new Label("Performance Summary");
        performanceSummary.setFont(new Font("Arial", 20));
        performanceSummary.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");

        // Create Performance Statistics Table and Performance Pie Chart
        performanceStatsTable = new PerformanceStatsTable();
        performancePieChart = new PerformancePieChart(performanceStatsTable);

        // Combine performance label and performance table into a VBox
        VBox performanceVBox = new VBox();
        performanceVBox.getChildren().addAll(performanceSummary, performanceStatsTable);

        // Create grid
        GridPane grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(0, 10, 0, 10));
        ColumnConstraints col1 = new ColumnConstraints(150);
        ColumnConstraints col2 = new ColumnConstraints(180);
        ColumnConstraints col3 = new ColumnConstraints(180);
        grid.getColumnConstraints().addAll(col1, col2, col3);

        // Create choose label
        Label chooseCategoryLabel = new Label("Choose Exercise Category:");
        chooseCategoryLabel.setAlignment(Pos.TOP_CENTER);
        chooseCategoryLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");

        // Create combo box for categories, i.e. arrays, loops, etc...
        categoryBox = new ComboBox();
        categoryBox.setMaxWidth(Double.MAX_VALUE);
        categoryBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                performancePieChart.setData(t1.toString());
            }
        });

        // Add elements to the grid and lay them out
        GridPane.setConstraints(chooseCategoryLabel, 0, 0);
        GridPane.setConstraints(categoryBox, 1, 0, 2, 1);
        GridPane.setConstraints(performancePieChart, 0, 1, 3, 1, HPos.CENTER, VPos.TOP);
        grid.getChildren().addAll(chooseCategoryLabel, categoryBox, performancePieChart);

        // Combine all statistics components into a HBox
        HBox statisticsHBox = new HBox(80);
        statisticsHBox.getChildren().addAll(grid, performanceVBox);

        // Complete profile statistics component
        profileStatistics.getChildren().add(statisticsHBox);

        // Add GRAPHS title bar
        Label graphs = new Label("      GRAPHS");
        graphs.setMaxWidth(Double.MAX_VALUE);
        graphs.setMinHeight(Control.USE_PREF_SIZE);
        graphs.getStyleClass().add("category-header");
        profileStatistics.getChildren().add(graphs);

        // Create choose label
        Label chooseGraphLabel = new Label("Choose Graph Type:");
        chooseGraphLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");

        // Create combo box for graph types
        ObservableList<String> graphTypes
                = FXCollections.observableArrayList("Monthly Progress", "Yearly Progress", "Knowledge Distribution");
        graphTypesBox = new ComboBox(graphTypes);
        graphTypesBox.setMaxWidth(Double.MAX_VALUE);
        graphTypesBox.getSelectionModel().selectFirst();

        // Combine the two and add to the scene
        HBox graphTypeHBox = new HBox(5);
        graphTypeHBox.getChildren().addAll(chooseGraphLabel, graphTypesBox);

        // Create choose exercise category for graph label
        final Label chooseGraphCategoryLabel = new Label("Choose Exercise Category:");
        chooseGraphCategoryLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");

        // Create graph exercise category box
        graphCategoryBox = new ComboBox();
        graphCategoryBox.setMinWidth(150);
        graphCategoryBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(graphCategoryBox, Priority.ALWAYS);
        graphCategoryBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if ("Monthly Progress".equals((String) graphTypesBox.getSelectionModel().getSelectedItem())) {
                    runFetchMonthlyProgressTask();
                } else if ("Yearly Progress".equals((String) graphTypesBox.getSelectionModel().getSelectedItem())) {
                    runFetchYearlyProgressTask();
                }
            }
        });

        // Combine the two
        final HBox graphCategoryHBox = new HBox(5);
        graphCategoryHBox.getChildren().addAll(chooseGraphCategoryLabel, graphCategoryBox);

        // Create choose month label
        final Label chooseMonthLabel = new Label("Choose Month:");
        chooseMonthLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");

        // Create combo box for months
        monthBox = new ComboBox();
        monthBox.setMaxWidth(Double.MAX_VALUE);
        monthBox.setMinWidth(150);
        monthBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if ("Monthly Progress".equals((String) graphTypesBox.getSelectionModel().getSelectedItem())) {
                    runFetchMonthlyProgressTask();
                }
            }
        });

        // Create HBox and add months stuff
        final HBox monthHBox = new HBox(5);
        monthHBox.getChildren().addAll(chooseMonthLabel, monthBox);

        // Create choose month label
        final Label chooseYearLabel = new Label("Choose Year:");
        chooseYearLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");

        // Create combo box for months
        yearBox = new ComboBox();
        yearBox.setMaxWidth(Double.MAX_VALUE);
        yearBox.setMinWidth(150);
        yearBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if ("Yearly Progress".equals((String) graphTypesBox.getSelectionModel().getSelectedItem())) {
                    runFetchYearlyProgressTask();
                }
            }
        });

        // Create HBox and add year stuff
        final HBox yearHBox = new HBox(5);
        yearHBox.getChildren().addAll(chooseYearLabel, yearBox);

        // Create main graph selection HBox and add to scene
        graphSelectHBox = new HBox(20);
        graphSelectHBox.getChildren().addAll(graphTypeHBox, graphCategoryHBox, monthHBox);
        profileStatistics.getChildren().add(graphSelectHBox);

        // Create Monthly Progress Chart
        CategoryAxis xAxisMonthlyProgress = new CategoryAxis();
        xAxisMonthlyProgress.setLabel("Days");
        NumberAxis yAxisMonthlyProgress = new NumberAxis();
        yAxisMonthlyProgress.setLabel("Exercises Answered");

        monthlyProgressChart = new MonthlyProgress(xAxisMonthlyProgress, yAxisMonthlyProgress);
        monthlyProgressChart.setMaxHeight(Double.MAX_VALUE);
        monthlyProgressChart.setMaxWidth(Double.MAX_VALUE);
        monthlyProgressChart.setVisible(false);

        // Create Yearly Progress Chart
        CategoryAxis xAxisYearlyProgress = new CategoryAxis();
        xAxisYearlyProgress.setLabel("Months");
        xAxisYearlyProgress.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(
                "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")));
        NumberAxis yAxisYearlyProgress = new NumberAxis(0, 800, 50);
        yAxisYearlyProgress.setLabel("Exercises Answered");

        yearlyProgressChart = new YearlyProgress(xAxisYearlyProgress, yAxisYearlyProgress);
        yearlyProgressChart.setMaxHeight(Double.MAX_VALUE);
        yearlyProgressChart.setMaxWidth(Double.MAX_VALUE);
        yearlyProgressChart.setVisible(false);

        StackPane graphStackPane = new StackPane();
        graphStackPane.getChildren().addAll(monthlyProgressChart, yearlyProgressChart);

        graphTypesBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if ("Monthly Progress".equals(t1.toString())) {
                    yearlyProgressChart.setVisible(false);
                    if (!monthlyProgressChart.getData().isEmpty()) {
                        monthlyProgressChart.setVisible(true);
                    }
                    if (graphSelectHBox.getChildren().contains(yearHBox)) {
                        graphSelectHBox.getChildren().remove(yearHBox);
                    }
                    graphSelectHBox.getChildren().add(monthHBox);
                    runFetchMonthlyProgressTask();
                } else if ("Yearly Progress".equals(t1.toString())) {
                    monthlyProgressChart.setVisible(false);
                    if (!yearlyProgressChart.getData().isEmpty()) {
                        yearlyProgressChart.setVisible(true);
                    }
                    if (graphSelectHBox.getChildren().contains(monthHBox)) {
                        graphSelectHBox.getChildren().remove(monthHBox);
                    }
                    graphSelectHBox.getChildren().add(yearHBox);
                    runFetchYearlyProgressTask();
                } else if ("Knowledge Distribution".equals(t1.toString())) {
                    monthlyProgressChart.setVisible(false);
                    if (graphSelectHBox.getChildren().contains(monthHBox)) {
                        graphSelectHBox.getChildren().remove(monthHBox);
                    } else {
                        graphSelectHBox.getChildren().remove(yearHBox);
                    }
                }
            }
        });

        profileStatistics.getChildren().add(graphStackPane);

        profileStatistics.getStyleClass().add("category-page");

        // Add scroll pane
        scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(profileStatistics);

        globalViewVBox = new VBox();

        Label globalViewStatistics = new Label("      GLOBAL VIEW STATISTICS - CLASS " + selectClassBox.getSelectionModel().getSelectedItem());
        globalViewStatistics.setMaxWidth(Double.MAX_VALUE);
        globalViewStatistics.setMinHeight(Control.USE_PREF_SIZE);
        globalViewStatistics.getStyleClass().add("category-header");

        // Create choose month label
        final Label chooseGlobalCategoryLabel = new Label("Choose Exercise Category:");
        chooseGlobalCategoryLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");

        globalViewCategoryBox = new ComboBox();
        globalViewCategoryBox.setMaxWidth(Double.MAX_VALUE);
        globalViewCategoryBox.setMinWidth(150);
        globalViewCategoryBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (t1 != null) {
                    globalViewStatsTable.setItems(PerformanceTable.getGlobalViewStats(t1.toString()));
                }
            }
        });

        HBox globalViewHBox = new HBox(8);
        globalViewHBox.getChildren().addAll(chooseGlobalCategoryLabel, globalViewCategoryBox);
        
        globalViewStatsTable = new GlobalViewStatsTable();
        
        HBox globalViewTableHBox = new HBox();
        globalViewTableHBox.getChildren().add(globalViewStatsTable);
        globalViewTableHBox.setAlignment(Pos.CENTER);
        VBox.setMargin(globalViewTableHBox, new Insets(80, 0, 0, 0));

        globalViewVBox.getChildren().addAll(globalViewStatistics, globalViewHBox, globalViewTableHBox);
        globalViewVBox.getStyleClass().add("category-page");
        globalViewVBox.setVisible(false);

        stackPane.getChildren().addAll(scrollPane, globalViewVBox);

        setLeft(profileInfo);
        setCenter(stackPane);
    }

    private void runFetchYearlyProgressTask() {
        String[] parts = ((String) selectStudentBox.getSelectionModel().getSelectedItem()).split("-");
        String loginId = parts[0].trim();

        if ((String) yearBox.getSelectionModel().getSelectedItem() != null) {
            if ("Class average".equals((String) selectStudentBox.getSelectionModel().getSelectedItem())) {

            } else {
                if ("Total".equals((String) graphCategoryBox.getSelectionModel().getSelectedItem())) {
                    ArrayList<String> graphStatsPayload = HistoryTable.getTotalForYear(loginId, Integer.valueOf((String) yearBox.getSelectionModel().getSelectedItem()));

                    yearlyProgressChart.setData(graphStatsPayload);
                    yearlyProgressChart.setVisible(true);
                } else {
                    ArrayList<String> graphStatsPayload = HistoryTable.getYearData(loginId,
                            (String) graphCategoryBox.getSelectionModel().getSelectedItem(),
                            Integer.valueOf((String) yearBox.getSelectionModel().getSelectedItem()));

                    yearlyProgressChart.setData(graphStatsPayload);
                    yearlyProgressChart.setVisible(true);
                }
            }
        }
    }

    private void runFetchMonthlyProgressTask() {
        String[] parts = ((String) selectStudentBox.getSelectionModel().getSelectedItem()).split("-");
        String loginId = parts[0].trim();

        if ((String) monthBox.getSelectionModel().getSelectedItem() != null) {
            if ("Total".equals((String) graphCategoryBox.getSelectionModel().getSelectedItem())) {
                ArrayList<String> graphStatsPayload = HistoryTable.getTotalForMonth(loginId, (String) monthBox.getSelectionModel().getSelectedItem());

                monthlyProgressChart.setData(graphStatsPayload, (String) monthBox.getSelectionModel().getSelectedItem());
                monthlyProgressChart.setVisible(true);
            } else {
                ArrayList<String> graphStatsPayload = HistoryTable.getHistoryDataForMonth(loginId,
                        (String) monthBox.getSelectionModel().getSelectedItem(),
                        (String) graphCategoryBox.getSelectionModel().getSelectedItem());

                monthlyProgressChart.setData(graphStatsPayload, (String) monthBox.getSelectionModel().getSelectedItem());
                monthlyProgressChart.setVisible(true);
            }
        }
    }

    private ArrayList<String> getYearList(ArrayList<String> payload) {
        ArrayList<String> yearList = new ArrayList<>();
        Set<String> yearSet = new HashSet<>();

        for (String s : payload) {
            String[] words = s.split(" ");
            yearSet.add(words[1]);
        }

        for (String s : yearSet) {
            yearList.add(s);
        }

        Collections.sort(yearList);

        return yearList;
    }
}
