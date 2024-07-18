package com.zustik.erpapp.ControllersPresentation;

import com.zustik.erpapp.ModelDataAccess.Employees;
import com.zustik.erpapp.ModelDataAccess.Invoices;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class EmployeesDialogEditController extends EmployeesDialogs{
    //jde o overload metodu, ne override.
    public void populateTextFields(Employees item) {
        ID.setText(String.valueOf(item.getID()));
        firstName.setText(item.getFirstName());
        lastName.setText(item.getLastName());
        hireDate.setValue(item.getHireDate());
        terminationDate.setValue(item.getTerminationDate());
        contractType.setValue(item.getContractType());
        department.setText(item.getDepartment());
        grossWage.setText(String.valueOf(item.getGrossWage()));
        degree.setValue(item.getDegree());
        address.setText(item.getAddress());
        bankAccNum.setText(item.getBankAccNum());
        maritalStatus.setValue(item.getMaritalStatus());
        numberOfChildren.setText(String.valueOf(item.getNumberOfChildren()));
        if (item.isActiveStatus()) {
            activeStatusYes.setSelected(true);
        } else {
            activeStatusNo.setSelected(true);
        }
        highestEducationCompleted.setValue(item.getHighestEducationCompleted());

    }
    public Employees processResult (Employees item) {
        item.setFirstName(firstName.getText());
        item.setLastName(lastName.getText());
        //try catch
        item.setHireDate(hireDate.getValue());
        if (activeStatusNo.isSelected())
            item.setTerminationDate(terminationDate.getValue());
        else if (activeStatusYes.isSelected()) {
            item.setTerminationDate(null);
        }
        item.setContractType(contractType.getValue());
        item.setDepartment(department.getText());
        //try catch
        item.setGrossWage(Double.parseDouble(grossWage.getText()));
        item.setDegree(degree.getValue());
        item.setAddress(address.getText()); //projnumberint
        item.setBankAccNum(bankAccNum.getText()); //projnumberint
        item.setMaritalStatus(maritalStatus.getValue());
        //try catch
        item.setNumberOfChildren(Integer.parseInt(numberOfChildren.getText()));
        item.setActiveStatus(activeStatusYes.isSelected());
        System.out.println(highestEducationCompleted.getValue());
        item.setHighestEducationCompleted(highestEducationCompleted.getValue());

        return item;
    }
}
