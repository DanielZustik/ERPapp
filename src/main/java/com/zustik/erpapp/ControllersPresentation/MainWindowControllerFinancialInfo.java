package com.zustik.erpapp.ControllersPresentation;

import com.zustik.erpapp.DBAccessDataAccess.DBdataForFinInfo;
import com.zustik.erpapp.DBAccessDataAccess.DBdataForReceivedInv;
import com.zustik.erpapp.Main;
import com.zustik.erpapp.ModelDataAccess.Invoices;
import com.zustik.erpapp.ModelDataAccess.ProfitAndLossItems;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;


public class MainWindowControllerFinancialInfo {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private TableView<Map.Entry<String, Double>> tableView; //bez new... instantionation protoze to probiha skrze fmx loader
    @FXML
    private TableColumn<Map.Entry<String, Double>, String> rowLabel;
    @FXML
    private TableColumn<Map.Entry<String, Double>, Double> amount;
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
    private PieChart pieChart;
    @FXML
    private ComboBox<String> comboBoxPieChart;
    @FXML
    private ComboBox<String> comboBoxTypeOfStatement;
    @FXML
    private ComboBox<LocalDate> comboBoxPeriod;
    @FXML
    private Button btnToolBar;
    private ProfitAndLossItems mapStatementItems = new ProfitAndLossItems();
    private final Stage stage = Main.getStage();
    private DBdataForReceivedInv dataFromDB = new DBdataForReceivedInv();;
    private ObservableList<Invoices> dataForTableView = FXCollections.observableArrayList();

    @FXML
    public void initialize() { //musi byt bez parametru a s anotaci. nastavuje UI prvotne
        tableViewInitz();
        tableViewFormating();
        tabPaneInitializ();
        pieChartInitz();
        comboBoxPieChartInitz();
        comboBoxTypeOfStatementInitz();
        comboBoxPeriodnInitz();
        formatingCellInTableViewInitz();

//        Image image = new Image(getClass().getResourceAsStream("/images/iconBtn.png"));
//        ImageView imageView = new ImageView(image);
//        imageView.setFitHeight(30);
//        imageView.setFitHeight(30);
//        imageView.setPreserveRatio(true);
//        btnToolBar.setGraphic(imageView);
    }

    private void formatingCellInTableViewInitz() {
        amount.setCellFactory(new Callback<TableColumn<Map.Entry<String, Double>, Double>, TableCell<Map.Entry<String, Double>, Double>>() {
            @Override
            public TableCell<Map.Entry<String, Double>, Double> call(TableColumn<Map.Entry<String, Double>, Double> param) {
                return new TableCell<Map.Entry<String, Double>, Double>() {
                    @Override
                    protected void updateItem(Double value, boolean empty) {
                        super.updateItem(value, empty);
                        if (empty || value == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(NumberFormat.getCurrencyInstance().format(value));
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
            }
        });
    }


    private void tableViewInitz() {

        rowLabel.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getKey()); //cellData is a TableColumn.CellDataFeatures<Map.Entry<String, Double>, String> object. cellData.getValue() returns a Map.Entry<String, Double>. .getKey() extracts the key from the Map.Entry.
        });
        amount.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getValue());
        });

        ObservableList<Map.Entry<String, Double>> items = FXCollections.observableArrayList(mapStatementItems.getMapEntries()); //map.entrySet() returns a Set of Map.Entry objects.
        tableView.setItems(items);

