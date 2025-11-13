package myBookTracker;

public class Ebook extends Book {
    private String format;  // e.g. "PDF", "EPUB", "MOBI"
    private String downloadLink;

    public Ebook(String title, String author, String format, String link) {
        super(title, author);
        this.format = format;
        this.downloadLink = link;
    }

    public String getFormat() { return format; }
    public String getDownloadLink() { return downloadLink; }

    @Override
    public String toString() {
        return super.toString() + " (Ebook - " + format + ", Link: " + downloadLink + ")";
    }

}

