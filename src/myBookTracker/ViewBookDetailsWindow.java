package myBookTracker;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class ViewBookDetailsWindow extends Stage {
    private Book book;

    public ViewBookDetailsWindow(Book book) {
        this.book = book;
        setTitle("Book Details");
        
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(25));
        mainLayout.setStyle("-fx-background-color: #ecf0f1;");
        
        // Header with title
        VBox headerBox = new VBox(5);
        Label titleLabel = new Label(book.getTitle());
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label authorLabel = new Label("by " + book.getAuthor());
        authorLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        
        headerBox.getChildren().addAll(titleLabel, authorLabel);
        
        // Info cards
        VBox infoCards = new VBox(15);
        
        // Basic Info Card
        VBox basicCard = createInfoCard("Basic Information");
        GridPane basicGrid = new GridPane();
        basicGrid.setHgap(15);
        basicGrid.setVgap(10);
        
        addInfoRow(basicGrid, 0, "Type:", book.getType(), getTypeColor(book.getType()));
        addInfoRow(basicGrid, 1, "Status:", book.getStatus(), getStatusColor(book.getStatus()));
        
        String stars = "★".repeat((int) book.getRating()) + "☆".repeat(5 - (int) book.getRating());
        addInfoRow(basicGrid, 2, "Rating:", String.format("%s (%.1f/5.0)", stars, book.getRating()), "#f39c12");
        
        basicCard.getChildren().add(basicGrid);
        infoCards.getChildren().add(basicCard);
        
        // Type-specific details
        if (book instanceof Ebook) {
            Ebook ebook = (Ebook) book;
            VBox ebookCard = createInfoCard("E-book Details");
            GridPane ebookGrid = new GridPane();
            ebookGrid.setHgap(15);
            ebookGrid.setVgap(10);
            
            addInfoRow(ebookGrid, 0, "Format:", ebook.getFormat(), "#3498db");
            
            Label linkLabel = new Label("Download Link:");
            linkLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");
            
            Hyperlink linkHyper = new Hyperlink(ebook.getDownloadLink());
            linkHyper.setStyle("-fx-font-size: 13px;");
            
            ebookGrid.add(linkLabel, 0, 1);
            ebookGrid.add(linkHyper, 1, 1);
            
            ebookCard.getChildren().add(ebookGrid);
            infoCards.getChildren().add(ebookCard);
            
        } else if (book instanceof HardCopy) {
            HardCopy hardCopy = (HardCopy) book;
            VBox hardCopyCard = createInfoCard("Hard Copy Details");
            GridPane hardCopyGrid = new GridPane();
            hardCopyGrid.setHgap(15);
            hardCopyGrid.setVgap(10);
            
            addInfoRow(hardCopyGrid, 0, "Pages:", String.valueOf(hardCopy.getPages()), "#e67e22");
            addInfoRow(hardCopyGrid, 1, "Ownership:", hardCopy.isOwned() ? "✓ Owned" : "✗ Not Owned", 
                      hardCopy.isOwned() ? "#27ae60" : "#95a5a6");
            
            hardCopyCard.getChildren().add(hardCopyGrid);
            infoCards.getChildren().add(hardCopyCard);
        }
        
        // Review Card
        if (book.getReview() != null && !book.getReview().isEmpty()) {
            VBox reviewCard = createInfoCard("My Review");
            
            TextArea reviewText = new TextArea(book.getReview());
            reviewText.setWrapText(true);
            reviewText.setEditable(false);
            reviewText.setPrefRowCount(6);
            reviewText.setStyle("-fx-control-inner-background: #ffffff; -fx-font-size: 13px; -fx-text-fill: #2c3e50;");
            
            reviewCard.getChildren().add(reviewText);
            infoCards.getChildren().add(reviewCard);
        } else {
            VBox reviewCard = createInfoCard("My Review");
            Label noReview = new Label("No review yet");
            noReview.setStyle("-fx-font-style: italic; -fx-text-fill: #95a5a6;");
            reviewCard.getChildren().add(noReview);
            infoCards.getChildren().add(reviewCard);
        }
        
        // Close button
        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-font-size: 14px; -fx-padding: 10 30 10 30; -fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
        closeBtn.setOnAction(e -> close());
        
        HBox buttonBox = new HBox(closeBtn);
        buttonBox.setAlignment(Pos.CENTER);
        
        mainLayout.getChildren().addAll(headerBox, new Separator(), infoCards, buttonBox);
        
        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #ecf0f1; -fx-background-color: #ecf0f1;");
        
        Scene scene = new Scene(scrollPane, 550, 650);
        setScene(scene);
    }
    
    private VBox createInfoCard(String title) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        card.getChildren().add(titleLabel);
        return card;
    }
    
    private void addInfoRow(GridPane grid, int row, String label, String value, String color) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold; -fx-text-fill: #34495e;");
        
        Label valueNode = new Label(value);
        valueNode.setStyle("-fx-font-size: 13px; -fx-text-fill: " + color + "; -fx-font-weight: bold;");
        
        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }
    
    private String getTypeColor(String type) {
        return switch (type) {
            case "Ebook" -> "#3498db";
            case "Hard Copy" -> "#e67e22";
            default -> "#95a5a6";
        };
    }
    
    private String getStatusColor(String status) {
        return switch (status) {
            case "Read" -> "#27ae60";
            case "Currently Reading" -> "#f39c12";
            default -> "#95a5a6";
        };
    }
}