package myBookTracker;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class BookCell extends ListCell<Book> {
    private VBox content;
    private Label titleLabel;
    private Label authorLabel;
    private Label typeLabel;
    private Label statusLabel;
    private Label ratingLabel;
    private Label detailsLabel;

    public BookCell() {
        super();
        
        // Create labels
        titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        authorLabel = new Label();
        authorLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        
        typeLabel = new Label();
        typeLabel.setStyle("-fx-font-size: 11px; -fx-padding: 3 8 3 8; -fx-background-radius: 3; -fx-text-fill: white;");
        
        statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 11px; -fx-padding: 3 8 3 8; -fx-background-radius: 3;");
        
        ratingLabel = new Label();
        ratingLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
        
        detailsLabel = new Label();
        detailsLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #95a5a6; -fx-padding: 3 0 0 0;");
        
        // Layout
        VBox leftBox = new VBox(5);
        leftBox.getChildren().addAll(titleLabel, authorLabel, detailsLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        VBox rightBox = new VBox(5);
        rightBox.setAlignment(Pos.TOP_RIGHT);
        rightBox.getChildren().addAll(typeLabel, statusLabel, ratingLabel);
        
        HBox mainBox = new HBox(15);
        mainBox.getChildren().addAll(leftBox, spacer, rightBox);
        mainBox.setPadding(new Insets(10));
        mainBox.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-border-color: #ecf0f1; -fx-border-width: 1; -fx-border-radius: 5;");
        
        content = new VBox();
        content.getChildren().add(mainBox);
        content.setPadding(new Insets(5));
    }

    @Override
    protected void updateItem(Book book, boolean empty) {
        super.updateItem(book, empty);
        
        if (empty || book == null) {
            setGraphic(null);
        } else {
            // Title and Author
            titleLabel.setText(book.getTitle());
            authorLabel.setText("by " + book.getAuthor());
            
            // Type badge
            String type = book.getType();
            typeLabel.setText(type);
            if (type.equals("Ebook")) {
                typeLabel.setStyle(typeLabel.getStyle() + "-fx-background-color: #3498db;");
            } else if (type.equals("Hard Copy")) {
                typeLabel.setStyle(typeLabel.getStyle() + "-fx-background-color: #e67e22;");
            } else {
                typeLabel.setStyle(typeLabel.getStyle() + "-fx-background-color: #95a5a6;");
            }
            
            // Status badge
            String status = book.getStatus();
            statusLabel.setText(status);
            if (status.equals("Read")) {
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #27ae60; -fx-text-fill: white;");
            } else if (status.equals("Currently Reading")) {
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #f39c12; -fx-text-fill: white;");
            } else {
                statusLabel.setStyle(statusLabel.getStyle() + "-fx-background-color: #95a5a6; -fx-text-fill: white;");
            }
            
            // Rating with stars
            double rating = book.getRating();
            String stars = "★".repeat((int) rating) + "☆".repeat(5 - (int) rating);
            ratingLabel.setText(String.format("%s %.1f", stars, rating));
            if (rating >= 4.0) {
                ratingLabel.setStyle(ratingLabel.getStyle() + "-fx-text-fill: #f39c12;");
            } else if (rating >= 2.0) {
                ratingLabel.setStyle(ratingLabel.getStyle() + "-fx-text-fill: #95a5a6;");
            } else {
                ratingLabel.setStyle(ratingLabel.getStyle() + "-fx-text-fill: #bdc3c7;");
            }
            
            // Additional details
            StringBuilder details = new StringBuilder();
            if (book instanceof Ebook) {
                Ebook ebook = (Ebook) book;
                details.append("Format: ").append(ebook.getFormat());
            } else if (book instanceof HardCopy) {
                HardCopy hardCopy = (HardCopy) book;
                details.append(hardCopy.getPages()).append(" pages");
                if (hardCopy.isOwned()) {
                    details.append(" • Owned");
                }
            }
            
            if (book.getReview() != null && !book.getReview().isEmpty()) {
                if (details.length() > 0) details.append(" • ");
                String preview = book.getReview();
                if (preview.length() > 60) {
                    preview = preview.substring(0, 60) + "...";
                }
                details.append("Review: ").append(preview);
            }
            
            detailsLabel.setText(details.toString());
            if (details.length() == 0) {
                detailsLabel.setVisible(false);
                detailsLabel.setManaged(false);
            } else {
                detailsLabel.setVisible(true);
                detailsLabel.setManaged(true);
            }
            
            setGraphic(content);
        }
    }
}