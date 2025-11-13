package myBookTracker;

public class Book {
    private String title;
    private String author;
    private String status; // "Read", "Want to Read", "Currently Reading", "Owned"
    private double rating; // from 0.0 to 5.0
    private String review;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.status = "Want to Read";
        this.rating = 0.0;
        this.review = "";
    }

    // Getters and setters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getStatus() { return status; }
    public double getRating() { return rating; }
    public String getReview() { return review; }

    public void setStatus(String status) { this.status = status; }
    public void setRating(double rating) { this.rating = rating; }
    public void setReview(String review) { this.review = review; }

    @Override
    public String toString() {
        return String.format("%s by %s [%s] ★%.1f", title, author, status, rating);
    }
}

