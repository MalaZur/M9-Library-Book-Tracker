package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.entity.Book;
import bootcamp.hibernate_practical.repository.BookRepository;
import bootcamp.hibernate_practical.exception.BookNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse createBook(CreateBookRequest request) {
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getGenre(),
                request.getPublicationYear(),
                true
        );
        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        return mapToResponse(book);
    }

    public BookResponse updateBook(Long id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setPublicationYear(request.getPublicationYear());
        book.setAvailable(request.getAvailable());
        book.setBorrowedStatus(!request.getAvailable());

        Book updatedBook = bookRepository.save(book);
        return mapToResponse(updatedBook);
    }

    public BookResponse borrowBook(Long id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        if (!book.isAvailable() || book.isBorrowedStatus()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book is already borrowed");
        }

        book.setAvailable(false);
        book.setBorrowedStatus(true);

        Book savedBook = bookRepository.save(book);
        return mapToResponse(book);
    }

    public BookResponse returnBook(Long id){
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        if (!book.isBorrowedStatus()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book is not currently borrowed");
        }

        book.setAvailable(true);
        book.setBorrowedStatus(false);

        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

        bookRepository.delete(book);
    }

    public List<BookResponse> findByAuthor(String author) {
        return bookRepository.findByAuthorIgnoreCase(author).stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> findAvailableBooks(){
        return bookRepository.findByAvailableTrue().stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> findByGenreIgnoreCase(String genre){
        return bookRepository.findByGenreIgnoreCase(genre).stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> findByPublicationYear(int publicationYear){
        return bookRepository.findByPublicationYear(publicationYear).stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> findByPartialTitle(String title){
        return bookRepository.findByTitleContainingIgnoreCase(title).stream().map(this::mapToResponse).toList();
    }


    private BookResponse mapToResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getPublicationYear(),
                book.isAvailable(),
                book.isBorrowedStatus());
    }
}
