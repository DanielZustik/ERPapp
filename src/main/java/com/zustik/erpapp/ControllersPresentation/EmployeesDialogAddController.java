package com.zustik.erpapp.ControllersPresentation;

import com.zustik.erpapp.ModelDataAccess.Employees;
import com.zustik.erpapp.ModelDataAccess.Invoices;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class EmployeesDialogAddController extends EmployeesDialogs{

    @Override
    public Employees processResult () {
        //try catch not necessary, type check in UserInputValidator. But when empty "" then giving it 0
        int numberOfChildrenInt = 0;
        if (!numberOfChildren.getText().equals("")) { //which means there is decimal value
            numberOfChildrenInt = Integer.parseInt(numberOfChildren.getText());
        }

        return new Employees(
        firstName.getText(),
        lastName.getText(),
        //try catch
        hireDate.getValue(),
        terminationDate.getValue(),
        contractType.getValue(),
        department.getText(),
        //try catch
        Double.parseDouble(grossWage.getText()),
        degree.getValue(),
        address.getText(),
        bankAccNum.getText(),
        maritalStatus.getValue(),
        //try catch
        numberOfChildrenInt,
        activeStatusYes.isSelected(),
        highestEducationCompleted.getValue()
        );
    }
}
