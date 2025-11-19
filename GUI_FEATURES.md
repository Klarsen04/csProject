# My Book Tracker - GUI Features

## ✅ Feature Parity with Console App

The JavaFX GUI now has **complete feature parity** with the console application!

### Features Available in Both Apps:

1. **Add Book** ✅
   - Support for Regular Books, Ebooks, and Hard Copy books
   - Validation for all input fields
   - Format validation for Ebooks (PDF/EPUB/MOBI)
   - Page count validation for Hard Copy books

2. **Edit Book** ✅
   - Change book status (Read / Want to Read / Currently Reading)
   - Update rating (0.0 - 5.0 with 0.5 increments)
   - Add/edit review text
   - Shows book type and basic info

3. **Remove Book** ✅
   - Select and delete books from library
   - Confirmation dialog before deletion

4. **Search Book** ✅
   - Search by title (case-insensitive)
   - Highlights found book in the list
   - Clear search to show all books

5. **Sort Alphabetically** ✅
   - Sort all books A-Z by title
   - Case-insensitive sorting

6. **Save Library** ✅
   - Save all books to `src/books.txt`
   - Success confirmation

7. **Load Library** ✅
   - Load books from `src/books.txt`
   - Auto-loads on startup
   - Shows loaded books in list

## GUI Enhancements (Better than Console!)

- **Visual book list** - See all books at a glance
- **Search bar** - Quick search without navigating menus
- **Click to select** - Easy book selection for edit/delete
- **Spinners for ratings** - Easier than typing numbers
- **Dropdown for status** - No typos in status names
- **Review text area** - Multi-line reviews with word wrap
- **Confirmation dialogs** - Prevents accidental deletions
- **Real-time updates** - List refreshes automatically

## How to Run

### Console App:
```bash
mvn clean package
java -cp target/csProject-1.0-SNAPSHOT.jar myBookTracker.Main
```

### GUI App:
```bash
mvn javafx:run
```

Both apps share the same `books.txt` file, so you can switch between them!

## Files Created/Updated:

1. `MainApp.java` - Main GUI window with all features
2. `AddBookWindow.java` - Dialog for adding new books
3. `EditBookWindow.java` - Dialog for editing existing books (NEW!)
4. `pom.xml` - Maven configuration with JavaFX dependencies

## Summary

Your Book Tracker app now has:
- ✅ Full-featured console interface
- ✅ Full-featured GUI interface  
- ✅ Complete feature parity between both
- ✅ Shared data file (books.txt)
- ✅ Proper validation and error handling
- ✅ Support for 3 book types (Regular, Ebook, Hard Copy)