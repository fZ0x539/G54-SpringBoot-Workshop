package se.lexicon.g54springbootworkshop.Repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.g54springbootworkshop.Entity.Details;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class DetailsRepositoryTest {

    @Autowired
    private DetailsRepository testRepository;

    private Details testDetails1;
    private Details testDetails2;
    private Details testDetails3;

    @BeforeEach
    void setUp() {
        testDetails1 = new Details("test1@example.com", "John Doe", LocalDate.of(1990, 1, 1));
        testDetails2 = new Details("test2@example.com", "Jane Smith", LocalDate.of(1995, 5, 5));
        testDetails3 = new Details("test3@example.com", "John Johnson", LocalDate.of(2000, 10, 10));

        testRepository.save(testDetails1);
        testRepository.save(testDetails2);
        testRepository.save(testDetails3);
    }

    @Test
    void testFindByEmail() {
        // Arrange
        String email = "test2@example.com";

        // Act
        Details found = testRepository.findByEmail(email);

        // Assert
        assertNotNull(found);
        assertEquals(email, found.getEmail());
        assertEquals(testDetails2.getId(), found.getId());
    }

    @Test
    void testFindByEmail_NotFound() {
        // Act
        Details found = testRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertNull(found);
    }

    @Test
    void testFindByNameContaining() {
        // Arrange
        String searchTerm = "John";

        // Act
        List<Details> found = testRepository.findByNameContaining(searchTerm);

        // Assert
        assertEquals(2, found.size());
        assertTrue(found.stream().anyMatch(d -> d.getId() == testDetails1.getId()));
        assertTrue(found.stream().anyMatch(d -> d.getId() == testDetails3.getId()));
    }

    @Test
    void testFindByNameContaining_NoResults() {
        // Act
        List<Details> found = testRepository.findByNameContaining("xyz");

        // Assert
        assertTrue(found.isEmpty());
    }

    @Test
    void testFindByNameIgnoreCase() {
        // Arrange
        String name = "jane smith"; // lowercase to test ignore case

        // Act
        List<Details> found = testRepository.findByNameIgnoreCase(name);

        // Assert
        assertEquals(1, found.size());
        assertEquals(testDetails2.getId(), found.get(0).getId());
        assertEquals("Jane Smith", found.get(0).getName());
    }

    @Test
    void testFindByNameIgnoreCase_NoResults() {
        // Act
        List<Details> found = testRepository.findByNameIgnoreCase("nonexistent");

        // Assert
        assertTrue(found.isEmpty());
    }

    @Test
    void testSaveNewDetails() {
        // Arrange
        Details newDetails = new Details("new@example.com", "New User", LocalDate.of(2000, 1, 1));

        // Act
        Details saved = testRepository.save(newDetails);

        // Assert
        assertNotNull(saved);
        assertTrue(saved.getId() > 0);
        assertEquals("new@example.com", saved.getEmail());
        assertEquals("New User", saved.getName());
    }

    @Test
    void testUpdateDetails() {
        // Arrange
        String newName = "Updated Name";
        testDetails1.setName(newName);

        // Act
        Details updated = testRepository.save(testDetails1);

        // Assert
        assertNotNull(updated);
        assertEquals(testDetails1.getId(), updated.getId());
        assertEquals(newName, updated.getName());
    }

    @Test
    void testDeleteDetails() {
        // Arrange
        int idToDelete = testDetails1.getId();

        // Act
        testRepository.deleteById(idToDelete);
        Optional<Details> deleted = testRepository.findById(idToDelete);

        // Assert
        assertFalse(deleted.isPresent());
    }

    @Test
    void testFindAll() {
        // Act
        Iterable<Details> allDetails = testRepository.findAll();

        // Assert
        int count = 0;
        for (Details details : allDetails) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void testEmailUniquenessConstraint() {
        // Arrange
        Details duplicateEmail = new Details("test1@example.com", "Duplicate Email", LocalDate.now());

        // Act & Assert
        assertThrows(Exception.class, () -> testRepository.save(duplicateEmail));
    }
}