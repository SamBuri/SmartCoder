/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.utils;

import com.saburi.dataacess.DataAccess;
import com.saburi.model.Model;
import com.saburi.model.Project;
import static com.saburi.utils.Utilities.isNullOrEmpty;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

/**
 *
 * @author Sam Buriima
 */
public class FXUIUtils {

    public static String browse(TextField textField) {
        try {

            FileChooser fileChooser = new FileChooser();
//                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
//                directoryChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Choose File");
            File file = fileChooser.showOpenDialog(null);
            if (file == null) {
                return "";
            }
            String path = file.getPath();
            textField.setText(path);
            return file.getName();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void browse(TextField textField, Stage stage) {
        try {

            FileChooser fileChooser = new FileChooser();
//                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
//                directoryChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Choose File");
            File file = fileChooser.showOpenDialog(stage);
            if (file == null) {
                return;
            }
            String path = file.getPath();
            textField.setText(path);
        } catch (Exception e) {
            errorMessage(e);
        }
    }

    public static void browseSave(TextField textField) {
        try {

            FileChooser fileChooser = new FileChooser();
//                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
//                directoryChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Choose File");
            File file = fileChooser.showSaveDialog(null);
            if (file == null) {
                return;
            }
            String path = file.getPath();
            textField.setText(path);
        } catch (Exception e) {
            errorMessage(e);
        }
    }

    public static void browseDirectory(TextField textField) {
        try {

            DirectoryChooser directoryChooser = new DirectoryChooser();
//                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
//                directoryChooser.getExtensionFilters().add(extFilter);
            directoryChooser.setTitle("Choose Location");
            File file = directoryChooser.showDialog(null);
            if (file == null) {
                return;
            }
            String path = file.getAbsolutePath();
            textField.setText(path);
        } catch (Exception e) {
            errorMessage(e);
        }
    }

    public static void browseDirectory(TextField textField, Stage stage) {
        try {

            DirectoryChooser directoryChooser = new DirectoryChooser();
//                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
//                directoryChooser.getExtensionFilters().add(extFilter);
            directoryChooser.setTitle("Choose Location");
            File file = directoryChooser.showDialog(stage);
            if (file == null) {
                return;
            }
            String path = file.getAbsolutePath();
            textField.setText(path);
        } catch (Exception e) {
            errorMessage(e);
        }
    }

    public static void browseDirectory(Button button, Node node) {
        button.setOnAction(a -> {
            try {

                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Choose Directory");
                File file = directoryChooser.showDialog(button.getScene().getWindow());
                if (file == null) {
                    return;
                }
                String path = file.getAbsolutePath();
                if (node instanceof TextField) {
                    ((TextField) node).setText(path);

                } else if (node instanceof TextArea) {
                    ((TextArea) node).setText(path);

                }

            } catch (Exception e) {
                errorMessage(e);
            }
        });

    }

    public static void browseFile(Button button, Node node) {
        button.setOnAction(a -> {
            try {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Directory");
                File file = fileChooser.showOpenDialog(button.getScene().getWindow());
                if (file == null) {
                    return;
                }
                String path = file.getAbsolutePath();
                if (node instanceof TextField) {
                    ((TextField) node).setText(path);

                } else if (node instanceof TextArea) {
                    ((TextArea) node).setText(path);

                }

            } catch (Exception e) {
                errorMessage(e);
            }
        });

    }

    public static void errorMessage(Exception e) {
//        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
//        alert.setTitle("Error:");
//        alert.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
//        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        alert.setResizable(true);
//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//        stage.setAlwaysOnTop(true);
//        alert.showAndWait();

        AlertManager.message(e.getMessage(), "Error", Alert.AlertType.ERROR);
        e.printStackTrace();

    }

    public static void message(Object message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION, message.toString(), ButtonType.OK);
//       alert.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
//        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        alert.setResizable(true);
//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//        stage.setAlwaysOnTop(true);
//        alert.showAndWait();

        AlertManager.message(message, null, Alert.AlertType.INFORMATION);
    }

    public static Optional<ButtonType> warningMessage(String title, String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        return alert.showAndWait();
    }

    public static Optional<ButtonType> warningMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        return alert.showAndWait();
    }

