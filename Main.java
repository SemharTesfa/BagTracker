package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ListView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//...
public class TicketList {
 @SuppressWarnings("unchecked")
public static void showTicketList(Main main, List<String> projectNames) {
     // Create a VBox for the ticket list page
     VBox ticketListBox = new VBox(10);
     ticketListBox.setStyle("-fx-font-size: 13px; -fx-font-family: 'Comic Sans MS';"); // Set background color
     ticketListBox.setAlignment(Pos.TOP_RIGHT);

     // Add a TextField for searching
     TextField searchField = new TextField();
     searchField.setStyle("-fx-font-size: 13px; -fx-font-family: 'Comic Sans MS';");
     searchField.setPromptText("🔍 Search tickets...");
     searchField.setMaxWidth(200);

     // Explicitly set prompt text color
     searchField.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);");

     // Create a ListView for search results
     ListView<String> searchResults = new ListView<>();
     searchResults.setStyle("-fx-font-size: 12px; -fx-font-family: 'Comic Sans MS';");
     searchResults.setMaxWidth(200);
     searchResults.setMaxHeight(100);
     searchResults.setVisible(false);

     // Create a table to display the tickets
     TableView<Ticket> ticketTable = new TableView<>();
     TableColumn<Ticket, String> ticketTitleColumn = new TableColumn<>("Ticket title");
     TableColumn<Ticket, String> projectColumn = new TableColumn<>("Project Name");
     TableColumn<Ticket, String> descriptionColumn = new TableColumn<>("Description");

     ticketTitleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
     projectColumn.setCellValueFactory(cellData -> cellData.getValue().projectNameProperty());
     descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

     ticketTable.getColumns().addAll(ticketTitleColumn, projectColumn, descriptionColumn);

     // Set the column resizing policy to evenly distribute space
     ticketTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

     // Fetch tickets for the selected project names
     List<Ticket> tickets = getTicketsForProjects(main.connectToDatabase, projectNames);
     ObservableList<Ticket> ticketsData = FXCollections.observableArrayList(tickets);
     ticketTable.setItems(ticketsData);

     // Add a ChangeListener to the search field
     searchField.textProperty().addListener((observable, oldValue, newValue) -> {
         performSearch(newValue, ticketsData, ticketTable, searchResults);
     });

     // Add a key event handler to the root container (VBox in this case)
     ticketListBox.setOnKeyPressed(event -> {
         if (event.getCode() == KeyCode.ESCAPE) {
             // Handle the ESC key press
             searchField.clear();
             searchResults.setVisible(false);
             ticketTable.setItems(ticketsData); // Reset TableView to show all tickets
         }
     });

     // Create a "Back" button
     Button backButton = new Button("Back");
     backButton.setAlignment(Pos.BASELINE_LEFT);
     backButton.setOnAction(event -> main.startScreen(main.loadProjectNamesFromDatabase()));

     // Create a StackPane to overlay the TableView and search results
     StackPane stackPane = new StackPane();
     stackPane.getChildren().addAll(ticketTable, searchResults);
     StackPane.setAlignment(searchResults, Pos.TOP_RIGHT); // Align search results to the top right

     ticketListBox.setPadding(new Insets(20, 20, 20, 20));

     // Add components to the ticket list VBox
     ticketListBox.getChildren().addAll(searchField, stackPane, backButton);

     // Set the scene and show it
     main.projectStage.setScene(new Scene(ticketListBox, 650, 400)); // Keep the original size
     main.projectStage.setTitle("Ticket List");
     main.projectStage.show();
 }

    private static List<Ticket> getTicketsForProjects(Connection connection, List<String> projectNames) {
        List<Ticket> tickets = new ArrayList<>();
        try {
            for (String projectName : projectNames) {
                String query = "SELECT T.title, T.description, P.name AS project_name " +
                        "FROM Tickets T " +
                        "INNER JOIN Projects P ON T.project_name = P.name " +
                        "WHERE P.name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, projectName);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    String projectNameForTicket = resultSet.getString("project_name");
                    tickets.add(new Ticket(title, description, projectNameForTicket));
                }

                resultSet.close();
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    private static void performSearch(String query, ObservableList<Ticket> ticketsData,
                                      TableView<Ticket> ticketTable, ListView<String> searchResults) {
        searchResults.getItems().clear();

        if (query.isEmpty()) {
            searchResults.setVisible(false);
            ticketTable.setItems(ticketsData);
        } else {
            searchResults.setVisible(true);

            ObservableList<String> searchResultsData = FXCollections.observableArrayList();
            for (Ticket ticket : ticketsData) {
                if (ticket.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    searchResultsData.add(ticket.getTitle()); // Add ticket title to search results
                }
            }

            if (searchResultsData.isEmpty()) {
                searchResults.getItems().add("No results found");
            } else {
                searchResults.setItems(searchResultsData);
            }
        }
    }
}
