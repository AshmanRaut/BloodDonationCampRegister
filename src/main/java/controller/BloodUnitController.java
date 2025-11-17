package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import dao.BloodUnitDAO;
import dao.DonorDAO;
import dao.EventDAO;
import model.BloodUnit;
import model.Donor;
import model.DonationEvent;
import java.sql.SQLException;
import java.time.LocalDate;

public class BloodUnitController {
    @FXML
    private TableView<BloodUnit> bloodUnitTable;
    @FXML
    private TableColumn<BloodUnit, Integer> unitIdColumn;
    @FXML
    private TableColumn<BloodUnit, String> bloodGroupColumn;
    @FXML
    private TableColumn<BloodUnit, Integer> volumeColumn;
    @FXML
    private TableColumn<BloodUnit, LocalDate> collectionDateColumn;
    @FXML
    private TableColumn<BloodUnit, LocalDate> expiryDateColumn;
    @FXML
    private TableColumn<BloodUnit, String> statusColumn;

    @FXML
    private ComboBox<Donor> donorCombo;
    @FXML
    private ComboBox<DonationEvent> eventCombo;
    @FXML
    private TextField bloodGroupField;
    @FXML
    private Spinner<Integer> volumeSpinner;
    @FXML
    private DatePicker collectionDatePicker;
    @FXML
    private DatePicker expiryDatePicker;
    @FXML
    private CheckBox testedCheckBox;
    @FXML
    private ComboBox<String> statusCombo;

    @FXML
    private Label infoLabel;
    @FXML
    private Label donorDetailsLabel;

    private BloodUnitDAO bloodUnitDAO;
    private DonorDAO donorDAO;
    private EventDAO eventDAO;
    private ObservableList<BloodUnit> unitList;

    @FXML
    public void initialize() {
        bloodUnitDAO = new BloodUnitDAO();
        donorDAO = new DonorDAO();
        eventDAO = new EventDAO();
        setupTableColumns();
        setupSpinner();
        setupComboBoxes();
        loadBloodUnits();
        setupDonorComboListener();
        setupTableSelectionListener();
    }

    private void setupTableColumns() {
        unitIdColumn.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        bloodGroupColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volumeMl"));
        collectionDateColumn.setCellValueFactory(new PropertyValueFactory<>("collectionDate"));
        expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupSpinner() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 450);
        volumeSpinner.setValueFactory(valueFactory);
    }

    private void setupComboBoxes() {
        try {
            ObservableList<Donor> donors = FXCollections.observableArrayList(donorDAO.getAllDonors());
            donorCombo.setItems(donors);

            ObservableList<DonationEvent> events = FXCollections.observableArrayList(eventDAO.getAllEvents());
            eventCombo.setItems(events);

            ObservableList<String> statuses = FXCollections.observableArrayList(
                    "Available", "Distributed", "Expired", "Discarded"
            );
            statusCombo.setItems(statuses);
        } catch (SQLException e) {
            showError("Error loading data: " + e.getMessage());
        }
    }

    private void setupDonorComboListener() {
        donorCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                bloodGroupField.setText(newVal.getBloodGroup());
                donorDetailsLabel.setText("Donor: " + newVal.getName() + " | Blood: " + newVal.getBloodGroup());
                collectionDatePicker.setValue(LocalDate.now());
                expiryDatePicker.setValue(LocalDate.now().plusDays(42));
            }
        });
    }

    private void loadBloodUnits() {
        try {
            unitList = FXCollections.observableArrayList(bloodUnitDAO.getAllBloodUnits());
            bloodUnitTable.setItems(unitList);
            infoLabel.setText("Loaded " + unitList.size() + " blood units");
        } catch (SQLException e) {
            showError("Error loading blood units: " + e.getMessage());
        }
    }

    private void setupTableSelectionListener() {
        bloodUnitTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateFields(newVal);
            }
        });
    }

    private void populateFields(BloodUnit unit) {
        try {
            donorCombo.setValue(donorDAO.getDonorById(unit.getDonorId()));
            eventCombo.setValue(eventDAO.getEventById(unit.getEventId()));
            bloodGroupField.setText(unit.getBloodGroup());
            volumeSpinner.getValueFactory().setValue(unit.getVolumeMl());
            collectionDatePicker.setValue(unit.getCollectionDate());
            expiryDatePicker.setValue(unit.getExpiryDate());
            testedCheckBox.setSelected(unit.isTested());
            statusCombo.setValue(unit.getStatus());
        } catch (SQLException e) {
            showError("Error loading unit details: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddBloodUnit() {
        if (!validateInput()) return;

        try {
            BloodUnit unit = new BloodUnit(
                    donorCombo.getValue().getDonorId(),
                    eventCombo.getValue().getEventId(),
                    bloodGroupField.getText(),
                    volumeSpinner.getValue(),
                    collectionDatePicker.getValue(),
                    expiryDatePicker.getValue(),
                    testedCheckBox.isSelected()
            );
            bloodUnitDAO.addBloodUnit(unit);
            infoLabel.setText("Blood unit added successfully");
            loadBloodUnits();
            clearFields();
        } catch (SQLException e) {
            showError("Error adding blood unit: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateBloodUnit() {
        BloodUnit selected = bloodUnitTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a blood unit to update");
            return;
        }

        if (!validateInput()) return;

        try {
            selected.setDonorId(donorCombo.getValue().getDonorId());
            selected.setEventId(eventCombo.getValue().getEventId());
            selected.setBloodGroup(bloodGroupField.getText());
            selected.setVolumeMl(volumeSpinner.getValue());
            selected.setCollectionDate(collectionDatePicker.getValue());
            selected.setExpiryDate(expiryDatePicker.getValue());
            selected.setTested(testedCheckBox.isSelected());
            selected.setStatus(statusCombo.getValue());

            bloodUnitDAO.updateBloodUnit(selected);
            infoLabel.setText("Blood unit updated successfully");
            loadBloodUnits();
            clearFields();
        } catch (SQLException e) {
            showError("Error updating blood unit: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteBloodUnit() {
        BloodUnit selected = bloodUnitTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a blood unit to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Blood Unit");
        alert.setContentText("Are you sure you want to delete this blood unit?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                bloodUnitDAO.deleteBloodUnit(selected.getUnitId());
                infoLabel.setText("Blood unit deleted successfully");
                loadBloodUnits();
                clearFields();
            } catch (SQLException e) {
                showError("Error deleting blood unit: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadBloodUnits();
        clearFields();
        setupComboBoxes();
    }

    @FXML
    private void handleClear() {
        clearFields();
        bloodUnitTable.getSelectionModel().clearSelection();
    }

    private void clearFields() {
        donorCombo.setValue(null);
        eventCombo.setValue(null);
        bloodGroupField.clear();
        volumeSpinner.getValueFactory().setValue(450);
        collectionDatePicker.setValue(null);
        expiryDatePicker.setValue(null);
        testedCheckBox.setSelected(false);
        statusCombo.setValue(null);
        donorDetailsLabel.setText("Select a donor");
    }

    private boolean validateInput() {
        if (donorCombo.getValue() == null || eventCombo.getValue() == null ||
                bloodGroupField.getText().isEmpty() || collectionDatePicker.getValue() == null ||
                expiryDatePicker.getValue() == null) {
            showError("Please fill all required fields");
            return false;
        }
        if (expiryDatePicker.getValue().isBefore(collectionDatePicker.getValue())) {
            showError("Expiry date must be after collection date");
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
