# AGENTS.md

## Project Overview

**MyBookTracker** is a Java-based command-line application for managing personal book collections. The application allows users to track books they own, want to read, or are currently reading, with support for different book formats (ebooks and hard copies).

## Initial Project Setup (Nov 18, 2025)

### Commit: `b8a0395` - "Project without UI"

The foundational version of MyBookTracker was established with a complete CLI-based book tracking system.

#### Core Architecture

**Object-Oriented Design:**
- Implemented inheritance hierarchy with `Book` as the base class
- Created specialized subclasses: `Ebook` and `HardCopy`
- Each book type includes format-specific attributes and behaviors

**Key Classes:**

1. **Book.java** - Base class with core functionality:
   - Properties: title, author, status, rating (0.0-5.0), review
   - Default status: "Want to Read"
   - Supports status tracking: "Read", "Want to Read", "Currently Reading"

2. **Ebook.java** - Digital book format:
   - Additional properties: format (PDF/EPUB/MOBI), download link
   - URL validation for download links

3. **HardCopy.java** - Physical book format:
   - Additional properties: page count, ownership status
   - Tracks whether user owns the physical copy

4. **Library.java** - Collection manager:
   - ArrayList-based storage for books
   - Methods: addBook(), searchByTitle(), sortAlphabetically(), listBooks()
   - File I/O operations: saveToFile(), loadFromFile()
   - CSV-based persistence (books.txt)
   - Automatic display of loaded books with emoji indicators (✅, ⚠️, 📖)

5. **Main.java** - CLI interface with 8 menu options:
   - Add books with type selection (Ebook/Hard Copy/Miscellaneous)
   - List all books in library
   - Search by title (case-insensitive)
   - Update book status and rating
   - Sort alphabetically by title
   - Save/load library to/from file
   - Exit application

#### Input Validation

Robust validation implemented throughout:
- **Format validation**: Ebooks restricted to PDF/EPUB/MOBI formats
- **URL validation**: Download links validated using java.net.URL
- **Numeric validation**: Page counts must be non-negative, ratings between 0-5
- **Boolean validation**: Ownership questions require yes/no responses
- **Status validation**: Limited to predefined status options
- **Error handling**: Try-catch blocks for NumberFormatException and IOException

#### Data Persistence

- Books stored in CSV format in `src/books.txt`
- Fields: title, author, status, rating, review
- Commas in reviews replaced with semicolons to avoid parsing issues
- Automatic recovery from malformed data (skips incomplete lines, defaults invalid ratings to 0.0)

#### Development Environment

- **IDE**: IntelliJ IDEA with Java 23
- **License**: Apache License 2.0
- **Ignore patterns**: Configured for IntelliJ, Eclipse, NetBeans, VS Code, and macOS

## Technical Highlights

- **Java Features Used**: Switch expressions, try-with-resources, stream forEach
- **Design Patterns**: Template method (polymorphic toString()), factory-like book creation
- **File Format**: CSV with sanitization for special characters
- **User Experience**: Clear prompts, error messages, and automatic feedback on operations

## Future Considerations

While no additional commits have been made since the initial implementation, potential areas for enhancement include:
- Adding a GUI interface
- Database integration (moving beyond CSV)
- Advanced search (by author, rating, status)
- Book categories/genres
- Reading statistics and analytics
- Import/export functionality for different formats

---

*Last updated: 2025-11-19*
*Based on git history through commit b8a0395*
