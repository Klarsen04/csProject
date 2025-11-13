package myBookTracker;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library lib = new Library();
        Scanner sc = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n MyBookShelf Menu:");
            System.out.println("1. Add Book");
            System.out.println("2. List Books");
            System.out.println("3. Search Book");
            System.out.println("4. Change Book Status / Rating");
            System.out.println("5. Sort Alphabetically");
            System.out.println("6. Save to File");
            System.out.println("7. Load from File");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.println("\nSelect book type:");
                    System.out.println("1. Ebook");
                    System.out.println("2. Hard Copy");
                    System.out.println("3. Miscellaneous Book");
                    System.out.print("Enter choice: ");

                    int typeChoice;
                    try {
                        typeChoice = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Defaulting to Regular Book.");
                        typeChoice = 3;
                    }

                    System.out.print("Enter title: ");
                    String title = sc.nextLine().trim();
                    System.out.print("Enter author: ");
                    String author = sc.nextLine().trim();

                    Book newBook;

                    switch (typeChoice) {
                        case 1 -> {
                            String format = "";
                            boolean validFormat = false;
                            while (!validFormat) {
                                System.out.print("Enter format (PDF/EPUB/MOBI): ");
                                format = sc.nextLine().trim().toUpperCase();
                                try {
                                    if (format.equals("PDF") || format.equals("EPUB") || format.equals("MOBI")) {
                                        validFormat = true;
                                    } else {
                                        throw new IllegalArgumentException("Invalid format. Must be PDF, EPUB, or MOBI.");
                                    }
                                } catch (IllegalArgumentException e) {
                                    System.out.println(e.getMessage());
                                }
                            }

                            String link = "";
                            boolean validLink = false;
                            while (!validLink) {
                                System.out.print("Enter download link: ");
                                link = sc.nextLine().trim();
                                try {
                                    java.net.URL url = new java.net.URL(link);  // checks if it's a valid URL
                                    url.toURI(); // further validation
                                    validLink = true;
                                } catch (Exception e) {
                                    System.out.println("Invalid link. Please enter a valid URL (e.g., https://example.com).");
                                }
                            }

                            newBook = new Ebook(title, author, format, link);
                        }
                        case 2 -> {
                            int pages = -1;
                            while (pages < 0) {
                                System.out.print("Enter page count: ");
                                String input = sc.nextLine().trim();
                                try {
                                    pages = Integer.parseInt(input);
                                    if (pages < 0) {
                                        System.out.println("Page count cannot be negative. Try again.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid number. Please enter a valid integer.");
                                }
                            }

                            boolean owned = false;
                            while (true) {
                                System.out.print("Do you own this book? (yes/no): ");
                                String ownedInput = sc.nextLine().trim().toLowerCase();
                                if (ownedInput.equals("yes")) {
                                    owned = true;
                                    break;
                                } else if (ownedInput.equals("no")) {
                                    owned = false;
                                    break;
                                } else {
                                    System.out.println("Please answer 'yes' or 'no'.");
                                }
                            }

                            newBook = new HardCopy(title, author, pages, owned);
                        }

                        default -> {
                            newBook = new Book(title, author);
                        }
                    }

                    lib.addBook(newBook);
                    System.out.println("Book added!");
                }

                case 2 -> lib.listBooks();
                case 3 -> {
                    System.out.print("Enter title to search: ");
                    String search = sc.nextLine();
                    Book found = lib.searchByTitle(search);
                    if (found == null) System.out.println("Book not found.");
                    else System.out.println("Found: " + found);
                }
                case 4 -> {
                    System.out.print("Enter title to update: ");
                    String title = sc.nextLine();
                    Book b = lib.searchByTitle(title);

                    if (b == null) {
                        System.out.println("Book not found.");
                        break;
                    }

                    // ----- STATUS SECTION -----
                    String newStatus = "";
                    while (true) {
                        System.out.print("Enter new status (Read / Want to Read / Currently Reading): ");
                        newStatus = sc.nextLine().trim();

                        if (newStatus.equalsIgnoreCase("Read") ||
                                newStatus.equalsIgnoreCase("Want to Read") ||
                                newStatus.equalsIgnoreCase("Currently Reading")) {
                            b.setStatus(newStatus);
                            break;
                        } else {
                            System.out.println("Invalid status. Please choose one of: Read, Want to Read, Currently Reading.");
                        }
                    }

                    // ----- RATING SECTION -----
                    while (true) {
                        System.out.print("Enter rating (0–5): ");
                        String input = sc.nextLine().trim();
                        try {
                            double r = Double.parseDouble(input);
                            if (r >= 0 && r <= 5) {
                                b.setRating(r);
                                break;
                            } else {
                                System.out.println("Invalid rating. Must be between 0–5.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number (0–5).");
                        }
                    }

                    System.out.println("Book updated successfully!");
                }


                case 5 -> {
                    lib.sortAlphabetically();
                    System.out.println("Sorted alphabetically!");
                }
                case 6 -> lib.saveToFile("src/books.txt");
                case 7 -> lib.loadFromFile("src/books.txt");
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 0);

        sc.close();
    }
}
