package com.zustik.erpapp.DBAccessDataAccess;

import com.zustik.erpapp.DBConnectionDataAccess.DBConnection;
import com.zustik.erpapp.ModelDataAccess.Employees;
import com.zustik.erpapp.ModelDataAccess.Invoices;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class DBdataForEmployees {
    private ObservableList<Employees> lastQueryData = FXCollections.observableArrayList(); //to have some state

    public DBdataForEmployees() {
    }

    public ObservableList<Employees> querySelectWithoutParam() {
        String query = "SELECT * from erp_items.employees";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = ps.executeQuery();
            return prepareData(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Employees> querySelectWithParam(boolean onlyActive) {
        String query = "SELECT * from erp_items.employees WHERE activeStatus=?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setBoolean(1, onlyActive);
            ResultSet resultSet = ps.executeQuery();
            return prepareData(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//    }
//    public ObservableList<Invoices> querySelectWithParam(String whereColumn, String whereEquals) {
//        String query = "SELECT * from erp_items.issuedinvoices WHERE " + whereColumn + "=" + "?";
//        Connection conn = DBConnection.getInstance().getConnection();
//        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//            ps.setString(1, whereEquals);
//            ResultSet resultSet = ps.executeQuery();
//            return prepareData(resultSet);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public ObservableList<Invoices> querySelectWithParam(String whereColumn, int whereEquals) {
//        String query = "SELECT * from erp_items.issuedinvoices WHERE " + whereColumn + "=" + "?";
//        Connection conn = DBConnection.getInstance().getConnection();
//        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//            ps.setDouble(1, whereEquals);
//            ResultSet resultSet = ps.executeQuery();
//            return prepareData(resultSet);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    public ObservableList<Invoices> querySelectWithParam(String whereColumn, LocalDate whereEquals) {
//        String query = "SELECT * from erp_items.issuedinvoices WHERE " + whereColumn + "=" + "?";
//        Connection conn = DBConnection.getInstance().getConnection();
//        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//            ps.setDate(1, Date.valueOf(whereEquals));
//            ResultSet resultSet = ps.executeQuery();
//            return prepareData(resultSet);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public ObservableList<Invoices> querySelectWithParam(String whereColumn, int whereFrom, int whereTo) {
//        String query = "SELECT * from erp_items.issuedinvoices WHERE " + whereColumn + " BETWEEN ? AND ?";
//        Connection conn = DBConnection.getInstance().getConnection();
//        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
//            ps.setInt(1, whereFrom);
//            ps.setInt(2, whereTo);
//            ResultSet resultSet = ps.executeQuery();
//            return prepareData(resultSet);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public ObservableList<Invoices> querySelectWithParam(String whereColumn, LocalDate whereFrom, LocalDate whereTo) {
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

    public void queryInsert(Employees item) {
        String query = "INSERT INTO erp_items.employees (" +
                "firstName,lastName,hireDate,contractType,department,grossWage,degree,address," +
                "bankAccNum,maritalStatus,numberOfChildren,activeStatus,highestEducationCompleted" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {;
            ps.setString(1, item.getFirstName());
            ps.setString(2, item.getLastName());
            ps.setDate(3, Date.valueOf(item.getHireDate()));
            ps.setString(4, item.getContractType());
            ps.setString(5, item.getDepartment());
            ps.setDouble(6, item.getGrossWage());
            ps.setString(7, item.getDegree());
            ps.setString(8, item.getAddress());
            ps.setString(9, item.getBankAccNum());
            ps.setString(10, item.getMaritalStatus());
            ps.setInt(11, item.getNumberOfChildren());
            ps.setBoolean(12, item.isActiveStatus());
            ps.setString(13, item.getHighestEducationCompleted());
            ps.executeUpdate(); // nize uvedene je pouze proto, ze v pameti pracuji s itemem, ktery nema jeste id (dostal by ho pri refreshi resep. select * resp. pri restartu appky)  a ja mu ho potrebuju priridat z databaze, abych mohl hned pripadne ten item ihned smazat, k cemuz je zapotrebi jeho id
            ResultSet generatedKeys = ps.getGeneratedKeys();// zpatky obdrzet vygenerovane id
            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                System.out.println(newId);
                item.setID(newId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void queryUpdate(Employees employee) {
        int item_id = employee.getID();
        String query =
                "UPDATE erp_items.employees SET " +
                        "firstName=?, " +
                        "lastName=?, " +
                        "hireDate=?, " +
                        "terminationDate=?, " +
                        "contractType=?, " +
                        "department=?, " +
                        "grossWage=?, " +
                        "degree=?, " +
                        "address=?, " +
                        "bankAccNum=?, " +
                        "maritalStatus=?, " +
                        "numberOfChildren=?, " +
                        "activeStatus=?, " +
                        "highestEducationCompleted=? " +
                        "WHERE id=?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            ps.setDate(3, Date.valueOf(employee.getHireDate()));
            if (employee.getTerminationDate() != null) {
                ps.setDate(4, Date.valueOf(employee.getTerminationDate()));
            } else {
                ps.setNull(4, Types.DATE);  // Properly handle the null case by setting the SQL parameter to NULL
            }
            ps.setString(5, employee.getContractType());
            ps.setString(6, employee.getDepartment());
            ps.setDouble(7, employee.getGrossWage());
            ps.setString(8, employee.getDegree());
            ps.setString(9, employee.getAddress());
            ps.setString(10, employee.getBankAccNum());
            ps.setString(11, employee.getMaritalStatus());
            ps.setInt(12, employee.getNumberOfChildren());
            ps.setBoolean(13, employee.isActiveStatus());
            ps.setString(14, employee.getHighestEducationCompleted());
            ps.setInt(15, employee.getID());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void queryDelete(Employees item) {
        int item_id = item.getID();
        String query = "DELETE FROM erp_items.employees WHERE id=?";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ObservableList<Employees> prepareData (ResultSet resultSet) {
        ObservableList<Employees> data = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                Date hireDate = resultSet.getDate("hireDate");
                Date terminationDate = resultSet.getDate("terminationDate");
                String contractType = resultSet.getString("contractType");
                String department = resultSet.getString("department");
                Double grossWage = resultSet.getDouble("grossWage");

                String degree = resultSet.getString("degree");
                String address = resultSet.getString("address");
                String bankAccNum = resultSet.getString("bankAccNum");
                String maritalStatus = resultSet.getString("maritalStatus");
                Integer numberOfChildren = resultSet.getInt("numberOfChildren");
                Boolean activeStatus = resultSet.getBoolean("activeStatus");
                String highestEducationCompleted = resultSet.getString("highestEducationCompleted");

                LocalDate localHireDate = hireDate != null ? hireDate.toLocalDate() : null;
                LocalDate localTerminationDate = terminationDate != null ? terminationDate.toLocalDate() : null;


                Employees item = new Employees(id,firstName,lastName, localHireDate,localTerminationDate, contractType,
                        department,grossWage, degree,address,bankAccNum,maritalStatus,
                        numberOfChildren,activeStatus, highestEducationCompleted);
                data.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error processing data from the database: " + e.getMessage());
            throw new RuntimeException("Error retrieving data from the database", e);
        }
        lastQueryData = data;
        return data;
    }
}
