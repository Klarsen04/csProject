package myBookTracker;


public class HardCopy extends Book {
    private int pages;
    private boolean owned;

    public HardCopy(String title, String author, int pages, boolean owned) {
        super(title, author);
        this.pages = pages;
        this.owned = owned;
    }

    public int getPages() { return pages; }
    public boolean isOwned() { return owned; }

    @Override
    public String toString() {
        return super.toString() + " (Printed - " + pages + " pages, " + (owned ? "Owned" : "Not Owned") + ")";
    }
}
