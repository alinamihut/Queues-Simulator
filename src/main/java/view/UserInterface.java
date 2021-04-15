package view;

import javafx.scene.control.*;

public class UserInterface {
    public static void showAlertForInvalidData() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText("Please enter valid data!");
        alert.show();
    }
}
