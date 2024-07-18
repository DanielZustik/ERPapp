package com.zustik.erpapp.ModelDataAccess;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Invoices {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty invoiceNumberProp = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> dateOfIssueOrReceiptProp = new SimpleObjectProperty<>();
    private final DoubleProperty totalAmountProp = new SimpleDoubleProperty();
    private final DoubleProperty amountDueProp = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDate> dueDateProp = new SimpleObjectProperty<>();
    private final StringProperty recipientOrSupplierProp = new SimpleStringProperty();
    private final StringProperty subjectProp = new SimpleStringProperty();
    private final IntegerProperty projectNumberProp = new SimpleIntegerProperty();
    private final StringProperty projectManagerProp = new SimpleStringProperty();
    private final StringProperty noteProp = new SimpleStringProperty();


    public Invoices() {
        setInvoiceNumberProp(0);
        setDateOfIssueOrReceiptProp(LocalDate.of(2000,01,01));
        setTotalAmountProp(0);
        setAmountDueProp(0);
        setDueDateProp(LocalDate.of(2000,01,01));
        setRecipientOrSupplierProp("not defined");
        setSubjectProp("not defined");
        setProjectNumberProp(0);
        setProjectManagerProp("not defined");
        setNoteProp("not defined");
    }

    public Invoices(int invoiceNumber, LocalDate dateOfIssueOrReceiptProp, double totalAmountProp, double amountDueProp, LocalDate dueDateProp,
                    String recipientOrSupplierProp, String subjectProp) {
        setInvoiceNumberProp(invoiceNumber);
        setDateOfIssueOrReceiptProp(dateOfIssueOrReceiptProp);
        setTotalAmountProp(totalAmountProp);
        setAmountDueProp(amountDueProp);
        setDueDateProp(dueDateProp);
        setRecipientOrSupplierProp(recipientOrSupplierProp);
        setSubjectProp(subjectProp);
        setProjectNumberProp(0);
        setProjectManagerProp("not defined");
        setNoteProp("");
    }

    public Invoices(int invoiceNumberProp, LocalDate dateOfIssueOrReceiptProp, double totalAmountProp, double amountDueProp, LocalDate dueDateProp,
                    String recipientOrSupplierProp, String subjectProp, int projectNumber, String projectManager, String note) {
        setInvoiceNumberProp(invoiceNumberProp) ;
        setDateOfIssueOrReceiptProp(dateOfIssueOrReceiptProp);
        setTotalAmountProp(totalAmountProp);
        setAmountDueProp(amountDueProp);
        setDueDateProp(dueDateProp);
        setRecipientOrSupplierProp(recipientOrSupplierProp);
        setSubjectProp(subjectProp);
        setProjectNumberProp(projectNumber);
        setProjectManagerProp(projectManager);
        setNoteProp(note);
    }

    public Invoices(int id, int invoiceNumberProp, LocalDate dateOfIssueOrReceiptProp, double totalAmountProp, double amountDueProp, LocalDate dueDateProp,
                    String recipientOrSupplierProp, String subjectProp, int projectNumber, String projectManager, String note) {
        setId(id);
        setInvoiceNumberProp(invoiceNumberProp);
        setDateOfIssueOrReceiptProp(dateOfIssueOrReceiptProp);
        setTotalAmountProp(totalAmountProp);
        setAmountDueProp(amountDueProp);
        setDueDateProp(dueDateProp);
        setRecipientOrSupplierProp(recipientOrSupplierProp);
        setSubjectProp(subjectProp);
        setProjectNumberProp(projectNumber);
        setProjectManagerProp(projectManager);
        setNoteProp(note);
    }


    public LocalDate getDateOfIssueOrReceiptProp() {
        return dateOfIssueOrReceiptProp.get();
    }

    public void setDateOfIssueOrReceiptProp(LocalDate value) {
        dateOfIssueOrReceiptProp.set(value);
    }

    public ObjectProperty<LocalDate> dateOfIssueOrReceiptPropProperty() {
        return dateOfIssueOrReceiptProp;
    }

    public double getTotalAmountProp() {
        return totalAmountProp.get();
    }

    public void setTotalAmountProp(double value) {
        totalAmountProp.set(value);
    }

    public DoubleProperty totalAmountPropProperty() {
        return totalAmountProp;
    }

    public double getAmountDueProp() {
        return amountDueProp.get();
    }

    public void setAmountDueProp(double value) {
        amountDueProp.set(value);
    }

    public DoubleProperty amountDuePropProperty() {
        return amountDueProp;
    }

    public LocalDate getDueDateProp() {
        return dueDateProp.get();
    }

    public void setDueDateProp(LocalDate value) {
        dueDateProp.set(value);
    }

    public ObjectProperty<LocalDate> dueDatePropProperty() {
        return dueDateProp;
    }

    public String getRecipientOrSupplierProp() {
        return recipientOrSupplierProp.get();
    }

    public void setRecipientOrSupplierProp(String value) {
        recipientOrSupplierProp.set(value);
    }

    public StringProperty recipientOrSupplierPropProperty() {
        return recipientOrSupplierProp;
    }

    public String getSubjectProp() {
        return subjectProp.get();
    }

    public void setSubjectProp(String value) {
        subjectProp.set(value);
    }

    public StringProperty subjectPropProperty() {
        return subjectProp;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getProjectNumberProp() {
        return projectNumberProp.get();
    }

    public IntegerProperty projectNumberPropProperty() {
        return projectNumberProp;
    }

    public void setProjectNumberProp(int projectNumberProp) {
        this.projectNumberProp.set(projectNumberProp);
    }

    public String getProjectManagerProp() {
        return projectManagerProp.get();
    }

    public StringProperty projectManagerPropProperty() {
        return projectManagerProp;
    }

    public void setProjectManagerProp(String projectManagerProp) {
        this.projectManagerProp.set(projectManagerProp);
    }

    public String getNoteProp() {
        return noteProp.get();
    }

    public StringProperty notePropProperty() {
        return noteProp;
    }

    public void setNoteProp(String noteProp) {
        this.noteProp.set(noteProp);
    }

    public int getInvoiceNumberProp() {
        return invoiceNumberProp.get();
    }

    public IntegerProperty invoiceNumberPropProperty() {
        return invoiceNumberProp;
    }

    public void setInvoiceNumberProp(int invoiceNumberProp) {
        this.invoiceNumberProp.set(invoiceNumberProp);
    }

    @Override
    public String toString() {
        return "Items{" +
                "dateOfIssue=" + dateOfIssueOrReceiptProp.get() +
                ", totalAmount=" + totalAmountProp.get() +
                ", amountDue=" + amountDueProp.get() +
                ", dueDate=" + dueDateProp.get() +
                ", recipient='" + recipientOrSupplierProp.get() + '\'' +
                '}';
    }
}