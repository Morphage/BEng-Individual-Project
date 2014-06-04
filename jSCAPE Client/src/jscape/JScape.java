/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import jscape.practice.PracticePane;
import jscape.profile.ProfilePane;
import jscape.about.AboutPane;
import jscape.help.HelpPane;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.DepthTest;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import jscape.communication.Message;
import jscape.communication.MessageCode;
import jscape.communication.RequestServerTask;

/**
 *
 * @author achantreau
 */
public class JScape extends Application {

    private static final String FXML = "Login.fxml";
    private static final String HOST = "localhost";
    private static final int PORT = 9000;

    private BorderPane rootPane;

    private StackPane modalDimmer;

    private TabPane tabPane;

    private ProfilePane profilePane;
    private PracticePane practicePane;
    private HelpPane helpPane;
    private AboutPane aboutPane;

    private static JScape jSCAPE;

    public boolean runPerformanceStatsService = false;

    public String myLoginName;

    private Stage currentStage = null;
    private LoginController loginController = null;

    public static JScape getJSCAPE() {
        return jSCAPE;
    }

    @Override
    public void start(final Stage stage) {
        currentStage = stage;
        gotoApp("ac6609");

        /* Commented out so that I don't have to keep login in all the time */
        /*try {
            loginController = (LoginController) gotoLogin();
            loginController.setApp(this);
        } catch (Exception e) {
        }*/
    }

    private Initializable gotoLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = JScape.class.getResourceAsStream(FXML);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(JScape.class.getResource(FXML));
        AnchorPane page = null;
        try {
            page = (AnchorPane) loader.load(in);
        } catch (IOException ex) {
        } finally {
            try {
                in.close();
            } catch (IOException ie) {
            }
        }
        Scene loginScene = new Scene(page, 1020, 700);
        currentStage.setScene(loginScene);
        currentStage.sizeToScene();
        currentStage.show();
        return (Initializable) loader.getController();
    }

    private void gotoApp(String loginName) {
        jSCAPE = this;
        myLoginName = loginName;
        StackPane layerPane = new StackPane();

        rootPane = new BorderPane();
        rootPane.setId("Window");
        layerPane.setDepthTest(DepthTest.DISABLE);
        layerPane.getChildren().add(rootPane);

        final Scene scene = new Scene(layerPane, currentStage.getWidth(), currentStage.getHeight());
        scene.getStylesheets().add(JScape.class.getResource("jscape.css").toExternalForm());

        // create modal dimmer, to dim screen when showing modal dialogs
        modalDimmer = new StackPane();
        modalDimmer.setId("ModalDimmer");
        modalDimmer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                t.consume();
            }
        });
        modalDimmer.setVisible(false);
        layerPane.getChildren().add(modalDimmer);

        profilePane = new ProfilePane();
        profilePane.runFetchPerformanceStatsService();
        profilePane.runFetchProfileInfoTask();
        profilePane.runFetchDateListTask();

        practicePane = new PracticePane();
        practicePane.runFetchCategoriesTask();

        helpPane = new HelpPane();
        aboutPane = new AboutPane();

        tabPane = new TabPane();
        tabPane.setId("MainTabs");

        Tab profileTab = new Tab("PROFILE");
        profileTab.setContent(profilePane);

        Tab practiceTab = new Tab("PRACTICE");
        practiceTab.setContent(practicePane);

        Tab helpTab = new Tab("HELP");
        helpTab.setContent(helpPane);

        Tab aboutTab = new Tab("ABOUT");
        aboutTab.setContent(aboutPane);

        tabPane.getTabs().addAll(profileTab, practiceTab, helpTab, aboutTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        String tabName = t1.getText();

                        if ("PROFILE".equals(tabName)) {
                            if (runPerformanceStatsService) {
                                profilePane.runFetchPerformanceStatsService();
                                runPerformanceStatsService = false;
                            }
                        }
                    }
                }
        );

        this.rootPane.setCenter(tabPane);

        currentStage.setScene(scene);
        currentStage.sizeToScene();
        currentStage.show();
    }

    public void login(final String loginName, final String password) {
        ArrayList<String> payload = new ArrayList<String>();
        payload.add(loginName);
        payload.add(password);

        Message requestMessage = new Message(MessageCode.LOGIN, payload);
        final RequestServerTask loginTask = new RequestServerTask(HOST, PORT, requestMessage);
        loginTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                if (t1 == Worker.State.SUCCEEDED) {
                    Message replyMessage = loginTask.getValue();
                    if ("success".equals(replyMessage.getPayload().get(0))) {
                        gotoApp(loginName);
                    } else {
                        loginController.failedLogin();
                    }
                }
            }
        });

        new Thread(loginTask, "LoginThread").start();
    }

    public void showModalMessage(Node message) {
        modalDimmer.getChildren().add(message);
        modalDimmer.setOpacity(0);
        modalDimmer.setVisible(true);
        modalDimmer.setCache(true);
        TimelineBuilder.create().keyFrames(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                modalDimmer.setCache(false);
                            }
                        },
                        new KeyValue(modalDimmer.opacityProperty(), 1, Interpolator.EASE_BOTH)
                )).build().play();
    }

    public void hideModalMessage() {
        modalDimmer.setCache(true);
        TimelineBuilder.create().keyFrames(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                modalDimmer.setCache(false);
                                modalDimmer.setVisible(false);
                                modalDimmer.getChildren().clear();
                            }
                        },
                        new KeyValue(modalDimmer.opacityProperty(), 0, Interpolator.EASE_BOTH)
                )).build().play();
    }

    /**
     * Java Main Method for launching application when not using JavaFX
     * Launcher, e.g. from IDE without JavaFX support
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
