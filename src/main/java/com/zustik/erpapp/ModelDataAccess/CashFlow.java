package com.zustik.erpapp.ModelDataAccess;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CashFlow {
    private LocalDate date;
    private double issuedInvoicesIncomes;
    private double otherIncomes;
    private double receivedInvoicesExpenses;
    private double personalExpenses;
    private double otherExpenses;
    private double sum;
    private double balance;

    public static List<CashFlow> CFRecords = new ArrayList<>();


    public CashFlow(LocalDate date, double issuedInvoicesIncomes, double otherIncomes, double receivedInvoicesExpenses,
                    double personalExpenses, double otherExpenses) {
        this.date = date;
        this.issuedInvoicesIncomes = issuedInvoicesIncomes;
        this.otherIncomes = otherIncomes;
        this.receivedInvoicesExpenses = receivedInvoicesExpenses;
        this.personalExpenses = personalExpenses;
        this.otherExpenses = otherExpenses;
        this.sum = calculateSum();
        this.balance = 0.0;
    }

    // Calculate sum based on incomes and expenses
    private double calculateSum() {
        return issuedInvoicesIncomes + otherIncomes - receivedInvoicesExpenses - personalExpenses - otherExpenses;
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public double getIssuedInvoicesIncomes() {
        return issuedInvoicesIncomes;
    }

    public double getOtherIncomes() {
        return otherIncomes;
    }

    public double getReceivedInvoicesExpenses() {
        return receivedInvoicesExpenses;
    }

    public double getPersonalExpenses() {
        return personalExpenses;
    }

    public double getOtherExpenses() {
        return otherExpenses;
    }

    public double getSum() {
        return sum;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setIssuedInvoicesIncomes(double issuedInvoicesIncomes) {
        this.issuedInvoicesIncomes = issuedInvoicesIncomes;
    }

    public void setOtherIncomes(double otherIncomes) {
        this.otherIncomes = otherIncomes;
    }

    public void setReceivedInvoicesExpenses(double receivedInvoicesExpenses) {
        this.receivedInvoicesExpenses = receivedInvoicesExpenses;
    }

    public void setPersonalExpenses(double personalExpenses) {
        this.personalExpenses = personalExpenses;
    }

    public void setOtherExpenses(double otherExpenses) {
        this.otherExpenses = otherExpenses;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
