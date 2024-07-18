package com.zustik.erpapp.ModelDataAccess.DataOperations;

import com.zustik.erpapp.DBAccessDataAccess.DBdataForEmployees;
import com.zustik.erpapp.ModelDataAccess.Employees;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class OperationsOnEmployees {
    private static DBdataForEmployees dBdataForEmployees = new DBdataForEmployees();
    private static Map<Integer, Map<LocalDate, Long>> howManydaysWasEmplActiveInMonth;
    private static ObservableList<Employees> wholeEmplData;

    public static Map<LocalDate, Double> personalExpansesThroughYearAtConcreteDate() {
        howManyDaysEmplActiveInConcreteMonth();

        Map<LocalDate, Double> personalExpensesThroughYearAtConcreteDate = new LinkedHashMap<>();
        Map <Integer, Map<LocalDate, Double>> map = calculatingPersonalExpensesByConcreteEmpl();
        LocalDate month;
        Double grossWage;
        for (Map.Entry<Integer, Map<LocalDate, Double>> emlpWageInMonths : map.entrySet()) {
            for ( Map.Entry<LocalDate, Double> i : emlpWageInMonths.getValue().entrySet()) {
                month = i.getKey();
                grossWage = i.getValue();
                LocalDate adjustedDate = month.plusDays(Employees.Payday); //so it doesnt push it over 30/31
                personalExpensesThroughYearAtConcreteDate.merge(adjustedDate,
                        grossWage, Double::sum);
            }

        }
        return personalExpensesThroughYearAtConcreteDate;
    }

    public static Map <Integer, Map<LocalDate, Double>> calculatingPersonalExpensesByConcreteEmpl () {
        Map <Integer, Map<LocalDate, Double>> personalExpensesByConcreteEmpl = new HashMap<>();
        Map<LocalDate, Long> innerMap;
        Map<LocalDate, Double> innerMap2;
        Double wage;

        for (Map.Entry<Integer, Map<LocalDate, Long>> i : howManydaysWasEmplActiveInMonth.entrySet()) {
            for (Employees e : wholeEmplData) {
                if (i.getKey() == e.getID()) {
                    innerMap = i.getValue(); // mapa pro empl pocet mesicu a hodin
                    innerMap2 = new LinkedHashMap<>(); //je dulezite zde vytvorit novou isntanci. jinak bych pouzival stale tu samou a jen prepisoval, a tak by vsichni meli stejna data posledniho emlp.
                    for (Map.Entry<LocalDate, Long> entries : innerMap.entrySet()) {
                        wage = e.getGrossWage() * entries.getValue() / entries.getKey().lengthOfMonth() * Employees.GrossWageCoef;
                        innerMap2.put(entries.getKey(), wage);
                    }
                    personalExpensesByConcreteEmpl.put(i.getKey(), innerMap2);
                }
            }
        }
        System.out.println(personalExpensesByConcreteEmpl);
        return personalExpensesByConcreteEmpl;
    }


    private static void howManyDaysEmplActiveInConcreteMonth() {
        howManydaysWasEmplActiveInMonth = new HashMap<>();
        wholeEmplData = dBdataForEmployees.querySelectWithoutParam();

        //je li date po/equal hire date a pred termination date. Je li termination date minus date vice nez dnu v mesici date, pak pouze tento reozdil, jinak plny pocet dnu.
        Long daysBetwenTermDateAndDate; //right now null varialbe for adrress to long obj created later. When creating new obj throug Chronos.. its address/reference is stored here and afterwards putted in map. In next cycle there is another address... so i am not putting in map same adresses to same object.
        LocalDate date;
        LocalDate termDate;
        LocalDate hireDate;
        Map<LocalDate, Long> innerMapDate;
        for (Employees employee : wholeEmplData) {
            innerMapDate = new LinkedHashMap<>(); //have to created new object for every empl, because later i m putting it into map.
            innerMapDate.put(LocalDate.of(2024,1,1),0L);
            innerMapDate.put(LocalDate.of(2024,2,1),0L);
            innerMapDate.put(LocalDate.of(2024,3,1),0L);
            innerMapDate.put(LocalDate.of(2024,4,1),0L);
            innerMapDate.put(LocalDate.of(2024,5,1),0L);
            innerMapDate.put(LocalDate.of(2024,6,1),0L);
            innerMapDate.put(LocalDate.of(2024,7,1),0L);
            innerMapDate.put(LocalDate.of(2024,8,1),0L);
            innerMapDate.put(LocalDate.of(2024,9,1),0L);
            innerMapDate.put(LocalDate.of(2024,10,1),0L);
            innerMapDate.put(LocalDate.of(2024,11,1),0L);
            innerMapDate.put(LocalDate.of(2024,12,1),0L);

            for (Map.Entry<LocalDate, Long> monthAndDaysWorked : innerMapDate.entrySet()) {
                date = monthAndDaysWorked.getKey();
                hireDate = employee.getHireDate();
                termDate = employee.getTerminationDate();

                if (termDate != null) {
                    if ((date.isAfter(hireDate) || date.isEqual(hireDate)) && (date.isBefore(termDate) || date.isEqual(termDate))) {
                        daysBetwenTermDateAndDate = ChronoUnit.DAYS.between(date, termDate);
                        if (daysBetwenTermDateAndDate <= date.lengthOfMonth()) {
                            innerMapDate.put(date, daysBetwenTermDateAndDate); //putting again same date easier then getting the date and putting only key
                        } else {
                            innerMapDate.put(date, Long.valueOf(date.lengthOfMonth())); //putting again same date easier then getting the date and putting only key
                        }
                    } else {
                        innerMapDate.put(date, 0L);
                    }
                } else {
                    if (date.isAfter(hireDate) || date.isEqual(hireDate)) {
                        System.out.println("--------");
                        System.out.println(date);
                        System.out.println(hireDate);
                        System.out.println("--------");
                        innerMapDate.put(date, Long.valueOf(date.lengthOfMonth()));
                    } else {
                        innerMapDate.put(date, 0L);
                    }
                }
            }
            System.out.println(innerMapDate);
            howManydaysWasEmplActiveInMonth.put(employee.getID(), innerMapDate);
        }

        for (Map.Entry<Integer, Map<LocalDate, Long>> integerMapEntry : howManydaysWasEmplActiveInMonth.entrySet()) {
            for (Map.Entry<LocalDate, Long> a : integerMapEntry.getValue().entrySet()) {
                System.out.println("ID " + integerMapEntry.getKey() + ", month: " +
                        a.getKey() + ", for days: " + a.getValue());
            }
        }
    }
}
