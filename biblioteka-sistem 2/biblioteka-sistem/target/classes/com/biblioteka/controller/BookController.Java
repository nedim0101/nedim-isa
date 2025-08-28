package com.biblioteka.controller;

import com.biblioteka.model.Book;
import com.biblioteka.model.Author;
import com.biblioteka.repository.BookRepository;
import com.biblioteka.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "books/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorRepository.findAll());
        return "books/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Neispravna knjiga ID: " + id));
        model.addAttribute("book", book);
        model.addAttribute("authors", authorRepository.findAll());
        return "books/form";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute Book book,
                           @RequestParam(required = false) List<Long> authorIds) {

        // Provera da li već postoji knjiga sa istim ISBN-om (samo kod kreiranja nove)
        if (book.getId() == null && bookRepository.existsByIsbn(book.getIsbn())) {
            return "redirect:/books/new?error=isbn";
        }

        // Povezivanje autora
        if (authorIds != null && !authorIds.isEmpty()) {
            List<Author> authors = authorRepository.findAllById(authorIds);
            book.setAuthors(authors);
        } else {
            book.setAuthors(null);
        }

        // Postavljanje dostupnih primeraka ako nije setovano
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(book.getTotalCopies());
        }

        bookRepository.save(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/view/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Neispravna knjiga ID: " + id));
        model.addAttribute("book", book);
        return "books/view";
    }
}