package com.zustik.erpapp.ModelDataAccess;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Employees {
    public static final double GrossWageCoef = 1.34;
    public static final Integer Payday = 20;
    private SimpleIntegerProperty ID;
    //photo
    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleObjectProperty<LocalDate> hireDate;
    private SimpleObjectProperty<LocalDate> terminationDate;
    private SimpleStringProperty contractType; //enum in SQL
    private SimpleStringProperty department;
    private SimpleDoubleProperty grossWage;
    private SimpleStringProperty degree; //enum in sql
    private SimpleStringProperty address;
    private SimpleStringProperty bankAccNum;
    private SimpleStringProperty maritalStatus; //enum in sql
    private SimpleIntegerProperty numberOfChildren;
    private SimpleBooleanProperty activeStatus;
    private SimpleStringProperty highestEducationCompleted; //enum in sql

    //with ID for selecting all items in tableview deaaultly. so its not letting user setting ID, its set based on ID from DB
    public Employees(Integer ID, String firstName, String lastName, LocalDate hireDate,
                     String contractType, String department, Double grossWage,
                     String degree, String address, String bankAccNum, String maritalStatus,
                     Integer numberOfChildren, Boolean activeStatus, String highestEducationCompleted) {
        this.ID = new SimpleIntegerProperty(ID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
        this.terminationDate = new SimpleObjectProperty<>(); //so its not null
        this.terminationDate = new SimpleObjectProperty<>();
        this.contractType = new SimpleStringProperty(contractType);
        this.department = new SimpleStringProperty(department);
        this.grossWage = new SimpleDoubleProperty(grossWage);
        this.degree = new SimpleStringProperty(degree);
        this.address = new SimpleStringProperty(address);
        this.bankAccNum = new SimpleStringProperty(bankAccNum);
        this.maritalStatus = new SimpleStringProperty(maritalStatus);
        this.numberOfChildren = new SimpleIntegerProperty(numberOfChildren);
        this.activeStatus = new SimpleBooleanProperty(activeStatus);
        this.highestEducationCompleted = new SimpleStringProperty(highestEducationCompleted);
    }

    //konstruktor pouze pro pripad ze by se pridaval empl s rovnou uvedenym termination date.. nejaka retrospektiva
    public Employees(Integer ID, String firstName, String lastName, LocalDate hireDate, LocalDate terminationDate,
                     String contractType, String department, Double grossWage,
                     String degree, String address, String bankAccNum, String maritalStatus,
                     Integer numberOfChildren, Boolean activeStatus, String highestEducationCompleted) {
        this.ID = new SimpleIntegerProperty(ID);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
        this.terminationDate = new SimpleObjectProperty<>(terminationDate);
        this.contractType = new SimpleStringProperty(contractType);
        this.department = new SimpleStringProperty(department);
        this.grossWage = new SimpleDoubleProperty(grossWage);
        this.degree = new SimpleStringProperty(degree);
        this.address = new SimpleStringProperty(address);
        this.bankAccNum = new SimpleStringProperty(bankAccNum);
        this.maritalStatus = new SimpleStringProperty(maritalStatus);
        this.numberOfChildren = new SimpleIntegerProperty(numberOfChildren);
        this.activeStatus = new SimpleBooleanProperty(activeStatus);
        this.highestEducationCompleted = new SimpleStringProperty(highestEducationCompleted);
    }
    //without ID for AddDialog
    public Employees(String firstName, String lastName, LocalDate hireDate, LocalDate terminationDate,
                     String contractType, String department, Double grossWage,
                     String degree, String address, String bankAccNum, String maritalStatus,
                     Integer numberOfChildren, Boolean activeStatus, String highestEducationCompleted) {
        this.ID = new SimpleIntegerProperty();
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.hireDate = new SimpleObjectProperty<>(hireDate);
        this.terminationDate = new SimpleObjectProperty<>(terminationDate);
        this.contractType = new SimpleStringProperty(contractType);
        this.department = new SimpleStringProperty(department);
        this.grossWage = new SimpleDoubleProperty(grossWage);
        this.degree = new SimpleStringProperty(degree);
        this.address = new SimpleStringProperty(address);
        this.bankAccNum = new SimpleStringProperty(bankAccNum);
        this.maritalStatus = new SimpleStringProperty(maritalStatus);
        this.numberOfChildren = new SimpleIntegerProperty(numberOfChildren);
        this.activeStatus = new SimpleBooleanProperty(activeStatus);
        this.highestEducationCompleted = new SimpleStringProperty(highestEducationCompleted);
    }


    public int getID() {
        return ID.get();
    }

    public SimpleIntegerProperty IDProperty() {
        return ID;
    }

    public void setID(int ID) {
        this.ID.set(ID);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public LocalDate getHireDate() {
        return hireDate.get();
    }

    public SimpleObjectProperty<LocalDate> hireDateProperty() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate.set(hireDate);
    }

    public String getContractType() {
        return contractType.get();
    }

    public SimpleStringProperty contractTypeProperty() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType.set(contractType);
    }

    public String getDepartment() {
        return department.get();
    }

    public SimpleStringProperty departmentProperty() {
        return department;
    }

    public void setDepartment(String department) {
        this.department.set(department);
    }

    public double getGrossWage() {
        return grossWage.get();
    }

    public SimpleDoubleProperty grossWageProperty() {
        return grossWage;
    }

    public void setGrossWage(double grossWage) {
        this.grossWage.set(grossWage);
    }

    public String getDegree() {
        return degree.get();
    }

    public SimpleStringProperty degreeProperty() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree.set(degree);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getBankAccNum() {
        return bankAccNum.get();
    }

    public SimpleStringProperty bankAccNumProperty() {
        return bankAccNum;
    }

    public void setBankAccNum(String bankAccNum) {
        this.bankAccNum.set(bankAccNum);
    }

    public String getMaritalStatus() {
        return maritalStatus.get();
    }

    public SimpleStringProperty maritalStatusProperty() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus.set(maritalStatus);
    }

    public int getNumberOfChildren() {
        return numberOfChildren.get();
    }

    public SimpleIntegerProperty numberOfChildrenProperty() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren.set(numberOfChildren);
    }

    public boolean isActiveStatus() {
        return activeStatus.get();
    }

    public SimpleBooleanProperty activeStatusProperty() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus.set(activeStatus);
    }

    public String getHighestEducationCompleted() {
        return highestEducationCompleted.get();
    }

    public SimpleStringProperty highestEducationCompletedProperty() {
        return highestEducationCompleted;
    }

    public void setHighestEducationCompleted(String highestEducationCompleted) {
        this.highestEducationCompleted.set(highestEducationCompleted);
    }

        public LocalDate getTerminationDate() {
            return terminationDate.get();
        }

    public SimpleObjectProperty<LocalDate> terminationDateProperty() {
        return terminationDate;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate.set(terminationDate);
    }
}