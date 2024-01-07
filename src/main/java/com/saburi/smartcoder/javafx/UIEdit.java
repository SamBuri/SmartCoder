/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.saburi.smartcoder.javafx;

import com.saburi.dataacess.FieldDAO;
import com.saburi.dataacess.SettingsDAO;
import com.saburi.model.Project;
import com.saburi.model.Settings;
import com.saburi.smartcoder.FieldPredicates;
import com.saburi.utils.Utilities;
import static com.saburi.utils.Utilities.addIfNotExists;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

/**
 *
 * @author CLINICMASTER13
 */
public class UIEdit {

    private final String objectName;
    private final String objectNameController;
    private final List<FieldDAO> fields;

    public UIEdit(String objectName, List<FieldDAO> fields) {
        this.objectName = objectName;
        this.fields = fields;
        this.objectNameController = objectName.concat("Controller");
    }

    private String imports() {
        String imports = "<?import javafx.scene.layout.AnchorPane?>\n"
                + "<?import javafx.scene.layout.HBox?>\n"
                + "<?import javafx.scene.layout.GridPane?>\n"
                + "<?import javafx.scene.layout.VBox?>\n"
                + "<?import javafx.geometry.Insets?>\n"
                + "<?import javafx.scene.control.ContextMenu?>\n"
                + "<?import javafx.scene.control.MenuItem?>\n"
                + "<?import javafx.scene.control.Button?>\n"
                + "<?import javafx.scene.control.Label?>\n";

        List<String> impts = new ArrayList<>();
        fields.forEach(f -> {
            addIfNotExists(impts, "<?" + f.getControlLibary() + f.getPrimaryControlType() + "?>\n");

            if (f.makeEditableTable()) {
                addIfNotExists(impts, "<?import javafx.scene.control.TableColumn?>\n");
                addIfNotExists(impts, "<?import javafx.scene.control.cell.PropertyValueFactory?>\n");
                FilteredList<FieldDAO> subListFields = new FilteredList<>(FXCollections.observableList(fields), e -> true);
                subListFields.setPredicate(FieldPredicates.hasSubFields());
                if (subListFields.size() > 1) {
                }
                addIfNotExists(impts, "<?import javafx.scene.control.TabPane?>\n");
                addIfNotExists(impts, "<?import javafx.scene.control.Tab?>\n");
            }
        });
        for (String type : impts) {
            imports += type;

        }
        return imports;
    }

    private String makeBody() throws Exception {
        String body = "<GridPane vgap =\"2\" hgap=\"5\">\n"
                + "<padding>\n"
                + "<Insets bottom=\"10.0\" left=\"10.0\" right=\"10.0\" top=\"10.0\" />\n"
                + "</padding>\n";

        FilteredList<FieldDAO> subListFields = new FilteredList<>(FXCollections.observableList(fields), e -> true);
        subListFields.setPredicate(FieldPredicates.hasSubFields());

        Optional<Settings> optSettings = new SettingsDAO().getOne();
        int endValue = 12;
        if (optSettings.isPresent()) {
            Settings settings = optSettings.get();
            endValue = settings.getLineBreak();
            if (!subListFields.isEmpty()) {
                endValue = settings.getMiniLineBreak();
            }
        }

        int size = fields.size();
        int rowIndex = 0;
        int columnIndex = 0;
        for (int i = 0; i < size; i++) {

            FieldDAO field = fields.get(i);

            if (!(field.isHelper() || field.isCollection())) {
                body += field.makeUIFXMLEditLine(objectName, rowIndex, columnIndex);
                rowIndex++;
                if (rowIndex == endValue) {
                    rowIndex = 0;
                    columnIndex += 2;
                }
            }

        }

        return body;
    }

    private String listComtrols() throws Exception {
        String body = "";

        FilteredList<FieldDAO> subListFields = new FilteredList<>(FXCollections.observableList(fields), e -> true);
        subListFields.setPredicate(FieldPredicates.hasSubFields());

        if (!subListFields.isEmpty()) {
            if (subListFields.size() == 1) {
                FieldDAO field = subListFields.get(0);
                List<FieldDAO> subFields = field.getSubFieldListDAO();
                String tableColumn = "";
                tableColumn = subFields.stream().map((subfield) -> subfield.getTableColumn(objectName, field.getReferences(), "true")).reduce(tableColumn, String::concat);
                String contextMenu = "<contextMenu>\n"
                        + "            <ContextMenu fx:id =\"cmu" + field.getFieldName() + "\" id = \"" + objectName + "\">\n"
                        + "              <items>\n"
                        + "                <MenuItem mnemonicParsing=\"false\" fx:id =\"cmiSelect" + field.getFieldName() + "\" id = \"" + objectName + "\" text=\"Select " + field.getCaption() + "\" />\n"
                        + "              </items>\n"
                        + "            </ContextMenu>\n"
                        + "         </contextMenu>\n";
                body += Utilities.makeTable(field.getFieldName(), tableColumn, contextMenu);
            } else {
                String tabs = "";
                for (FieldDAO field : subListFields) {
                    List<FieldDAO> subFields = field.getSubFieldListDAO();
                    String tableColumn = "";
                    tableColumn = subFields.stream().map((subfield) -> subfield.getTableColumn(objectName, field.getReferences(), "true")).reduce(tableColumn, String::concat);

                    tabs += "<Tab text=\"" + field.getCaption() + "\"  fx:id =\"tb" + field.getFieldName() + "\" id = \"" + field.getFieldName() + "\">\n"
                            + "      <content>";
                    String contextMenu = "<contextMenu>\n"
                            + "            <ContextMenu fx:id =\"cmu" + field.getFieldName() + "\" id = \"" + objectName + "\">\n"
                            + "              <items>\n"
                            + "                <MenuItem mnemonicParsing=\"false\" fx:id =\"cmiSelect" + field.getFieldName() + "\" id = \"" + objectName + "\" text=\"Select " + field.getCaption() + "\" />\n"
                            + "              </items>\n"
                            + "            </ContextMenu>\n"
                            + "         </contextMenu>\n";
                    tabs += Utilities.makeTable(field.getFieldName(), tableColumn, contextMenu);
                    tabs += "</content>\n"
                            + "    </Tab>";

                }
                body = "<TabPane GridPane.hgrow=\"ALWAYS\" minHeight=\"200\" tabClosingPolicy=\"UNAVAILABLE\" xmlns=\"http://javafx.com/javafx/10.0.1\" xmlns:fx=\"http://javafx.com/fxml/1\">\n"
                        + "  <tabs>";
                body += tabs;
                body += " </tabs>\n"
                        + "</TabPane>";
            }
        }

        return body;
    }

    public String create(Project currentProject) throws Exception {
        return new FXMLFile(imports(), makeBody()).create(currentProject, objectName, objectNameController, listComtrols(), true);

    }

}
