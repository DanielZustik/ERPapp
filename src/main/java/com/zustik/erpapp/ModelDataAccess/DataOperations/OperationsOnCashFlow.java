package com.zustik.erpapp.ModelDataAccess.DataOperations;

import com.zustik.erpapp.DBAccessDataAccess.DBdataForIssuedInv;
import com.zustik.erpapp.DBAccessDataAccess.DBdataForReceivedInv;
import com.zustik.erpapp.ModelDataAccess.AppSettings;
import com.zustik.erpapp.ModelDataAccess.CashFlow;
import com.zustik.erpapp.ModelDataAccess.Invoices;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OperationsOnCashFlow {
    public static List<CashFlow> computeCF() {
        DBdataForIssuedInv dbDataIssInv = new DBdataForIssuedInv();
        ObservableList<Invoices> issuedInv = dbDataIssInv.querySelectWithoutParam();
        DBdataForReceivedInv dbDataRecInv = new DBdataForReceivedInv();
        ObservableList<Invoices> receivedInv = dbDataRecInv.querySelectWithoutParam();
        Map<LocalDate, Double> persExpThroughYear = OperationsOnEmployees.personalExpansesThroughYearAtConcreteDate();

        Year year2024 = Year.of(2024);
        LocalDate startingDate = LocalDate.of(2024,1,1);
        LocalDate date = startingDate;

        //listOfRecords.add(new R(LocalDate.of(2024,1,1), ))

        Double issuedInvIncomeAtGivenDay = 0.0;
        Double receivedInvIncomeAtGivenDay = 0.0;
        Double personalExp = 0.0;

        for (int i = 1; i <= AppSettings.year.length(); i++) {
            for (Invoices invoice : issuedInv) {
                if (invoice.getDueDateProp().isEqual(date))
                    issuedInvIncomeAtGivenDay += invoice.getTotalAmountProp();
            }
            for (Invoices invoice : receivedInv) {
                if (invoice.getDueDateProp().isEqual(date))
                    receivedInvIncomeAtGivenDay += invoice.getTotalAmountProp();
            }
            for (Map.Entry<LocalDate, Double> e : persExpThroughYear.entrySet()) {
                if (e.getKey().isEqual(date)) personalExp = e.getValue();
            }
            CashFlow.CFRecords.add(new CashFlow(date, issuedInvIncomeAtGivenDay, 0, receivedInvIncomeAtGivenDay,
                    personalExp, 0));

            //reset for next day
            receivedInvIncomeAtGivenDay = 0.0;
            issuedInvIncomeAtGivenDay = 0.0;
            personalExp = 0.0;
            date = date.plusDays(1);

        }
        return  CashFlow.CFRecords;
    }

    public static List<CashFlow> computeCFWithoutEmptyDays() {
        //List<CashFlow> cf = computeCF();
        List<CashFlow> cfWithoutEmptyDays = new ArrayList<>();
        for (CashFlow c : CashFlow.CFRecords) {
            if (!(c.getIssuedInvoicesIncomes() == 0 && c.getOtherIncomes() == 0 &&
                    c.getReceivedInvoicesExpenses() == 0 && c.getPersonalExpenses()== 0 &&
                    c.getOtherExpenses()== 0 && c.getSum() == 0)) {
                cfWithoutEmptyDays.add(c);
            }

        }
        return cfWithoutEmptyDays;
    }

        public static void computeAddedSums (List<CashFlow> listOfCFRecords) {
        Double firstSumOfDay = listOfCFRecords.get(0).getSum() + AppSettings.startingMoney;
        CashFlow.CFRecords.getFirst().setBalance(firstSumOfDay);

        for (int i = 1; i < AppSettings.year.length(); i++) {
            Double previousDayAddedSum = CashFlow.CFRecords.get(i - 1).getBalance();
            Double sumOfDay = listOfCFRecords.get(i).getSum();
            CashFlow.CFRecords.get(i).setBalance(previousDayAddedSum + sumOfDay);
        }
    }
}
