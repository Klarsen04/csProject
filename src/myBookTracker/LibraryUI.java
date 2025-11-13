package myBookTracker;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibraryUI {
    private Library library;
    private JTextArea textArea;

    public LibraryUI() {
        library = new Library();
        createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("📚 My Book Library");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        // ----- Top panel with buttons -----
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Book");
        JButton viewButton = new JButton("View Books");
        JButton searchButton = new JButton("Search Book");
        JButton updateButton = new JButton("Update Status/Rating");
        JButton sortButton = new JButton("Sort Alphabetically");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(sortButton);

        frame.add(buttonPanel, BorderLayout.NORTH);

        // ----- Text area to display books -----
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // ----- Button Actions -----
        addButton.addActionListener(e -> addBookAction());
        viewButton.addActionListener(e -> viewBooks());
        searchButton.addActionListener(e -> searchBook());
        updateButton.addActionListener(e -> updateBook());
        sortButton.addActionListener(e -> {
            library.sortAlphabetically();
            textArea.append("✅ Library sorted alphabetically.\n");
        });

        frame.setVisible(true);
    }

    // ----- Add Book Action -----
    private void addBookAction() {
        String[] types = {"Ebook", "Hard Copy", "Misc"};
        String type = (String) JOptionPane.showInputDialog(null, "Select Book Type:", "Add Book",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
        if (type == null) return;

        String title = JOptionPane.showInputDialog("Enter Title:");
        if (title == null) return;

        String author = JOptionPane.showInputDialog("Enter Author:");
        if (author == null) return;

        Book newBook;
        switch (type) {
            case "Ebook" -> {
                String format;
                while (true) {
                    format = JOptionPane.showInputDialog("Enter format (PDF/EPUB/MOBI):").trim().toUpperCase();
                    if (format.equals("PDF") || format.equals("EPUB") || format.equals("MOBI")) break;
                    JOptionPane.showMessageDialog(null, "Invalid format. Must be PDF, EPUB, or MOBI.");
                }

                String link;
                while (true) {
                    link = JOptionPane.showInputDialog("Enter download link:").trim();
                    try {
                        java.net.URL url = new java.net.URL(link);
                        url.toURI();
                        break;
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Invalid link. Please enter a valid URL.");
                    }
                }

                newBook = new Ebook(title, author, format, link);
            }
            case "Hard Copy" -> {
                int pages = -1;
                while (pages < 0) {
                    String input = JOptionPane.showInputDialog("Enter page count:");
                    try {
                        pages = Integer.parseInt(input);
                        if (pages < 0) JOptionPane.showMessageDialog(null, "Page count cannot be negative.");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid number. Please enter an integer.");
                    }
                }

                boolean owned = JOptionPane.showConfirmDialog(null, "Do you own this book?", "Owned?",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

                newBook = new HardCopy(title, author, pages, owned);
            }
            default -> newBook = new Book(title, author);
        }

        library.addBook(newBook);
        textArea.append("✅ Book added: " + title + "\n");
    }

    // ----- View Books -----
    private void viewBooks() {
        textArea.setText("");
        List<Book> books = library.getBooks();
        if (books.isEmpty()) {
            textArea.append("No books in library.\n");
        } else {
            for (Book b : books) textArea.append(b.toString() + "\n");
        }
    }

    // ----- Search Book -----
    private void searchBook() {
        String searchTitle = JOptionPane.showInputDialog("Enter Title to Search:");
        if (searchTitle == null) return;
        Book found = library.searchByTitle(searchTitle);
        textArea.setText("");
        if (found != null) textArea.append("Found: " + found + "\n");
        else textArea.append("Book not found.\n");
    }

    // ----- Update Status / Rating -----
    private void updateBook() {
        String title = JOptionPane.showInputDialog("Enter Title to Update:");
        if (title == null) return;
        Book b = library.searchByTitle(title);
        if (b == null) {
            textArea.append("Book not found.\n");
            return;
        }

        String[] statuses = {"Read", "Want to Read", "Currently Reading", "Owned"};
        String newStatus = (String) JOptionPane.showInputDialog(null, "Select new status:",
                "Update Status", JOptionPane.QUESTION_MESSAGE, null, statuses, b.getStatus());
        if (newStatus != null) b.setStatus(newStatus);

        while (true) {
            String ratingStr = JOptionPane.showInputDialog("Enter rating (0–5):", b.getRating());
            if (ratingStr == null) break;
            try {
                double rating = Double.parseDouble(ratingStr);
                if (rating >= 0 && rating <= 5) {
                    b.setRating(rating);
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Rating must be between 0 and 5.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            }
        }

        textArea.append("✅ Book updated: " + b.getTitle() + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryUI::new);
    }
}
