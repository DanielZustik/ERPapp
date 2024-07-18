package com.zustik.erpapp.ControllersPresentation;

import com.zustik.erpapp.DBAccessDataAccess.DBdataForIssuedInv;
import com.zustik.erpapp.Main;
import com.zustik.erpapp.Utility.UserInputValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.zustik.erpapp.ModelDataAccess.Invoices;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainWindowControllerIssuedInvoices {
    @FXML
    private TableView<Invoices> tableView;
    @FXML
    private TableColumn<Invoices, Integer> invoiceNumber;
    @FXML
    private TableColumn<Invoices, LocalDate> issueDate;
    @FXML
    private TableColumn<Invoices, Double> totalAmount;
    @FXML
    private TableColumn<Invoices, Double> amountDue;
    @FXML
    private TableColumn<Invoices, LocalDate> dueDate;
    @FXML
    private TableColumn<Invoices, String> recipient;
    @FXML
    private TableColumn<Invoices, String> subject;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu tableViewContextMenu;
    @FXML
    private Button clearFilterBtn;
    @FXML
    private Label activeFilterInfoLabel;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tabPaneIssuedInv;
    @FXML
    private Tab tabPaneReceivedInv;
    @FXML
    private Tab tabPaneAssets;
    @FXML
    private Tab tabPaneEmployees;
    @FXML
    private Tab tabPaneFinancialInfo;
    @FXML
    private Tab tabPaneCashFlow;
    private final Stage stage = Main.getStage();

    private DBdataForIssuedInv dataFromDB = new DBdataForIssuedInv();;
    private ObservableList<Invoices> dataForTableView = dataFromDB.querySelectWithoutParam();
    private SortedList<Invoices> dataForTableViewSortable;
    private UserInputValidator userInputValidator = new UserInputValidator();

    @FXML
    public void initialize() { //musi byt bez parametru a s anotaci. nastavuje UI prvotne
        tableViewInitz();
        tabPaneInitializ();
        contextMenuInTableViewInitz();
        formatingCellInTableViewInitz();



        tableView.setEditable(true); //umoznuje editovat radky data poklepanim
    }

    private void formatingCellInTableViewInitz() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        invoiceNumber.setCellFactory(new Callback<TableColumn<Invoices, Integer>, TableCell<Invoices, Integer>>() {
            @Override
            public TableCell<Invoices, Integer> call(TableColumn<Invoices, Integer> itemsDoubleTableColumn) {
                TableCell<Invoices, Integer> cell = new TableCell<Invoices, Integer>() {
                    @Override
                    protected void updateItem(Integer aInteger, boolean b) {
                        super.updateItem(aInteger, b);
                        if (b || aInteger == null) { //tohle se musi nadefinovat bez toho se v bunkach nic nezobrazuje
                            setText(null);
                            setGraphic(null);

                        } else {
                            setText(String.valueOf(aInteger));
                            setAlignment(Pos.CENTER);
                            setContextMenu(tableViewContextMenu);
                        }
                    }
                };

                return cell;
            }
        });
        totalAmount.setCellFactory(new Callback<TableColumn<Invoices, Double>, TableCell<Invoices, Double>>() {
            @Override
            public TableCell<Invoices, Double> call(TableColumn<Invoices, Double> itemsDoubleTableColumn) {
                TableCell<Invoices, Double> cell = new TableCell<Invoices, Double>() {

                    @Override //kdybych chtel upravovat vzhled bunek pak zde pod super.update...
                    protected void updateItem(Double aDouble, boolean b) {
                        super.updateItem(aDouble, b);
                        if (b || aDouble == null) { //tohle se musi nadefinovat bez toho se v bunkach nic nezobrazuje
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(NumberFormat.getCurrencyInstance().format(aDouble));
                            setAlignment(Pos.CENTER);
                            setContextMenu(tableViewContextMenu);
                        }
                    }
                };
                return cell;
            }
        });
        issueDate.setCellFactory(new Callback<TableColumn<Invoices, LocalDate>, TableCell<Invoices, LocalDate>>() {

            @Override
            public TableCell<Invoices, LocalDate> call(TableColumn<Invoices, LocalDate> itemsLocalDateTableColumn) {
                TableCell<Invoices, LocalDate> cell = new TableCell<Invoices, LocalDate>() {

                    @Override //kdybych chtel upravovat vzhled bunek pak zde pod super.update...
                    protected void updateItem(LocalDate localDate, boolean b) {
                        super.updateItem(localDate, b);
                        if (b || localDate == null) { //tohle se musi nadefinovat bez toho se v bunkach nic nezobrazuje
                            setText(null);
                            setGraphic(null);
                        } else {

                            setText(dateTimeFormatter.format(localDate));
                            setAlignment(Pos.CENTER);
                            setContextMenu(tableViewContextMenu);
                        }
                    }
                };
                return cell;
            }
        });
        dueDate.setCellFactory(new Callback<TableColumn<Invoices, LocalDate>, TableCell<Invoices, LocalDate>>() {

            @Override
            public TableCell<Invoices, LocalDate> call(TableColumn<Invoices, LocalDate> itemsLocalDateTableColumn) {
                TableCell<Invoices, LocalDate> cell = new TableCell<Invoices, LocalDate>() {

                    @Override //kdybych chtel upravovat vzhled bunek pak zde pod super.update...
                    protected void updateItem(LocalDate localDate, boolean b) {
                        super.updateItem(localDate, b);

                        // Reset the cell to a clean state every time it's updated
                        setText(null);
                        setGraphic(null);
                        setTextFill(Color.BLACK); // Reset to default text color

                        // Only set text and other properties if the cell is not empty and contains a valid date
                        if (!b && localDate != null) {
                            setText(dateTimeFormatter.format(localDate));
                            setAlignment(Pos.CENTER);
                            setContextMenu(tableViewContextMenu);

                            // Check if the date is before the current date and the amount due is non-zero
                            if (localDate.isBefore(LocalDate.now())) {
                                Invoices item = tableView.getItems().get(getIndex());
                                if (item.getAmountDueProp() != 0) {
                                    setTextFill(Color.RED); // Apply red color only under specific conditions
                                }
                            }
                        }
                    }
                };
                return cell;
            }
        });
        amountDue.setCellFactory(new Callback<TableColumn<Invoices, Double>, TableCell<Invoices, Double>>() {
            @Override
            public TableCell<Invoices, Double> call(TableColumn<Invoices, Double> itemsDoubleTableColumn) {
                TableCell<Invoices, Double> cell = new TableCell<Invoices, Double>() {

                    @Override //kdybych chtel upravovat vzhled bunek pak zde pod super.update...
                    protected void updateItem(Double aDouble, boolean b) {
                        super.updateItem(aDouble, b);
                        if (b || aDouble == null) { //tohle se musi nadefinovat bez toho se v bunkach nic nezobrazuje
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(NumberFormat.getCurrencyInstance().format(aDouble));
                            //setText(String.valueOf(aDouble));
                            setAlignment(Pos.CENTER);
                            setContextMenu(tableViewContextMenu);
                        }
                    }
                };
                return cell;
            }
        });
        recipient.setCellFactory(new Callback<TableColumn<Invoices, String>, TableCell<Invoices, String>>() {
            @Override
            public TableCell<Invoices, String> call(TableColumn<Invoices, String> itemsDoubleTableColumn) {
                TableCell<Invoices, String> cell = new TableCell<Invoices, String>() {

                    @Override //kdybych chtel upravovat vzhled bunek pak zde pod super.update...
                    protected void updateItem(String string, boolean b) {
                        super.updateItem(string, b);
                        if (b || string == null) { //tohle se musi nadefinovat bez toho se v bunkach nic nezobrazuje
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(String.valueOf(string));
                            setAlignment(Pos.CENTER_LEFT);
                            setContextMenu(tableViewContextMenu);
                        }
                    }
                };
                return cell;
            }
        });
        subject.setCellFactory(new Callback<TableColumn<Invoices, String>, TableCell<Invoices, String>>() {
            @Override
            public TableCell<Invoices, String> call(TableColumn<Invoices, String> itemsDoubleTableColumn) {
                TableCell<Invoices, String> cell = new TableCell<Invoices, String>() {

                    @Override //kdybych chtel upravovat vzhled bunek pak zde pod super.update...
                    protected void updateItem(String string, boolean b) {
                        super.updateItem(string, b);
                        if (b || string == null) { //tohle se musi nadefinovat bez toho se v bunkach nic nezobrazuje
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(String.valueOf(string));
                            setAlignment(Pos.CENTER_LEFT);
                            setContextMenu(tableViewContextMenu);
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void contextMenuInTableViewInitz() {
        tableViewContextMenu = new ContextMenu(); //inicializace zde, protoze nedava se do .fxml, protoze je to kontextove menu, pohyblive, nema definovanou pozici
        MenuItem editItem = new MenuItem("Edit");
        MenuItem deleteMenuItem = new MenuItem("Remove");
        MenuItem markItem = new MenuItem("Mark");

        editItem.setOnAction(this::editItemDialog);

        deleteMenuItem.setOnAction(e ->{
            Invoices itemToRemove = tableView.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Removing invoice");
            alert.setHeaderText("Removing invoice: " + itemToRemove.getSubjectProp());
            alert.setContentText("Are sure? Press OK to confirm, or cancel to back out.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                removeItemFromTableView(itemToRemove);
                removeItemFromDB(itemToRemove);
            }
        });
        markItem.setOnAction(e->{

        });
        tableViewContextMenu.getItems().addAll(editItem,deleteMenuItem, markItem);
    }

    private void tableViewInitz() {
        invoiceNumber.setCellValueFactory(new PropertyValueFactory<Invoices, Integer>("invoiceNumberProp"));
        issueDate.setCellValueFactory(new PropertyValueFactory<Invoices, LocalDate>("dateOfIssueOrReceiptProp"));
        totalAmount.setCellValueFactory(new PropertyValueFactory<Invoices, Double>("totalAmountProp"));
        amountDue.setCellValueFactory(new PropertyValueFactory<Invoices, Double>("amountDueProp"));
        dueDate.setCellValueFactory(new PropertyValueFactory<Invoices, LocalDate>("dueDateProp"));
        recipient.setCellValueFactory(new PropertyValueFactory<Invoices, String>("recipientOrSupplierProp"));
        subject.setCellValueFactory(new PropertyValueFactory<Invoices, String>("subjectProp"));

        selectAllItemsForTableView();

        tableView.getSelectionModel().select(0);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void tabPaneInitializ() {
        tabPane.getSelectionModel().select(0);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldtab, newTab) ->
        {
            if (newTab == tabPaneReceivedInv) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("receivedInvoices/MainWindow.fxml"));
                Scene scene = null; //v fmxl je pritom nastaveny nejaky root obejct ktery je root scene grafu pujde asi o gridpane. ten root pritom musi byt potomkem Node. To se hodi pritom, kdy si chci napriklad udelat vlastni obalovaci classu do ktere bych vlozil nejaky kod naprilkad k tableview aby se vse zprehlednilo a nebylo v jedne classe mainWindow, kde bych dal vsech kod pro vytvoreni UI, musel bych potom dedit z Node, abych mohl plugnout tu clasu do Scene viz https://www.youtube.com/watch?v=b-0QMxCB2zI
                try {
                    scene = new Scene(fxmlLoader.load(), 1024, 640);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setTitle("ERP application Zustik");
                stage.setScene(scene);
                stage.show();
            }
            if (newTab == tabPaneEmployees) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("employees/MainWindow.fxml"));
                Scene scene = null; //v fmxl je pritom nastaveny nejaky root obejct ktery je root scene grafu pujde asi o gridpane. ten root pritom musi byt potomkem Node. To se hodi pritom, kdy si chci napriklad udelat vlastni obalovaci classu do ktere bych vlozil nejaky kod naprilkad k tableview aby se vse zprehlednilo a nebylo v jedne classe mainWindow, kde bych dal vsech kod pro vytvoreni UI, musel bych potom dedit z Node, abych mohl plugnout tu clasu do Scene viz https://www.youtube.com/watch?v=b-0QMxCB2zI
                try {
                    scene = new Scene(fxmlLoader.load(), 1024, 640);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setTitle("ERP application Zustik");
                stage.setScene(scene);
                stage.show();
            }
            if (newTab == tabPaneIssuedInv) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("issuedInvoices/MainWindow.fxml"));
                Scene scene = null; //v fmxl je pritom nastaveny nejaky root obejct ktery je root scene grafu pujde asi o gridpane. ten root pritom musi byt potomkem Node. To se hodi pritom, kdy si chci napriklad udelat vlastni obalovaci classu do ktere bych vlozil nejaky kod naprilkad k tableview aby se vse zprehlednilo a nebylo v jedne classe mainWindow, kde bych dal vsech kod pro vytvoreni UI, musel bych potom dedit z Node, abych mohl plugnout tu clasu do Scene viz https://www.youtube.com/watch?v=b-0QMxCB2zI
                try {
                    scene = new Scene(fxmlLoader.load(), 1024, 640);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setTitle("ERP application Zustik");
                stage.setScene(scene);
                stage.show();
            }
            if (newTab == tabPaneFinancialInfo) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("financialInfo/MainWindow.fxml"));
                Scene scene = null; //v fmxl je pritom nastaveny nejaky root obejct ktery je root scene grafu pujde asi o gridpane. ten root pritom musi byt potomkem Node. To se hodi pritom, kdy si chci napriklad udelat vlastni obalovaci classu do ktere bych vlozil nejaky kod naprilkad k tableview aby se vse zprehlednilo a nebylo v jedne classe mainWindow, kde bych dal vsech kod pro vytvoreni UI, musel bych potom dedit z Node, abych mohl plugnout tu clasu do Scene viz https://www.youtube.com/watch?v=b-0QMxCB2zI
                try {
                    scene = new Scene(fxmlLoader.load(), 1024, 640);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setTitle("ERP application Zustik");
                stage.setScene(scene);
                stage.show();
            }
            if (newTab == tabPaneCashFlow) {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("cashFlow/MainWindow.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 1024, 640);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setTitle("ERP application Zustik");
                stage.setScene(scene);
                stage.show();
            }
        });
    }

    @FXML
    public void addItemDialog(ActionEvent e) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow()); //nastavuje parent pro dialogove okno na mainBorderpane.. pry by se to melo delat
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/zustik/erpapp/issuedInvoices/DialogAddView.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ee) {
            System.out.println(ee);
        }

        InvoicesDialogAddController controllerAdd = fxmlLoader.getController();  //nami vytvorena classa

        dialog.setTitle("Add the new invoice");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        userInputValidator.inputValidationOnAddDialog(fxmlLoader, dialog);
//        if (!validInputCheck.get()) controllerAdd.getNotEnoughInfoLabel().setText("Fill in all necessary information, please!");
//        controllerAdd.getProjectNumberField().setBackground(Background.fill(Color.RED));
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Invoices itemEmpty = new Invoices(); // protoze je nutne poskytnout item... flaw design tim ze pouzivam spolecnou abstract classu pro edit a add. nicmene tak jako tak bych ten item vytvarel, takze neni to zbytecne
            InvoicesDialogs.InvoiceDataContainer container = controllerAdd.processResults(itemEmpty); //nami vytvorene. Vraci item ktery je mozne potom selectnou listView.getSelectionmodel().select(newItem) protoze ale nemam tabulku s itemy, tak to nepouziji nyni
            addNewItem(container.getItem());
        } else {
            System.out.println("Cancel");
        }
    }

    private void addNewItem (Invoices invoice) {
        addNewItemIntoDB(invoice);
        addNewItemIntoTableView(invoice); //poradi zavolani je dulezite, protoze pri pridani do db se k itemu prida i id, ktere je zase nutne mit, kdyz chci chci hned smazat
    }

    private void addNewItemIntoTableView (Invoices item) {
        dataForTableView.add(item);
        tableView.getSelectionModel().selectLast();
        tableView.refresh();
    }

    private void addNewItemIntoDB (Invoices invoice) {
        dataFromDB.queryInsert(invoice);
    }
    @FXML
    public void editItemDialog(ActionEvent e) {
        Invoices selectedItem = tableView.getSelectionModel().getSelectedItem();
        tableView.getSelectionModel().getFocusedIndex(); //zbytecne?
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow()); //nastavuje parent pro dialogove okno na mainBorderpane.. pry by se to melo delat
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/zustik/erpapp/issuedInvoices/DialogEditView.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ee) {
            System.out.println(ee);
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        dialog.setTitle("Edit the invoice");
        InvoicesDialogEditController controllerEdit = fxmlLoader.getController();
        controllerEdit.populateTextFields(selectedItem);

        AtomicBoolean validInput = userInputValidator.inputValidationOnAddDialog(fxmlLoader, dialog);
        System.out.println("valid input?" + validInput);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK");
            controllerEdit.processResult(selectedItem);
            editItem(selectedItem); //i m using this same selected item reference here, because i change props on it via processresult already
        }
    }

    private void editItem (Invoices item) {
        editItemInDB(item);
    }

    private void editItemInDB(Invoices invoice) {
        dataFromDB.queryUpdate(invoice);
    }
    @FXML
    protected void removeItem(ActionEvent e) { //kdyz se chce pouzit eventhandler u vice controls, tak z e.getSource() lze potom zjistit, ktery konkretni control mel event
        Invoices selectedItem = tableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Removing the invoice");
        alert.setHeaderText("Removing the invoice: \n" +
                "number: " + selectedItem.getInvoiceNumberProp());
        alert.setContentText("Are sure? Press OK to confirm, or cancel to back out.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {

            removeItemFromTableView(selectedItem);
            removeItemFromDB(selectedItem);
        }
    }

    private void removeItemFromTableView (Invoices item) {
        dataForTableView.remove(item);
        tableView.refresh();
    }

    private void removeItemFromDB (Invoices invoice) {
        dataFromDB.queryDelete(invoice);
    }

    @FXML
    protected void filterItems(ActionEvent e) {
        Dialog<ButtonType> dialog = new Dialog<>();
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zustik/erpapp/issuedInvoices/DialogFilterView.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ee) {
            System.out.println("Failed to load dialog: " + ee);
        }
        DialogFilterController controller = fxmlLoader.getController();

        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!controller.dataValidationOkBtnPressed().validationCheck) {
                System.out.println("Validation failed, not closing the dialog.");
                event.consume();
            } else {
                System.out.println("event not consumed");
            }
        });

        dialog.setTitle("Filter the content");

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == okButtonType) {
            DialogFilterController.DataToCreateQueryContainer dataToCreateQuery = controller.dataValidationOkBtnPressed();
            System.out.println(dataToCreateQuery);
            try {
                switch (dataToCreateQuery.selectedChoiceInDialog) {
                    case "Invoice number" -> {
                        if (dataToCreateQuery.equalsChosen) {
                            int invoiceNumber = Integer.parseInt(dataToCreateQuery.inputInEqualsField);
                            dataForTableView = dataFromDB.querySelectWithParam("invoiceNumberProp",
                                    invoiceNumber);
                            activeFilterInfoLabel.setVisible(true);
                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
                                    ", value equals to: " + invoiceNumber);
                        } else {
                            int invoiceNumberFrom = Integer.parseInt(dataToCreateQuery.inputInFromField);
                            int invoiceNumberTo = Integer.parseInt(dataToCreateQuery.inputInToField);
                            dataForTableView = dataFromDB.querySelectWithParam("invoiceNumberProp",
                                    invoiceNumberFrom, invoiceNumberTo);
                            activeFilterInfoLabel.setVisible(true);
                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
                                    ", value from: " + invoiceNumberFrom + " to: " + invoiceNumberTo);
                        }
                    }
                    case "Total amount" -> {
                        if (dataToCreateQuery.equalsChosen) {
                            int totalAmount = Integer.parseInt(dataToCreateQuery.inputInEqualsField);
                            dataForTableView = dataFromDB.querySelectWithParam("totalAmountProp",
                                    totalAmount);
                            activeFilterInfoLabel.setVisible(true);
                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
                                    ", value equals to: " + totalAmount);
                        } else {
                            int totalAmountFrom = Integer.parseInt(dataToCreateQuery.inputInFromField);
                            int totalAmountTo = Integer.parseInt(dataToCreateQuery.inputInToField);
                            dataForTableView = dataFromDB.querySelectWithParam("totalAmountProp",
                                    totalAmountFrom, totalAmountTo);
                            activeFilterInfoLabel.setVisible(true);
                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
                                    ", value from: " + totalAmountFrom + " to: " + totalAmountTo);
                        }
                    }
                    case "Due amount" -> {
                        if (dataToCreateQuery.equalsChosen) {
                            int dueAmount = Integer.parseInt(dataToCreateQuery.inputInEqualsField);
                            dataForTableView = dataFromDB.querySelectWithParam("amountDueProp", //pozor nutna presna shoda s Item prop resp. MySql column
                                    dueAmount);
                            activeFilterInfoLabel.setVisible(true);
                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
                                    ", value equals to: " + dueAmount);
                        } else {
                            int dueAmountFrom = Integer.parseInt(dataToCreateQuery.inputInFromField);
                            int dueAmountTo = Integer.parseInt(dataToCreateQuery.inputInToField);
                            dataForTableView = dataFromDB.querySelectWithParam("amountDueProp",
                                    dueAmountFrom, dueAmountTo);
                            activeFilterInfoLabel.setVisible(true);
                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
                                    ", value from: " + dueAmountFrom + " to: " + dueAmountTo);
                        }
                    }
                    case "Issue date" -> {
                        DateTimeFormatter fr = DateTimeFormatter.ofPattern("dd.MM.yyyy");  // Output format for display
                        if (dataToCreateQuery.equalsChosen) {
                            LocalDate dateOfIssue = LocalDate.parse(dataToCreateQuery.inputInEqualsField, fr); // fr only for parsing, local date will never be as is (LocalDate) saved in different format. it can be .formated() though by formatter into string
                            dataForTableView = dataFromDB.querySelectWithParam("dateOfIssueProp",
                                    dateOfIssue);
                            activeFilterInfoLabel.setVisible(true);
                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
                                    ", value equals to: " + dateOfIssue.format(fr));
                        } else {
                            LocalDate dateOfIssueFrom = LocalDate.parse(dataToCreateQuery.inputInFromField, fr);
                            LocalDate dateOfIssueTo = LocalDate.parse(dataToCreateQuery.inputInToField, fr);
                            String formattedDateFrom = dateOfIssueFrom.format(fr);  // For display only
                            String formattedDateTo = dateOfIssueTo.format(fr);  // For display only
                            dataForTableView = dataFromDB.querySelectWithParam("dateOfIssueProp",
                                    dateOfIssueFrom, dateOfIssueTo);
                            activeFilterInfoLabel.setVisible(true);
                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
                                    ", value from: " + formattedDateFrom + " to: " + formattedDateTo);
                        }
                    }
                    case "Due date" -> {
                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");  // Output format for display

                        if (dataToCreateQuery.equalsChosen) {
                            LocalDate dueDate = LocalDate.parse(dataToCreateQuery.inputInEqualsField, inputFormatter);
                            String formattedDateOfIssue = dueDate.format(outputFormatter);  // For display only
                            dataForTableView = dataFromDB.querySelectWithParam("dueDateProp", //pozor nutna presna shoda s Item prop resp. MySql column
                                    dueDate);
                            activeFilterInfoLabel.setVisible(true);
                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
                                    ", value equals to: " + formattedDateOfIssue);
                        } else {
                            LocalDate dueDateFrom = LocalDate.parse(dataToCreateQuery.inputInFromField, inputFormatter);
                            LocalDate dueDateTo = LocalDate.parse(dataToCreateQuery.inputInToField, inputFormatter);
                            String formattedDateFrom = dueDateFrom.format(outputFormatter);  // For display only
                            String formattedDateTo = dueDateTo.format(outputFormatter);  // For display only
                            dataForTableView = dataFromDB.querySelectWithParam("dueDateProp",
                                    dueDateFrom, dueDateTo);
                            activeFilterInfoLabel.setVisible(true);
                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
                                    ", value from: " + formattedDateFrom + " to: " + formattedDateTo);
                        }
                    }
                    case "Recipient" -> {
                        dataForTableView = dataFromDB.querySelectWithParam("recipientProp", //pozor nutna presna shoda s Item prop resp. MySql column
                                dataToCreateQuery.inputInEqualsField);
                        activeFilterInfoLabel.setVisible(true);
                        activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
                                ", value equals to: " + dataToCreateQuery.inputInEqualsField);
                    }
                }
                clearFilterBtn.setDisable(false);
            } catch (NumberFormatException ee) {
                System.out.println("Input Error Please enter a valid number for Invoice Number.");
            } catch (DateTimeParseException ee) {
                System.out.println("Date Input Error: Please enter valid dates in the format YYYY.MM.DD");
            }
            tableView.setItems(dataForTableViewSortable);
            System.out.println(dataToCreateQuery.inputInFromField);
            //new function call in db related class taking dataToCreateQuery and making query from them and returning stuff here where i plug them in tablview
        }
    }

    @FXML
    protected void clearFilter () {
        selectAllItemsForTableView();
        activeFilterInfoLabel.setVisible(false);
        clearFilterBtn.setDisable(true);

    }

    private void selectAllItemsForTableView () {
        dataForTableViewSortable = new SortedList<>(dataForTableView,
                new Comparator<Invoices>() {
                    @Override
                    //must return 0 for queal obj, -1 fro o1 being less then o2, +1 o2 > o1

                    public int compare(Invoices o1, Invoices o2) {
                        if (o1.getInvoiceNumberProp() < o2.getInvoiceNumberProp()) return -1;
                        else if (o1.getInvoiceNumberProp() == o2.getInvoiceNumberProp()) return 0;
                        else return 1;
                    }
                });
        dataForTableViewSortable.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(dataForTableViewSortable); //data binding je to observable kdyz se neco zmeni v datech automaticky se promitne
        tableView.getSortOrder().add(invoiceNumber);
    }

    @FXML
    protected void copyItem (ActionEvent e) {
        Invoices sInv = tableView.getSelectionModel().getSelectedItem();
        Invoices invoiceCopy = new Invoices(sInv.getInvoiceNumberProp(),sInv.getDateOfIssueOrReceiptProp(),
                sInv.getTotalAmountProp(), sInv.getAmountDueProp(), sInv.getDueDateProp(), sInv.getRecipientOrSupplierProp(),
                sInv.getSubjectProp(), sInv.getProjectNumberProp(), sInv.getProjectManagerProp(), sInv.getNoteProp());
        addNewItem(invoiceCopy);
    }
}