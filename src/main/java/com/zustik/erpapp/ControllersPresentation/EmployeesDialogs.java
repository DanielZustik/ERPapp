package com.zustik.erpapp.ControllersPresentation;

import com.zustik.erpapp.ModelDataAccess.Employees;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public abstract class EmployeesDialogs {

    @FXML
    protected TextField ID;
    @FXML
    protected TextField firstName;
    @FXML
    protected TextField lastName;
    @FXML
    protected DatePicker hireDate;
    @FXML
    protected ChoiceBox<String> contractType;
    @FXML
    protected TextField department;
    @FXML
    protected TextField grossWage;
    @FXML
    protected ChoiceBox<String> degree;
    @FXML
    protected TextArea address;
    @FXML
    protected TextField bankAccNum;
    @FXML
    protected ChoiceBox<String> maritalStatus;
    @FXML
    protected TextField numberOfChildren;
    protected ToggleGroup activeStatus;
    @FXML
    protected RadioButton activeStatusYes;
    @FXML
    protected RadioButton activeStatusNo;
    @FXML
    protected DatePicker terminationDate;
    @FXML
    protected ChoiceBox<String> highestEducationCompleted;

    @FXML
    protected Label terminationDateLabel;
    @FXML
    private Label notEnoughInfoLabel;

    @FXML
    public void initialize() {
        degree.getItems().addAll("Dis.", "Bc.", "Ing.", "Mgr.", "Ph.D.");
        contractType.getItems().addAll("full time", "part-time", "task-based");
        maritalStatus.getItems().addAll("single", "married", "divorced", "widowed");
        highestEducationCompleted.getItems().addAll("bachelor's", "master's", "doctorate", "associate");

        activeStatus = new ToggleGroup(); //init here because its not in FXML (IDE doesnt read TAG)
        activeStatusYes.setToggleGroup(activeStatus);
        activeStatusNo.setToggleGroup(activeStatus);
        activeStatusNo.selectedProperty().addListener(
                (observableValue, old, newValue) -> {
                    if (newValue) {
                        terminationDate.setVisible(true);
                        terminationDate.setDisable(false);
                        terminationDateLabel.setVisible(true);
                    } else {
                        terminationDate.setVisible(false);
                        terminationDate.setDisable(true);
                        terminationDateLabel.setVisible(false);
                    }
                }
        );
    }
    Employees processResult() {return null;};

    public TextField getID() {
        return ID;
    }

    public TextField getFirstName() {
        return firstName;
    }

    public TextField getLastName() {
        return lastName;
    }

    public DatePicker getHireDate() {
        return hireDate;
    }

    public ChoiceBox<String> getContractType() {
        return contractType;
    }

    public TextField getDepartment() {
        return department;
    }

    public TextField getGrossWage() {
        return grossWage;
    }

    public ChoiceBox<String> getDegree() {
        return degree;
    }

    public TextArea getAddress() {
        return address;
    }

    public TextField getBankAccNum() {
        return bankAccNum;
    }

    public ChoiceBox<String> getMaritalStatus() {
        return maritalStatus;
    }

    public TextField getNumberOfChildren() {
        return numberOfChildren;
    }

    public ToggleGroup getActiveStatus() {
        return activeStatus;
    }

    public RadioButton getActiveStatusYes() {
        return activeStatusYes;
    }

    public RadioButton getActiveStatusNo() {
        return activeStatusNo;
    }

    public ChoiceBox<String> getHighestEducationCompleted() {
        return highestEducationCompleted;
    }

    public Label getNotEnoughInfoLabel() {
        return notEnoughInfoLabel;
    }


    public DatePicker getTerminationDate() {
        return terminationDate;
    }
}
