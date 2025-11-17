package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import dao.DonorDAO;
import model.Donor;
import java.sql.SQLException;
import java.time.LocalDate;

public class DonorController {
    @FXML
    private TableView<Donor> donorTable;
    @FXML
    private TableColumn<Donor, Integer> idColumn;
    @FXML
    private TableColumn<Donor, String> nameColumn;
    @FXML
    private TableColumn<Donor, String> phoneColumn;
    @FXML
    private TableColumn<Donor, String> emailColumn;
    @FXML
    private TableColumn<Donor, String> bloodGroupColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private ComboBox<String> bloodGroupCombo;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private TextArea addressField;

    @FXML
    private Label infoLabel;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button clearButton;

    private DonorDAO donorDAO;
    private ObservableList<Donor> donorList;

    @FXML
    public void initialize() {
        donorDAO = new DonorDAO();
        setupTableColumns();
        setupBloodGroupCombo();
        loadDonors();
        setupTableSelectionListener();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("donorId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        bloodGroupColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
    }

    private void setupBloodGroupCombo() {
        ObservableList<String> bloodGroups = FXCollections.observableArrayList(
                "O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"
        );
        bloodGroupCombo.setItems(bloodGroups);
    }

    private void loadDonors() {
        try {
            donorList = FXCollections.observableArrayList(donorDAO.getAllDonors());
            donorTable.setItems(donorList);
            infoLabel.setText("Loaded " + donorList.size() + " donors");
        } catch (SQLException e) {
            showError("Error loading donors: " + e.getMessage());
        }
    }

    private void setupTableSelectionListener() {
        donorTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateFields(newVal);
            }
        });
    }

    private void populateFields(Donor donor) {
        nameField.setText(donor.getName());
        phoneField.setText(donor.getPhone());
        emailField.setText(donor.getEmail());
        bloodGroupCombo.setValue(donor.getBloodGroup());
        dateOfBirthPicker.setValue(donor.getDateOfBirth());
        addressField.setText(donor.getAddress());
    }

    @FXML
    private void handleAddDonor() {
        if (!validateInput()) return;

        try {
            Donor donor = new Donor(
                    nameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    bloodGroupCombo.getValue(),
                    dateOfBirthPicker.getValue(),
                    addressField.getText()
            );
            donorDAO.addDonor(donor);
            infoLabel.setText("Donor added successfully");
            loadDonors();
            clearFields();
        } catch (SQLException e) {
            showError("Error adding donor: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateDonor() {
        Donor selected = donorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a donor to update");
            return;
        }

        if (!validateInput()) return;

        try {
            selected.setName(nameField.getText());
            selected.setPhone(phoneField.getText());
            selected.setEmail(emailField.getText());
            selected.setBloodGroup(bloodGroupCombo.getValue());
            selected.setDateOfBirth(dateOfBirthPicker.getValue());
            selected.setAddress(addressField.getText());

            donorDAO.updateDonor(selected);
            infoLabel.setText("Donor updated successfully");
            loadDonors();
            clearFields();
        } catch (SQLException e) {
            showError("Error updating donor: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteDonor() {
        Donor selected = donorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a donor to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Donor");
        alert.setContentText("Are you sure you want to delete this donor?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                donorDAO.deleteDonor(selected.getDonorId());
                infoLabel.setText("Donor deleted successfully");
                loadDonors();
                clearFields();
            } catch (SQLException e) {
                showError("Error deleting donor: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadDonors();
        clearFields();
    }

    @FXML
    private void handleClear() {
        clearFields();
        donorTable.getSelectionModel().clearSelection();
    }

    private void clearFields() {
        nameField.clear();
        phoneField.clear();
        emailField.clear();
        bloodGroupCombo.setValue(null);
        dateOfBirthPicker.setValue(null);
        addressField.clear();
    }

    private boolean validateInput() {
        if (nameField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                bloodGroupCombo.getValue() == null || dateOfBirthPicker.getValue() == null) {
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
