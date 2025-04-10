package se.lexicon.g54springbootworkshop.Repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.g54springbootworkshop.Entity.AppUser;
import se.lexicon.g54springbootworkshop.Entity.Details;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository testRepository;

    @Autowired
    private DetailsRepository detailsRepository;

    private AppUser testUser1;
    private AppUser testUser2;
    private Details testDetails1;
    private Details testDetails2;


    @BeforeEach
    void setUp() {
        testDetails1 = new Details("test1@example.com", "Test User One", LocalDate.of(1990, 1, 1));
        testDetails2 = new Details("test2@example.com", "Test User Two", LocalDate.of(1995, 5, 5));

        detailsRepository.save(testDetails1);
        detailsRepository.save(testDetails2);

        testUser1 = new AppUser(
                "user1",
                "password1",
                LocalDate.now().minusDays(5),
                testDetails1
        );

        testUser2 = new AppUser(
                "user2",
                "password2",
                LocalDate.now().minusDays(2),
                testDetails2
        );

        testRepository.save(testUser1);
        testRepository.save(testUser2);
    }

    @Test
    void testFindByUsername() {
        // Arrange
        String username = "user1";

        // Act
        AppUser found = testRepository.findByUsername(username);

        // Assert
        assertNotNull(found);
        assertEquals(username, found.getUsername());
        assertEquals(testUser1.getId(), found.getId());
    }

    @Test
    void testFindByUsername_NotFound() {
        // Act
        AppUser found = testRepository.findByUsername("nonexistent");

        // Assert
        assertNull(found);
    }

    @Test
    void testFindByRegDateBetween() {
        // Arrange
        LocalDate startDate = LocalDate.now().minusDays(3);
        LocalDate endDate = LocalDate.now();

        // Act
        List<AppUser> found = testRepository.findByRegDateBetween(startDate, endDate);

        // Assert
        assertEquals(1, found.size());
        assertEquals(testUser2.getId(), found.get(0).getId());
    }

    @Test
    void testFindByRegDateBetween_NoResults() {
        // Arrange
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(5);

        // Act
        List<AppUser> found = testRepository.findByRegDateBetween(startDate, endDate);

        // Assert
        assertTrue(found.isEmpty());
    }

    @Test
    void testFindByUserDetails_Id() {
        // Act
        AppUser found = testRepository.findByUserDetails_Id(testDetails1.getId());

        // Assert
        assertNotNull(found);
        assertEquals(testUser1.getId(), found.getId());
        assertEquals(testDetails1.getId(), found.getUserDetails().getId());
    }

    @Test
    void testFindByUserDetails_Id_NotFound() {
        // Act
        AppUser found = testRepository.findByUserDetails_Id(999);

        // Assert
        assertNull(found);
    }

    @Test
    void testFindByUserDetailsEmailIgnoreCase() {
        // Arrange
        String email = "TEST1@EXAMPLE.COM"; // uppercase to test ignore case

        // Act
        AppUser found = testRepository.findByUserDetailsEmailIgnoreCase(email);

        // Assert
        assertNotNull(found);
        assertEquals(testUser1.getId(), found.getId());
        assertEquals(testDetails1.getEmail().toLowerCase(), found.getUserDetails().getEmail().toLowerCase());
    }

    @Test
    void testFindByUserDetailsEmailIgnoreCase_NotFound() {
        // Act
        AppUser found = testRepository.findByUserDetailsEmailIgnoreCase("nonexistent@example.com");

        // Assert
        assertNull(found);
    }

    @Test
    void testSaveNewAppUser() {
        // Arrange
        Details newDetails = new Details("new@example.com", "New User", LocalDate.of(2000, 1, 1));
        detailsRepository.save(newDetails);

        AppUser newUser = new AppUser(
                "newuser",
                "newpass",
                LocalDate.now(),
                newDetails
        );

        // Act
        AppUser saved = testRepository.save(newUser);

        // Assert
        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals("newuser", saved.getUsername());
        assertEquals(newDetails.getId(), saved.getUserDetails().getId());
    }

    @Test
    void testUpdateAppUser() {
        // Arrange
        String newPassword = "updatedPassword";
        testUser1.setPassword(newPassword);

        // Act
        AppUser updated = testRepository.save(testUser1);

        // Assert
        assertNotNull(updated);
        assertEquals(testUser1.getId(), updated.getId());
        assertEquals(newPassword, updated.getPassword());
    }

    @Test
    void testDeleteAppUser() {
        // Arrange
        int idToDelete = testUser1.getId();

        // Act
        testRepository.deleteById(idToDelete);
        Optional<AppUser> deleted = testRepository.findById(idToDelete);

        // Assert
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindAll() {
        // Act
        Iterable<AppUser> allUsers = testRepository.findAll();

        // Assert
        int count = 0;
        for (AppUser user : allUsers) {
            count++;
        }
        assertEquals(2, count);
    }

}
