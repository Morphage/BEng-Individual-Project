/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import jscape.JScape;
import jscape.communication.Message;
import jscape.communication.MessageCode;
import jscape.communication.RequestServerTask;
import jscape.profile.graphs.MonthlyProgress;
import jscape.profile.graphs.YearlyProgress;
import jscape.profile.performance.PerformancePieChart;
import jscape.profile.performance.PerformanceStatsTable;

/**
 *
 * @author achantreau
 */
public class ProfilePane extends BorderPane {

    private static final String HOST = "localhost";
    private static final int PORT = 9000;

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

    private RequestServerTask fetchProfileInfoTask;
    private RequestServerTask fetchDateListTask;

    private Service fetchPerformanceStatsService;

    private String myLoginName;

    public ProfilePane() {
        super();

        myLoginName = JScape.getJSCAPE().myLoginName;

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

        ArrayList<String> payload = new ArrayList<String>();
        payload.add(myLoginName);
        Message requestMessage = new Message(MessageCode.PROFILE_INFO, payload);
        fetchProfileInfoTask = new RequestServerTask(HOST, PORT, requestMessage);
        fetchProfileInfoTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                if (t1 == Worker.State.SUCCEEDED) {
                    Message replyMessage = fetchProfileInfoTask.getValue();
                    firstName.setText(replyMessage.getPayload().get(0));
                    lastName.setText(replyMessage.getPayload().get(1));
                    loginName.setText(myLoginName);
                    className.setText(replyMessage.getPayload().get(2));

                    if ("null".equals(replyMessage.getPayload().get(3))) {
                        lastLogin.setText("Last login: NO DATA YET");
                    } else {
                        lastLogin.setText("Last login: " + replyMessage.getPayload().get(3));
                    }

                    if ("null".equals(replyMessage.getPayload().get(4))) {
                        lastExerciseAnswered.setText("Last exercise answered: NO DATA YET");
                    } else {
                        lastExerciseAnswered.setText("Last exercise answered: " + replyMessage.getPayload().get(4));
                    }
                } else if (t1 == Worker.State.FAILED) {
                    // Platform run later not needed here I think....check to be sure
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // Stack Pane stuff + error message + modal dimmer + terminate app
                        }
                    });
                }
            }
        });

        BorderPane profileInfo = new BorderPane();
        profileInfo.setMinWidth(285);
        profileInfo.setMaxWidth(285);
        profileInfo.setPrefWidth(285);

        profileInfo.setId("page-tree");

        VBox profileInfoTop = new VBox();
        profileInfoTop.getChildren().addAll(firstName, lastName, loginName, className);
        profileInfoTop.setAlignment(Pos.TOP_CENTER);
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

        fetchPerformanceStatsService = new Service<Message>() {
            @Override
            protected Task<Message> createTask() {
                ArrayList<String> payload = new ArrayList<String>();
                payload.add(myLoginName);
                Message requestMessage = new Message(MessageCode.PERFORMANCE_STATS, payload);

                return new RequestServerTask(HOST, PORT, requestMessage);
            }
        };
        fetchPerformanceStatsService.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                if (t1 == Worker.State.SUCCEEDED) {
                    Message replyMessage = (Message) fetchPerformanceStatsService.getValue();
                    performanceStatsTable.setItems(replyMessage.getPayload());

                    /* Add categories to comboBox, select the first item which triggers
                     the listener to add the data to the pie chart. */
                    if (categoryBox.getSelectionModel().getSelectedItem() == null) {
                        ObservableList<String> graphCategories = performanceStatsTable.getExerciseCategories();
                        graphCategoryBox.setItems(graphCategories);

                        ObservableList<String> exerciseCategories = performanceStatsTable.getExerciseCategories();
                        exerciseCategories.add("-- Total answers per category --");

                        categoryBox.setItems(exerciseCategories);
                        categoryBox.getSelectionModel().selectFirst();
                    } else {
                        String selectedCategory = categoryBox.getSelectionModel().getSelectedItem().toString();
                        performancePieChart.setData(selectedCategory);
                    }
                } else if (t1 == Worker.State.FAILED) {
                    // Platform run later not needed here I think....check to be sure
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            // Stack Pane stuff + error message + modal dimmer + terminate app
                        }
                    });
                }
            }
        });

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

        // Fetch date list from database
        payload = new ArrayList<String>();
        payload.add(myLoginName);
        requestMessage = new Message(MessageCode.GET_DATE_LIST, payload);
        fetchDateListTask = new RequestServerTask(HOST, PORT, requestMessage);
        fetchDateListTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                if (t1 == Worker.State.SUCCEEDED) {
                    Message replyMessage = fetchDateListTask.getValue();
                    ObservableList<String> monthsAndYears = FXCollections.observableArrayList(replyMessage.getPayload());
                    monthBox.setItems(monthsAndYears);
                    yearBox.setItems(FXCollections.observableArrayList(getYearList(replyMessage.getPayload())));
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
        yAxisMonthlyProgress.setLabel("Questions Answered");

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
        yAxisYearlyProgress.setLabel("Questions Answered");

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
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(profileStatistics);

        setLeft(profileInfo);
        setCenter(scrollPane);
    }

    public void runFetchProfileInfoTask() {
        new Thread(fetchProfileInfoTask, "FetchProfileInfoThread").start();
    }

    public void runFetchDateListTask() {
        new Thread(fetchDateListTask, "FetchDateListThread").start();
    }

    public void runFetchPerformanceStatsService() {
        fetchPerformanceStatsService.restart();
    }

    private void runFetchYearlyProgressTask() {
        if ((String) yearBox.getSelectionModel().getSelectedItem() != null) {
            if ("Total".equals((String) graphCategoryBox.getSelectionModel().getSelectedItem())) {
                ArrayList<String> payload = new ArrayList<String>();
                payload.add(myLoginName);
                payload.add((String) yearBox.getSelectionModel().getSelectedItem());

                Message requestMessage = new Message(MessageCode.GET_TOTAL_PER_YEAR, payload);
                final RequestServerTask getGraphTotalStatsTask = new RequestServerTask(HOST, PORT, requestMessage);
                getGraphTotalStatsTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                        if (t1 == Worker.State.SUCCEEDED) {
                            Message replyMessage = getGraphTotalStatsTask.getValue();
                            yearlyProgressChart.setData(replyMessage.getPayload());
                            yearlyProgressChart.setVisible(true);
                        }
                    }
                });
                new Thread(getGraphTotalStatsTask, "GetGraphYearTotalStatsThread").start();
            } else {
                ArrayList<String> payload = new ArrayList<String>();
                payload.add(myLoginName);
                payload.add((String) graphCategoryBox.getSelectionModel().getSelectedItem());
                payload.add((String) yearBox.getSelectionModel().getSelectedItem());

                Message requestMessage = new Message(MessageCode.GET_YEARLY_PROGRESS, payload);
                final RequestServerTask getGraphStatsTask = new RequestServerTask(HOST, PORT, requestMessage);
                getGraphStatsTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                        if (t1 == Worker.State.SUCCEEDED) {
                            Message replyMessage = getGraphStatsTask.getValue();
                            yearlyProgressChart.setData(replyMessage.getPayload());
                            yearlyProgressChart.setVisible(true);
                        }
                    }
                });
                new Thread(getGraphStatsTask, "GetGraphYearStatsThread").start();
            }
        }
    }

    private void runFetchMonthlyProgressTask() {
        if ((String) monthBox.getSelectionModel().getSelectedItem() != null) {
            if ("Total".equals((String) graphCategoryBox.getSelectionModel().getSelectedItem())) {
                ArrayList<String> payload = new ArrayList<String>();
                payload.add(myLoginName);
                payload.add((String) monthBox.getSelectionModel().getSelectedItem());

                Message requestMessage = new Message(MessageCode.GET_TOTAL_PER_DAY, payload);
                final RequestServerTask getGraphTotalStatsTask = new RequestServerTask(HOST, PORT, requestMessage);
                getGraphTotalStatsTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                        if (t1 == Worker.State.SUCCEEDED) {
                            Message replyMessage = getGraphTotalStatsTask.getValue();
                            monthlyProgressChart.setData(replyMessage.getPayload(), (String) monthBox.getSelectionModel().getSelectedItem());
                            monthlyProgressChart.setVisible(true);
                        }
                    }
                });
                new Thread(getGraphTotalStatsTask, "GetGraphTotalStatsThread").start();
            } else {
                ArrayList<String> payload = new ArrayList<String>();
                payload.add(myLoginName);
                payload.add((String) monthBox.getSelectionModel().getSelectedItem());
                payload.add((String) graphCategoryBox.getSelectionModel().getSelectedItem());

                Message requestMessage = new Message(MessageCode.GET_MONTHLY_PROGRESS, payload);
                final RequestServerTask getGraphStatsTask = new RequestServerTask(HOST, PORT, requestMessage);
                getGraphStatsTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                        if (t1 == Worker.State.SUCCEEDED) {
                            Message replyMessage = getGraphStatsTask.getValue();
                            monthlyProgressChart.setData(replyMessage.getPayload(), (String) monthBox.getSelectionModel().getSelectedItem());
                            monthlyProgressChart.setVisible(true);
                        }
                    }
                });
                new Thread(getGraphStatsTask, "GetGraphStatsThread").start();
            }
        }
    }

    private ArrayList<String> getYearList(ArrayList<String> payload) {
        ArrayList<String> yearList = new ArrayList<String>();
        Set<String> yearSet = new HashSet<String>();

        for (String s : payload) {
            String[] words = s.split(" ");
            yearSet.add(words[1]);
        }

        for (String s : yearSet) {
            yearList.add(s);
        }

        Collections.shuffle(yearList);

        return yearList;
    }
}
