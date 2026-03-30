package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainController {
    @FXML
    private VBox rootContainer;

    @FXML
    private Button btnCalculate;

    @FXML
    private Label lblDistance;

    @FXML
    private TextField txtDistance;

    @FXML
    private TextField txtConsumption;

    @FXML
    private Label lblConsumption;

    @FXML
    private TextField txtPrice;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblResult;

    @FXML
    private Button btnEN;

    @FXML
    private Button btnFR;

    @FXML
    private Button btnJP;

    @FXML
    private Button btnIR;

    private ResourceBundle resourceBundle;

    @FXML
    public void initialize() {
        System.out.println("Initializing Controller...");
        if (btnEN == null || btnFR == null || btnJP == null || btnIR == null || btnCalculate == null) {
            System.err.println("FXML components are NOT properly injected!");
        } else {
            setLanguage("en", "US"); // Default language
        }
    }

    public void onCalculateButton(ActionEvent actionEvent) {
        try {
            double totalFuel = (Double.parseDouble(txtConsumption.getText()) / 100) *  Double.parseDouble(txtDistance.getText());
            double totalCost = totalFuel * Double.parseDouble(txtPrice.getText());

            lblResult.setText(resourceBundle.getString("label.result") + " " + totalFuel + "L " + totalCost + "€");
            System.out.println(totalCost);
        } catch (NumberFormatException e) {
            lblResult.setText(resourceBundle.getString("error.invalidInput"));
        }
    }

    private void setLanguage(String language, String country) {
        Locale locale = new Locale(language, country);
        resourceBundle = ResourceBundle.getBundle("messages", locale);

        btnEN.setText(resourceBundle.getString("language.english"));
        btnFR.setText(resourceBundle.getString("language.french"));
        btnJP.setText(resourceBundle.getString("language.japanese"));
        btnIR.setText(resourceBundle.getString("language.persian"));
        lblDistance.setText(resourceBundle.getString("label.distance"));
        lblConsumption.setText(resourceBundle.getString("label.fuelConsumption"));
        lblPrice.setText(resourceBundle.getString("label.fuelPrice"));
        lblResult.setText(resourceBundle.getString("label.result"));
        btnCalculate.setText(resourceBundle.getString("button.calculate"));

        // ADD THIS LINE - Apply text direction based on locale
        applyTextDirection(locale);
    }

    private void applyTextDirection(Locale locale) {
        boolean isRTL = switch (locale.getLanguage()) {
            case "fa" -> true;
            default -> false;
        };

        Platform.runLater(() -> {
            if (rootContainer == null) return;

            rootContainer.setNodeOrientation(
                    isRTL
                            ? NodeOrientation.RIGHT_TO_LEFT
                            : NodeOrientation.LEFT_TO_RIGHT
            );

            // Let JavaFX handle layout naturally
            rootContainer.setAlignment(Pos.TOP_LEFT);

            rootContainer.requestLayout();
        });
    }

    public void btnEN(ActionEvent actionEvent) {
        setLanguage("en", "US");
    }

    public void btnFR(ActionEvent actionEvent) {
        setLanguage("fr", "FR");
    }

    public void btnJA(ActionEvent actionEvent) {
        setLanguage("ja", "JP");
    }

    public void btnFA(ActionEvent actionEvent) {
        setLanguage("fa", "IR");
    }
}
