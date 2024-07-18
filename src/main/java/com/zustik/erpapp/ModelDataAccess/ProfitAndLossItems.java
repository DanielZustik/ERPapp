package com.zustik.erpapp.ModelDataAccess;

import com.zustik.erpapp.DBAccessDataAccess.DBdataForFinInfo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ProfitAndLossItems {
    private final Map<String, Double> map;

    public ProfitAndLossItems() {
        double revenueServices = revenue();
        double revenueGoods = 0;
        double workInProgress = 57400.0;
        double costServEng = energyServices();

        double personalCosts = 0;
        Map<Integer, Double> costsMap = personalCosts(LocalDate.of(2024, 1, 1));
        for (Map.Entry<Integer, Double> entry : costsMap.entrySet()) {
            personalCosts += entry.getValue();
        }

        double depreciation = 200000;
        double finResult = 0;

        double ebit = revenueServices + revenueGoods + workInProgress - costServEng - personalCosts
                - depreciation + finResult;

        map = new LinkedHashMap<>();
        map.put("Revenue from services", revenueServices);
        map.put("Revenue from sale of goods", revenueGoods);
        map.put("Work in progress", workInProgress);
        map.put("Costs of energy and services", costServEng);
        map.put("Personnel costs", personalCosts);
        map.put("Deprecation", depreciation);
        map.put("Financial result", finResult);
        map.put("EBIT", ebit);
    }

    private Map<Integer, Double> personalCostsFromHireDateOrChoosenDate(LocalDate date) {
        Map<Integer, Double> personalCostsByID = new LinkedHashMap<>();

        // Integer = ID, Double = grossWage, LocalDate = hireDate
        for (Map.Entry<Integer, Map<Double, LocalDate>> entry :
                DBdataForFinInfo.querySelectPersonalCostsByID().entrySet()) {
            //nyni neni nutny cyklus kazdy empl ma jen jeden udaj grossWageXhireDate
            for (Map.Entry<Double, LocalDate> entryInner: entry.getValue().entrySet()) {
                Double grossWage = entryInner.getKey();
                LocalDate hireDate = entryInner.getValue();
                LocalDate dateToConsider;
                dateToConsider = date.isBefore(hireDate) ? hireDate : date;
                //prumerny mesic ma 30,4 dnu vc svatku vseho. zjednoduseni
                long workedForDays = ChronoUnit.DAYS.between(dateToConsider, LocalDate.now());
                double aproxCosts = grossWage * 1.34 * workedForDays / 30.4;

                Integer ID = entry.getKey();
                personalCostsByID.put(ID, aproxCosts);
            }
        }
        return personalCostsByID;
    }
    private Map<Integer, Double> personalCosts(LocalDate costsFromDate) {
        Map<Integer, Double> personalCostsByID = new LinkedHashMap<>();

        for (Map.Entry<Integer, Map<Double, LocalDate>> entry :
                DBdataForFinInfo.querySelectPersonalCostsByID().entrySet()) {
            for (Map.Entry<Double, LocalDate> entryInner: entry.getValue().entrySet()) {
                Double grossWage = entryInner.getKey();
                //prumerny mesic ma 30,4 dnu vc svatku vseho. zjednoduseni
                long workedForXDays = ChronoUnit.DAYS.between(costsFromDate, LocalDate.now());
                double aproxCosts = grossWage * 1.34 * workedForXDays / 30.4;
                System.out.println(aproxCosts);
                Integer ID = entry.getKey();
                personalCostsByID.put(ID, aproxCosts);
            }
        }
        return personalCostsByID;
    }


    public Set<Map.Entry<String, Double>> getMapEntries() {
        return map.entrySet();
    }

    private double revenue () {
        return DBdataForFinInfo.querySelectRevSum();
    }
    private double energyServices() {
        return DBdataForFinInfo.querySelectServEnergSum();
    }

}
