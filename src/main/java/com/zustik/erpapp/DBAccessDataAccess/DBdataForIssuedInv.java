package com.zustik.erpapp.DBAccessDataAccess;

import com.zustik.erpapp.DBConnectionDataAccess.DBConnection;
import com.zustik.erpapp.ModelDataAccess.Invoices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class DBdataForIssuedInv {
    private ObservableList<Invoices> lastQueryData = FXCollections.observableArrayList(); //to have some state

    public DBdataForIssuedInv() {
    }

    public ObservableList<Invoices> querySelectWithoutParam() {
        String query = "SELECT * from erp_items.issuedinvoices";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet resultSet = ps.executeQuery();
            return prepareData(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ObservableList<Invoices> querySelectWithParam(String whereColumn, String whereEquals) {
        String query = "SELECT * from erp_items.issuedinvoices WHERE " + whereColumn + "=" + "?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, whereEquals);
            ResultSet resultSet = ps.executeQuery();
            return prepareData(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Invoices> querySelectWithParam(String whereColumn, int whereEquals) {
        String query = "SELECT * from erp_items.issuedinvoices WHERE " + whereColumn + "=" + "?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, whereEquals);
            ResultSet resultSet = ps.executeQuery();
            return prepareData(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ObservableList<Invoices> querySelectWithParam(String whereColumn, LocalDate whereEquals) {
        System.out.println("asd");
        String query = "SELECT * from erp_items.issuedinvoices WHERE " + whereColumn + "=" + "?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(whereEquals));
            ResultSet resultSet = ps.executeQuery();
            return prepareData(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Invoices> querySelectWithParam(String whereColumn, int whereFrom, int whereTo) {
        String query = "SELECT * from erp_items.issuedinvoices WHERE " + whereColumn + " BETWEEN ? AND ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, whereFrom);
            ps.setInt(2, whereTo);
            ResultSet resultSet = ps.executeQuery();
            return prepareData(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ObservableList<Invoices> querySelectWithParam(String whereColumn, LocalDate whereFrom, LocalDate whereTo) {
        String query = "SELECT * from erp_items.issuedinvoices WHERE " + whereColumn + " BETWEEN ? AND ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(whereFrom));
            ps.setDate(2, Date.valueOf(whereTo));
            ResultSet resultSet = ps.executeQuery();
            return prepareData(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void queryInsert(Invoices invoice) {
        String query = "INSERT INTO erp_items.issuedinvoices (invoiceNumberProp, dateOfIssueProp, totalAmountProp, amountDueProp, " +
                "dueDateProp, recipientProp, subjectProp, projectNumberProp, projectManagerProp, noteProp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {;
            ps.setInt(1, invoice.getInvoiceNumberProp());
            ps.setDate(2, Date.valueOf(invoice.getDateOfIssueOrReceiptProp()));
            ps.setDouble(3, invoice.getTotalAmountProp());
            ps.setDouble(4, invoice.getAmountDueProp());
            ps.setDate(5, Date.valueOf(invoice.getDueDateProp()));
            ps.setString(6, invoice.getRecipientOrSupplierProp());
            ps.setString(7, invoice.getSubjectProp());
            ps.setInt(8, invoice.getProjectNumberProp());
            ps.setString(9, invoice.getProjectManagerProp());
            ps.setString(10, invoice.getNoteProp());
            ps.executeUpdate(); // nize uvedene je pouze proto, ze v pameti pracuji s itemem, ktery nema jeste id (dostal by ho pri refreshi resep. select * resp. pri restartu appky)  a ja mu ho potrebuju priridat z databaze, abych mohl hned pripadne ten item ihned smazat, k cemuz je zapotrebi jeho id
            ResultSet generatedKeys = ps.getGeneratedKeys();// zpatky obdrzet vygenerovane id
            if (generatedKeys.next()) {
                int newId = generatedKeys.getInt(1);
                invoice.setId(newId);
            }
            System.out.println("uspech zapojeni do SQL");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void queryUpdate(Invoices invoice) {
        int item_id = invoice.getId();
        String query =
                "UPDATE erp_items.issuedinvoices SET " +
                        "invoiceNumberProp=?, " +
                        "dateOfIssueProp=?, " +
                        "totalAmountProp=?, " +
                        "amountDueProp=?, " +
                        "dueDateProp=?, " +
                        "recipientProp=?, " +
                        "subjectProp=?, " +
                        "projectNumberProp=?, " +
                        "projectManagerProp=?, " +
                        "noteProp=? " +
                        "WHERE id=?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, invoice.getInvoiceNumberProp());
            ps.setDate(2, Date.valueOf(invoice.getDateOfIssueOrReceiptProp()));
            ps.setDouble(3, invoice.getTotalAmountProp());
            ps.setDouble(4, invoice.getAmountDueProp());
            ps.setDate(5, Date.valueOf(invoice.getDueDateProp()));
            ps.setString(6, invoice.getRecipientOrSupplierProp());
            ps.setString(7, invoice.getSubjectProp());
            ps.setInt(8, invoice.getProjectNumberProp());
            ps.setString(9, invoice.getProjectManagerProp());
            ps.setString(10, invoice.getNoteProp());
            ps.setInt(11, item_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void queryDelete(Invoices invoice) {
        int item_id = invoice.getId();
        String query = "DELETE FROM erp_items.issuedinvoices WHERE id=?";

        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item_id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ObservableList<Invoices> prepareData (ResultSet resultSet) {
        ObservableList<Invoices> data = FXCollections.observableArrayList();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int invoiceNumber = resultSet.getInt("invoiceNumberProp");
                Date date = resultSet.getDate("dateOfIssueProp");
                int amount = resultSet.getInt("totalAmountProp");
                int dueAmount = resultSet.getInt("amountDueProp");
                Date dueDate = resultSet.getDate("dueDateProp");
                String recipient = resultSet.getString("recipientProp");
                String subject = resultSet.getString("subjectProp");
                int projectNumber = resultSet.getInt("projectNumberProp");
                String projectManager = resultSet.getString("projectManagerProp");
                String note = resultSet.getString("noteProp");

                LocalDate localDateOfIssue = date != null ? date.toLocalDate() : null;
                LocalDate localDueDate = dueDate != null ? dueDate.toLocalDate() : null;

                Invoices item = new Invoices(id, invoiceNumber, localDateOfIssue, amount, dueAmount, localDueDate,
                        recipient, subject, projectNumber, projectManager, note);
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