//        tableView.setFixedCellSize(25);  // Set the fixed cell size
//        tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(Bindings.size(tableView.getItems()).add(1.03)));  // 1.01 accounts for the header

        tableView.getSelectionModel().select(0);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void pieChartInitz () {
        Map<String, Integer> data = DBdataForFinInfo.querySelectRevByClient();
        ObservableList<PieChart.Data> dataInChart = FXCollections.observableArrayList(
        );

        for (Map.Entry<String, Integer> d : data.entrySet()) {
            dataInChart.add(new PieChart.Data(d.getKey(), d.getValue()));
        }

        dataInChart.forEach(dataEntry ->
                dataEntry.nameProperty().bind(
                        Bindings.concat(
                                dataEntry.getName(), " amount: ", NumberFormat.getCurrencyInstance().format(
                                        dataEntry.pieValueProperty().getValue())
                        )
                )
        );
        pieChart.setData(dataInChart);
    }

    private void tabPaneInitializ() {
        tabPane.getSelectionModel().select(4);
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

    private void tableViewFormating () {
        amount.setCellFactory(new Callback<TableColumn<Map.Entry<String, Double>, Double>, TableCell<Map.Entry<String, Double>, Double>>() {
            @Override
            public TableCell<Map.Entry<String, Double>, Double>call(TableColumn<Map.Entry<String, Double>, Double>itemsDoubleTableColumn) {
                TableCell<Map.Entry<String, Double>, Double> cell = new TableCell<Map.Entry<String, Double>, Double>() {

                    @Override //kdybych chtel upravovat vzhled bunek pak zde pod super.update...
                    protected void updateItem(Double d, boolean b) {
                        super.updateItem(d, b);
                        if (b || d == null) { //tohle se musi nadefinovat bez toho se v bunkach nic nezobrazuje
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(String.valueOf(d));
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
                return cell;
            }
        });
    }

    private void comboBoxPieChartInitz () {
        comboBoxPieChart.setItems(FXCollections.observableArrayList("Revenue by clients",
                "Costs by general type","Costs by suppliers", "Personnel costs by employees", "Depreciation by assets"));
        comboBoxPieChart.getSelectionModel().select(0);
    }

    private void comboBoxTypeOfStatementInitz () {
        comboBoxTypeOfStatement.setItems(FXCollections.observableArrayList("Profit and Loss",
                "Balance"));
        comboBoxTypeOfStatement.getSelectionModel().select(0);
    }


    private void comboBoxPeriodnInitz() {
        comboBoxPeriod.setItems(FXCollections.observableArrayList(LocalDate.of(2024,1,1),
                LocalDate.of(2023,1,1)));
        comboBoxPeriod.getSelectionModel().select(0);

        DateTimeFormatter dt = DateTimeFormatter.ofPattern("yyyy");
        comboBoxPeriod.setCellFactory(new Callback<ListView<LocalDate>, ListCell<LocalDate>>() {
            @Override
            public ListCell<LocalDate> call(ListView<LocalDate> localDateListView) {
                return new ListCell<>(){
                    @Override
                    protected void updateItem(LocalDate value, boolean empty) {
                        super.updateItem(value, empty);
                        if (empty || value == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(value.format(dt));
                            setAlignment(Pos.CENTER);
                        }
                    }
                };
            }
        });

        comboBoxPeriod.setButtonCell(new ListCell<LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(dt));
                }
            }
        });
    }
    @FXML
    public void actionOnComboBox(ActionEvent actionEvent) {
        new Thread(task).start();

    }
    Runnable task = new Runnable() {
        @Override
        public void run() {
            if (comboBoxPieChart.getSelectionModel().getSelectedItem().equals("Revenue by clients")) {
                //code to populate chart is already there, Revenue... is default option on combobox
                Platform.runLater(() -> {
                    pieChartInitz();
                });
            }
            if (comboBoxPieChart.getSelectionModel().getSelectedItem().equals("Costs by general type")) {
                ObservableList<PieChart.Data> dataInChart = FXCollections.observableArrayList(
                );

                for (Map.Entry<String, Double> d : mapStatementItems.getMapEntries()) {
                    if (d.getKey().equals("Costs of energy and services") || d.getKey().equals("Personnel costs")
                            || d.getKey().equals("Deprecation")) dataInChart.add(new PieChart.Data(d.getKey(), d.getValue()));
                }

                dataInChart.forEach(dataEntry ->
                        dataEntry.nameProperty().bind(
                                Bindings.concat(
                                        dataEntry.getName(), " amount: ", NumberFormat.getCurrencyInstance().format(
                                                dataEntry.pieValueProperty().getValue())
                                )
                        )
                );
                // updatign actual UI node must run on UI Thread (JavaFX safety restrc. for not going ot of sync with UI. Only UI thread updates it), so giving it back to it later to run.
                System.out.println(Platform.isFxApplicationThread() ? "running on UI thread" : "Background thread");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Platform.isFxApplicationThread() ? "running on UI thread" : "Background thread");
                        pieChart.setData(dataInChart);
                    }
                });

            }
            if (comboBoxPieChart.getSelectionModel().getSelectedItem().equals("Costs by suppliers")) {
                Map<String, Integer> data = DBdataForFinInfo.querySelectCostsBySuppliers();
                ObservableList<PieChart.Data> dataInChart = FXCollections.observableArrayList(
                );

                for (Map.Entry<String, Integer> d : data.entrySet()) {
                    dataInChart.add(new PieChart.Data(d.getKey(), d.getValue()));
                }

                dataInChart.forEach(dataEntry ->
                        dataEntry.nameProperty().bind(
                                Bindings.concat(
                                        dataEntry.getName(), " amount: ", NumberFormat.getCurrencyInstance().format(
                                                dataEntry.pieValueProperty().getValue())
                                )
                        )
                );
                Platform.runLater(() -> {
                    pieChart.setData(dataInChart);
                });
            }
            if (comboBoxPieChart.getSelectionModel().getSelectedItem().equals("Personnel costs by employees")) {
                Map<String, DBdataForFinInfo.EmplCostsByName>  data = DBdataForFinInfo.querySelectPersonalCostsByName();
                ObservableList<PieChart.Data> dataInChart = FXCollections.observableArrayList();
                for (Map.Entry<String, DBdataForFinInfo.EmplCostsByName> d : data.entrySet()) {
                    LocalDate fromDate;
                    if (comboBoxPeriod.getValue().isBefore(d.getValue().getHireDate())) fromDate = d.getValue().getHireDate();
                    else fromDate = comboBoxPeriod.getValue();

                    LocalDate toDate;
                    if (d.getValue().getTerminationDate() != null)
                        if (LocalDate.now().isAfter(d.getValue().getTerminationDate())) toDate = d.getValue().getTerminationDate();
                        else toDate = LocalDate.now();
                    else toDate = LocalDate.now();

                    long workedForXDays = ChronoUnit.DAYS.between(fromDate, toDate);

                    dataInChart.add(new PieChart.Data(d.getKey(), d.getValue().getGrossWage() * 1.34 * workedForXDays / 30.4));
                }
                dataInChart.forEach(dataEntry ->
                        dataEntry.nameProperty().bind(
                                Bindings.concat(
                                        dataEntry.getName(), " amount: ", NumberFormat.getCurrencyInstance().format(
                                                dataEntry.pieValueProperty().getValue())
                                )
                        )
                );
                Platform.runLater(() -> {
                    pieChart.setData(dataInChart);
                });            }
            if (comboBoxPieChart.getSelectionModel().getSelectedItem().equals("Depreciation by assets")) {

            }
        }
    };

}