// MemberRepository.java
package com.biblioteka.repository;

import com.biblioteka.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByMembershipNumber(String membershipNumber);
    boolean existsByEmail(String email);
    boolean existsByMembershipNumber(String membershipNumber);
}
