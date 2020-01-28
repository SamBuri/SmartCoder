module com.saburi.smartcoder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    opens com.saburi.smartcoder to javafx.fxml;
    opens com.saburi.model to javafx.base;
    exports com.saburi.smartcoder;
}
