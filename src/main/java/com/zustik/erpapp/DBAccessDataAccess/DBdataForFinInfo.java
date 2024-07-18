package com.zustik.erpapp.DBAccessDataAccess;

import com.zustik.erpapp.DBConnectionDataAccess.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class DBdataForFinInfo {
    public static class EmplCostsByName{
        private Double grossWage;
        private LocalDate hireDate;
        private LocalDate terminationDate;

        public EmplCostsByName(Double grossWage, LocalDate hireDate, LocalDate terminationDate) {
            this.grossWage = grossWage;
            this.hireDate = hireDate;
            this.terminationDate = terminationDate;

        }

        public Double getGrossWage() {
            return grossWage;
        }

        public void setGrossWage(Double grossWage) {
            this.grossWage = grossWage;
        }

        public LocalDate getHireDate() {
            return hireDate;
        }

        public void setHireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
        }

        public LocalDate getTerminationDate() {
            return terminationDate;
        }

        public void setTerminationDate(LocalDate terminationDate) {
            this.terminationDate = terminationDate;
        }
    }

    public static Map<String, Integer> querySelectRevByClient() {
        String query = "SELECT recipientProp, SUM(totalAmountProp) FROM issuedInvoices group by recipientProp order by SUM(totalAmountProp) desc;";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = ps.executeQuery();
            return prepareData(resultSet, "recipientProp");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Integer> querySelectCostsBySuppliers() {
        String query = "SELECT supplierProp, SUM(totalAmountProp) FROM receivedInvoices group by supplierProp order by SUM(totalAmountProp) desc;";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = ps.executeQuery();
            return prepareData(resultSet, "supplierProp");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static double querySelectRevSum() {
        String query = "SELECT SUM(totalAmountProp) FROM issuedInvoices;";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getDouble(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static double querySelectServEnergSum() {
        String query = "SELECT SUM(totalAmountProp) FROM receivedInvoices;";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            return resultSet.getDouble(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static  Map<Integer, Map<Double, LocalDate>> querySelectPersonalCostsByID() {
        String query = "SELECT ID, grossWage, hireDate FROM employees;";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = ps.executeQuery();
            return prepareDataEmplCosts(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static  Map<String, EmplCostsByName> querySelectPersonalCostsByName() {
        String query = "SELECT firstName, lastName, grossWage, hireDate, terminationDate FROM employees;";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = ps.executeQuery();
            return prepareDataEmplCostsHelper(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public ObservableList<InvoicesModel> querySelectWithParam(String whereColumn, LocalDate whereFrom, LocalDate whereTo) {
//        String query = "SELECT * from erp_items.issuedinvoices WHERE " + whereColumn + " BETWEEN ? AND ?";
//        Connection conn = DBConnection.getInstance().getConnection();
//        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//            ps.setDate(1, Date.valueOf(whereFrom));
//            ps.setDate(2, Date.valueOf(whereTo));
//            ResultSet resultSet = ps.executeQuery();
//            return prepareData(resultSet);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


    private static Map<String, Integer> prepareData (ResultSet resultSet, String column) {
        Map<String, Integer> data = new LinkedHashMap<>();
        try {
            while (resultSet.next()) {
                String columnLabel = resultSet.getString(column);
                int totalAmount = resultSet.getInt("SUM(totalAmountProp)");
                data.put(columnLabel, totalAmount);
            }
        } catch (SQLException e) {
            System.err.println("Error processing data from the database: " + e.getMessage());
            throw new RuntimeException("Error retrieving data from the database", e);
        }
        return data;
    }

    private static  Map<Integer, Map<Double, LocalDate>>  prepareDataEmplCosts (ResultSet resultSet) {
        Map<Integer, Map<Double, LocalDate>> data = new LinkedHashMap<>();
        try {
            while (resultSet.next()) {
                Integer ID = resultSet.getInt("ID");
                Double grossWAge = resultSet.getDouble("grossWage");
                LocalDate hireDate = resultSet.getDate("hireDate").toLocalDate();
                Map<Double, LocalDate> innerMapData = new LinkedHashMap<>();
                innerMapData.put(grossWAge, hireDate);
                data.put(ID, innerMapData);
            }
        } catch (SQLException e) {
            System.err.println("Error processing data from the database: " + e.getMessage());
            throw new RuntimeException("Error retrieving data from the database", e);
        }
        return data;
    }
    //potrebuje dodelat zakomponovani hiredate a terminationdate
    private static  Map<String, EmplCostsByName>  prepareDataEmplCostsHelper(ResultSet resultSet) throws NullPointerException  {
        Map<String, EmplCostsByName> data = new LinkedHashMap<>();

        try {
            while (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String name = firstName + " " + lastName;
                Double grossWage = resultSet.getDouble("grossWage");
                LocalDate hireDate = resultSet.getDate("hireDate").toLocalDate();
                LocalDate terminationDate;
                if (resultSet.getDate("terminationDate") != null)
                    terminationDate = resultSet.getDate("terminationDate").toLocalDate();
                else terminationDate = null;

                EmplCostsByName dataHelper = new EmplCostsByName(grossWage,hireDate, terminationDate);
                data.put(name, dataHelper);
            }
        } catch (SQLException e) {
            System.err.println("Error processing data from the database: " + e.getMessage());
            throw new RuntimeException("Error retrieving data from the database", e);
        }
        return data;
    }

}
