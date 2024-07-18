module com.zustik.erpapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    // Open  package to the javafx.base module
    opens com.zustik.erpapp.ModelDataAccess to javafx.base;

    opens com.zustik.erpapp to javafx.fxml;
    exports com.zustik.erpapp;
    exports com.zustik.erpapp.ControllersPresentation;
    opens com.zustik.erpapp.ControllersPresentation to javafx.fxml;
    exports com.zustik.erpapp.Utility;
    opens com.zustik.erpapp.Utility to javafx.fxml;
    opens com.zustik.erpapp.ModelDataAccess.DataOperations to javafx.base;

}