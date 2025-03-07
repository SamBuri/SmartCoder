package com.saburi.utils;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.LinkedBlockingQueue;

public class AlertManager {

    private static final LinkedBlockingQueue<Alert> alertQueue = new LinkedBlockingQueue<>();
    private static boolean isShowingAlert = false;

    // General message method
    public static void message(Object message, String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType, message.toString(), ButtonType.OK);
        if(!Utilities.isNullOrEmpty(title)) alert.setTitle(title);
        configureAlert(alert);
        enqueueAlert(alert);
    }

    // Utility method for confirmation dialog with title, header, and message
    public static boolean warningOk(String title, String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        configureAlert(alert);
        enqueueAlert(alert);
        return waitForAlertResponse(alert);
    }

    // Utility method for confirmation dialog with title and message (no header)
    public static boolean warningOK(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        configureAlert(alert);
        enqueueAlert(alert);
        return waitForAlertResponse(alert);
    }

    // Common configuration for all alerts
    private static void configureAlert(Alert alert) {
        alert.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(false); // Disable resizing to avoid rendering issues
        alert.initModality(Modality.APPLICATION_MODAL); // Ensure the alert stays within the application
    }

    // Enqueue the alert for processing
    private static void enqueueAlert(Alert alert) {
        alertQueue.offer(alert);
        processAlerts();
    }

    // Process alerts in the queue
    private static void processAlerts() {
        if (!isShowingAlert) {
            Platform.runLater(() -> {
                Alert nextAlert = alertQueue.poll();
                if (nextAlert != null) {
                    isShowingAlert = true;
                    nextAlert.showAndWait().ifPresent(response -> {
                        isShowingAlert = false;
                        processAlerts(); // Process the next alert in the queue
                    });
                }
            });
        }
    }

    // Wait for the user's response to a confirmation alert
    private static boolean waitForAlertResponse(Alert alert) {
        // Since the alert is already enqueued and processed, we need to wait for its result
        // This method assumes the alert is already being shown via the queue system
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
