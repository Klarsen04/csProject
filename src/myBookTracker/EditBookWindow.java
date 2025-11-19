package myBookTracker;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class EditBookWindow extends Stage {
    private Book book;
    
    private ComboBox<String> statusCombo = new ComboBox<>();
    private Spinner<Double> ratingSpinner = new Spinner<>();
    private TextArea reviewArea = new TextArea();

    public EditBookWindow(Book book) {
        this.book = book;
        setTitle("Edit Book: " + book.getTitle());
        
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        
        // Book info (read-only)
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(10);
        
        infoGrid.add(new Label("Title:"), 0, 0);
        infoGrid.add(new Label(book.getTitle()), 1, 0);
        infoGrid.add(new Label("Author:"), 0, 1);
        infoGrid.add(new Label(book.getAuthor()), 1, 1);
        infoGrid.add(new Label("Type:"), 0, 2);
        infoGrid.add(new Label(book.getType()), 1, 2);
        
        // Editable fields
        GridPane editGrid = new GridPane();
        editGrid.setHgap(10);
        editGrid.setVgap(10);
        
        // Status dropdown
        statusCombo.getItems().addAll("Read", "Want to Read", "Currently Reading");
        statusCombo.setValue(book.getStatus());
        editGrid.add(new Label("Status:"), 0, 0);
        editGrid.add(statusCombo, 1, 0);
        
        // Rating spinner
        SpinnerValueFactory<Double> valueFactory = 
            new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 5.0, book.getRating(), 0.5);
        ratingSpinner.setValueFactory(valueFactory);
        ratingSpinner.setEditable(true);
        ratingSpinner.setPrefWidth(100);
        editGrid.add(new Label("Rating (0-5):"), 0, 1);
        editGrid.add(ratingSpinner, 1, 1);
        
        // Review text area
        reviewArea.setText(book.getReview());
        reviewArea.setPrefRowCount(5);
        reviewArea.setWrapText(true);
        reviewArea.setPromptText("Enter your review here...");
        
        // Clear review button
        Button clearReviewBtn = new Button("Clear Review");
        clearReviewBtn.setStyle("-fx-font-size: 11px;");
        clearReviewBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Clear Review");
            confirm.setHeaderText("Clear Review");
            confirm.setContentText("Are you sure you want to clear the review?");
            if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                reviewArea.clear();
            }
        });
        
        HBox reviewHeader = new HBox(10);
        reviewHeader.getChildren().addAll(new Label("Review:"), clearReviewBtn);
        
        VBox reviewBox = new VBox(5);
        reviewBox.getChildren().addAll(reviewHeader, reviewArea);
        
        // Buttons
        HBox buttonBox = new HBox(10);
        Button saveButton = new Button("Save Changes");
        Button cancelButton = new Button("Cancel");
        
        saveButton.setOnAction(e -> saveChanges());
        cancelButton.setOnAction(e -> close());
        
        buttonBox.getChildren().addAll(saveButton, cancelButton);
        
        // Add separator
        Separator sep1 = new Separator();
        Separator sep2 = new Separator();
        
        mainLayout.getChildren().addAll(
            new Label("Book Information:"),
            infoGrid,
            sep1,
            new Label("Edit Details:"),
            editGrid,
            reviewBox,
            sep2,
            buttonBox
        );
        
        Scene scene = new Scene(mainLayout, 450, 500);
        setScene(scene);
    }
    
    private void saveChanges() {
        try {
            String newStatus = statusCombo.getValue();
            String newReview = reviewArea.getText().trim();
            
            if (newStatus == null || newStatus.isEmpty()) {
                showAlert("Error", "Please select a status!");
                return;
            }
            
            // Validate rating from spinner
            double newRating;
            try {
                String ratingText = ratingSpinner.getEditor().getText().trim();
                newRating = Double.parseDouble(ratingText);
                
                if (newRating < 0.0 || newRating > 5.0) {
                    showAlert("Error", "Rating must be between 0.0 and 5.0!");
                    return;
                }
                
                // Round to nearest 0.5
                newRating = Math.round(newRating * 2) / 2.0;
                
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid rating (0.0 - 5.0)!");
                return;
            }
            
            book.setStatus(newStatus);
            book.setRating(newRating);
            book.setReview(newReview);
            
            showAlert("Success", "Book updated successfully!");
            close();
            
        } catch (Exception e) {
            showAlert("Error", "Error updating book: " + e.getMessage());
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

