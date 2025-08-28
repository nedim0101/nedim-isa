package com.biblioteka.config;

import com.biblioteka.model.*;
import com.biblioteka.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Kreiranje admin korisnika
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Administrator");
            admin.setEmail("admin@biblioteka.rs");
            admin.setRole(User.Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
        }

        // Kreiranje test korisnika
        if (!userRepository.existsByUsername("test")) {
            User testUser = new User();
            testUser.setUsername("test");
            testUser.setPassword(passwordEncoder.encode("123"));
            testUser.setFullName("Test Korisnik");
            testUser.setEmail("test@biblioteka.rs");
            testUser.setRole(User.Role.BIBLIOTEKAR); // ili ADMIN po potrebi
            testUser.setEnabled(true);
            userRepository.save(testUser);
        }
        
        // Kreiranje bibliotekar korisnika
        if (!userRepository.existsByUsername("bibliotekar")) {
            User bibliotekar = new User();
            bibliotekar.setUsername("bibliotekar");
            bibliotekar.setPassword(passwordEncoder.encode("bibliotekar123"));
            bibliotekar.setFullName("Petar Petrović");
            bibliotekar.setEmail("petar@biblioteka.rs");
            bibliotekar.setRole(User.Role.BIBLIOTEKAR);
            bibliotekar.setEnabled(true);
            userRepository.save(bibliotekar);
        }
        
        // Kreiranje autora
        if (authorRepository.count() == 0) {
            Author autor1 = new Author();
            autor1.setFirstName("Ivo");
            autor1.setLastName("Andrić");
            autor1.setBiography("Nobelovac, jedan od najvećih srpskih pisaca");
            autor1.setNationality("Srpska");
            authorRepository.save(autor1);
            
            Author autor2 = new Author();
            autor2.setFirstName("Mesa");
            autor2.setLastName("Selimović");
            autor2.setBiography("Jedan od najznačajnijih pisaca sa prostora bivše Jugoslavije");
            autor2.setNationality("Bosanska");
            authorRepository.save(autor2);
            
            Author autor3 = new Author();
            autor3.setFirstName("Miloš");
            autor3.setLastName("Crnjanski");
            autor3.setBiography("Veliki srpski pesnik i pisac");
            autor3.setNationality("Srpska");
            authorRepository.save(autor3);
        }
        
        // Kreiranje knjiga
        if (bookRepository.count() == 0) {
            Author andric = authorRepository.findByLastNameContainingIgnoreCase("Andrić").get(0);
            Author selimovic = authorRepository.findByLastNameContainingIgnoreCase("Selimović").get(0);
            Author crnjanski = authorRepository.findByLastNameContainingIgnoreCase("Crnjanski").get(0);
            
            Book knjiga1 = new Book();
            knjiga1.setTitle("Na Drini ćuprija");
            knjiga1.setIsbn("978-86-521-0123-1");
            knjiga1.setPublisher("Laguna");
            knjiga1.setPublicationYear(1945);
            knjiga1.setGenre("Roman");
            knjiga1.setDescription("Hronika Višegrada kroz četiri veka");
            knjiga1.setTotalCopies(5);
            knjiga1.setAvailableCopies(5);
            knjiga1.setAuthors(Arrays.asList(andric));
            bookRepository.save(knjiga1);
            
            Book knjiga2 = new Book();
            knjiga2.setTitle("Derviš i smrt");
            knjiga2.setIsbn("978-86-521-0124-2");
            knjiga2.setPublisher("Vulkan");
            knjiga2.setPublicationYear(1966);
            knjiga2.setGenre("Roman");
            knjiga2.setDescription("Priča o derviši Ahmetu Nurudinu");
            knjiga2.setTotalCopies(3);
            knjiga2.setAvailableCopies(3);
            knjiga2.setAuthors(Arrays.asList(selimovic));
            bookRepository.save(knjiga2);
            
            Book knjiga3 = new Book();
            knjiga3.setTitle("Seobe");
            knjiga3.setIsbn("978-86-521-0125-3");
            knjiga3.setPublisher("Delfi");
            knjiga3.setPublicationYear(1929);
            knjiga3.setGenre("Roman");
            knjiga3.setDescription("Epski roman o seobama Srba");
            knjiga3.setTotalCopies(4);
            knjiga3.setAvailableCopies(4);
            knjiga3.setAuthors(Arrays.asList(crnjanski));
            bookRepository.save(knjiga3);
        }
        
        // Kreiranje članova
        if (memberRepository.count() == 0) {
            Member clan1 = new Member();
            clan1.setFirstName("Marko");
            clan1.setLastName("Marković");
            clan1.setEmail("marko@example.com");
            clan1.setPhone("060/123-4567");
            clan1.setAddress("Knez Mihailova 1, Beograd");
            clan1.setMembershipNumber("MEM00001");
            clan1.setJoinDate(LocalDate.now().minusMonths(6));
            clan1.setExpiryDate(LocalDate.now().plusMonths(6));
            clan1.setActive(true);
            memberRepository.save(clan1);
            
            Member clan2 = new Member();
            clan2.setFirstName("Ana");
            clan2.setLastName("Anić");
            clan2.setEmail("ana@example.com");
            clan2.setPhone("061/234-5678");
            clan2.setAddress("Narodnog fronta 23, Novi Sad");
            clan2.setMembershipNumber("MEM00002");
            clan2.setJoinDate(LocalDate.now().minusMonths(3));
            clan2.setExpiryDate(LocalDate.now().plusMonths(9));
            clan2.setActive(true);
            memberRepository.save(clan2);
        }
    }
}