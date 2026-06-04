package bookstoread;

import java.util.*;

public class BookShelf {
    private final List<String> books = new ArrayList<>();

    public List<String> books() {
        return Collections.unmodifiableList(books);
    }

    public void add(String... bookToAdd) {
        books.addAll(Arrays.asList(bookToAdd));
    }

    public List<String> arrange() {
        List<String> sortedBooks = new ArrayList<>(books);
        sortedBooks.sort(Comparator.naturalOrder());
        return sortedBooks;
    }
}