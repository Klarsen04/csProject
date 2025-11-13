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
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(title)) {
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
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            for (Book b : books) {
                out.printf("%s,%s,%s,%.1f,%s%n",
                        b.getTitle(), b.getAuthor(), b.getStatus(), b.getRating(), b.getReview().replace(",", ";"));
            }
            System.out.println("Library saved to file: " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            books.clear();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 5); // only split into 5 parts max

                // skip any incomplete or empty lines
                if (parts.length < 4) continue;

                Book b = new Book(parts[0].trim(), parts[1].trim());
                b.setStatus(parts[2].trim());

                try {
                    b.setRating(Double.parseDouble(parts[3].trim()));
                } catch (NumberFormatException e) {
                    b.setRating(0.0);
                }

                b.setReview(parts.length == 5 ? parts[4].trim() : "");
                books.add(b);
            }

            System.out.println("✅ Library loaded from file.");

            // Automatically display what was loaded
            if (books.isEmpty()) {
                System.out.println("⚠️ No books found in file.");
            } else {
                System.out.println("\n📖 Books currently in library:");
                for (Book b : books) {
                    System.out.println(b);
                }
            }

        } catch (IOException e) {
            System.out.println("⚠️ Error reading file: " + e.getMessage());
        }
    }

}

