package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import java.io.IOException;

public class MainViewController {
    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab donorTab;
    @FXML
    private Tab eventTab;
    @FXML
    private Tab bloodUnitTab;
    @FXML
    private Tab distributionTab;

    @FXML
    public void initialize() {
        loadDonorView();
        loadEventView();
        loadBloodUnitView();
        loadDistributionView();
    }

    private void loadDonorView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DonorView.fxml"));
            Parent content = loader.load();
            donorTab.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadEventView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EventView.fxml"));
            Parent content = loader.load();
            eventTab.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBloodUnitView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BloodUnitView.fxml"));
            Parent content = loader.load();
            bloodUnitTab.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDistributionView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DistributionView.fxml"));
            Parent content = loader.load();
            distributionTab.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
