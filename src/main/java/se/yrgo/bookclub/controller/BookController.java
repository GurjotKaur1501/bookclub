package se.yrgo.bookclub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.yrgo.bookclub.domain.Book;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BookController {

    // A helper method to create a sample list of Book objects.
    private List<Book> createSampleBooks() {
        List<Book> list = new ArrayList<>();
        list.add(new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy"));
        list.add(new Book("1984", "George Orwell", "Dystopian"));
        list.add(new Book("Brave New World", "Aldous Huxley", "Dystopian"));
        list.add(new Book("Pride and Prejudice", "Jane Austen", "Romance"));
        list.add(new Book("The Name of the Wind", "Patrick Rothfuss", "Fantasy"));
        list.add(new Book("The Catcher in the Rye", "J.D. Salinger", "Classic"));
        list.add(new Book("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "Non-fiction"));
        list.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "Classic"));
        // Add more if you like
        return list;
    }

    @GetMapping("/home")
    public String home(Model model) {
        // Today's date
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Greeting message
        String greeting = "Välkommen till Bokklubben!"; // Swedish greeting as an example

        model.addAttribute("date", dateStr);
        model.addAttribute("greeting", greeting);
        model.addAttribute("group", "se.yrgo"); // include group name in the model

        // Return the thymeleaf template name (templates/home.html)
        return "home";
    }

    @GetMapping("/books")
    public String books(Model model) {
        List<Book> books = createSampleBooks();
        model.addAttribute("books", books);
        model.addAttribute("group", "se.yrgo");
        return "booklist";
    }

    @GetMapping("/genre")
    public String genre(@RequestParam(name = "type", required = false) String type, Model model) {
        List<Book> books = createSampleBooks();

        // Build set of available genres
        Set<String> genres = books.stream()
                .map(Book::getGenre)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new)); // sorted

        model.addAttribute("genres", genres);
        model.addAttribute("group", "se.yrgo");

        if (type == null || type.isBlank()) {
            // No type: show list of genres
            return "genre";
        } else {
            // Filter books by genre (case-insensitive)
            String wanted = type.trim();
            List<Book> filtered = books.stream()
                    .filter(b -> b.getGenre() != null && b.getGenre().equalsIgnoreCase(wanted))
                    .collect(Collectors.toList());

            model.addAttribute("selectedGenre", wanted);
            model.addAttribute("filteredBooks", filtered);
            return "genre"; // same template shows either genres or books for a selected genre
        }
    }
}
