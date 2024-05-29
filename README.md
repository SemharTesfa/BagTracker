# Bug Tracker Application

This is a Bug Tracker application developed using Java and JavaFX. The application helps in managing projects, tickets, and comments related to bugs and issues.

## Features
- Create and manage projects
- Create and manage tickets within projects
- Add and manage comments on tickets
- Connect to SQLite database for persistent storage

## Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- JavaFX SDK
- SQLite database

### Steps
1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/bug-tracker.git
   
2. Navigate to the project directory:
   ```sh
   cd bug-tracker

3. Download JavaFX SDK from GluonHQ
   Add JavaFX libraries to your project's classpath
   Compile and run the application:

   ```sh
   javac -d bin -cp /path/to/javafx-sdk/lib --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml src/*.java
   java -cp bin --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml Main

### Usage
Create a New Project
Click on the "Create New Project" button
Fill in the project details and save
Create a New Ticket
Select a project
Click on the "Create New Ticket" button
Fill in the ticket details and save
Add Comments
Select a ticket
Click on the "Add Comments" button
Fill in the comment details and save
View Projects, Tickets, and Comments
SqliteConnection.java: Manages the connection to the SQLite database.
   AddComments: Manages the addition of comments to tickets.
   CommentsList: Lists all comments.
   ProjectList: Lists all projects.
   TicketList: Lists all tickets.
