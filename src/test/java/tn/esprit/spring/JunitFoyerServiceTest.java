import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import tn.esprit.spring.Entities.Etudiant; // Ensure this matches the actual package
import tn.esprit.spring.Repositories.EtudiantRepository; // Ensure this matches the actual package
import tn.esprit.spring.Services.EtudiantService; // Ensure this matches the actual package

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JunitFoyerServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(JunitFoyerServiceTest.class);

    private static final String ECOLE_NAME = "ESPRIT";
    private static final String SAVED_FOYER_MESSAGE = "Saved Foyer with ID {}: {}"; // Message constant
    private static final String TEST_FOYER_1 = "test foyer 1"; // Test constant
    private static final String TEST_FOYER_NAME = "Test Foyer"; // Test constant

    @Autowired
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    private Etudiant createdEtudiant;

    @BeforeEach
    void setUp() {
        // Any setup required before each test can be added here.
    }

    @AfterEach
    void tearDown() {
        if (createdEtudiant != null) {
            logger.info("Deleting Etudiant: {}", createdEtudiant);
            etudiantService.delete(createdEtudiant);
            createdEtudiant = null;
        }
    }

    @Test
    void testAddOrUpdate() {
        Etudiant etudiant = Etudiant.builder()
                .nomEt("Alice")
                .prenomEt("Smith")
                .cin(987654321)
                .ecole(ECOLE_NAME)
                .dateNaissance(LocalDate.of(2000, 5, 10))
                .build();

        logger.info(SAVED_FOYER_MESSAGE, etudiant.getIdEtudiant(), etudiant);
        createdEtudiant = etudiantService.addOrUpdate(etudiant);

        assertTrue(createdEtudiant.getIdEtudiant() > 0, "ID should be generated and greater than 0");
        assertEquals("Alice", createdEtudiant.getNomEt(), "Nom should be 'Alice'");
    }

    @Test
    void testFindAll() {
        // Add logic to save an etudiant before fetching all
        Etudiant etudiant1 = Etudiant.builder()
                .nomEt("Bob")
                .prenomEt("Brown")
                .cin(112233445)
                .ecole(ECOLE_NAME)
                .dateNaissance(LocalDate.of(2001, 3, 15))
                .build();

        logger.info("Saving Etudiant: {}", etudiant1);
        createdEtudiant = etudiantRepository.save(etudiant1);

        List<Etudiant> etudiants = etudiantService.findAll();
        assertEquals(1, etudiants.size(), "There should be one etudiant");
        assertEquals("Bob", etudiants.get(0).getNomEt(), "Nom should be 'Bob'");
    }

    @Test
    void testFindById() {
        // Add logic to save an etudiant before fetching by ID
        Etudiant etudiant = Etudiant.builder()
                .nomEt("Charlie")
                .prenomEt("Johnson")
                .cin(556677889)
                .ecole(ECOLE_NAME)
                .dateNaissance(LocalDate.of(1999, 11, 22))
                .build();

        logger.info("Saving Etudiant: {}", etudiant);
        createdEtudiant = etudiantRepository.save(etudiant);
        Etudiant foundEtudiant = etudiantService.findById(createdEtudiant.getIdEtudiant());

        assertNotNull(foundEtudiant, "Found etudiant should not be null");
        assertEquals("Charlie", foundEtudiant.getNomEt(), "Nom should be 'Charlie'");
    }

    // You can continue adding more test methods as needed
}
