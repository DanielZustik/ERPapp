package com.zustik.erpapp.ControllersPresentation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DialogFilterController {

    @FXML
    private ChoiceBox<String> filterByChoice;
    @FXML
    private GridPane filterDialogGridPane;
    @FXML
    private DatePicker datePickFrom;
    @FXML
    private DatePicker datePickTo;
    @FXML
    private TextField textFieldFrom;
    @FXML
    private TextField textFieldTo;
    @FXML
    private Slider sliderFrom;
    @FXML
    private Slider sliderTo;
    @FXML
    private TextField textFieldEquals;
    @FXML
    private DatePicker datePickEquals;
    private List<Control> controls;
    private ChangeListener<Number> listenerFromSlider;
    private ChangeListener<Number> listenerToSlider;
    private DecimalFormat formatter;
    private DateTimeFormatter dtFormatter;



    public DialogFilterController() {
        // Constructor is left intentionally empty for FXML loader use
    }

    public class DataToCreateQueryContainer {
        public boolean validationCheck;
        public String inputInFromField;
        public String inputInToField;
        public String inputInEqualsField;
        public String selectedChoiceInDialog;
        public boolean equalsChosen;

        public DataToCreateQueryContainer(boolean validationCheck) {
            this.validationCheck = validationCheck;
        }

        public DataToCreateQueryContainer(boolean validationCheck, String selectedChoiceInDialog, String inputInEqualsField) {
            this.validationCheck = validationCheck;
            this.selectedChoiceInDialog = selectedChoiceInDialog;
            this.inputInEqualsField = inputInEqualsField;
            this.equalsChosen = true;

        }

        public DataToCreateQueryContainer(boolean validationCheck, String selectedChoiceInDialog, String inputInFromField, String inputInToField) {
            this.validationCheck = validationCheck;
            this.inputInFromField = inputInFromField;
            this.inputInToField = inputInToField;
            this.selectedChoiceInDialog = selectedChoiceInDialog;
            this.equalsChosen = false;

        }
    }

    @FXML
    public void initialize() {

        controls = new ArrayList<>();
        controls.addAll(Arrays.asList(filterByChoice, datePickFrom, datePickTo, textFieldFrom, textFieldTo, sliderFrom,
                sliderTo, textFieldEquals));
        for (Control control : controls) {
            control.setDisable(true);
        }
        filterByChoice.setDisable(false);
        setupChoiceBoxInvoices();

        formatter = new DecimalFormat("#,###");
        formatter.setGroupingSize(3);  // Sets the grouping size to three digits (for thousands)
        DecimalFormatSymbols customSymbols = new DecimalFormatSymbols();
        customSymbols.setGroupingSeparator(' ');  // Sets the grouping separator to a space
        formatter.setDecimalFormatSymbols(customSymbols);

        //ok so normally I would create class that would implement interface and afterward I would created instance of the class.... here i m creating instance a writing implementation right away.. .that will apply to only one created instance... because its anonymous class u cant reuse it then
        listenerFromSlider = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                textFieldFrom.setText(String.valueOf(newValue.longValue()));
            }
        };
        //same thing as above but in newer lambda expr.
        listenerToSlider = (observable, oldValue, newValue) ->
                textFieldTo.setText(String.valueOf(newValue.longValue()));

        textFieldEquals.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldStr, String newStr) {
                if (!textFieldEquals.getText().isEmpty()) {
                    textFieldFrom.setDisable(true);
                    textFieldTo.setDisable(true);
                    datePickTo.setDisable(true);
                    datePickFrom.setDisable(true);

                } else {
                    textFieldFrom.setDisable(false);
                    textFieldTo.setDisable(false);
                    datePickTo.setDisable(false);
                    datePickFrom.setDisable(false);
                }
            }
        });

    }

    private void resetControlsDisVis() {
        for (Control control : controls) {
            control.setDisable(true);
        }
        textFieldEquals.setDisable(false);
        textFieldEquals.setVisible(true);
        datePickEquals.setVisible(false);
        datePickEquals.setDisable(true);
        filterByChoice.setDisable(false);
        datePickFrom.setVisible(false);
        datePickTo.setVisible(false);
        textFieldFrom.setVisible(true);
        textFieldTo.setVisible(true);
        sliderFrom.valueProperty().removeListener(listenerFromSlider);
        sliderFrom.valueProperty().removeListener(listenerToSlider);

    }

    private void setupChoiceBoxInvoices() {
        filterByChoice.getItems().addAll("Invoice number", "Issue date", "Total amount",
                "Due amount", "Due date", "Recipient");

    }

    private void setupChoiceBoxEmployees() {
        filterByChoice.getItems().addAll("Invoice number", "Issue date", "Total amount",
                "Due amount", "Due date", "Recipient");

    }
    @FXML
    private void choiceBoxSelection(ActionEvent event) {
        String selection = filterByChoice.getSelectionModel().getSelectedItem();
        if (selection != null) {
            processData(selection);
        }
    }

    private void processData(String selection) {
        switch (selection) {
            case "Invoice number" -> {
                resetControlsDisVis();
                for (Control control : controls) {
                    control.setDisable(false);
                }
                sliderFrom.setMin(202400);
                sliderFrom.setMax(202499);
                sliderFrom.setValue(202400);
                sliderTo.setMin(202400);
                sliderTo.setMax(202499);
                sliderTo.setValue(202400);
                textFieldFrom.setText("202400");
                textFieldTo.setText("202400");
                sliderFrom.valueProperty().addListener(listenerFromSlider);
                sliderTo.valueProperty().addListener(listenerToSlider);



            }
            case "Issue date", "Due date" -> {
                resetControlsDisVis();
                for (Control control : controls) {
                    control.setDisable(false);
                }
                datePickEquals.setVisible(true);
                datePickEquals.setDisable(false);
                textFieldEquals.setDisable(true);
                textFieldEquals.setVisible(false);
                datePickFrom.setVisible(true);
                datePickTo.setVisible(true);
                textFieldFrom.setVisible(false);
                textFieldFrom.setDisable(true);
                textFieldTo.setVisible(false);
                textFieldTo.setDisable(true);
                sliderFrom.setDisable(true);
                sliderTo.setDisable(true);
            }
            case "Total amount", "Due amount" -> {
                resetControlsDisVis();
                for (Control control : controls) {
                    control.setDisable(false);
                }
                sliderFrom.setMin(0);
                sliderFrom.setMax(1000000);
                sliderFrom.setValue(0);
                sliderTo.setMin(0);
                sliderTo.setMax(1000000);
                sliderTo.setValue(0);
                textFieldFrom.setText("0");
                textFieldTo.setText("0");
                sliderFrom.valueProperty().addListener(listenerFromSlider);
                sliderTo.valueProperty().addListener(listenerToSlider);

            }
        case "Recipient" -> {
                resetControlsDisVis();
                textFieldEquals.setDisable(false);
            }
        }
    }
    public DataToCreateQueryContainer dataValidationOkBtnPressed() {
        boolean validationSucces = true;

        String selectedChoice = filterByChoice.getSelectionModel().getSelectedItem();
        switch (selectedChoice) {
            case "Invoice number", "Total amount", "Due amount"-> {
                if (textFieldEquals.getText().isEmpty()){ //this means i get info from other fields not this one
                    Double from;
                    Double to;
                    try {
                         from = Double.parseDouble(textFieldFrom.getText());
                         to = Double.parseDouble(textFieldTo.getText());
                    } catch (NumberFormatException e){
                        validationSucces = false;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Text Entered Instead of Decimal Numbers");
                        alert.setContentText("Please ensure that only decimal numbers are entered.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                        }
                        return new DataToCreateQueryContainer(validationSucces);
                    }
                    if (from > to) {
                        validationSucces = false;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("\"From\" > \"To\"");
                        alert.setContentText("Please make sure to input correct information.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                        }
                        return new DataToCreateQueryContainer(validationSucces);
                    } else {
                        return new DataToCreateQueryContainer(validationSucces, selectedChoice, textFieldFrom.getText(),
                                textFieldTo.getText()); //from and to inputed right now dealing with the case
                    }
                } else {
                    try {
                        Double.parseDouble(textFieldEquals.getText());
                    } catch (NumberFormatException e){
                        validationSucces = false;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Text Entered Instead of Decimal Numbers");
                        alert.setContentText("Please ensure that only decimal numbers are entered.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                        }
                        return new DataToCreateQueryContainer(validationSucces);
                    }
                    return new DataToCreateQueryContainer(validationSucces, selectedChoice, textFieldEquals.getText()); //equals - dealing with the case
                }
            }
            case "Issue date", "Due date" -> {
                if (datePickEquals.getEditor().getText().isEmpty()){ //this means i get info from other fields not this one
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    try {
                        LocalDate.parse(datePickFrom.getEditor().getText(), df);
                        LocalDate.parse(datePickTo.getEditor().getText(), df);
                    } catch (DateTimeParseException | NullPointerException e) {
                        validationSucces = false;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("No Date Recognized");
                        alert.setContentText("Please make sure to enter date in right format: dd.mm.yyyy.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                        }
                        return new DataToCreateQueryContainer(validationSucces);
                    }

                    if (datePickFrom.getValue().isAfter(datePickTo.getValue())){
                        validationSucces = false;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("\"From\" > \"To\"");
                        alert.setContentText("Please make sure to input correct information.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                        }
                        return new DataToCreateQueryContainer(validationSucces);
                    }

                    return new DataToCreateQueryContainer(validationSucces, selectedChoice, datePickFrom.getEditor().getText(),
                            datePickTo.getEditor().getText());
                } else {
                    System.out.println("111");

                    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    try {
                        System.out.println("asd");
                        System.out.println(datePickEquals.getEditor().getText());
                        LocalDate.parse(datePickEquals.getEditor().getText(), df);
                    } catch (DateTimeParseException | NullPointerException e) {
                        System.out.println("exception occured");
                        validationSucces = false;
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("No Date Recognized");
                        alert.setContentText("Please make sure to enter date in right format: dd.mm.yyyy.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                        }
                        return new DataToCreateQueryContainer(validationSucces);
                    }
                    return new DataToCreateQueryContainer(validationSucces, selectedChoice, datePickEquals.getEditor().getText());
                }
            }
            case "Recipient" -> {
                return new DataToCreateQueryContainer(validationSucces, selectedChoice, textFieldEquals.getText());
            }
            default -> {
                return new DataToCreateQueryContainer(false, "Unknown", "Unknown"); // Handle unexpected case
            }
        }
    }
}
