package com.zustik.erpapp.ControllersPresentation;

import com.zustik.erpapp.DBAccessDataAccess.DBdataForEmployees;
import com.zustik.erpapp.DBAccessDataAccess.DBdataForFinInfo;
import com.zustik.erpapp.Main;
import com.zustik.erpapp.ModelDataAccess.AppSettings;
import com.zustik.erpapp.ModelDataAccess.CashFlow;
import com.zustik.erpapp.ModelDataAccess.DataOperations.OperationsOnCashFlow;
import com.zustik.erpapp.ModelDataAccess.Employees;
import com.zustik.erpapp.Utility.UserInputValidator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


public class MainWindowControllerCashFlow {
    @FXML
    private LineChart<CategoryAxis, NumberAxis> lineChart;
    @FXML
    private TableView<CashFlow> tableView;
    @FXML
    private TableColumn<CashFlow, LocalDate> date = new TableColumn<>("date");
    @FXML
    private TableColumn<CashFlow, Double> invoiceIncomes = new TableColumn<>("issuedInvoicesIncomes");
    @FXML
    private TableColumn<CashFlow, Double> otherIncomes = new TableColumn<>("otherIncomes");
    @FXML
    private TableColumn<CashFlow, Double> invoiceExpenses = new TableColumn<>("receivedInvoicesExpenses");
    @FXML
    private TableColumn<CashFlow, Double> personalExpenses = new TableColumn<>("personalExpenses");
    @FXML
    private TableColumn<CashFlow, Double> otherExpenses = new TableColumn<>("otherExpenses");
    @FXML
    private TableColumn<CashFlow, Double> sum = new TableColumn<>("sum");
    @FXML
    private TableColumn<CashFlow, Double> balance = new TableColumn<>("balance");
    @FXML
    private BorderPane mainBorderPane;
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

    private final DBdataForEmployees dataFromDB = new DBdataForEmployees();;
    private ObservableList<CashFlow> dataForTableView = FXCollections.observableArrayList();
    private final UserInputValidator userInputValidator = new UserInputValidator();
    private List<CashFlow> cf;
    private List<CashFlow> cfWithoutEmptyddays;

    @FXML
    public void initialize() { //musi byt bez parametru a s anotaci. nastavuje UI prvotne
        tableViewInitz();
        tabPaneInitializ();
        formatingCellInTableViewInitz();
        lineChartInitz();
    }


