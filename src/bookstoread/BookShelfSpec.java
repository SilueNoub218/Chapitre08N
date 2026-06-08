package bookstoread;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Une bibliothèque")
class BookShelfSpec {

    private BookShelf shelf;

    private Book effectiveJava;
    private Book codeComplete;
    private Book mythicalManMonth;
    private Book cleanCode;

    @BeforeEach
    void init() {
        shelf = new BookShelf();

        effectiveJava = new Book(
                "Effective Java",
                "Joshua Bloch",
                LocalDate.of(2008, Month.MAY, 8));

        codeComplete = new Book(
                "Code Complete",
                "Steve McConnel",
                LocalDate.of(2004, Month.JUNE, 9));

        mythicalManMonth = new Book(
                "The Mythical Man-Month",
                "Frederick Phillips Brooks",
                LocalDate.of(1975, Month.JANUARY, 1));

        cleanCode = new Book(
                "Clean Code",
                "Robert C. Martin",
                LocalDate.of(2008, Month.AUGUST, 1));
    }

    @Nested
    @DisplayName("est vide")
    class EmptyShelf {

        @Test
        @DisplayName("quand aucun livre n'est ajouté")
        void shelfEmptyWhenNoBookAdded() {
            assertTrue(shelf.books().isEmpty());
        }

        @Test
        @DisplayName("quand add() est appelé sans argument")
        void emptyBookShelfWhenAddIsCalledWithoutBooks() {
            shelf.add();

            assertTrue(shelf.books().isEmpty());
        }
    }

    @Nested
    @DisplayName("après ajout de livres")
    class BooksAdded {

        @BeforeEach
        void addBooks() {
            shelf.add(effectiveJava, codeComplete, mythicalManMonth);
        }

        @Test
        @DisplayName("contient trois livres")
        void containsThreeBooks() {
            assertEquals(3, shelf.books().size());
        }

        @Test
        @DisplayName("retourne une collection immuable")
        void bookshelfIsImmutableForClient() {
            List<Book> books = shelf.books();

            assertThrows(
                    UnsupportedOperationException.class,
                    () -> books.add(cleanCode)
            );
        }

        @Test
        @DisplayName("peut être triée par titre")
        void bookshelfArrangedByTitle() {
            List<Book> books = shelf.arrange();

            assertEquals(
                    asList(codeComplete, effectiveJava, mythicalManMonth),
                    books
            );
        }

        @Test
        @DisplayName("conserve l'ordre d'insertion après arrange()")
        void insertionOrderPreserved() {
            shelf.arrange();

            assertEquals(
                    asList(effectiveJava, codeComplete, mythicalManMonth),
                    shelf.books()
            );
        }

        @Test
        @DisplayName("peut être triée selon un comparateur fourni")
        void arrangedByCustomCriteria() {
            List<Book> books =
                    shelf.arrange(Comparator.<Book>naturalOrder().reversed());

            assertEquals(
                    asList(mythicalManMonth, effectiveJava, codeComplete),
                    books
            );
        }
    }

    @Nested
    @DisplayName("regroupement des livres")
    class GroupingBooks {

        @BeforeEach
        void addBooks() {
            shelf.add(
                    effectiveJava,
                    codeComplete,
                    mythicalManMonth,
                    cleanCode
            );
        }

        @Test
        @DisplayName("par année de publication")
        void groupBooksByPublicationYear() {

            Map<Year, List<Book>> result =
                    shelf.groupByPublicationYear();

            assertThat(result)
                    .containsKey(Year.of(2008))
                    .containsValue(Arrays.asList(effectiveJava, cleanCode));

            assertThat(result)
                    .containsKey(Year.of(2004))
                    .containsValue(Collections.singletonList(codeComplete));

            assertThat(result)
                    .containsKey(Year.of(1975))
                    .containsValue(Collections.singletonList(mythicalManMonth));
        }

        @Test
        @DisplayName("par auteur")
        void groupBooksByAuthor() {

            Map<String, List<Book>> result =
                    shelf.groupBy(Book::getAuthor);

            assertThat(result)
                    .containsKey("Joshua Bloch")
                    .containsValue(Collections.singletonList(effectiveJava));

            assertThat(result)
                    .containsKey("Steve McConnel")
                    .containsValue(Collections.singletonList(codeComplete));

            assertThat(result)
                    .containsKey("Frederick Phillips Brooks")
                    .containsValue(Collections.singletonList(mythicalManMonth));

            assertThat(result)
                    .containsKey("Robert C. Martin")
                    .containsValue(Collections.singletonList(cleanCode));
        }
    }
}
