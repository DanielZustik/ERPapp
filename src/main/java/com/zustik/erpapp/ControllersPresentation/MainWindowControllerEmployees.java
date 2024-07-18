package com.zustik.erpapp.ControllersPresentation;

import com.zustik.erpapp.DBAccessDataAccess.DBdataForEmployees;
import com.zustik.erpapp.Main;
import com.zustik.erpapp.ModelDataAccess.CashFlow;
import com.zustik.erpapp.ModelDataAccess.DataOperations.OperationsOnCashFlow;
import com.zustik.erpapp.ModelDataAccess.Employees;
import com.zustik.erpapp.Utility.UserInputValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainWindowControllerEmployees {
    @FXML
    private TableView<Employees> tableView;
    @FXML
    private TableColumn<Employees, Integer> ID;
    @FXML
    private TableColumn<Employees, Boolean> activeStatus;
    @FXML
    private TableColumn<Employees, String> firstName;
    @FXML
    private TableColumn<Employees, String> lastName;
    @FXML
    private TableColumn<Employees, LocalDate> hireDate;
    @FXML
    private TableColumn<Employees, String> contractType;
    @FXML
    private TableColumn<Employees, String> department;
    @FXML
    private TableColumn<Employees, Double> grossWage;
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
    @FXML
    private ChoiceBox<String> choiceBoxActiveShown;
    private final Stage stage = Main.getStage();

    private final DBdataForEmployees dataFromDB = new DBdataForEmployees();;
    private ObservableList<Employees> dataForTableView = FXCollections.observableArrayList();
    private final UserInputValidator userInputValidator = new UserInputValidator();

    @FXML
    public void initialize() { //musi byt bez parametru a s anotaci. nastavuje UI prvotne
        tableViewInitz();
        tabPaneInitializ();
        contextMenuInTableViewInitz();
        formatingCellInTableViewInitz();
        choiceBoxActiveShownInitz();
    }

    private void tableViewInitz() {
        ID.setCellValueFactory(new PropertyValueFactory<Employees, Integer>("ID")); //skrze reflection hleda IDproperty v data modelu Employees
        activeStatus.setCellValueFactory(new PropertyValueFactory<>("activeStatus"));
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        hireDate.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        contractType.setCellValueFactory(new PropertyValueFactory<>("contractType"));
        department.setCellValueFactory(new PropertyValueFactory<>("department"));
        grossWage.setCellValueFactory(new PropertyValueFactory<>("grossWage"));

//      tohle je jina verze. vyse jde o reflection resp. zjednoduseni implementace callbacku. jde tedy ve skutecnosti o instanci/implmentaci call back intrface skrze annonymni classu ocekavajici nazpatek observable value, proto je v data modelu propertyString etc. definovan.
//        lastNameCol.setCellValueFactory(new Callback<CellDataFeatures<Person, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(CellDataFeatures<Person, String> p) {
//                // p.getValue() returns the Person instance for a particular TableView row
//                return p.getValue().lastNameProperty();
//            }
//        });
//    }
        selectAllItemsForTableView();
        tableView.getSelectionModel().select(0);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void formatingCellInTableViewInitz() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        ID.setCellFactory(new Callback<TableColumn<Employees, Integer>, TableCell<Employees, Integer>>() { // vytvoreni annonymni classy implementujici interface callBack. toto si tableView zavola bude pouzivat kdyz bude vykreslovat se. Interface callback ma dva vstupni parametry, tablcolumn  a cell a vyzaduje implementaci call metody. ta vraci cell a jako vstupni para ma tablcolumn.tablecell kterou vytvorime pro vraceni ovsem zase vyzaduje implementaci resp. nevyzaduje ale my to udelame, abychom mohli pozmenit zvhled. A to skrze definovani anonymni classy, ktera extenduje ListCell a prepisuje metodu updateItem, kde tedy presne zmenime urcite parametry cellu pro ejho vykreslovani. Nakonec vratime instanci cell za nsi anonymni tridy do call metody a ta to vrati frameworku, ktery si ji zavola, az bude vykreslovat tableview.
            @Override
            public TableCell<Employees, Integer> call(TableColumn<Employees, Integer> param) {
                TableCell<Employees, Integer> cell = new TableCell<Employees, Integer>() {
                    @Override
                    protected void updateItem(Integer aInteger, boolean b) {
                        super.updateItem(aInteger, b);
                        if (b || aInteger == null) { //v tomto miste jsem uvnitr tableCell classy , ktera extenduje sve parenty jako node atd, graphic a z nich ma metody ajko settext
                            setText(null); //musi se zavvolat... protoze prepisujeme implementaci updateItem kde by to defaultni cell factory tez volala. v super.updatItem to neni, tam jsou asi jine veci. je dulezite volat settext null kdyz neni hodnota v cell, protoze jinak by se vykreslila hodnota, ktera tam byla z drivejska napriklad, tedy byla by zobrazena najeka hodnota, ktera ve skutenocnosti neni jako value ulozena (skrze valuefactory, underlying datamodel atd.)
                            setGraphic(null);

                        } else {
                            setText(String.valueOf(aInteger));
                            setAlignment(Pos.CENTER);
                            setContextMenu(tableViewContextMenu);
                            Employees emp = getTableView().getItems().get(getIndex());
                            if (emp != null && !emp.isActiveStatus()) {
                                setTextFill(Color.GREY);
                            } else {
                                setTextFill(Color.BLACK);
                            }
                        }
                    }
                };

                return cell;
            }
        });

        activeStatus.setCellFactory(new Callback<TableColumn<Employees, Boolean>, TableCell<Employees, Boolean>>() { // vytvoreni annonymni classy implementujici interface callBack. toto si tableView zavola bude pouzivat kdyz bude vykreslovat se. Interface callback ma dva vstupni parametry, tablcolumn  a cell a vyzaduje implementaci call metody. ta vraci cell a jako vstupni para ma tablcolumn.tablecell kterou vytvorime pro vraceni ovsem zase vyzaduje implementaci resp. nevyzaduje ale my to udelame, abychom mohli pozmenit zvhled. A to skrze definovani anonymni classy, ktera extenduje ListCell a prepisuje metodu updateItem, kde tedy presne zmenime urcite parametry cellu pro ejho vykreslovani. Nakonec vratime instanci cell za nsi anonymni tridy do call metody a ta to vrati frameworku, ktery si ji zavola, az bude vykreslovat tableview.
            @Override
            public TableCell<Employees, Boolean> call(TableColumn<Employees, Boolean> param) {
                TableCell<Employees, Boolean> cell = new TableCell<Employees, Boolean>() {
                    @Override
                    protected void updateItem(Boolean aBol, boolean b) {
                        super.updateItem(aBol, b);
                        if (b || aBol == null) {
                            setText(null);
                            setGraphic(null);

                        } else {
                            setText(String.valueOf(aBol));
                            setAlignment(Pos.CENTER);
                            setContextMenu(tableViewContextMenu);
                            Employees emp = getTableView().getItems().get(getIndex());
                            if (emp != null && !emp.isActiveStatus()) {
                                setTextFill(Color.GREY);
                            } else {
                                setTextFill(Color.BLACK);
                            }
//                            if (choiceBoxActiveShown.getSelectionModel().isSelected(0))
//                                selectAllItemsForTableView();
                        }
                    }
                };

                return cell;
            }
        });
        firstName.setCellFactory(new Callback<TableColumn<Employees, String>, TableCell<Employees, String>>() {
            // Creates an anonymous class implementing the Callback interface.
            // This interface has two type parameters: TableColumn and TableCell.
            // The interface requires the implementation of the call method, which returns a TableCell.
            // The call method takes a TableColumn as a parameter and returns a TableCell.
            @Override
            public TableCell<Employees, String> call(TableColumn<Employees, String> param) {
                // Creating an anonymous class extending TableCell to customize the cell rendering.
                // The updateItem method is overridden to modify the cell's appearance based on its state.
                TableCell<Employees, String> cell = new TableCell<Employees, String>() {
                    @Override
                    protected void updateItem(String aString, boolean empty) {
                        super.updateItem(aString, empty);
                        // If the cell is empty or the item is null, clear the text and graphic.
                        if (empty || aString == null) {
                            setText(null); /// this is im[ortant. when there is no value, its rendered emplty so previsous value is not shown. otherwise it would show it. eg u remove row and it would still be rendered the value there...
                            setGraphic(null);
                        } else {
                            // Otherwise, set the text to the string representation of the item.
                            setText(String.valueOf(aString));
                            // Center-align the text.
                            setAlignment(Pos.CENTER);
                            // Set a context menu for the cell.
                            setContextMenu(tableViewContextMenu);
                            Employees emp = getTableView().getItems().get(getIndex());
                            if (emp != null && !emp.isActiveStatus()) {
                                setTextFill(Color.GREY);
                            } else {
                                setTextFill(Color.BLACK);
                            }
                        }
                    }
                };
                // Return the customized cell to the framework, which will use it to render the TableView.
                return cell;
            }
        });
        lastName.setCellFactory(new Callback<TableColumn<Employees, String>, TableCell<Employees, String>>() {
            @Override
            public TableCell<Employees, String> call(TableColumn<Employees, String> param) {
                TableCell<Employees, String> cell = new TableCell<Employees, String>() {
                    @Override
                    protected void updateItem(String aString, boolean empty) {
                        super.updateItem(aString, empty);
                        if (empty || aString == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(String.valueOf(aString));
                            setAlignment(Pos.CENTER);
                            setContextMenu(tableViewContextMenu);
                            Employees emp = getTableView().getItems().get(getIndex());
                            if (emp != null && !emp.isActiveStatus()) {
                                setTextFill(Color.GREY);
                            } else {
                                setTextFill(Color.BLACK);
                            }
                        }
                    }
                };
                return cell;
            }
        });
        hireDate.setCellFactory(new Callback<TableColumn<Employees, LocalDate>, TableCell<Employees, LocalDate>>() {
            @Override
            public TableCell<Employees, LocalDate> call(TableColumn<Employees, LocalDate> itemsLocalDateTableColumn) {
                TableCell<Employees, LocalDate> cell = new TableCell<Employees, LocalDate>() {
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
                            Employees emp = getTableView().getItems().get(getIndex());
                            if (emp != null && !emp.isActiveStatus()) {
                                setTextFill(Color.GREY);
                            } else {
                                setTextFill(Color.BLACK);
                            }
                        }
                    }
                };
                return cell;
            }
        });
        contractType.setCellFactory(new Callback<TableColumn<Employees, String>, TableCell<Employees, String>>() {
            @Override
            public TableCell<Employees, String> call(TableColumn<Employees, String> param) {
                TableCell<Employees, String> cell = new TableCell<Employees, String>() {
                    @Override
                    protected void updateItem(String aString, boolean empty) {
                        super.updateItem(aString, empty);
                        if (empty || aString == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(String.valueOf(aString));
                            setAlignment(Pos.CENTER);
                            setContextMenu(tableViewContextMenu);
                            Employees emp = getTableView().getItems().get(getIndex());
                            if (emp != null && !emp.isActiveStatus()) {
                                setTextFill(Color.GREY);
                            } else {
                                setTextFill(Color.BLACK);
                            }
                        }
                    }
                };
                return cell;
            }
        });
        department.setCellFactory(new Callback<TableColumn<Employees, String>, TableCell<Employees, String>>() {
            @Override
            public TableCell<Employees, String> call(TableColumn<Employees, String> param) {
                TableCell<Employees, String> cell = new TableCell<Employees, String>() {
                    @Override
                    protected void updateItem(String aString, boolean empty) {
                        super.updateItem(aString, empty);
                        if (empty || aString == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(String.valueOf(aString));
                            setAlignment(Pos.CENTER);
                            setContextMenu(tableViewContextMenu);
                            Employees emp = getTableView().getItems().get(getIndex());
                            if (emp != null && !emp.isActiveStatus()) {
                                setTextFill(Color.GREY);
                            } else {
                                setTextFill(Color.BLACK);
                            }
                        }
                    }
                };
                return cell;
            }
        });
        grossWage.setCellFactory(new Callback<TableColumn<Employees, Double>, TableCell<Employees, Double>>() { // vytvoreni annonymni classy implementujici interface callBack. toto si tableView zavola bude pouzivat kdyz bude vykreslovat se. Interface callback ma dva vstupni parametry, tablcolumn  a cell a vyzaduje implementaci call metody. ta vraci cell a jako vstupni para ma tablcolumn.tablecell kterou vytvorime pro vraceni ovsem zase vyzaduje implementaci resp. nevyzaduje ale my to udelame, abychom mohli pozmenit zvhled. A to skrze definovani anonymni classy, ktera extenduje ListCell a prepisuje metodu updateItem, kde tedy presne zmenime urcite parametry cellu pro ejho vykreslovani. Nakonec vratime instanci cell za nsi anonymni tridy do call metody a ta to vrati frameworku, ktery si ji zavola, az bude vykreslovat tableview.
            @Override
            public TableCell<Employees, Double> call(TableColumn<Employees, Double> param) {
                TableCell<Employees, Double> cell = new TableCell<Employees, Double>() {
                    @Override
                    protected void updateItem(Double aDouble, boolean b) {
                        super.updateItem(aDouble, b);
                        if (b || aDouble == null) {
                            setText(null);
                            setGraphic(null);

                        } else {
                            setText(String.valueOf(aDouble));
                            setAlignment(Pos.CENTER);
                            setContextMenu(tableViewContextMenu);
                            Employees emp = getTableView().getItems().get(getIndex());
                            if (emp != null && !emp.isActiveStatus()) {
                                setTextFill(Color.GREY);
                            } else {
                                setTextFill(Color.BLACK);
                            }
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
            Employees itemToRemove = tableView.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Removing employee");
            alert.setHeaderText("Removing employee: " + itemToRemove.getFirstName() + " " + itemToRemove.getLastName());
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



    private void tabPaneInitializ() {
        tabPane.getSelectionModel().select(3);
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

    private void choiceBoxActiveShownInitz() {
        choiceBoxActiveShown.getItems().addAll("Active empl. shown", "All empl. shown");

        //tableView.setItems(dataFromDB.querySelectWithParam(true));

        choiceBoxActiveShown.valueProperty().addListener(
                (observableValue, old, newValue) -> {
                    if (newValue.equals("All empl. shown")) selectAllItemsForTableView();
                    else if (newValue.equals("Active empl. shown")) {
                        tableView.setItems(dataFromDB.querySelectWithParam(true));
                    }
                }
        );
        choiceBoxActiveShown.getSelectionModel().select(0);
        tableView.getSelectionModel().select(0);

    }

    private void choiceBoxActiveShownReapply() { //manually triggering listener
        String value = choiceBoxActiveShown.getValue();
        if (value.equals("All empl. shown")) choiceBoxActiveShown.getSelectionModel().select("Active empl. shown");
        else choiceBoxActiveShown.getSelectionModel().select("All empl. shown");

        choiceBoxActiveShown.getSelectionModel().select(value);
    }

    @FXML
    public void addItemDialog(ActionEvent e) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow()); //nastavuje parent pro dialogove okno na mainBorderpane.. pry by se to melo delat
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/zustik/erpapp/employees/DialogAddView.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ee) {
            System.out.println(ee);
        }

        EmployeesDialogAddController controllerAdd = fxmlLoader.getController();  //nami vytvorena classa

        dialog.setTitle("Add the new employee");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        userInputValidator.inputValidationOnAddDialog(fxmlLoader, dialog);
//        if (!validInputCheck.get()) controllerAdd.getNotEnoughInfoLabel().setText("Fill in all necessary information, please!");
//        controllerAdd.getProjectNumberField().setBackground(Background.fill(Color.RED));
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Employees itemCreated = controllerAdd.processResult(); //nami vytvorene. Vraci item ktery je mozne potom selectnou listView.getSelectionmodel().select(newItem) protoze ale nemam tabulku s itemy, tak to nepouziji nyni
            addNewItem(itemCreated);
            tableView.refresh();
            choiceBoxActiveShownReapply();
            tableView.getSelectionModel().select(itemCreated);
        } else {
            System.out.println("Cancel");
        }
    }

    private void addNewItem (Employees item) {
        addNewItemIntoDB(item);
        addNewItemIntoTableView(item); //poradi zavolani je dulezite, protoze pri pridani do db se k itemu prida i id, ktere je zase nutne mit, kdyz chci chci hned smazat resp. zobrazit v tableview
    }

    private void addNewItemIntoTableView (Employees item) {
        dataForTableView.add(item);
        tableView.getSelectionModel().selectLast();

    }

    private void addNewItemIntoDB (Employees item) {
        dataFromDB.queryInsert(item);
    }
    @FXML
    public void editItemDialog(ActionEvent e) {
        Employees selectedItem = tableView.getSelectionModel().getSelectedItem();
        System.out.println("teminat date" + selectedItem.getTerminationDate());
        tableView.getSelectionModel().getFocusedIndex(); //zbytecne?
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow()); //nastavuje parent pro dialogove okno na mainBorderpane.. pry by se to melo delat
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/com/zustik/erpapp/employees/DialogEditView.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException ee) {
            System.out.println(ee);
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        dialog.setTitle("Edit the employee info");
        EmployeesDialogEditController controllerEdit = fxmlLoader.getController();
        controllerEdit.populateTextFields(selectedItem);

        AtomicBoolean validInput = userInputValidator.inputValidationOnAddDialog(fxmlLoader, dialog);
        System.out.println("valid input?" + validInput);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("OK");
            controllerEdit.processResult(selectedItem);
            editItem(selectedItem); //i m using this same selected item reference here, because i change props on it via processresult already
            tableView.refresh();
            choiceBoxActiveShownReapply();
            //if (choiceBoxActiveShown.getSelectionModel().isSelected(0)) choiceBoxActiveShown.fireEvent();
            tableView.getSelectionModel().select(selectedItem);
        }
    }

    private void editItem (Employees item) {
        editItemInDB(item);
    }

    private void editItemInDB(Employees item) {
        dataFromDB.queryUpdate(item);
    }
    @FXML
    protected void removeItem(ActionEvent e) { //kdyz se chce pouzit eventhandler u vice controls, tak z e.getSource() lze potom zjistit, ktery konkretni control mel event
        Employees selectedItem = tableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Removing the employee");
        alert.setHeaderText("Removing the employee: \n" +
                "ID: " + selectedItem.getID());
        alert.setContentText("Are sure? Press OK to confirm, or cancel to back out.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {

            removeItemFromTableView(selectedItem);
            removeItemFromDB(selectedItem);
            tableView.refresh(); // refresh the table view
            choiceBoxActiveShownReapply(); // reapply filter

        }
    }

    private void removeItemFromTableView (Employees item) {
        dataForTableView.remove(item);
    }

    private void removeItemFromDB (Employees item) {
        dataFromDB.queryDelete(item);
    }
//
//    @FXML
//    protected void filterItems(ActionEvent e) {
//        Dialog<ButtonType> dialog = new Dialog<>();
//        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
//        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/zustik/erpapp/issuedInvoicesIncomes/DialogFilterView.fxml"));
//        try {
//            dialog.getDialogPane().setContent(fxmlLoader.load());
//        } catch (IOException ee) {
//            System.out.println("Failed to load dialog: " + ee);
//        }
//        DialogFilterController controller = fxmlLoader.getController();
//
//        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
//        okButton.addEventFilter(ActionEvent.ACTION, event -> {
//            if (!controller.dataValidationOkBtnPressed().validationCheck) {
//                System.out.println("Validation failed, not closing the dialog.");
//                event.consume();
//            } else {
//                System.out.println("event not consumed");
//            }
//        });
//
//        dialog.setTitle("Filter the content");
//
//        Optional<ButtonType> result = dialog.showAndWait();
//        if (result.isPresent() && result.get() == okButtonType) {
//            DialogFilterController.DataToCreateQueryContainer dataToCreateQuery = controller.dataValidationOkBtnPressed();
//            System.out.println(dataToCreateQuery);
//            try {
//                switch (dataToCreateQuery.selectedChoiceInDialog) {
//                    case "Invoice number" -> {
//                        if (dataToCreateQuery.equalsChosen) {
//                            int invoiceNumber = Integer.parseInt(dataToCreateQuery.inputInEqualsField);
//                            dataForTableView = dataFromDB.querySelectWithParam("invoiceNumberProp",
//                                    invoiceNumber);
//                            activeFilterInfoLabel.setVisible(true);
//                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
//                                    ", value equals to: " + invoiceNumber);
//                        } else {
//                            int invoiceNumberFrom = Integer.parseInt(dataToCreateQuery.inputInFromField);
//                            int invoiceNumberTo = Integer.parseInt(dataToCreateQuery.inputInToField);
//                            dataForTableView = dataFromDB.querySelectWithParam("invoiceNumberProp",
//                                    invoiceNumberFrom, invoiceNumberTo);
//                            activeFilterInfoLabel.setVisible(true);
//                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
//                                    ", value from: " + invoiceNumberFrom + " to: " + invoiceNumberTo);
//                        }
//                    }
//                    case "Total amount" -> {
//                        if (dataToCreateQuery.equalsChosen) {
//                            int totalAmount = Integer.parseInt(dataToCreateQuery.inputInEqualsField);
//                            dataForTableView = dataFromDB.querySelectWithParam("totalAmountProp",
//                                    totalAmount);
//                            activeFilterInfoLabel.setVisible(true);
//                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
//                                    ", value equals to: " + totalAmount);
//                        } else {
//                            int totalAmountFrom = Integer.parseInt(dataToCreateQuery.inputInFromField);
//                            int totalAmountTo = Integer.parseInt(dataToCreateQuery.inputInToField);
//                            dataForTableView = dataFromDB.querySelectWithParam("totalAmountProp",
//                                    totalAmountFrom, totalAmountTo);
//                            activeFilterInfoLabel.setVisible(true);
//                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
//                                    ", value from: " + totalAmountFrom + " to: " + totalAmountTo);
//                        }
//                    }
//                    case "Due amount" -> {
//                        if (dataToCreateQuery.equalsChosen) {
//                            int dueAmount = Integer.parseInt(dataToCreateQuery.inputInEqualsField);
//                            dataForTableView = dataFromDB.querySelectWithParam("amountDueProp", //pozor nutna presna shoda s Item prop resp. MySql column
//                                    dueAmount);
//                            activeFilterInfoLabel.setVisible(true);
//                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
//                                    ", value equals to: " + dueAmount);
//                        } else {
//                            int dueAmountFrom = Integer.parseInt(dataToCreateQuery.inputInFromField);
//                            int dueAmountTo = Integer.parseInt(dataToCreateQuery.inputInToField);
//                            dataForTableView = dataFromDB.querySelectWithParam("amountDueProp",
//                                    dueAmountFrom, dueAmountTo);
//                            activeFilterInfoLabel.setVisible(true);
//                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
//                                    ", value from: " + dueAmountFrom + " to: " + dueAmountTo);
//                        }
//                    }
//                    case "Issue date" -> {
//                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");  // Output format for display
//
//                        if (dataToCreateQuery.equalsChosen) {
//                            LocalDate dateOfIssue = LocalDate.parse(dataToCreateQuery.inputInEqualsField, inputFormatter);
//                            String formattedDateOfIssue = dateOfIssue.format(outputFormatter);  // For display only
//                            dataForTableView = dataFromDB.querySelectWithParam("dateOfIssueProp", //pozor nutna presna shoda s Item prop resp. MySql column
//                                    dateOfIssue);
//                            activeFilterInfoLabel.setVisible(true);
//                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
//                                    ", value equals to: " + formattedDateOfIssue);
//                        } else {
//                            LocalDate dateOfIssueFrom = LocalDate.parse(dataToCreateQuery.inputInFromField, inputFormatter);
//                            LocalDate dateOfIssueTo = LocalDate.parse(dataToCreateQuery.inputInToField, inputFormatter);
//                            String formattedDateFrom = dateOfIssueFrom.format(outputFormatter);  // For display only
//                            String formattedDateTo = dateOfIssueTo.format(outputFormatter);  // For display only
//                            dataForTableView = dataFromDB.querySelectWithParam("dateOfIssueProp",
//                                    dateOfIssueFrom, dateOfIssueTo);
//                            activeFilterInfoLabel.setVisible(true);
//                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
//                                    ", value from: " + formattedDateFrom + " to: " + formattedDateTo);
//                        }
//                    }
//                    case "Due date" -> {
//                        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");  // Output format for display
//
//                        if (dataToCreateQuery.equalsChosen) {
//                            LocalDate dueDate = LocalDate.parse(dataToCreateQuery.inputInEqualsField, inputFormatter);
//                            String formattedDateOfIssue = dueDate.format(outputFormatter);  // For display only
//                            dataForTableView = dataFromDB.querySelectWithParam("dueDateProp", //pozor nutna presna shoda s Item prop resp. MySql column
//                                    dueDate);
//                            activeFilterInfoLabel.setVisible(true);
//                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
//                                    ", value equals to: " + formattedDateOfIssue);
//                        } else {
//                            LocalDate dueDateFrom = LocalDate.parse(dataToCreateQuery.inputInFromField, inputFormatter);
//                            LocalDate dueDateTo = LocalDate.parse(dataToCreateQuery.inputInToField, inputFormatter);
//                            String formattedDateFrom = dueDateFrom.format(outputFormatter);  // For display only
//                            String formattedDateTo = dueDateTo.format(outputFormatter);  // For display only
//                            dataForTableView = dataFromDB.querySelectWithParam("dueDateProp",
//                                    dueDateFrom, dueDateTo);
//                            activeFilterInfoLabel.setVisible(true);
//                            activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
//                                    ", value from: " + formattedDateFrom + " to: " + formattedDateTo);
//                        }
//                    }
//                    case "Recipient" -> {
//                        dataForTableView = dataFromDB.querySelectWithParam("recipientProp", //pozor nutna presna shoda s Item prop resp. MySql column
//                                dataToCreateQuery.inputInEqualsField);
//                        activeFilterInfoLabel.setVisible(true);
//                        activeFilterInfoLabel.setText("Filtered by: " + dataToCreateQuery.selectedChoiceInDialog +
//                                ", value equals to: " + dataToCreateQuery.inputInEqualsField);
//                    }
//                }
//                clearFilterBtn.setDisable(false);
//            } catch (NumberFormatException ee) {
//                System.out.println("Input Error Please enter a valid number for Invoice Number.");
//            } catch (DateTimeParseException ee) {
//                System.out.println("Date Input Error: Please enter valid dates in the format YYYY.MM.DD");
//            }
//            tableView.setItems(dataForTableView);
//            System.out.println(dataToCreateQuery.inputInFromField);
//            //new function call in db related class taking dataToCreateQuery and making query from them and returning stuff here where i plug them in tablview
//        }
//    }
//
    @FXML
    protected void clearFilter () {
        selectAllItemsForTableView();
        activeFilterInfoLabel.setVisible(false);
        clearFilterBtn.setDisable(true);

    }

    private void selectAllItemsForTableView () {
        dataForTableView = dataFromDB.querySelectWithoutParam();
        tableView.setItems(dataForTableView); //data binding je to observable kdyz se neco zmeni v datech automaticky se promitne

    }

}