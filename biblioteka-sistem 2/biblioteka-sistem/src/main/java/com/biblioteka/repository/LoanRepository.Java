// LoanRepository.java
package com.biblioteka.repository;

import com.biblioteka.model.Loan;
import com.biblioteka.model.Member;
import com.biblioteka.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByMember(Member member);
    List<Loan> findByBook(Book book);
    List<Loan> findByStatus(Loan.Status status);
    List<Loan> findByMemberAndStatus(Member member, Loan.Status status);
}
