/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.practice;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import jscape.JScape;
import jscape.communication.Message;
import jscape.communication.MessageCode;
import jscape.communication.RequestServerTask;
import jscape.practice.views.BinaryTree;
import jscape.practice.views.CodeEditor;

/**
 *
 * @author achantreau
 */
public class PracticePane extends BorderPane {

    private static final String HOST = "localhost";
    private static final int PORT = 9000;

    private HashMap<String, CategorySidebarInfo> sideBarInfo = new HashMap<String, CategorySidebarInfo>();

    private RequestServerTask fetchCategoriesTask;

    private GridPane exerciseCategories;
    private VBox mainMenu;
    
    private String myLoginName;

    public PracticePane() {
        super();
        
        myLoginName = JScape.getJSCAPE().loginName;

        mainMenu = new VBox(80);

        // Add JAVA PROGRAMMING EXERCISES title bar
        Label javaHeader = new Label("      JAVA PROGRAMMING EXERCISES");
        javaHeader.setMaxWidth(Double.MAX_VALUE);
        javaHeader.setMinHeight(Control.USE_PREF_SIZE);
        javaHeader.getStyleClass().add("category-header");
        mainMenu.getChildren().add(javaHeader);

        // Add grid of exercise categories, e.g. arrays, syntax, data structures...
        exerciseCategories = new GridPane();
        exerciseCategories.setHgap(50);
        exerciseCategories.setVgap(50);
        exerciseCategories.setPadding(new Insets(0, 10, 0, 10));
        exerciseCategories.getStyleClass().add("category-page-flow");
        exerciseCategories.setAlignment(Pos.CENTER);
        mainMenu.getChildren().add(exerciseCategories);

        Message requestMessage = new Message(MessageCode.EXERCISE_CATEGORIES, null);
        fetchCategoriesTask = new RequestServerTask(HOST, PORT, requestMessage);
        fetchCategoriesTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                if (t1 == Worker.State.SUCCEEDED) {
                    Message replyMessage = fetchCategoriesTask.getValue();
                    ArrayList<String> payload = replyMessage.getPayload();

                    for (int i = 0; i < payload.size(); i += 4) {
                        sideBarInfo.put(payload.get(i),
                                new CategorySidebarInfo(payload.get(i + 1),
                                        payload.get(i + 2), payload.get(i + 3)));

                        Node tile = createTile(payload.get(i));
                        GridPane.setConstraints(tile, (i / 4) % 5, (i / 4) / 5);
                        exerciseCategories.getChildren().add(tile);
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

        mainMenu.getStyleClass().add("category-page");

        setCenter(mainMenu);
    }

    public void runFetchCategoriesTask() {
        new Thread(fetchCategoriesTask, "FetchCategoriesThread").start();
    }

    public Node createTile(String exerciseCategory) {
        //ImageView buttonLogo = new ImageView(new Image(JScape.class.getResourceAsStream("images/" + exerciseCategory + ".png")));
        ImageView buttonLogo = new ImageView(new Image(JScape.class.getResourceAsStream("images/StackedAreaChartSample.png")));
        Button tile = new Button(exerciseCategory, buttonLogo);
        tile.setMinSize(140, 145);
        tile.setPrefSize(140, 145);
        tile.setMaxSize(140, 145);
        tile.setContentDisplay(ContentDisplay.TOP);
        tile.getStyleClass().clear();
        tile.getStyleClass().add("sample-tile");
        tile.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Button source = (Button) event.getSource();
                PracticeSplitPane psp = new PracticeSplitPane(source.getText());

                //run service/task here and when successful center the window
                psp.runFetchExerciseService();
                setCenter(psp);
            }
        });
        return tile;
    }

    private class PracticeSplitPane extends BorderPane {

        private final Font font20 = new Font("Arial", 20);
        private final Font font14 = new Font("Arial", 14);
        private final Font font12 = new Font("Arial", 12);

        VBox practiceMain;

        private Exercise currentExercise;

        private Service fetchExerciseService;

        private final Button nextButton = new Button("Next Exercise");
        private final Button submitButton = new Button("Submit Answer");

        public PracticeSplitPane(final String exerciseCategory) {
            practiceMain = new VBox(30);

            // Add JAVA PROGRAMMING EXERCISES title bar
            Label javaHeader = new Label("      JAVA PROGRAMMING EXERCISES - " + exerciseCategory);
            javaHeader.setMaxWidth(Double.MAX_VALUE);
            javaHeader.setMinHeight(Control.USE_PREF_SIZE);
            javaHeader.getStyleClass().add("category-header");
            practiceMain.getChildren().add(javaHeader);

            // Create Left split pane and add all elements to it            
            Button backButton = new Button("Back to Practice Menu");
            backButton.setId("dark-blue");
            backButton.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    setCenter(mainMenu);
                    setRight(null);
                }
            });

            Label exerciseData = new Label("Exercise Data:");
            exerciseData.setFont(font20);
            exerciseData.setStyle("-fx-text-fill: #e1fdff;"
                    + "-fx-font-weight: bold;");

            //final CodeEditor codeEditor = new CodeEditor("");
            final BinaryTree binaryTree = new BinaryTree("");

            VBox leftVBox = new VBox(8);
            //leftVBox.getChildren().addAll(exerciseData, codeEditor);
            //VBox.setVgrow(codeEditor, Priority.NEVER);
            
            leftVBox.getChildren().addAll(exerciseData, binaryTree);
            VBox.setVgrow(binaryTree, Priority.NEVER);
            
            BorderPane leftSplitPane = new BorderPane();
            leftSplitPane.setCenter(leftVBox);
            leftSplitPane.setBottom(backButton);
            VBox.setVgrow(leftSplitPane, Priority.NEVER);

            // Create Right split pane and all elements to it
            Label exerciseLabel = new Label("Exercise:");
            exerciseLabel.setFont(font20);
            exerciseLabel.setStyle("-fx-text-fill: #e1fdff;"
                    + "-fx-font-weight: bold;");

            final Text exerciseText = new Text();
            exerciseText.setFont(font14);
            exerciseText.setWrappingWidth(600);

            final ToggleGroup group = new ToggleGroup();
            group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                @Override
                public void changed(ObservableValue<? extends Toggle> ov,
                        Toggle old_toggle, Toggle new_toggle) {
                    submitButton.setDisable(false);
                }
            });

            final RadioButton rb1 = new RadioButton();
            rb1.setFont(font12);
            rb1.setToggleGroup(group);

            final RadioButton rb2 = new RadioButton();
            rb2.setFont(font12);
            rb2.setToggleGroup(group);

            final RadioButton rb3 = new RadioButton();
            rb3.setFont(font12);
            rb3.setToggleGroup(group);

            final RadioButton rb4 = new RadioButton();
            rb4.setFont(font12);
            rb4.setToggleGroup(group);

            final Text correctOrWrong = new Text();
            correctOrWrong.setFont(font14);
            correctOrWrong.setVisible(false);

            VBox exerciseVBox = new VBox(8);
            exerciseVBox.getChildren().addAll(exerciseLabel, exerciseText, rb1,
                    rb2, rb3, rb4, correctOrWrong);
            VBox.setMargin(exerciseLabel, new Insets(0, 0, 0, 15));
            VBox.setMargin(exerciseText, new Insets(0, 0, 0, 15));
            VBox.setMargin(rb1, new Insets(0, 0, 0, 45));
            VBox.setMargin(rb2, new Insets(0, 0, 0, 45));
            VBox.setMargin(rb3, new Insets(0, 0, 0, 45));
            VBox.setMargin(rb4, new Insets(0, 0, 0, 45));
            VBox.setMargin(correctOrWrong, new Insets(15, 0, 0, 45));

            Label solutionLabel = new Label("Solution:");
            solutionLabel.setFont(font20);
            solutionLabel.setStyle("-fx-text-fill: #e1fdff;"
                    + "-fx-font-weight: bold;");

            final Text solutionText = new Text();
            solutionText.setFont(font12);

            final VBox solutionVBox = new VBox(8);
            solutionVBox.getChildren().addAll(solutionLabel, solutionText);
            solutionVBox.setVisible(false);
            VBox.setMargin(solutionLabel, new Insets(0, 0, 0, 15));
            VBox.setMargin(solutionText, new Insets(0, 0, 0, 45));

            VBox rightMainVBox = new VBox(30);
            rightMainVBox.getChildren().addAll(exerciseVBox, solutionVBox);

            nextButton.setId("dark-blue");
            nextButton.setDisable(true);
            nextButton.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    runFetchExerciseService();
                    correctOrWrong.setVisible(false);
                    rb1.setDisable(false);
                    rb2.setDisable(false);
                    rb3.setDisable(false);
                    rb4.setDisable(false);
                    nextButton.setDisable(true);
                    solutionVBox.setVisible(false);
                    group.getSelectedToggle().setSelected(false);
                    submitButton.setDisable(true);
                }
            });

            submitButton.setId("dark-blue");
            submitButton.setDisable(true);
            submitButton.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    JScape.getJSCAPE().runPerformanceStatsService = true;
                    int selected = group.getToggles().indexOf(group.getSelectedToggle());
                    RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
                    boolean isCorrectAnswer = (selectedButton.getText().equals(currentExercise.getSolution()));

                    runAnswerExerciseTask(myLoginName, currentExercise.getExerciseId(), selected,
                            exerciseCategory, isCorrectAnswer);
                    rb1.setDisable(true);
                    rb2.setDisable(true);
                    rb3.setDisable(true);
                    rb4.setDisable(true);

                    if (isCorrectAnswer) {
                        correctOrWrong.setText("CORRECT ANSWER");
                        correctOrWrong.setFill(Color.GREEN);
                    } else {
                        correctOrWrong.setText("WRONG ANSWER");
                        correctOrWrong.setFill(Color.RED);
                    }

                    correctOrWrong.setVisible(true);

                    submitButton.setDisable(true);
                    solutionVBox.setVisible(true);
                    nextButton.setDisable(false);
                    nextButton.getParent().requestFocus();
                }
            });

            HBox buttonHBox = new HBox(5);
            buttonHBox.setAlignment(Pos.CENTER_RIGHT);
            buttonHBox.getChildren().addAll(submitButton, nextButton);

            BorderPane rightSplitPane = new BorderPane();
            rightSplitPane.setCenter(rightMainVBox);
            rightSplitPane.setBottom(buttonHBox);
            VBox.setVgrow(rightSplitPane, Priority.ALWAYS);

            SplitPane splitPane = new SplitPane();
            splitPane.getItems().addAll(leftSplitPane, rightSplitPane);
            splitPane.setDividerPosition(0, 0.5);

            fetchExerciseService = new Service<Message>() {
                @Override
                protected Task<Message> createTask() {
                    ArrayList<String> payload = new ArrayList<String>();
                    payload.add(myLoginName);
                    payload.add(exerciseCategory);
                    Message requestMessage = new Message(MessageCode.GET_EXERCISE, payload);

                    return new RequestServerTask(HOST, PORT, requestMessage);
                }
            };
            fetchExerciseService.stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                    if (t1 == Worker.State.SUCCEEDED) {
                        Message replyMessage = (Message) fetchExerciseService.getValue();
                        ArrayList<String> payload = replyMessage.getPayload();

                        if (payload == null) {
                            JScape.getJSCAPE().showModalMessage(new NoMoreExercicesPopup(exerciseCategory));
                        } else {
                            printDebugExerciseInfo(payload);

                            currentExercise = new Exercise(Integer.valueOf(payload.get(0)), payload.get(1),
                                    payload.get(2), payload.get(3), payload.get(4), payload.get(5),
                                    payload.get(6), payload.get(7), payload.get(8), payload.get(9));

                            prepareExercise(currentExercise);

                            //codeEditor.setCode(payload.get(2));
                            binaryTree.setJSON(payload.get(2));
                            exerciseText.setText(payload.get(4));
                            rb1.setText(payload.get(5));
                            rb2.setText(payload.get(6));
                            rb3.setText(payload.get(7));
                            rb4.setText(payload.get(8));
                            solutionText.setText(payload.get(9));
                        }
                    } else if (t1 == Worker.State.FAILED) {
                        //Failed to get exercise => show stuff
                    }
                }
            });

            practiceMain.getChildren().add(splitPane);
            practiceMain.getStyleClass().add("category-page");

            // This is needed, don't remove
            VBox.setVgrow(splitPane, Priority.ALWAYS);
            setCenter(practiceMain);
            setRight(createSidebar(exerciseCategory));
        }

        private void prepareExercise(Exercise exercise) {

        }

        private GridPane createSidebar(String exerciseCategory) {
            GridPane sidebar = new GridPane();
            sidebar.getStyleClass().add("right-sidebar");
            sidebar.setMaxWidth(Double.MAX_VALUE);
            sidebar.setMaxHeight(Double.MAX_VALUE);
            int sideRow = 0;

            // Create sidebar content - description
            Label discTitle = new Label("Description");
            discTitle.getStyleClass().add("right-sidebar-title");
            GridPane.setConstraints(discTitle, 0, sideRow++);
            sidebar.getChildren().add(discTitle);
            Text disc = new Text(sideBarInfo.get(exerciseCategory).getDescription());
            disc.setWrappingWidth(320);
            disc.getStyleClass().add("right-sidebar-body");
            GridPane.setConstraints(disc, 0, sideRow++);
            sidebar.getChildren().add(disc);

            // Lecture notes
            Separator separator = new Separator();
            GridPane.setConstraints(separator, 0, sideRow++);
            sidebar.getChildren().add(separator);
            Label docsTitle = new Label("Lecture Notes");
            docsTitle.getStyleClass().add("right-sidebar-title");
            GridPane.setConstraints(docsTitle, 0, sideRow++);
            sidebar.getChildren().add(docsTitle);

            String[] lectureNotes = sideBarInfo.get(exerciseCategory).getLectureNotes();

            for (final String link : lectureNotes) {
                Hyperlink hyperLink = new Hyperlink(link);
                hyperLink.setWrapText(true);
                hyperLink.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        JScape.getJSCAPE().getHostServices().showDocument(link);
                    }
                });
                GridPane.setConstraints(hyperLink, 0, sideRow++);
                sidebar.getChildren().add(hyperLink);
            }

            // Helpful links
            separator = new Separator();
            GridPane.setConstraints(separator, 0, sideRow++);
            sidebar.getChildren().add(separator);
            Label relatedTitle = new Label("Helpful Links");
            relatedTitle.getStyleClass().add("right-sidebar-title");
            GridPane.setConstraints(relatedTitle, 0, sideRow++);
            sidebar.getChildren().add(relatedTitle);

            String[] helpfulLinks = sideBarInfo.get(exerciseCategory).getHelpfulLinks();

            for (final String link : helpfulLinks) {
                Hyperlink hyperLink = new Hyperlink(link);
                hyperLink.setWrapText(true);
                hyperLink.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        JScape.getJSCAPE().getHostServices().showDocument(link);
                    }
                });
                GridPane.setConstraints(hyperLink, 0, sideRow++);
                sidebar.getChildren().add(hyperLink);
            }

            GridPane.setVgrow(sidebar, Priority.NEVER);
            return sidebar;
        }

        public void runFetchExerciseService() {
            fetchExerciseService.restart();
        }

        public void runAnswerExerciseTask(String loginName, int exerciseId, int answerId,
                String exerciseCategory, boolean isCorrectAnswer) {
            ArrayList<String> payload = new ArrayList<String>();
            payload.add(loginName);
            payload.add("" + exerciseId);
            payload.add("" + answerId);
            payload.add(exerciseCategory);
            payload.add("" + isCorrectAnswer);

            Message requestMessage = new Message(MessageCode.ANSWER_EXERCISE, payload);
            RequestServerTask answerExerciseTask = new RequestServerTask(HOST, PORT, requestMessage);

            new Thread(answerExerciseTask, "AnswerExerciseThread").start();
        }

        private void printDebugExerciseInfo(ArrayList<String> payload) {
            System.out.println("*****DEBUG EXERCISE INFO**********");
            System.out.println("Exercise id= " + payload.get(0));
            System.out.println("Left display view= " + payload.get(1));
            System.out.println("Left display value= " + payload.get(2));
            System.out.println("Right display view= " + payload.get(3));
            System.out.println("Right display value= " + payload.get(4));
            System.out.println("Choice 0= " + payload.get(5));
            System.out.println("Choice 1= " + payload.get(6));
            System.out.println("Choice 2= " + payload.get(7));
            System.out.println("Choice 3= " + payload.get(8));
            System.out.println("Solution= " + payload.get(9));
            System.out.println("*********************************");
        }
    }

    private class NoMoreExercicesPopup extends VBox {

        public NoMoreExercicesPopup(String exerciseCategory) {
            setId("PopupWindow");
            setSpacing(10);
            setMaxSize(530, USE_PREF_SIZE);
            // block mouse clicks
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    t.consume();
                }
            });

            Text explanation = new Text("The system could not find an exercise which"
                    + " you haven't answered yet for this particular exercise category."
                    + " Please notify your course supervisor or press the OK button"
                    + " to go back to the Practice menu.");
            explanation.setWrappingWidth(500);

            BorderPane explPane = new BorderPane();
            VBox.setMargin(explPane, new Insets(5, 5, 5, 5));
            explPane.setCenter(explanation);
            BorderPane.setMargin(explanation, new Insets(5, 5, 5, 5));

            // create title
            Label title = new Label("NO MORE EXERCISES FOR " + exerciseCategory.toUpperCase()
                    + " CATEGORY");
            title.setId("title");
            title.setMaxWidth(Double.MAX_VALUE);
            title.setAlignment(Pos.CENTER);
            getChildren().add(title);

            Button okButton = new Button("OK");
            okButton.setAlignment(Pos.CENTER_RIGHT);
            okButton.setId("dark-blue");
            okButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    JScape.getJSCAPE().hideModalMessage();
                    setCenter(mainMenu);
                    setRight(null);
                }
            });
            
            HBox buttonHBox = new HBox(0);
            buttonHBox.setAlignment(Pos.CENTER_RIGHT);
            buttonHBox.getChildren().add(okButton);
            
            getChildren().addAll(explPane, buttonHBox);
        }
    }
}