    private void tableViewInitz() {
        date.setCellValueFactory(
                p -> new SimpleObjectProperty<LocalDate>(p.getValue().getDate())
        );
        invoiceIncomes.setCellValueFactory(
                p -> new SimpleDoubleProperty(p.getValue().getIssuedInvoicesIncomes()).asObject()
        );
        otherIncomes.setCellValueFactory(
                p -> new SimpleDoubleProperty(p.getValue().getOtherIncomes()).asObject()
        );
        invoiceExpenses.setCellValueFactory(
                p -> new SimpleDoubleProperty(p.getValue().getReceivedInvoicesExpenses()).asObject()
        );
        personalExpenses.setCellValueFactory(
                p -> new SimpleDoubleProperty(p.getValue().getPersonalExpenses()).asObject()
        );
        otherExpenses.setCellValueFactory(
                p -> new SimpleDoubleProperty(p.getValue().getOtherExpenses()).asObject()
        );
        sum.setCellValueFactory(
                p -> new SimpleDoubleProperty(p.getValue().getSum()).asObject()
        );
        balance.setCellValueFactory(
                p -> new SimpleDoubleProperty(p.getValue().getBalance()).asObject()
        );

//      tohle je jina verze. vyse jde o reflection resp. zjednoduseni implementace callbacku. jde tedy ve skutecnosti o instanci/implmentaci call back intrface skrze annonymni classu ocekavajici nazpatek observable value, proto je v data modelu propertyString etc. definovan.
//        lastNameCol.setCellValueFactory(new Callback<CellDataFeatures<Person, String>, ObservableValue<String>>() {
//            public ObservableValue<String> call(CellDataFeatures<Person, String> p) {
//                // p.getValue() returns the Person instance for a particular TableView row
//                return p.getValue().lastNameProperty();
//            }
//        });
//    }
        selectAllItemsForTableView();
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void formatingCellInTableViewInitz() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        date.setCellFactory(new Callback<TableColumn<CashFlow, LocalDate>, TableCell<CashFlow, LocalDate>>() {
            @Override
            public TableCell<CashFlow, LocalDate> call(TableColumn<CashFlow, LocalDate> itemsLocalDateTableColumn) {
                TableCell<CashFlow, LocalDate> cell = new TableCell<CashFlow, LocalDate>() {
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
                        }
                    }
                };
                return cell;
            }
        });
        invoiceIncomes.setCellFactory(new Callback<TableColumn<CashFlow, Double>, TableCell<CashFlow, Double>>() { // vytvoreni annonymni classy implementujici interface callBack. toto si tableView zavola bude pouzivat kdyz bude vykreslovat se. Interface callback ma dva vstupni parametry, tablcolumn  a cell a vyzaduje implementaci call metody. ta vraci cell a jako vstupni para ma tablcolumn.tablecell kterou vytvorime pro vraceni ovsem zase vyzaduje implementaci resp. nevyzaduje ale my to udelame, abychom mohli pozmenit zvhled. A to skrze definovani anonymni classy, ktera extenduje ListCell a prepisuje metodu updateItem, kde tedy presne zmenime urcite parametry cellu pro ejho vykreslovani. Nakonec vratime instanci cell za nsi anonymni tridy do call metody a ta to vrati frameworku, ktery si ji zavola, az bude vykreslovat tableview.
            @Override
            public TableCell<CashFlow, Double> call(TableColumn<CashFlow, Double> param) {
                TableCell<CashFlow, Double> cell = new TableCell<CashFlow, Double>() {
                    @Override
                    protected void updateItem(Double aDouble, boolean b) {
                        super.updateItem(aDouble, b);
                        if (b || aDouble == null) {
                            setText(null);
                            setGraphic(null);

                        } else {
                            setText(NumberFormat.getCurrencyInstance().format(aDouble));
                            setAlignment(Pos.CENTER);
                        }
                    }
                };

                return cell;
            }
        });
        otherIncomes.setCellFactory(new Callback<TableColumn<CashFlow, Double>, TableCell<CashFlow, Double>>() { // vytvoreni annonymni classy implementujici interface callBack. toto si tableView zavola bude pouzivat kdyz bude vykreslovat se. Interface callback ma dva vstupni parametry, tablcolumn  a cell a vyzaduje implementaci call metody. ta vraci cell a jako vstupni para ma tablcolumn.tablecell kterou vytvorime pro vraceni ovsem zase vyzaduje implementaci resp. nevyzaduje ale my to udelame, abychom mohli pozmenit zvhled. A to skrze definovani anonymni classy, ktera extenduje ListCell a prepisuje metodu updateItem, kde tedy presne zmenime urcite parametry cellu pro ejho vykreslovani. Nakonec vratime instanci cell za nsi anonymni tridy do call metody a ta to vrati frameworku, ktery si ji zavola, az bude vykreslovat tableview.
            @Override
            public TableCell<CashFlow, Double> call(TableColumn<CashFlow, Double> param) {
                TableCell<CashFlow, Double> cell = new TableCell<CashFlow, Double>() {
                    @Override
                    protected void updateItem(Double aDouble, boolean b) {
                        super.updateItem(aDouble, b);
                        if (b || aDouble == null) {
                            setText(null);
                            setGraphic(null);

                        } else {
                            setText(NumberFormat.getCurrencyInstance().format(aDouble));
                            setAlignment(Pos.CENTER);
                        }
                    }
                };

                return cell;
            }
        });

        invoiceExpenses.setCellFactory(new Callback<TableColumn<CashFlow, Double>, TableCell<CashFlow, Double>>() { // vytvoreni annonymni classy implementujici interface callBack. toto si tableView zavola bude pouzivat kdyz bude vykreslovat se. Interface callback ma dva vstupni parametry, tablcolumn  a cell a vyzaduje implementaci call metody. ta vraci cell a jako vstupni para ma tablcolumn.tablecell kterou vytvorime pro vraceni ovsem zase vyzaduje implementaci resp. nevyzaduje ale my to udelame, abychom mohli pozmenit zvhled. A to skrze definovani anonymni classy, ktera extenduje ListCell a prepisuje metodu updateItem, kde tedy presne zmenime urcite parametry cellu pro ejho vykreslovani. Nakonec vratime instanci cell za nsi anonymni tridy do call metody a ta to vrati frameworku, ktery si ji zavola, az bude vykreslovat tableview.
            @Override
            public TableCell<CashFlow, Double> call(TableColumn<CashFlow, Double> param) {
                TableCell<CashFlow, Double> cell = new TableCell<CashFlow, Double>() {
                    @Override
                    protected void updateItem(Double aDouble, boolean b) {
                        super.updateItem(aDouble, b);
                        if (b || aDouble == null) {
                            setText(null);
                            setGraphic(null);

                        } else {
                            setText(NumberFormat.getCurrencyInstance().format(aDouble));
                            setAlignment(Pos.CENTER);
                        }
                    }
                };

                return cell;
            }
        });

        personalExpenses.setCellFactory(new Callback<TableColumn<CashFlow, Double>, TableCell<CashFlow, Double>>() { // vytvoreni annonymni classy implementujici interface callBack. toto si tableView zavola bude pouzivat kdyz bude vykreslovat se. Interface callback ma dva vstupni parametry, tablcolumn  a cell a vyzaduje implementaci call metody. ta vraci cell a jako vstupni para ma tablcolumn.tablecell kterou vytvorime pro vraceni ovsem zase vyzaduje implementaci resp. nevyzaduje ale my to udelame, abychom mohli pozmenit zvhled. A to skrze definovani anonymni classy, ktera extenduje ListCell a prepisuje metodu updateItem, kde tedy presne zmenime urcite parametry cellu pro ejho vykreslovani. Nakonec vratime instanci cell za nsi anonymni tridy do call metody a ta to vrati frameworku, ktery si ji zavola, az bude vykreslovat tableview.
            @Override
            public TableCell<CashFlow, Double> call(TableColumn<CashFlow, Double> param) {
                TableCell<CashFlow, Double> cell = new TableCell<CashFlow, Double>() {
                    @Override
                    protected void updateItem(Double aDouble, boolean b) {
                        super.updateItem(aDouble, b);
                        if (b || aDouble == null) {
                            setText(null);
                            setGraphic(null);

                        } else {
                            setText(NumberFormat.getCurrencyInstance().format(aDouble));
                            setAlignment(Pos.CENTER);
                        }
                    }
                };

                return cell;
            }
        });

        otherExpenses.setCellFactory(new Callback<TableColumn<CashFlow, Double>, TableCell<CashFlow, Double>>() { // vytvoreni annonymni classy implementujici interface callBack. toto si tableView zavola bude pouzivat kdyz bude vykreslovat se. Interface callback ma dva vstupni parametry, tablcolumn  a cell a vyzaduje implementaci call metody. ta vraci cell a jako vstupni para ma tablcolumn.tablecell kterou vytvorime pro vraceni ovsem zase vyzaduje implementaci resp. nevyzaduje ale my to udelame, abychom mohli pozmenit zvhled. A to skrze definovani anonymni classy, ktera extenduje ListCell a prepisuje metodu updateItem, kde tedy presne zmenime urcite parametry cellu pro ejho vykreslovani. Nakonec vratime instanci cell za nsi anonymni tridy do call metody a ta to vrati frameworku, ktery si ji zavola, az bude vykreslovat tableview.
            @Override
            public TableCell<CashFlow, Double> call(TableColumn<CashFlow, Double> param) {
                TableCell<CashFlow, Double> cell = new TableCell<CashFlow, Double>() {
                    @Override
                    protected void updateItem(Double aDouble, boolean b) {
                        super.updateItem(aDouble, b);
                        if (b || aDouble == null) {
                            setText(null);
                            setGraphic(null);

                        } else {
                            setText(NumberFormat.getCurrencyInstance().format(aDouble));
                            setAlignment(Pos.CENTER);
                        }
                    }
                };

                return cell;
            }
        });

        sum.setCellFactory(new Callback<TableColumn<CashFlow, Double>, TableCell<CashFlow, Double>>() { // vytvoreni annonymni classy implementujici interface callBack. toto si tableView zavola bude pouzivat kdyz bude vykreslovat se. Interface callback ma dva vstupni parametry, tablcolumn  a cell a vyzaduje implementaci call metody. ta vraci cell a jako vstupni para ma tablcolumn.tablecell kterou vytvorime pro vraceni ovsem zase vyzaduje implementaci resp. nevyzaduje ale my to udelame, abychom mohli pozmenit zvhled. A to skrze definovani anonymni classy, ktera extenduje ListCell a prepisuje metodu updateItem, kde tedy presne zmenime urcite parametry cellu pro ejho vykreslovani. Nakonec vratime instanci cell za nsi anonymni tridy do call metody a ta to vrati frameworku, ktery si ji zavola, az bude vykreslovat tableview.
            @Override
            public TableCell<CashFlow, Double> call(TableColumn<CashFlow, Double> param) {
                TableCell<CashFlow, Double> cell = new TableCell<CashFlow, Double>() {
                    @Override
                    protected void updateItem(Double aDouble, boolean b) {
                        super.updateItem(aDouble, b);
                        if (b || aDouble == null) {
                            setText(null);
                            setGraphic(null);

                        } else {
                            setText(NumberFormat.getCurrencyInstance().format(aDouble));
                            setAlignment(Pos.CENTER);
                        }
                    }
                };

                return cell;
            }
        });

        balance.setCellFactory(new Callback<TableColumn<CashFlow, Double>, TableCell<CashFlow, Double>>() { // vytvoreni annonymni classy implementujici interface callBack. toto si tableView zavola bude pouzivat kdyz bude vykreslovat se. Interface callback ma dva vstupni parametry, tablcolumn  a cell a vyzaduje implementaci call metody. ta vraci cell a jako vstupni para ma tablcolumn.tablecell kterou vytvorime pro vraceni ovsem zase vyzaduje implementaci resp. nevyzaduje ale my to udelame, abychom mohli pozmenit zvhled. A to skrze definovani anonymni classy, ktera extenduje ListCell a prepisuje metodu updateItem, kde tedy presne zmenime urcite parametry cellu pro ejho vykreslovani. Nakonec vratime instanci cell za nsi anonymni tridy do call metody a ta to vrati frameworku, ktery si ji zavola, az bude vykreslovat tableview.
            @Override
            public TableCell<CashFlow, Double> call(TableColumn<CashFlow, Double> param) {
                TableCell<CashFlow, Double> cell = new TableCell<CashFlow, Double>() {
                    @Override
                    protected void updateItem(Double aDouble, boolean b) {
                        super.updateItem(aDouble, b);
                        if (b || aDouble == null) {
                            setText(null);
                            setGraphic(null);

                        } else {
                            setText(NumberFormat.getCurrencyInstance().format(aDouble));
                            setAlignment(Pos.CENTER);
                        }
                    }
                };

                return cell;
            }
        });

    }

    private void tabPaneInitializ() {
        tabPane.getSelectionModel().select(5);
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

    private void lineChartInitz() {
        OperationsOnCashFlow.computeAddedSums(cf);

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Balance");

        // Assuming there are exactly as many CashFlow records as days in the year
        int dayOfYear = 1;
        List<XYChart.Data> data = new ArrayList<>();

        for (int i = 1; i <= AppSettings.year.length(); i += 10) {
            data.add(new XYChart.Data<>(String.valueOf(i), CashFlow.CFRecords.get(i).getBalance()));
            //if (i > AppSettings.year.length() - 10) break;
        }
        //for drawing last day of the year
        data.add(new XYChart.Data<>(String.valueOf(AppSettings.year.length()-1), CashFlow.CFRecords.get(AppSettings.year.length()-1).getBalance()));


//        for (Double sumOfDay : addedCFSums) {
//            data.add(new XYChart.Data<>(String.valueOf(dayOfYear), sumOfDay));
//            dayOfYear++;
//        }

        lineChart.getData().clear();
        series3.getData().addAll(data);
        lineChart.getData().addAll(series3);
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
        cf = OperationsOnCashFlow.computeCF();
        cfWithoutEmptyddays = OperationsOnCashFlow.computeCFWithoutEmptyDays();

        dataForTableView = FXCollections.observableArrayList(cfWithoutEmptyddays);
        tableView.setItems(dataForTableView); //data binding je to observable kdyz se neco zmeni v datech automaticky se promitne

    }

}