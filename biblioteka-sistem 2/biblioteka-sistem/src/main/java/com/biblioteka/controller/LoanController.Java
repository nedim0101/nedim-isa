// LoanController.java
package com.biblioteka.controller;

import com.biblioteka.model.Loan;
import com.biblioteka.model.Book;
import com.biblioteka.model.Member;
import com.biblioteka.repository.LoanRepository;
import com.biblioteka.repository.BookRepository;
import com.biblioteka.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/loans")
public class LoanController {
    
    @Autowired
    private LoanRepository loanRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @GetMapping
    public String listLoans(Model model) {
        model.addAttribute("loans", loanRepository.findAll());
        return "loans/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("loan", new Loan());
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("members", memberRepository.findAll());
        return "loans/form";
    }
    
    @PostMapping("/save")
    public String saveLoan(@ModelAttribute Loan loan,
                          @RequestParam Long bookId,
                          @RequestParam Long memberId) {
        
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("Neispravna knjiga ID"));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("Neispravan član ID"));
        
        if (book.getAvailableCopies() <= 0) {
            return "redirect:/loans/new?error=nocopies";
        }
        
        loan.setBook(book);
        loan.setMember(member);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatus(Loan.Status.ACTIVE);
        
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        
        loanRepository.save(loan);
        return "redirect:/loans";
    }
    
    @PostMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        Loan loan = loanRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Neispravna pozajmica ID"));
        
        loan.setReturnDate(LocalDate.now());
        loan.setStatus(Loan.Status.RETURNED);
        
        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);
        
        loanRepository.save(loan);
        return "redirect:/loans";
    }
}
