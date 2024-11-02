package com.dutyfree;

import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Scanner;

import com.exceptions.*;

public class DutyFreeManagement {
    GridPane sourceGrid;
    DutyFreeItem[] dutyFreeItems;
    private int itemCount;
    private final int maxItems=100;

    public DutyFreeManagement() {
        dutyFreeItems = new DutyFreeItem[maxItems];
        itemCount = 0;
    }

    public void manageDutyFree() throws InvalidChoiceException {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nDuty-Free Management:");
            System.out.println("1. Add Item");
            System.out.println("2. View Items");
            System.out.println("3. Update Item");
            System.out.println("4. Delete Item");
            System.out.println("5. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume nextline
            System.out.print("\n");

            switch (choice) {
                case 1:
                    // addDutyFreeItem();
                    break;
                case 2:
                    printDutyFreeItems();
                    break;
                case 3:
                    // updateDutyFreeItem();
                    break;
                case 4:
                    // deleteDutyFreeItem();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    throw new InvalidChoiceException("Invalid Menu Choice.");
            }
        }
    }

    public void printDutyFreeItems() {
        System.out.printf("%-15s %-15s %-15s\n", "Item Name", "Price", "Section");
        System.out.println("--------------------------------------------------");
        for (DutyFreeItem item : dutyFreeItems) {
            if (item != null) {
                System.out.printf("%-15s $%-14.2f %-15s\n", item.name, item.price, item.section);
            }
        }
    }

    //####################################### JAVAFX IMPLEMENTATION PROGRAM BEGINS HERE ###############################################

    private GridPane getMainMenuGrid(Scene scene) {
        return getMainMenuGrid(scene, sourceGrid);
    }

    public GridPane getMainMenuGrid(Scene scene, GridPane rootGrid) {
        sourceGrid = rootGrid;

        GridPane dutyFreeGrid = new GridPane();
        dutyFreeGrid.setAlignment(Pos.CENTER);
        dutyFreeGrid.setHgap(10);
        dutyFreeGrid.setVgap(10);

        Label lblHeader = new Label("Please select an option from the menu below:");
        Button btnAdd = new Button("Add Duty-Free Item");
        btnAdd.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Button btnView = new Button("View Duty-Free Items");
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
                scene.setRoot(sourceGrid);
            }
        });

        dutyFreeGrid.add(lblHeader, 0, 0, 2, 1);
        dutyFreeGrid.add(btnAdd, 0, 1, 2, 1);
        dutyFreeGrid.add(btnView, 0, 2, 2, 1);
        dutyFreeGrid.add(btnBack, 0, 3, 2, 1);

        return dutyFreeGrid;
    }

    GridPane getAddItemGrid(Scene scene) throws ExceededMaxSizeException {
        if (itemCount>=maxItems) {
            throw new ExceededMaxSizeException("Duty-Free Menu is full.");
        }

        GridPane addItemGrid = new GridPane();
        addItemGrid.setAlignment(Pos.CENTER);
        addItemGrid.setHgap(10);
        addItemGrid.setVgap(10);

        Label lblHeader = new Label("ADD ITEM TO DUTY-FREE MENU");
        Label lblItemName = new Label("Enter item name:");
        TextField tfItemName = new TextField();
        Label lblType = new Label("Select item type:");
        ComboBox<DutyFreeType> cboxType = new ComboBox<>();
        cboxType.getItems().addAll(DutyFreeType.values());
        Label lblPrice = new Label("Enter price:");
        TextField tfPrice = new TextField();
        Label lblResponse = new Label();
        Button btnAddItem = new Button("Add Item");
        btnAddItem.setPrefWidth(150);
        Button btnBack = new Button("Back");
        btnBack.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        btnAddItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                if (tfItemName.getText().isBlank() || cboxType.getSelectionModel().getSelectedItem()==null) {
                    try {
                        throw new InvalidChoiceException("One of more mandatory options was not selected");
                    } catch (InvalidChoiceException e) {
                        @SuppressWarnings("unused")
                        ExceptionWindow error = new ExceptionWindow(e);
                        lblResponse.setText("Adding item failed.");
                        return;
                    }
                }
                dutyFreeItems[itemCount++] = new DutyFreeItem(tfItemName.getText(), cboxType.getSelectionModel().getSelectedItem(), Float.parseFloat(tfPrice.getText()));
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
        addItemGrid.add(lblItemName, 0, 1);
        addItemGrid.add(tfItemName, 1, 1);
        addItemGrid.add(lblType, 0, 2);
        addItemGrid.add(cboxType, 1, 2);
        addItemGrid.add(lblPrice, 0, 3);
        addItemGrid.add(tfPrice, 1, 3);
        addItemGrid.add(btnAddItem, 0, 4);
        addItemGrid.add(btnBack, 1, 4);
        addItemGrid.add(lblResponse, 0, 5, 2, 1);

        return addItemGrid;
    }

    private GridPane getViewItemGrid(Scene scene) {
        return getViewItemGrid(scene, sourceGrid);
    }

    public GridPane getViewItemGrid(Scene scene, GridPane rootGrid) {
        sourceGrid = rootGrid;
        GridPane viewItemGrid = new GridPane();
        viewItemGrid.setAlignment(Pos.CENTER);
        viewItemGrid.setHgap(10);
        viewItemGrid.setVgap(10);

        Button btnBack = new Button("<-");
        viewItemGrid.add(btnBack, 0, 0);
        Label lblHeader = new Label("FLIGHT DUTY-FREE MENU");
        viewItemGrid.add(lblHeader, 0, 1, 3, 1);

        String[] columns = {"Item name", "Type", "Price"};
        for (int i=0; i<columns.length; i++) {
            viewItemGrid.add(new Label(columns[i]), i*2, 2, 2, 1);
        }

        for (int i=0; i<itemCount; i++) {
            viewItemGrid.add(new Label(dutyFreeItems[i].name), 0, i+3, 2, 1);
            viewItemGrid.add(new Label(String.valueOf(dutyFreeItems[i].section)), 2, i+3, 2, 1);
            viewItemGrid.add(new Label(String.format("$ %.2f", dutyFreeItems[i].price)), 4, i+3, 2, 1);
        }

        btnBack.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent o) {
                scene.setRoot(sourceGrid);
            }
        });

        return viewItemGrid;
    }
}