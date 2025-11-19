package myBookTracker;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class AddBookWindow extends Stage {
    private Library library;
    
    private TextField titleField = new TextField();
    private TextField authorField = new TextField();
    private ComboBox<String> typeCombo = new ComboBox<>();
    private TextField formatField = new TextField();
    private TextField linkField = new TextField();
    private TextField pagesField = new TextField();
    private CheckBox ownedCheckBox = new CheckBox("Owned");
    
    private VBox ebookFields = new VBox(10);
    private VBox hardcopyFields = new VBox(10);

    public AddBookWindow(Library library) {
        this.library = library;
        setTitle("Add New Book");
        
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        
        // Basic fields
        GridPane basicFields = new GridPane();
        basicFields.setHgap(10);
        basicFields.setVgap(10);
        
        basicFields.add(new Label("Title:"), 0, 0);
        basicFields.add(titleField, 1, 0);
        basicFields.add(new Label("Author:"), 0, 1);
        basicFields.add(authorField, 1, 1);
        basicFields.add(new Label("Type:"), 0, 2);
        
        typeCombo.getItems().addAll("Regular Book", "Ebook", "Hard Copy");
        typeCombo.setValue("Regular Book");
        basicFields.add(typeCombo, 1, 2);
        
        // Ebook specific fields
        ebookFields.getChildren().addAll(
            new Label("Format (PDF/EPUB/MOBI):"),
            formatField,
            new Label("Download Link (optional):"),
            linkField
        );
        linkField.setPromptText("Leave empty if no link available");
        ebookFields.setVisible(false);
        ebookFields.setManaged(false);
        
        // Hard copy specific fields
        hardcopyFields.getChildren().addAll(
            new Label("Number of Pages:"),
            pagesField,
            ownedCheckBox
        );
        hardcopyFields.setVisible(false);
        hardcopyFields.setManaged(false);
        
        // Type selection handler
        typeCombo.setOnAction(e -> {
            String selected = typeCombo.getValue();
            ebookFields.setVisible(selected.equals("Ebook"));
            ebookFields.setManaged(selected.equals("Ebook"));
            hardcopyFields.setVisible(selected.equals("Hard Copy"));
            hardcopyFields.setManaged(selected.equals("Hard Copy"));
        });
        
        // Buttons
        HBox buttonBox = new HBox(10);
        Button addButton = new Button("Add Book");
        Button cancelButton = new Button("Cancel");
        
        addButton.setOnAction(e -> addBook());
        cancelButton.setOnAction(e -> close());
        
        buttonBox.getChildren().addAll(addButton, cancelButton);
        
        mainLayout.getChildren().addAll(basicFields, ebookFields, hardcopyFields, buttonBox);
        
        Scene scene = new Scene(mainLayout, 400, 400);
        setScene(scene);
    }
    
    private void addBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        
        // Validate title and author
        if (title.isEmpty() || author.isEmpty()) {
            showAlert("Error", "Title and Author are required!");
            return;
        }
        
        // Check for duplicate books
        if (library.searchByTitle(title) != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Duplicate Book");
            confirm.setHeaderText("Book Already Exists");
            confirm.setContentText("A book with this title already exists. Add anyway?");
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                return;
            }
        }
        
        Book newBook;
        String type = typeCombo.getValue();
        
        try {
            if (type.equals("Ebook")) {
                String format = formatField.getText().trim().toUpperCase();
                String link = linkField.getText().trim();
                
                // Validate format
                if (format.isEmpty()) {
                    showAlert("Error", "Format is required for Ebooks!");
                    return;
                }
                
                if (!format.matches("PDF|EPUB|MOBI")) {
                    showAlert("Error", "Format must be PDF, EPUB, or MOBI");
                    return;
                }
                
                // Validate link if provided
                if (!link.isEmpty() && !isValidUrl(link)) {
                    showAlert("Error", "Invalid URL format! Please enter a valid URL starting with http:// or https://\nOr leave it empty if no link is available.");
                    return;
                }
                
                newBook = new Ebook(title, author, format, link.isEmpty() ? "No link" : link);
                
            } else if (type.equals("Hard Copy")) {
                String pagesText = pagesField.getText().trim();
                
                if (pagesText.isEmpty()) {
                    showAlert("Error", "Page count is required for Hard Copy books!");
                    return;
                }
                
                int pages;
                try {
                    pages = Integer.parseInt(pagesText);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Page count must be a valid number!");
                    return;
                }
                
                if (pages <= 0) {
                    showAlert("Error", "Page count must be greater than 0!");
                    return;
                }
                
                if (pages > 10000) {
                    showAlert("Error", "Page count seems too high! Please enter a reasonable number.");
                    return;
                }
                
                boolean owned = ownedCheckBox.isSelected();
                newBook = new HardCopy(title, author, pages, owned);
                
            } else {
                newBook = new Book(title, author);
            }
            
            library.addBook(newBook);
            showAlert("Success", "Book added successfully!");
            close();
            
        } catch (Exception e) {
            showAlert("Error", "Error adding book: " + e.getMessage());
        }
    }
    
    private boolean isValidUrl(String url) {
        // More strict URL validation
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return false;
        }
        
        try {
            java.net.URL urlObj = new java.net.URL(url);
            String host = urlObj.getHost();
            
            // Check if host has at least one dot (e.g., example.com)
            // or is localhost
            if (host.equals("localhost") || host.contains(".")) {
                return true;
            }
            
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Error") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}