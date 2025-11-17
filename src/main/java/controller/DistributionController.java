package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import dao.DistributionDAO;
import dao.EventDAO;
import dao.BloodUnitDAO;
import model.DistributionRecord;
import model.DonationEvent;
import model.BloodUnit;
import java.sql.SQLException;
import java.time.LocalDate;

public class DistributionController {
    @FXML
    private TableView<DistributionRecord> distributionTable;
    @FXML
    private TableColumn<DistributionRecord, Integer> distributionIdColumn;
    @FXML
    private TableColumn<DistributionRecord, Integer> eventIdColumn;
    @FXML
    private TableColumn<DistributionRecord, LocalDate> distributionDateColumn;
    @FXML
    private TableColumn<DistributionRecord, String> hospitalColumn;

    @FXML
    private TableView<BloodUnit> availableUnitsTable;
    @FXML
    private TableColumn<BloodUnit, Integer> unitIdColumn;
    @FXML
    private TableColumn<BloodUnit, String> bloodGroupColumn;
    @FXML
    private TableColumn<BloodUnit, Integer> volumeColumn;

    @FXML
    private ComboBox<DonationEvent> eventCombo;
    @FXML
    private DatePicker distributionDatePicker;
    @FXML
    private TextField hospitalField;
    @FXML
    private TextArea notesArea;

    @FXML
    private Label infoLabel;
    @FXML
    private Label availableUnitsLabel;

    private DistributionDAO distributionDAO;
    private EventDAO eventDAO;
    private BloodUnitDAO bloodUnitDAO;
    private ObservableList<DistributionRecord> recordList;

    @FXML
    public void initialize() {
        distributionDAO = new DistributionDAO();
        eventDAO = new EventDAO();
        bloodUnitDAO = new BloodUnitDAO();
        setupTableColumns();
        setupEventCombo();
        loadDistributionRecords();
        setupTableSelectionListener();
    }

    private void setupTableColumns() {
        distributionIdColumn.setCellValueFactory(new PropertyValueFactory<>("distributionId"));
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        distributionDateColumn.setCellValueFactory(new PropertyValueFactory<>("distributionDate"));
        hospitalColumn.setCellValueFactory(new PropertyValueFactory<>("hospitalName"));

        unitIdColumn.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        bloodGroupColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volumeMl"));
    }

    private void setupEventCombo() {
        try {
            ObservableList<DonationEvent> events = FXCollections.observableArrayList(eventDAO.getAllEvents());
            eventCombo.setItems(events);
        } catch (SQLException e) {
            showError("Error loading events: " + e.getMessage());
        }
    }

    private void loadDistributionRecords() {
        try {
            recordList = FXCollections.observableArrayList(distributionDAO.getAllRecords());
            distributionTable.setItems(recordList);
            infoLabel.setText("Loaded " + recordList.size() + " distribution records");
        } catch (SQLException e) {
            showError("Error loading distribution records: " + e.getMessage());
        }
    }

    private void setupTableSelectionListener() {
        distributionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateFields(newVal);
            }
        });
    }

    private void populateFields(DistributionRecord record) {
        try {
            eventCombo.setValue(eventDAO.getEventById(record.getEventId()));
            distributionDatePicker.setValue(record.getDistributionDate());
            hospitalField.setText(record.getHospitalName());
            notesArea.setText(record.getNotes() != null ? record.getNotes() : "");
        } catch (SQLException e) {
            showError("Error loading record details: " + e.getMessage());
        }
    }

    @FXML
    private void handleLoadAvailableUnits() {
        if (eventCombo.getValue() == null) {
            showError("Please select an event");
            return;
        }

        try {
            ObservableList<BloodUnit> units = FXCollections.observableArrayList(
                    bloodUnitDAO.getBloodUnitsByEvent(eventCombo.getValue().getEventId())
            );

            // Filter tested and non-expired units
            ObservableList<BloodUnit> filtered = FXCollections.observableArrayList(
                    units.stream()
                            .filter(u -> u.isTested() && u.getExpiryDate().isAfter(LocalDate.now()))
                            .toList()
            );

            availableUnitsTable.setItems(filtered);
            availableUnitsLabel.setText("Available Units: " + filtered.size());
        } catch (SQLException e) {
            showError("Error loading units: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddDistribution() {
        if (!validateInput()) return;

        try {
            DistributionRecord record = new DistributionRecord(
                    eventCombo.getValue().getEventId(),
                    distributionDatePicker.getValue(),
                    availableUnitsTable.getItems().size(),
                    hospitalField.getText()
            );
            record.setNotes(notesArea.getText());
            distributionDAO.addDistributionRecord(record);
            infoLabel.setText("Distribution record added successfully");
            loadDistributionRecords();
            clearFields();
        } catch (SQLException e) {
            showError("Error adding distribution record: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateDistribution() {
        DistributionRecord selected = distributionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a record to update");
            return;
        }

        if (!validateInput()) return;

        try {
            selected.setEventId(eventCombo.getValue().getEventId());
            selected.setDistributionDate(distributionDatePicker.getValue());
            selected.setHospitalName(hospitalField.getText());
            selected.setNotes(notesArea.getText());

            distributionDAO.updateDistributionRecord(selected);
            infoLabel.setText("Distribution record updated successfully");
            loadDistributionRecords();
            clearFields();
        } catch (SQLException e) {
            showError("Error updating distribution record: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteDistribution() {
        DistributionRecord selected = distributionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a record to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Distribution Record");
        alert.setContentText("Are you sure you want to delete this distribution record?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                distributionDAO.deleteDistributionRecord(selected.getDistributionId());
                infoLabel.setText("Distribution record deleted successfully");
                loadDistributionRecords();
                clearFields();
            } catch (SQLException e) {
                showError("Error deleting distribution record: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadDistributionRecords();
        setupEventCombo();
        clearFields();
    }

    @FXML
    private void handleClear() {
        clearFields();
        distributionTable.getSelectionModel().clearSelection();
        availableUnitsTable.getItems().clear();
    }

    private void clearFields() {
        eventCombo.setValue(null);
        distributionDatePicker.setValue(null);
        hospitalField.clear();
        notesArea.clear();
        availableUnitsTable.getItems().clear();
        availableUnitsLabel.setText("Available Units: 0");
    }

    private boolean validateInput() {
        if (eventCombo.getValue() == null || distributionDatePicker.getValue() == null ||
                hospitalField.getText().isEmpty()) {
            showError("Please fill all required fields");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
