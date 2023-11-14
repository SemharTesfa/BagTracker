package application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CreateNewProject {
    public static void showCreateNewProject(Main main) {
        // Create a VBox for the new project page
        VBox newProjectBox = new VBox(10);
        newProjectBox.setStyle("-fx-background-color: Lavender; -fx-font-size: 12px; -fx-font-family: 'Comic Sans MS';");

        // Create input fields and buttons for the new project
        TextField newProjectField = new TextField();
        newProjectField.setStyle("-fx-prompt-text-fill: gray;");
        newProjectField.setPromptText("Enter New Project Name (required)");

        DatePicker newProjectDatePicker = new DatePicker();
        newProjectDatePicker.setValue(LocalDate.now());

        TextArea newProjectDescription = new TextArea();
        newProjectDescription.setPromptText("Project's Description");

        Button saveProjectButton = new Button("Save New Project");
        Button backButton = new Button("Back");

        // Define actions for the buttons
        saveProjectButton.setOnAction(event -> {
            // Check if a new project name is provided and if it doesn't already exist
            if (newProjectField.getText().isEmpty() || projectExists(main.connectToDatabase, newProjectField.getText())) {
                showError("Invalid Input", "Project name is required and must be unique.");
            } else {
                try {
                    // SQL query to insert a new project into the database
                    String insertProjectSQL = "INSERT INTO Projects (name, startDate, description) VALUES (?, ?, ?);";
                    PreparedStatement pstmt = main.connectToDatabase.prepareStatement(insertProjectSQL);
                    pstmt.setString(1, newProjectField.getText());
                    pstmt.setString(2, newProjectDatePicker.getValue().toString());
                    pstmt.setString(3, newProjectDescription.getText());
                    pstmt.executeUpdate();
                    pstmt.close(); // Close the PreparedStatement
                    main.startScreen(main.loadProjectNamesFromDatabase()); // Navigate back to the home page
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        backButton.setOnAction(event -> main.startScreen(main.loadProjectNamesFromDatabase())); // Navigate back to the home page

        // Add components to the new project VBox
        newProjectBox.getChildren().addAll(newProjectField, newProjectDatePicker, newProjectDescription, saveProjectButton, backButton);

        // Set the scene and show it
        main.projectStage.setScene(new Scene(newProjectBox, 650, 400));
        main.projectStage.setTitle(main.title);
        main.projectStage.show();
    }

    private static void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static boolean projectExists(Connection connection, String projectName) {
        try {
            String query = "SELECT name FROM Projects WHERE name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, projectName);
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean exists = resultSet.next();
            resultSet.close();
            preparedStatement.close();
            return exists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
