package com.catering;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import com.exceptions.*;

public class CateringMenuManagement {
    GridPane managerGrid;
    CateringMenuItem[] cateringMenu;
    private int menuCount;
    private final int maxMenuItems = 100; // Set the max size for catering items

    public CateringMenuManagement() {
        cateringMenu = new CateringMenuItem[maxMenuItems];
        menuCount = 0;
    }

    //####################################### JAVAFX IMPLEMENTATION PROGRAM BEGINS HERE ###############################################

    private GridPane getMainMenuGrid(Scene scene) {
        return getMainMenuGrid(scene, managerGrid);
    }

    public GridPane getMainMenuGrid(Scene scene, GridPane rootGrid) {
        managerGrid = rootGrid;
        GridPane catMenuGrid = new GridPane();
        catMenuGrid.setAlignment(Pos.CENTER);
        catMenuGrid.setHgap(10);
        catMenuGrid.setVgap(10);

        Label lblHeader = new Label("Please select an option from the menu below:");
        Button btnAdd = new Button("Add Catering Item");
        btnAdd.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnView = new Button("View Catering Items");
        btnView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnBack = new Button("Back");
        btnBack.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                try {
                    scene.setRoot(getAddItemGrid(scene));
                } catch (ExceededMaxSizeException e) {
                    @SuppressWarnings("unused")
                    ExceptionWindow error = new ExceptionWindow(e);
                }
            }
        });

        btnView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getViewItemGrid(scene));
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(managerGrid);
            }
        });

        catMenuGrid.add(lblHeader, 0, 0, 2, 1);
        catMenuGrid.add(btnAdd, 0, 1, 2, 1);
        catMenuGrid.add(btnView, 0, 2, 2, 1);
        catMenuGrid.add(btnBack, 0, 3, 2, 1);

        return catMenuGrid;
    }

    GridPane getAddItemGrid(Scene scene) throws ExceededMaxSizeException {
        if (menuCount >= maxMenuItems) {
            throw new ExceededMaxSizeException("Catering Menu is full");
        }
        GridPane addItemGrid = new GridPane();
        addItemGrid.setAlignment(Pos.CENTER);
        addItemGrid.setHgap(10);
        addItemGrid.setVgap(10);

        Label lblHeader = new Label("ADD ITEM TO CATERING MENU");
        Label lblDishName = new Label("Enter dish name:");
        TextField tfDishName = new TextField();
        Label lblType = new Label("Select item type:");
        ComboBox<CateringType> cboxType = new ComboBox<>();
        cboxType.getItems().addAll(CateringType.values());
        Label lblAllergens = new Label("Enter allergens:");
        TextField tfAllergens = new TextField();
        Label lblResponse = new Label();
        Button btnAddItem = new Button("Add Item");
        btnAddItem.setPrefWidth(150);
        Button btnBack = new Button("Back");
        btnBack.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        btnAddItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (tfDishName.getText().isBlank() || cboxType.getSelectionModel().getSelectedItem()==null) {
                    try {
                        throw new InvalidChoiceException("One of more mandatory options was not selected");
                    } catch (InvalidChoiceException e) {
                        @SuppressWarnings("unused")
                        ExceptionWindow error = new ExceptionWindow(e);
                        lblResponse.setText("Adding item failed.");
                        return;
                    }
                }
                cateringMenu[menuCount++] = new CateringMenuItem(tfDishName.getText(), cboxType.getSelectionModel().getSelectedItem(), tfAllergens.getText());
                lblResponse.setText("Item added successfully.");
            }
        });

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public  void handle(ActionEvent o) {
                scene.setRoot(getMainMenuGrid(scene));
            }
        });

        addItemGrid.add(lblHeader, 0, 0, 2, 1);
        addItemGrid.add(lblDishName, 0, 1);
        addItemGrid.add(tfDishName, 1, 1);
        addItemGrid.add(lblType, 0, 2);
        addItemGrid.add(cboxType, 1, 2);
        addItemGrid.add(lblAllergens, 0, 3);
        addItemGrid.add(tfAllergens, 1, 3);
        addItemGrid.add(btnAddItem, 0, 4);
        addItemGrid.add(btnBack, 1, 4);
        addItemGrid.add(lblResponse, 0, 5, 2, 1);

        return addItemGrid;
    }

    public GridPane getViewItemGrid(Scene scene) {
        GridPane viewItemGrid = new GridPane();
        viewItemGrid.setAlignment(Pos.CENTER);
        viewItemGrid.setHgap(10);
        viewItemGrid.setVgap(10);

        Button btnBack = new Button("<-");
        viewItemGrid.add(btnBack, 0, 0);
        Label lblHeader = new Label("FLIGHT CATERING MENU");
        viewItemGrid.add(lblHeader, 0, 1, 3, 1);

        String[] columns = {"Dish Name", "Type", "Allergens"};
        for (int i=0; i<columns.length; i++) {
            viewItemGrid.add(new Label(columns[i]), i*2, 2, 2, 1);
        }

        for (int i=0; i<menuCount; i++) {
            viewItemGrid.add(new Label(cateringMenu[i].name), 0, i+3, 2, 1);
            viewItemGrid.add(new Label(String.valueOf(cateringMenu[i].type)), 2, i+3, 2, 1);
            viewItemGrid.add(new Label(cateringMenu[i].allergens), 4, i+3, 2, 1);
        }

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(getMainMenuGrid(scene));
            }
        });

        return viewItemGrid;
    }
}