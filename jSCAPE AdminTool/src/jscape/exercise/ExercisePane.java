/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jscape.exercise;

import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ContextMenuBuilder;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import jscape.database.ExerciseBankTable;

/**
 *
 * @author achantreau
 */
public class ExercisePane extends BorderPane {

    private ScrollPane scrollPane;
    private VBox exerciseManagementVBox;

    private ExerciseInfoTable exerciseInfoTable;

    public ExercisePane() {
        super();

        TreeItem<String> rootItem = new TreeItem<>("EXERCISE CATEGORIES");
        rootItem.setExpanded(true);

        TreeItem<String> item = new TreeItem<>("Arrays");
        rootItem.getChildren().add(item);
        item = new TreeItem<>("Syntax");
        rootItem.getChildren().add(item);
        item = new TreeItem<>("Binary Trees");
        rootItem.getChildren().add(item);
        item = new TreeItem<>("Conditionals");
        rootItem.getChildren().add(item);
        item = new TreeItem<>("Loops");
        rootItem.getChildren().add(item);
        item = new TreeItem<>("Strings");
        rootItem.getChildren().add(item);
        item = new TreeItem<>("Objects");
        rootItem.getChildren().add(item);

        TreeView<String> exerciseCategoryTree = new TreeView<>(rootItem);

        exerciseCategoryTree.setId("page-tree");
        exerciseCategoryTree.setMinWidth(285);
        exerciseCategoryTree.setMaxWidth(285);
        exerciseCategoryTree.setPrefWidth(285);

        exerciseCategoryTree.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        exerciseCategoryTree.setShowRoot(true);
        exerciseCategoryTree.setEditable(false);
        exerciseCategoryTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        exerciseCategoryTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue,
                    Object newValue) {

            }
        });
        exerciseCategoryTree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {

            @Override
            public TreeCell<String> call(TreeView<String> arg0) {
                // custom tree cell that defines a context menu for the root tree item
                return new MyTreeCell();
            }
        });

        setLeft(exerciseCategoryTree);

        // Create display area for exercise options
        exerciseManagementVBox = new VBox() {
            // stretch to allways fill height of scrollpane
            @Override
            protected double computePrefHeight(double width) {
                return Math.max(
                        super.computePrefHeight(width),
                        getParent().getBoundsInLocal().getHeight()
                );
            }
        };

        // Add EXISTING EXERCISES title bar
        Label existingExercisesLabel = new Label("      EXISTING EXERCISES - BINARY TREES");
        existingExercisesLabel.setMaxWidth(Double.MAX_VALUE);
        existingExercisesLabel.setMinHeight(Control.USE_PREF_SIZE);
        existingExercisesLabel.getStyleClass().add("category-header");

        exerciseInfoTable = new ExerciseInfoTable();
        ArrayList<String> exerciseData = ExerciseBankTable.getExerciseInfo("Binary Trees");
        exerciseInfoTable.setItems(exerciseData);

        HBox tableHBox = new HBox();
        tableHBox.getChildren().add(exerciseInfoTable);
        tableHBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(exerciseInfoTable, Priority.ALWAYS);
        exerciseManagementVBox.getChildren().addAll(existingExercisesLabel, tableHBox);

        // Add ADD EXERCISE MANUALLY title bar
        Label addManualExerciseLabel = new Label("      ADD EXERCISE MANUALLY - BINARY TREES");
        addManualExerciseLabel.setMaxWidth(Double.MAX_VALUE);
        addManualExerciseLabel.setMinHeight(Control.USE_PREF_SIZE);
        addManualExerciseLabel.getStyleClass().add("category-header");
        exerciseManagementVBox.getChildren().add(addManualExerciseLabel);
        
        Label displayLeftViewLabel = new Label("Left view:");
        displayLeftViewLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");
        
        ObservableList<String> leftViewDisplays
                = FXCollections.observableArrayList("BinaryTree", "CodeEditor");
        final ComboBox leftViewComboBox = new ComboBox(leftViewDisplays);
        
        HBox hbox1 = new HBox(8);
        hbox1.getChildren().addAll(displayLeftViewLabel, leftViewComboBox);
        HBox.setMargin(displayLeftViewLabel, new Insets(0, 0, 0, 20));
        
        Label displayLeftValueLabel = new Label("Left value:");
        displayLeftValueLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");
        
        final TextArea displayLeftValue = new TextArea();
        
        HBox hbox2 = new HBox(8);
        hbox2.getChildren().addAll(displayLeftValueLabel, displayLeftValue);
        HBox.setMargin(displayLeftValueLabel, new Insets(0, 0, 0, 20));
        
        Label displayRightViewLabel = new Label("Right view:");
        displayRightViewLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");
        
        ObservableList<String> rightViewDisplays
                = FXCollections.observableArrayList("Multiple Choice Question");
        final ComboBox rightViewComboBox = new ComboBox(rightViewDisplays);
        
        HBox hbox3 = new HBox(8);
        hbox3.getChildren().addAll(displayRightViewLabel, rightViewComboBox);
        HBox.setMargin(displayRightViewLabel, new Insets(0, 0, 0, 20));
        
        Label displayRightValueLabel = new Label("Right value:");
        displayRightValueLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");
        
        final TextArea displayRightValue = new TextArea();
        
        HBox hbox4 = new HBox(8);
        hbox4.getChildren().addAll(displayRightValueLabel, displayRightValue);
        HBox.setMargin(displayRightValueLabel, new Insets(0, 0, 0, 20));
        
        Label solutionLabel = new Label("Solution:");
        solutionLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");
        
        final TextField solutionField = new TextField();
        
        HBox hbox5 = new HBox(8);
        hbox5.getChildren().addAll(solutionLabel, solutionField);
        HBox.setMargin(solutionLabel, new Insets(0, 0, 0, 20));
        
        Label difficultyLabel = new Label("Difficulty:");
        difficultyLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");
        
        final TextField difficultyField = new TextField();
        
        HBox hbox6 = new HBox(8);
        hbox6.getChildren().addAll(difficultyLabel, difficultyField);
        HBox.setMargin(difficultyLabel, new Insets(0, 0, 0, 20));
        
        VBox manualAddVBox = new VBox(8);
        manualAddVBox.getChildren().addAll(hbox1, hbox2, hbox3, hbox4, hbox5, hbox6);
        
        exerciseManagementVBox.getChildren().addAll(manualAddVBox);

        // Add AUTOMATED GENERATION OF EXERCISES title bar
        Label addAutomatedExerciseLabel = new Label("      AUTOMATED GENERATION OF EXERCISES - BINARY TREES");
        addAutomatedExerciseLabel.setMaxWidth(Double.MAX_VALUE);
        addAutomatedExerciseLabel.setMinHeight(Control.USE_PREF_SIZE);
        addAutomatedExerciseLabel.getStyleClass().add("category-header");
        exerciseManagementVBox.getChildren().add(addAutomatedExerciseLabel);

        Label generateNumberLabel = new Label("Generate how many exercises:");
        generateNumberLabel.setFont(new Font("Arial", 15));
        generateNumberLabel.setStyle("-fx-text-fill: #e1fdff;"
                + "-fx-font-weight: bold;");

        final TextField numberOfAutoExercises = new TextField();
        numberOfAutoExercises.setPrefColumnCount(4);

        Button generateButton = new Button("Generate");
        generateButton.setId("dark-blue");

        HBox generateHBox = new HBox(8);
        HBox.setMargin(generateNumberLabel, new Insets(0, 0, 0, 20));
        generateHBox.getChildren().addAll(generateNumberLabel, numberOfAutoExercises, generateButton);
        exerciseManagementVBox.getChildren().add(generateHBox);

        exerciseManagementVBox.getStyleClass().add("category-page");

        scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(exerciseManagementVBox);

        setCenter(scrollPane);
    }

    class MyTreeCell extends TextFieldTreeCell<String> {

        private ContextMenu rootContextMenu;

        public MyTreeCell() {
            // instantiate the root context menu
            rootContextMenu
                    = ContextMenuBuilder.create()
                    .items(
                            MenuItemBuilder.create()
                            .text("Add new category")
                            .onAction(
                                    new EventHandler<ActionEvent>() {
                                        @Override
                                        public void handle(ActionEvent arg0) {
                                            System.out.println("Menu Item Clicked!");
                                        }
                                    }
                            )
                            .build()
                    )
                    .build();
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            // if the item is not empty and is a root...
            if (!empty && getTreeItem().getParent() == null) {
                setContextMenu(rootContextMenu);
            }
        }
    }
}
