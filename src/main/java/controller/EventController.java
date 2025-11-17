package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import dao.EventDAO;
import dao.BloodUnitDAO;
import model.DonationEvent;
import model.BloodUnit;
import java.sql.SQLException;
import java.time.LocalDate;

public class EventController {
    @FXML
    private TableView<DonationEvent> eventTable;
    @FXML
    private TableColumn<DonationEvent, Integer> idColumn;
    @FXML
    private TableColumn<DonationEvent, String> eventNameColumn;
    @FXML
    private TableColumn<DonationEvent, LocalDate> eventDateColumn;
    @FXML
    private TableColumn<DonationEvent, String> locationColumn;
    @FXML
    private TableColumn<DonationEvent, Integer> targetUnitsColumn;

    @FXML
    private TableView<BloodUnit> bloodUnitsTable;
    @FXML
    private TableColumn<BloodUnit, Integer> unitIdColumn;
    @FXML
    private TableColumn<BloodUnit, String> bloodGroupColumn;
    @FXML
    private TableColumn<BloodUnit, Integer> volumeColumn;
    @FXML
    private TableColumn<BloodUnit, String> statusColumn;

    @FXML
    private TextField eventNameField;
    @FXML
    private DatePicker eventDatePicker;
    @FXML
    private TextField locationField;
    @FXML
    private TextField organizerField;
    @FXML
    private Spinner<Integer> targetUnitsSpinner;

    @FXML
    private Label infoLabel;
    @FXML
    private Label statisticsLabel;

    private EventDAO eventDAO;
    private BloodUnitDAO bloodUnitDAO;
    private ObservableList<DonationEvent> eventList;

    @FXML
    public void initialize() {
        eventDAO = new EventDAO();
        bloodUnitDAO = new BloodUnitDAO();
        setupTableColumns();
        setupSpinner();
        loadEvents();
        setupTableSelectionListener();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        eventDateColumn.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        targetUnitsColumn.setCellValueFactory(new PropertyValueFactory<>("targetUnits"));

        unitIdColumn.setCellValueFactory(new PropertyValueFactory<>("unitId"));
        bloodGroupColumn.setCellValueFactory(new PropertyValueFactory<>("bloodGroup"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volumeMl"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupSpinner() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 50);
        targetUnitsSpinner.setValueFactory(valueFactory);
    }

    private void loadEvents() {
        try {
            eventList = FXCollections.observableArrayList(eventDAO.getAllEvents());
            eventTable.setItems(eventList);
            infoLabel.setText("Loaded " + eventList.size() + " events");
        } catch (SQLException e) {
            showError("Error loading events: " + e.getMessage());
        }
    }

    private void setupTableSelectionListener() {
        eventTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateEventFields(newVal);
                loadBloodUnitsForEvent(newVal.getEventId());
            }
        });
    }

    private void populateEventFields(DonationEvent event) {
        eventNameField.setText(event.getEventName());
        eventDatePicker.setValue(event.getEventDate());
        locationField.setText(event.getLocation());
        organizerField.setText(event.getOrganizer());
        targetUnitsSpinner.getValueFactory().setValue(event.getTargetUnits());
    }

    private void loadBloodUnitsForEvent(int eventId) {
        try {
            ObservableList<BloodUnit> units = FXCollections.observableArrayList(
                    bloodUnitDAO.getBloodUnitsByEvent(eventId)
            );
            bloodUnitsTable.setItems(units);
            updateStatistics(units);
        } catch (SQLException e) {
            showError("Error loading blood units: " + e.getMessage());
        }
    }

    private void updateStatistics(ObservableList<BloodUnit> units) {
        int totalUnits = units.size();
        int testedUnits = (int) units.stream().filter(BloodUnit::isTested).count();
        int totalVolume = units.stream().mapToInt(BloodUnit::getVolumeMl).sum();

        statisticsLabel.setText(String.format(
                "Total Units: %d | Tested: %d | Total Volume: %d ml",
                totalUnits, testedUnits, totalVolume
        ));
    }

    @FXML
    private void handleAddEvent() {
        if (!validateInput()) return;

        try {
            DonationEvent event = new DonationEvent(
                    eventNameField.getText(),
                    eventDatePicker.getValue(),
                    locationField.getText(),
                    organizerField.getText(),
                    targetUnitsSpinner.getValue()
            );
            eventDAO.addEvent(event);
            infoLabel.setText("Event added successfully");
            loadEvents();
            clearFields();
        } catch (SQLException e) {
            showError("Error adding event: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdateEvent() {
        DonationEvent selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an event to update");
            return;
        }

        if (!validateInput()) return;

        try {
            selected.setEventName(eventNameField.getText());
            selected.setEventDate(eventDatePicker.getValue());
            selected.setLocation(locationField.getText());
            selected.setOrganizer(organizerField.getText());
            selected.setTargetUnits(targetUnitsSpinner.getValue());

            eventDAO.updateEvent(selected);
            infoLabel.setText("Event updated successfully");
            loadEvents();
            clearFields();
        } catch (SQLException e) {
            showError("Error updating event: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteEvent() {
        DonationEvent selected = eventTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an event to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Event");
        alert.setContentText("Are you sure you want to delete this event? Related blood units will also be deleted.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                eventDAO.deleteEvent(selected.getEventId());
                infoLabel.setText("Event deleted successfully");
                loadEvents();
                clearFields();
                bloodUnitsTable.getItems().clear();
            } catch (SQLException e) {
                showError("Error deleting event: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleRefresh() {
        loadEvents();
        clearFields();
        bloodUnitsTable.getItems().clear();
    }

    @FXML
    private void handleClear() {
        clearFields();
        eventTable.getSelectionModel().clearSelection();
        bloodUnitsTable.getItems().clear();
    }

    private void clearFields() {
        eventNameField.clear();
        eventDatePicker.setValue(null);
        locationField.clear();
        organizerField.clear();
        targetUnitsSpinner.getValueFactory().setValue(50);
    }

    private boolean validateInput() {
        if (eventNameField.getText().isEmpty() || eventDatePicker.getValue() == null ||
                locationField.getText().isEmpty()) {
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