    public static boolean warningOk(String title, String headerText, String message) {
        return AlertManager.warningOk(title, headerText, message);
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(headerText);
//        alert.setContentText(message);
//        alert.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
//        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        alert.setResizable(true);
//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//        stage.setAlwaysOnTop(true);
//        // Show the alert and check if the user pressed OK
//        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public static boolean warningOK(String title, String message) {
        return AlertManager.warningOK(title, message);
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//
//        alert.getDialogPane().setMaxHeight(Region.USE_PREF_SIZE);
//        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
//        alert.setResizable(true);
//        // Retrieve the dialog's stage and set it to always be on top
//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//        stage.setAlwaysOnTop(true);
//        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;

    }

    public static void setTableEditable(TableView table) {
        table.setEditable(true);
        // allows the individual cells to be selected
        table.getSelectionModel().cellSelectionEnabledProperty().set(true);
        // when character or numbers pressed it will start edit in editable
        // fields
        table.setOnKeyPressed(event -> {
            if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                editFocusedCell(table);
            } else if (event.getCode() == KeyCode.RIGHT
                    || event.getCode() == KeyCode.TAB) {
                table.getSelectionModel().selectNext();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT) {
                // work around due to
                // TableView.getSelectionModel().selectPrevious() due to a bug
                // stopping it from working on
                // the first column in the last row of the table
                selectPrevious(table);
                event.consume();
            } else if (event.getCode() == KeyCode.DELETE) {
                table.getItems().removeAll(table.getSelectionModel().getSelectedItems());
            }
        });
    }

    private static void editFocusedCell(TableView<Class> table) {
        final TablePosition<Class, ?> focusedCell = table
                .focusModelProperty().get().focusedCellProperty().get();
        table.edit(focusedCell.getRow(), focusedCell.getTableColumn());
    }

    @SuppressWarnings("unchecked")
    private static void selectPrevious(TableView table) {
        if (table.getSelectionModel().isCellSelectionEnabled()) {
            // in cell selection mode, we have to wrap around, going from
            // right-to-left, and then wrapping to the end of the previous line
            TablePosition<Class, ?> pos = table.getFocusModel()
                    .getFocusedCell();
            if (pos.getColumn() - 1 >= 0) {
                // go to previous row
                table.getSelectionModel().select(pos.getRow(),
                        getTableColumn(table, pos.getTableColumn(), -1));
            } else if (pos.getRow() < table.getItems().size()) {
                // wrap to end of previous row
                table.getSelectionModel().select(pos.getRow() - 1,
                        table.getVisibleLeafColumn(
                                table.getVisibleLeafColumns().size() - 1));
            }
        } else {
            int focusIndex = table.getFocusModel().getFocusedIndex();
            if (focusIndex == -1) {
                table.getSelectionModel().select(table.getItems().size() - 1);
            } else if (focusIndex > 0) {
                table.getSelectionModel().select(focusIndex - 1);
            }
        }
    }

    private static TableColumn<Class, ?> getTableColumn(TableView table,
            final TableColumn<Class, ?> column, int offset) {
        int columnIndex = table.getVisibleLeafIndex(column);
        int newColumnIndex = columnIndex + offset;
        return table.getVisibleLeafColumn(newColumnIndex);
    }

    public static void copySelectionToClipboard(TableView<?> table) {

        StringBuilder clipboardString = new StringBuilder();

        ObservableList<TablePosition> positionList = table.getSelectionModel().getSelectedCells();

        int prevRow = -1;

        for (TablePosition position : positionList) {

            int row = position.getRow();
            int col = position.getColumn();

            Object cell = (Object) table.getColumns().get(col).getCellData(row);

            // null-check: provide empty string for nulls
            if (cell == null) {
                cell = "";
            }

            // determine whether we advance in a row (tab) or a column
            // (newline).
            if (prevRow == row) {

                clipboardString.append('\t');

            } else if (prevRow != -1) {

                clipboardString.append('\n');

            }

            // create string from cell
            String text = cell.toString();

            // add new item to clipboard
            clipboardString.append(text);

            // remember previous
            prevRow = row;
        }

        // create clipboard content
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(clipboardString.toString());

        // set clipboard content
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }

    public static class TableKeyEventHandler implements EventHandler<KeyEvent> {

        KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);

        @Override
        public void handle(final KeyEvent keyEvent) {

            if (copyKeyCodeCompination.match(keyEvent)) {

                if (keyEvent.getSource() instanceof TableView) {

                    // copy to clipboard
                    copySelectionToClipboard((TableView<?>) keyEvent.getSource());

                    // event is handled, consume it
                    keyEvent.consume();

                }

            }

        }

    }

    public static void installCopyPasteHandler(TableView<?> table) {

        // install copy/paste keyboard handler
        table.setOnKeyPressed(new TableKeyEventHandler());

    }

    public static String validate(String value, String string) throws Exception {

        if (isNullOrEmpty(value)) {
            throw new Exception("You must enter: " + string + "!");
        }
        return value;
    }

    public static String getText(TextField field, String string) throws Exception {
        String enteredString = field.getText();
        if (isNullOrEmpty(enteredString)) {
            field.requestFocus();
            throw new Exception("You must enter: " + string + "!");
        }
        return enteredString.trim();
    }

    public static String getText(TextField field, String string, boolean condition) throws Exception {
        return condition ? getText(field, string) : getText(field);
    }

    public static String getText(ComboBox box, String string) throws Exception {
        Object value = box.getValue();
        if (value == null) {
            box.requestLayout();
            throw new Exception("You must enter: " + string + "!");
        }
        return String.valueOf(value);
    }

    public static String getTextValue(ComboBox box, String string) throws Exception {
        Pair<String, Object> pair = (Pair<String, Object>) box.getValue();
        if (pair == null) {
            throw new Exception("You must enter: " + string + "!");
        };
        String enteredString = pair.getValue().toString().trim();
        if (isNullOrEmpty(enteredString)) {
            box.requestLayout();
            throw new Exception("You must enter: " + string + "!");
        }
        return enteredString;
    }

    public static Object getSelectedValue(ComboBox box, String string) throws Exception {
        Object object = box.getValue();
        if (object == null) {
            box.requestLayout();
            throw new Exception("You must select: " + string + "!");

        }
        return object;
    }

    public static Object getSelectedValue(ComboBox box) {
        return box.getValue();
    }

    public static Object getSelectedValue(ComboBox box, String string, boolean condition) throws Exception {
        Object object = box.getValue();
        if (object == null && condition) {
            box.requestLayout();
            throw new Exception("You must select: " + string + "!");

        }
        return object;
    }

    public static String getText(TextArea textArea, String string) throws Exception {
        String enteredString = textArea.getText().trim();
        if (isNullOrEmpty(enteredString)) {
            textArea.requestFocus();
            throw new Exception("You must enter: " + string + "!");
        }
        return enteredString;
    }

    public static String getText(TextArea textArea) {
        String enteredString = textArea.getText().trim();
        return enteredString;
    }

    public static String getText(TextArea textArea, String string, boolean condition) throws Exception {
        return condition ? getText(textArea, string) : getText(textArea);
    }
    
     public static String getText(ComboBox comboBox, String string, boolean condition) throws Exception {
        return condition ? getText(comboBox, string) : getText(comboBox);
    }

    public static LocalDate getDate(DatePicker picker, String string) throws Exception {
        LocalDate enteredDate = picker.getValue();
        if (enteredDate == null) {
            picker.requestFocus();
            throw new Exception("You must enter: " + string + "!");
        }
        return enteredDate;
    }

    public static LocalDate getDate(DatePicker picker, String string, boolean condition) throws Exception {
        LocalDate enteredDate = picker.getValue();
        if (enteredDate == null && condition) {
            picker.requestFocus();
            throw new Exception("You must enter: " + string + "!");
        }
        return enteredDate;
    }

    public static String getText(TextField field) {
        String enteredString = field.getText().trim();
        return enteredString;
    }

    public static String getText(ComboBox box) {
        try {
            return box.getValue().toString().trim();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getComboDisplay(ComboBox box) {
        Pair<String, Object> pair = (Pair<String, Object>) box.getValue();
        String enteredString = pair.getKey().trim();
        return enteredString;
    }

    public static LocalDate getDate(DatePicker picker) {
        LocalDate enteredDate = picker.getValue();

        return enteredDate;
    }

    public static int getInt(ComboBox box) throws Exception {
        String enteredString = box.getValue().toString().trim();
        if (isNullOrEmpty(enteredString)) {
            return 0;
        } else if (!Utilities.isInteger(enteredString)) {
            box.requestFocus();
            throw new Exception("Invalid Integer value: " + enteredString + "!");
        } else {
            return Integer.parseInt(enteredString);
        }
    }

    public static int getIntValue(ComboBox box) throws Exception {
        Pair<String, Object> pair = (Pair<String, Object>) box.getValue();
        String enteredString = pair.getValue().toString().trim();
        if (isNullOrEmpty(enteredString)) {
            return 0;
        } else if (!Utilities.isInteger(enteredString)) {
            box.requestFocus();
            throw new Exception("Invalid Integer value: " + enteredString + "!");
        } else {
            return Integer.parseInt(enteredString);
        }
    }

    public static int getInt(TextArea field) throws Exception {
        String enteredString = field.getText().trim();
        if (isNullOrEmpty(enteredString)) {
            return 0;
        } else if (!Utilities.isInteger(enteredString)) {
            field.requestFocus();
            throw new Exception("Invalid Integer value: " + enteredString + "!");
        } else {
            return Integer.parseInt(enteredString);
        }
    }

    public static int getInt(TextField field, String string) throws Exception {
        String enteredString = field.getText().trim();
        if (isNullOrEmpty(enteredString)) {
            field.requestFocus();
            throw new Exception("You must enter: " + string + "!");
        } else if (!Utilities.isInteger(enteredString)) {
            field.requestFocus();
            throw new Exception("Invalid Integer value: " + enteredString + " for " + string + "!");
        } else {
            return Utilities.defortInteger(enteredString);
        }

    }

    public static void validateIteger(TextField field) {
        field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                field.setText(newValue.replaceAll("[^\\d*]", ""));
            }
        });
    }

    public static void validateNumber(TextField field) {
        field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*\\.")) {
                field.setText(newValue.replaceAll("[^\\d*\\.\\,]", ""));
            }
        });

    }

    public static ImageView getImage(ImageView imageView, byte[] bytes, int width, int height, boolean mAspectRation, boolean mSmoothness) {
        InputStream in = new ByteArrayInputStream(bytes);
        Image image = new Image(in, width, height, mAspectRation, mSmoothness);
        imageView.setImage(image);
        return imageView;
    }

    public static void loadSearchColumnCombo(List<SearchColumn> searchColumns, ComboBox<SearchColumn> combo) {
        SearchColumn searchColumn = new SearchColumn("", "", "", SearchColumn.SearchDataTypes.STRING);
        combo.getItems().clear();
        combo.getItems().add(searchColumn);
        combo.getItems().addAll(searchColumns);
        combo.setValue(searchColumn);
        Callback<ListView<SearchColumn>, ListCell<SearchColumn>> factory
                = (lv)
                -> new ListCell<SearchColumn>() {
            @Override
            protected void updateItem(SearchColumn item,
                    boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(item.getDisplayValue());
                }
            }
        };
        combo.setCellFactory(factory);
        combo.setButtonCell(factory.call(null));
    }

    public static void searchColumnSelected(ComboBox<SearchColumn> cboSearchColumn, ComboBox cboSearchType) {
        SearchColumn searchColumn = cboSearchColumn.getValue();
        if (searchColumn == null) {
            return;
        }
        ObservableList searchTypes = FXCollections.observableList(searchColumn.getSearchTypes());
        cboSearchType.setItems(searchTypes);
        cboSearchType.setValue(searchColumn.getDefaultSearchType());

    }

    public static void setContainsSearch(FilteredList filteredList, ComboBox cboSearchType,
            ComboBox<SearchColumn> cboSearchColumn, TextField txtSearch, TableView table) {
        SearchColumn selectedSearchColumn = cboSearchColumn.getValue();

        String searchType = getText(cboSearchType);

        txtSearch.textProperty().addListener((obervableValue, oldValue, newValue) -> {
            if (!(searchType.equalsIgnoreCase(SearchColumn.SearchType.Contains.name()) || searchType.isBlank())) {
                return;
            }
            filteredList.setPredicate((Predicate<DataAccess>) dbAccess -> {

                String lowerNewValue = txtSearch.getText().trim();
                List<SearchColumn> searchColumns = dbAccess.getSearchColumns();
                if (newValue.isBlank()) {
                    return true;
                } else {
                    if (selectedSearchColumn.getName().isBlank()) {
                        if (searchColumns.stream().anyMatch((searchColumn) -> (searchColumn.getValue().toString().equalsIgnoreCase(newValue)))) {
                            return true;
                        }
                    } else {

                        if (searchColumns.stream().filter((searchColumn) -> (searchColumn.getName().equalsIgnoreCase(selectedSearchColumn.getName()))).anyMatch((searchColumn) -> (searchColumn.getValue().toString().equalsIgnoreCase(lowerNewValue)))) {
                            return true;
                        }

                    }
                }
                return false;
            });

        });

        SortedList<DataAccess> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);

    }

    public static void setSearch(FilteredList filteredList, ComboBox cboSearchType,
            ComboBox<SearchColumn> cboSearchColumn, TextField txtSearch, TextField txtSecondValue, TableView table) {
        String searchType = getText(cboSearchType);
//        if (searchType.equalsIgnoreCase(SearchColumn.SearchType.Contains.name()) || isNullOrEmpty(searchType)) {
//            return;
//        }

        String enteredText = txtSearch.getText();
        String enteredText1 = txtSecondValue.getText();

        SearchColumn selectedColumn = cboSearchColumn.getValue();

        if (searchType.equalsIgnoreCase(SearchColumn.SearchType.Equal.name())) {

            filteredList.setPredicate(selectedColumn.equalsPrediacte(selectedColumn, enteredText));

        }
        if (searchType.equalsIgnoreCase(SearchColumn.SearchType.Contains.name())) {

            filteredList.setPredicate(selectedColumn.containsPrediacte(selectedColumn, enteredText));

        } else if (searchType.equalsIgnoreCase(SearchColumn.SearchType.Between.name())) {

            filteredList.setPredicate(selectedColumn.betweenPredicate(selectedColumn, enteredText, enteredText1));
        } else if (searchType.equalsIgnoreCase(SearchColumn.SearchType.Begins.name())) {
            filteredList.setPredicate(selectedColumn.beginsPrediacte(selectedColumn, enteredText));
        } else if (searchType.equalsIgnoreCase(SearchColumn.SearchType.Ends.name())) {
            filteredList.setPredicate(selectedColumn.endsPrediacte(selectedColumn, enteredText));
        } else if (searchType.equalsIgnoreCase(SearchColumn.SearchType.Length_Equal.name())) {
            try {
                filteredList.setPredicate(selectedColumn.lengthLessEqualPredicate(selectedColumn, getInt(txtSearch, "Search Value")));
            } catch (Exception ex) {
                errorMessage(ex);
            }

        } else if (searchType.equalsIgnoreCase(SearchColumn.SearchType.Length_Greater.name())) {
            try {
                filteredList.setPredicate(selectedColumn.lengthLessGreaterPredicate(selectedColumn, getInt(txtSearch, "Search Value")));
            } catch (Exception ex) {
                errorMessage(ex);
            }

        } else if (searchType.equalsIgnoreCase(SearchColumn.SearchType.Length_Less.name())) {
            try {
                filteredList.setPredicate(selectedColumn.lengthLessPrediacte(selectedColumn, getInt(txtSearch, "Search Value")));
            } catch (Exception ex) {
                errorMessage(ex);
            }

        }

        SortedList<DataAccess> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    public static void loadModels(List<? extends Model> dBEntities, ComboBox<Model> combo) {
        combo.getItems().clear();
        combo.getItems().add(null);
        combo.getItems().addAll(dBEntities);
        combo.setValue(null);
        Callback<ListView<Model>, ListCell<Model>> factory
                = (lv)
                -> new ListCell<Model>() {
            @Override
            protected void updateItem(Model item,
                    boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(item == null ? "" : item.getDisplay());
                }
            }
        };
        combo.setCellFactory(factory);
        combo.setButtonCell(factory.call(null));
    }

    public static void loadProjects(List<Project> projects, ComboBox<Project> combo) {
        combo.getItems().clear();
        combo.getItems().add(null);
        combo.getItems().addAll(projects);
        combo.setValue(null);
        Callback<ListView<Project>, ListCell<Project>> factory
                = (lv)
                -> new ListCell<Project>() {
            @Override
            protected void updateItem(Project item,
                    boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(item == null ? "" : item.getProjectName());
                }
            }
        };
        combo.setCellFactory(factory);
        combo.setButtonCell(factory.call(null));
    }

    public static void addRow(TableView tableView, Object object) {

        Object lastID = null;
        TablePosition pos = tableView.getFocusModel().getFocusedCell();
        List items = tableView.getItems();
        int size = items.size();
        Object lastObject = items.isEmpty() ? null : tableView.getItems().get(size - 1);
        if (lastObject instanceof DataAccess dataAccess) {
            lastID = dataAccess.getModel().getId();
        }
        if (pos.getRow() == size - 1 || lastID != null) {

            tableView.getItems().add(object);
        }
    }

     public static void clear(Node node) {
        if (node instanceof TextField textField) {
            textField.clear();
        } else if (node instanceof TextArea textArea) {
            textArea.setText("");
        } else if (node instanceof ComboBox comboBox) {
            comboBox.setValue(null);
        } else if (node instanceof DatePicker datePicker) {
            datePicker.setValue(null);
        } else if (node instanceof CheckBox checkBox) {
            checkBox.setSelected(false);
        } else if (node instanceof TableView tableView) {
            tableView.getItems().clear();
        } else if (node instanceof ImageView imageView) {
            imageView.setImage(null);
        }
        
        

    }
     
     public static void clear(List<Node> nodes) {
       nodes.forEach((Node node) -> {
            if (node instanceof GridPane gridPane) {
                gridPane.getChildren().forEach(n -> clear(n));
            } else if (node instanceof HBox hBox) {
                hBox.getChildren().forEach(n -> clear(n));
            } else if (node instanceof TableView tableView) {
                tableView.getItems().clear();
            } else if (node instanceof ImageView imageView) {
                imageView.setImage(null);
            }
        });
    }

    
    public static void clear(VBox vBOX) {
        List<Node> nodes = vBOX.getChildren();

        nodes.forEach((node) -> {
            if (node instanceof GridPane) {
                GridPane gridPane = (GridPane) node;
                gridPane.getChildren().forEach(n -> clear(n));
            } else if (node instanceof HBox) {
                HBox hBox = (HBox) node;
                hBox.getChildren().forEach(n -> clear(n));
            } else if (node instanceof TableView) {
                TableView tableView = (TableView) node;
                tableView.getItems().clear();
            } else if (node instanceof TextArea) {
                TextArea textAtrea = (TextArea) node;
                textAtrea.clear();
            }else if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                imageView.setImage(null);
            }
        });
    }

   
    public static void clear(VBox vBOX, List<Node> exclusions) {
        List<Node> nodes = vBOX.getChildren();
        nodes.removeAll(exclusions);
        nodes.forEach((node) -> {
            if (node instanceof GridPane) {
                GridPane gridPane = (GridPane) node;
                gridPane.getChildren().stream().filter((p) -> !exclusions.contains(p)).forEach(n -> clear(n));
            } else if (node instanceof HBox) {
                HBox hBox = (HBox) node;
                hBox.getChildren().forEach(n -> clear(n));
            } else if (node instanceof TableView) {
                TableView tableView = (TableView) node;
                tableView.getItems().clear();
            } else if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                imageView.setImage(null);
            }
            else if (node instanceof TextArea) {
                TextArea textAtrea = (TextArea) node;
                textAtrea.clear();
            }

        });
    }

}
