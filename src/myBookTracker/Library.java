package myBookTracker;

import java.io.*;
import java.util.*;

public class Library {
    private ArrayList<Book> books = new ArrayList<>();

    public void addBook(Book b) {
        books.add(b);
    }
    public java.util.List<Book> getBooks() {
        return books;
    }


    public Book searchByTitle(String title) {
        // Exact match first
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                return b;
            }
        }
        
        // Partial match if no exact match
        for (Book b : books) {
            if (b.getTitle().toLowerCase().contains(title.toLowerCase())) {
                return b;
            }
        }
        
        return null;
    }

    public void sortAlphabetically() {
        books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
    }

    public void listBooks() {
        if (books.isEmpty()) System.out.println("No books found.");
        else books.forEach(System.out::println);
    }

    public void saveToFile(String filename) {
        try {
            // Create parent directory if it doesn't exist
            File file = new File(filename);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
                for (Book b : books) {
                    // Save book type and all relevant data
                    String bookType = b.getType();
                    String review = b.getReview().replace(",", ";;").replace("\n", "\\n");
                    
                    if (b instanceof Ebook) {
                        Ebook ebook = (Ebook) b;
                        out.printf("EBOOK,%s,%s,%s,%.1f,%s,%s,%s%n",
                            b.getTitle(), b.getAuthor(), b.getStatus(), b.getRating(), 
                            review, ebook.getFormat(), ebook.getDownloadLink());
                    } else if (b instanceof HardCopy) {
                        HardCopy hardCopy = (HardCopy) b;
                        out.printf("HARDCOPY,%s,%s,%s,%.1f,%s,%d,%b%n",
                            b.getTitle(), b.getAuthor(), b.getStatus(), b.getRating(), 
                            review, hardCopy.getPages(), hardCopy.isOwned());
                    } else {
                        out.printf("BOOK,%s,%s,%s,%.1f,%s%n",
                            b.getTitle(), b.getAuthor(), b.getStatus(), b.getRating(), review);
                    }
                }
                System.out.println("✅ Library saved to file: " + filename);
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving file: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        try {
            File file = new File(filename);
            
            // If file doesn't exist, create it
            if (!file.exists()) {
                System.out.println("ℹ️ No existing library file found. Starting with empty library.");
                books.clear();
                return;
            }
            
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                books.clear();
                String line;
                int lineNumber = 0;
                
                while ((line = br.readLine()) != null) {
                    lineNumber++;
                    line = line.trim();
                    
                    if (line.isEmpty()) continue;
                    
                    try {
                        String[] parts = line.split(",");
                        
                        if (parts.length < 5) {
                            System.out.println("⚠️ Skipping invalid line " + lineNumber);
                            continue;
                        }
                        
                        String type = parts[0].trim();
                        String title = parts[1].trim();
                        String author = parts[2].trim();
                        String status = parts[3].trim();
                        double rating = 0.0;
                        
                        try {
                            rating = Double.parseDouble(parts[4].trim());
                        } catch (NumberFormatException e) {
                            rating = 0.0;
                        }
                        
                        String review = parts.length > 5 ? parts[5].trim().replace(";;", ",").replace("\\n", "\n") : "";
                        
                        Book b;
                        
                        if (type.equals("EBOOK") && parts.length >= 8) {
                            String format = parts[6].trim();
                            String link = parts[7].trim();
                            b = new Ebook(title, author, format, link);
                        } else if (type.equals("HARDCOPY") && parts.length >= 8) {
                            int pages = Integer.parseInt(parts[6].trim());
                            boolean owned = Boolean.parseBoolean(parts[7].trim());
                            b = new HardCopy(title, author, pages, owned);
                        } else {
                            b = new Book(title, author);
                        }
                        
                        b.setStatus(status);
                        b.setRating(rating);
                        b.setReview(review);
                        books.add(b);
                        
                    } catch (Exception e) {
                        System.out.println("⚠️ Error parsing line " + lineNumber + ": " + e.getMessage());
                    }
                }

                System.out.println("✅ Library loaded from file.");

                if (books.isEmpty()) {
                    System.out.println("⚠️ No books found in file.");
                } else {
                    System.out.println("\n📖 Books currently in library: " + books.size());
                    for (Book b : books) {
                        System.out.println(b);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("⚠️ Error reading file: " + e.getMessage());
            books.clear();
        }
    }

}

