package myBookTracker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MainApp extends Application {
    private Library library = new Library();
    private ListView<Book> bookListView = new ListView<>();
    private TextField searchField = new TextField();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("📚 My Book Tracker");

        // Create main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #ecf0f1;");
        
        // Create top search bar
        HBox searchBar = new HBox(10);
        searchBar.setPadding(new Insets(15));
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setStyle("-fx-background-color: #34495e;");
        
        Label searchLabel = new Label("🔍 Search:");
        searchLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        
        searchField.setPrefWidth(300);
        searchField.setPromptText("Enter book title to search...");
        searchField.setStyle("-fx-font-size: 13px; -fx-padding: 8;");
        
        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15; -fx-cursor: hand;");
        
        Button clearSearchBtn = new Button("Clear");
        clearSearchBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15; -fx-cursor: hand;");
        
        searchBar.getChildren().addAll(searchLabel, searchField, searchBtn, clearSearchBtn);
        
        // Create button panel
        VBox buttonPanel = new VBox(10);
        buttonPanel.setPadding(new Insets(15));
        buttonPanel.setStyle("-fx-background-color: white;");
        
        Button viewDetailsBtn = new Button("📖 View Details");
        Button addBookBtn = new Button("➕ Add Book");
        Button editBookBtn = new Button("✏️ Edit Book");
        Button removeBookBtn = new Button("🗑️ Remove Book");
        Button sortBtn = new Button("🔤 Sort A-Z");
        Button saveBtn = new Button("💾 Save Library");
        Button loadBtn = new Button("📂 Load Library");
        
        String buttonStyle = "-fx-font-size: 13px; -fx-padding: 10 15 10 15; -fx-cursor: hand; -fx-background-radius: 5;";
        
        viewDetailsBtn.setStyle(buttonStyle + "-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold;");
        addBookBtn.setStyle(buttonStyle + "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        editBookBtn.setStyle(buttonStyle + "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        removeBookBtn.setStyle(buttonStyle + "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        sortBtn.setStyle(buttonStyle + "-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold;");
        saveBtn.setStyle(buttonStyle + "-fx-background-color: #16a085; -fx-text-fill: white; -fx-font-weight: bold;");
        loadBtn.setStyle(buttonStyle + "-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold;");
        
        viewDetailsBtn.setPrefWidth(150);
        addBookBtn.setPrefWidth(150);
        editBookBtn.setPrefWidth(150);
        removeBookBtn.setPrefWidth(150);
        sortBtn.setPrefWidth(150);
        saveBtn.setPrefWidth(150);
        loadBtn.setPrefWidth(150);
        
        Separator sep1 = new Separator();
        Separator sep2 = new Separator();
        
        buttonPanel.getChildren().addAll(viewDetailsBtn, addBookBtn, editBookBtn, removeBookBtn, 
                                         sep1, sortBtn, sep2, saveBtn, loadBtn);
        
        // Set up book list view with custom cell factory
        bookListView.setPrefWidth(600);
        bookListView.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: transparent;");
        bookListView.setCellFactory(param -> new BookCell());
        
        // Add event handlers
        viewDetailsBtn.setOnAction(e -> {
            Book selected = bookListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Please select a book to view details!");
            } else {
                openViewDetailsWindow(selected);
            }
        });
        
        addBookBtn.setOnAction(e -> openAddBookWindow());
        
        editBookBtn.setOnAction(e -> {
            Book selected = bookListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Please select a book to edit!");
            } else {
                openEditBookWindow(selected);
            }
        });
        
        removeBookBtn.setOnAction(e -> {
            Book selected = bookListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "Please select a book to remove!");
            } else {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete");
                confirm.setHeaderText("Delete Book");
                confirm.setContentText("Are you sure you want to delete: " + selected.getTitle() + "?");
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        library.getBooks().remove(selected);
                        refreshBookList();
                        showAlert("Success", "Book removed successfully!");
                    }
                });
            }
        });
        
        sortBtn.setOnAction(e -> {
            library.sortAlphabetically();
            refreshBookList();
            showAlert("Success", "Books sorted alphabetically!");
        });
        
        searchBtn.setOnAction(e -> performSearch());
        clearSearchBtn.setOnAction(e -> {
            searchField.clear();
            refreshBookList();
        });
        
        searchField.setOnAction(e -> performSearch());
        
        saveBtn.setOnAction(e -> {
            library.saveToFile("src/books.txt");
            showAlert("Success", "Library saved successfully!");
        });
        
        loadBtn.setOnAction(e -> {
            library.loadFromFile("src/books.txt");
            refreshBookList();
        });
        
        mainLayout.setTop(searchBar);
        mainLayout.setLeft(buttonPanel);
        mainLayout.setCenter(bookListView);
        
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Load existing books
        library.loadFromFile("src/books.txt");
        refreshBookList();
    }
    
    private void performSearch() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            showAlert("Error", "Please enter a search term!");
            return;
        }
        
        Book found = library.searchByTitle(searchText);
        if (found == null) {
            showAlert("Not Found", "No book found with title: " + searchText);
        } else {
            bookListView.getItems().clear();
            bookListView.getItems().add(found);
            bookListView.getSelectionModel().select(found);
        }
    }
    
    private void openAddBookWindow() {
        AddBookWindow addWindow = new AddBookWindow(library);
        addWindow.show();
        addWindow.setOnHiding(e -> refreshBookList());
    }
    
    private void openViewDetailsWindow(Book book) {
        ViewBookDetailsWindow detailsWindow = new ViewBookDetailsWindow(book);
        detailsWindow.show();
    }
    
    private void openEditBookWindow(Book book) {
        EditBookWindow editWindow = new EditBookWindow(book);
        editWindow.show();
        editWindow.setOnHiding(e -> refreshBookList());
    }
    
    private void refreshBookList() {
        bookListView.getItems().clear();
        bookListView.getItems().addAll(library.getBooks());
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}