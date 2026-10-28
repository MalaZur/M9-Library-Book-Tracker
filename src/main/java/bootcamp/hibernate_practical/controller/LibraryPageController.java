package bootcamp.hibernate_practical.controller;

import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.service.BookService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.service.BookService;

@Controller
@RequestMapping("/library")
public class LibraryPageController {

    private final BookService bookService;

    public LibraryPageController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public String showLibrary(Model model, Authentication authentication){
        addPageData(model, authentication);
        model.addAttribute("bookRequest", new CreateBookRequest());
        return "library";
    }

    @PostMapping
    public String addBook(@Valid @ModelAttribute("bookRequest")
                          CreateBookRequest request,
                          BindingResult result,
                          Model model,
                          Authentication authentication){

        if (result.hasErrors()){
            addPageData(model, authentication);
            return "library";
        }

        bookService.createBook(request);

        return "redirect:/library";
    }

    private void addPageData(Model model, Authentication authentication){

        model.addAttribute("books", bookService.getAllBooks());

        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);
    }
}
