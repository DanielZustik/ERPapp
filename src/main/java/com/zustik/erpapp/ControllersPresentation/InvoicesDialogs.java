package com.zustik.erpapp.ControllersPresentation;

import com.zustik.erpapp.ModelDataAccess.Invoices;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public abstract class InvoicesDialogs {
    @FXML
    protected TextField invoiceNumberField;
    @FXML
    protected TextField recipientField;
    @FXML
    protected TextField subjectField;
    @FXML
    protected TextField totalAmountField;
    @FXML
    protected TextField dueAmountField;
    @FXML
    protected TextField projectNumberField;
    @FXML
    protected DatePicker isssueDateField;
    @FXML
    protected DatePicker dueDatePicker;
    @FXML
    protected TextField projectManagerField;
    @FXML
    protected TextArea noteField;
    @FXML
    private Label notEnoughInfoLabel;

    //inner class container for returning multiple datatypes
    public class InvoiceDataContainer {
        public int invoiceNumber;
        public LocalDate invoiceDate;
        public double totalAmount;
        public double dueAmount;
        public LocalDate dueDate;
        public String recipient;
        public String subject;
        public int projectNumber;
        public String projectManager;
        public String note;
        public Invoices item;

        public InvoiceDataContainer(int invoiceNumber, LocalDate invoiceDate, double totalAmount, double dueAmount,
                                    LocalDate dueDate, String recipient, String subject, int projectNumber,
                                    String projectManager, String note, Invoices item) {
            this.invoiceNumber = invoiceNumber;
            this.invoiceDate = invoiceDate;
            this.totalAmount = totalAmount;
            this.dueAmount = dueAmount;
            this.dueDate = dueDate;
            this.recipient = recipient;
            this.subject = subject;
            this.projectNumber = projectNumber;
            this.projectManager = projectManager;
            this.note = note;
            this.item = item;
        }

        public Invoices getItem() {
            return item;
        }

        public int getInvoiceNumber() {
            return invoiceNumber;
        }

        public LocalDate getInvoiceDate() {
            return invoiceDate;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public double getDueAmount() {
            return dueAmount;
        }

        public LocalDate getDueDate() {
            return dueDate;
        }

        public String getRecipient() {
            return recipient;
        }

        public String getSubject() {
            return subject;
        }

        public int getProjectNumber() {
            return projectNumber;
        }

        public String getProjectManager() {
            return projectManager;
        }

        public String getNote() {
            return note;
        }
    }

    protected InvoiceDataContainer processResult (Invoices item) {
        //not necessary to try catch because its done beforehand via Validator class
        String invoiceNumber = invoiceNumberField.getText().trim();
        LocalDate invoiceDate = isssueDateField.getValue();
        String totalAmount = totalAmountField.getText().trim();
        String dueAmount = dueAmountField.getText().trim();
        LocalDate dueDate = dueDatePicker.getValue();
        String recipient = recipientField.getText().trim();
        String subject = subjectField.getText().trim();
        String projectNumber = projectNumberField.getText().trim();
        String projectManager = projectManagerField.getText().trim();
        String note = noteField.getText().trim();

        int invoiceNumberInt = 0;
        double totalAmountDouble = 0.0;
        double dueAmountDouble = 0.0;
        int projectNumberInt = 0;

        invoiceNumberInt = Integer.parseInt(invoiceNumber);
        totalAmountDouble = Double.parseDouble(totalAmount);
        dueAmountDouble = Double.parseDouble(dueAmount);
        try {
            if (!projectNumber.isEmpty() && projectNumber.matches("\\d+")) { // Ensure it's all digits
                projectNumberInt = Integer.parseInt(projectNumber);
            } else if (projectNumber.isEmpty()) {
                //fine it can be null in db
            } else {
                //fine delt with in validator if there are other then digits
            }
        } catch (NumberFormatException e) {
//            System.out.println("Invalid format for project number.");
        }

        Invoices itemCreated = new Invoices(invoiceNumberInt, invoiceDate, totalAmountDouble, dueAmountDouble, dueDate,
                recipient, subject, projectNumberInt, projectManager, note);

        return new InvoiceDataContainer(invoiceNumberInt, invoiceDate, totalAmountDouble, dueAmountDouble, dueDate, //i have created container because did nt want to create new Items object and wanted to edit the existing one... so i needed to store info somewhere
                recipient, subject, projectNumberInt, projectManager, note, itemCreated);
    }

    public TextField getInvoiceNumberField() {
        return invoiceNumberField;
    }

    public TextField getRecipientField() {
        return recipientField;
    }

    public TextField getSubjectField() {
        return subjectField;
    }

    public TextField getTotalAmountField() {
        return totalAmountField;
    }

    public TextField getDueAmountField() {
        return dueAmountField;
    }

    public TextField getProjectNumberField() {
        return projectNumberField;
    }

    public DatePicker getIsssueDateField() {
        return isssueDateField;
    }

    public DatePicker getDueDatePicker() {
        return dueDatePicker;
    }

    public TextField getProjectManagerField() {
        return projectManagerField;
    }

    public TextArea getNoteField() {
        return noteField;
    }

    public Label getNotEnoughInfoLabel() {
        return notEnoughInfoLabel;
    }

    public void setNotEnoughInfoLabel(Label notEnoughInfoLabel) {
        this.notEnoughInfoLabel = notEnoughInfoLabel;
    }
}
