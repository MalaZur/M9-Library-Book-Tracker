package bootcamp.hibernate_practical.controller;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.service.BookService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public BookResponse createBook(@Valid @RequestBody CreateBookRequest createBookRequest) {
        return bookService.createBook(createBookRequest);
    }

    @GetMapping
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public BookResponse updateBook(@PathVariable Long id, @Valid @RequestBody UpdateBookRequest updateBookRequest) {
        return bookService.updateBook(id, updateBookRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/author/{author}")
    public List<BookResponse> getBooksByAuthor(@PathVariable String author) {
        return bookService.findByAuthor(author);
    }

    @GetMapping("/available")
    public List<BookResponse> getAvailableBooks() {
        return bookService.findAvailableBooks();
    }

    @GetMapping("/genre/{genre}")
    public List<BookResponse> getBooksByGenre(
            @PathVariable String genre) {

        return bookService.findByGenreIgnoreCase(genre);
    }

    @GetMapping("/year/{year}")
    public List<BookResponse> getBooksByPublicationYear(
            @PathVariable int year) {

        return bookService.findByPublicationYear(year);
    }

    @GetMapping("/search")
    public List<BookResponse> searchByTitle(@RequestParam String title){
        return bookService.findByPartialTitle(title);
    }

    @GetMapping("/{id}/borrow")
    public BookResponse borrowBook(@PathVariable Long id){
        return bookService.borrowBook(id);
    }

    @GetMapping("/{id}/return")
    public BookResponse returnBook(@PathVariable Long id) {
        return bookService.returnBook(id);
    }
}
