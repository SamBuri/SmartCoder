module com.saburi.smartcoder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.google.gson;
    requires java.desktop;
    opens com.saburi.smartcoder to javafx.fxml;
    opens com.saburi.controller to javafx.fxml;
    opens com.saburi.model to com.google.gson;
    opens com.saburi.dataacess to javafx.base;
    
   
    exports com.saburi.smartcoder;
    exports com.saburi.controller;
}
