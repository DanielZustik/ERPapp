package com.zustik.erpapp.ControllersPresentation;

import com.zustik.erpapp.ModelDataAccess.Invoices;

public class InvoicesDialogEditController extends InvoicesDialogs {

    public void populateTextFields(Invoices item) {
        invoiceNumberField.setText(String.valueOf(item.getInvoiceNumberProp()));
        recipientField.setText(item.getRecipientOrSupplierProp());
        subjectField.setText(item.getSubjectProp());
        totalAmountField.setText(String.valueOf(item.getTotalAmountProp()));
        dueAmountField.setText(String.valueOf(item.getAmountDueProp()));
        projectNumberField.setText(String.valueOf(item.getProjectNumberProp()));
        isssueDateField.setValue(item.getDateOfIssueOrReceiptProp());
        dueDatePicker.setValue(item.getDueDateProp());
        projectManagerField.setText(item.getProjectManagerProp());
        noteField.setText(item.getNoteProp());
    }

    @Override
    public InvoiceDataContainer processResult (Invoices item) {
        InvoiceDataContainer invoiceDataContainer = super.processResult(item);
        item.setInvoiceNumberProp(invoiceDataContainer.getInvoiceNumber());
        item.setRecipientOrSupplierProp(invoiceDataContainer.getRecipient());
        item.setSubjectProp(invoiceDataContainer.getSubject());
        item.setTotalAmountProp(invoiceDataContainer.getTotalAmount());
        item.setAmountDueProp(invoiceDataContainer.getDueAmount());
        item.setDateOfIssueOrReceiptProp(invoiceDataContainer.getInvoiceDate());
        item.setDueDateProp(invoiceDataContainer.getDueDate());
        item.setProjectNumberProp(invoiceDataContainer.getProjectNumber()); //projnumberint
        item.setProjectManagerProp(invoiceDataContainer.getProjectManager()); //projnumberint
        item.setNoteProp(invoiceDataContainer.getNote());

        return invoiceDataContainer;

    }

}
