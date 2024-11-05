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

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import tn.esprit.spring.Entities.Etudiant; // Ensure this matches the actual package
import tn.esprit.spring.Repositories.EtudiantRepository; // Ensure this matches the actual package
import tn.esprit.spring.Services.EtudiantService; // Ensure this matches the actual package

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EtudiantServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(EtudiantServiceTest.class);
    private static final String ECOLE_NAME = "ESPRIT";

    @Autowired
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    private Etudiant createdEtudiant;

    @BeforeEach
    void setUp() {
        // Here, you can perform any necessary setup for the tests.
    }

    @AfterEach
    void tearDown() {
        if (createdEtudiant != null) {
            logger.info("Deleting Etudiant: {}", createdEtudiant);
            etudiantService.delete(createdEtudiant);
            createdEtudiant = null; // Reset after deletion
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

        logger.info("Adding or updating Etudiant: {}", etudiant);
        createdEtudiant = etudiantService.addOrUpdate(etudiant);

        assertTrue(createdEtudiant.getIdEtudiant() > 0, "ID should be generated and greater than 0");
        assertEquals("Alice", createdEtudiant.getNomEt(), "Nom should be 'Alice'");
    }

    @Test
    void testFindAll() {
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

    @Test
    void testDeleteById() {
        Etudiant etudiant = Etudiant.builder()
                .nomEt("David")
                .prenomEt("Wilson")
                .cin(123456789)
                .ecole(ECOLE_NAME)
                .dateNaissance(LocalDate.of(2002, 7, 25))
                .build();

        logger.info("Saving Etudiant for deletion test: {}", etudiant);
        createdEtudiant = etudiantRepository.save(etudiant);

        logger.info("Deleting Etudiant with ID: {}", createdEtudiant.getIdEtudiant());
        etudiantService.deleteById(createdEtudiant.getIdEtudiant());

        // Verify that the Etudiant was deleted
        assertThrows(EntityNotFoundException.class, () -> {
            etudiantService.findById(createdEtudiant.getIdEtudiant());
        });
    }

    @Test
    void testDelete() {
        Etudiant etudiant = Etudiant.builder()
                .nomEt("Emma")
                .prenomEt("Davis")
                .cin(998877665)
                .ecole(ECOLE_NAME)
                .dateNaissance(LocalDate.of(1998, 12, 30))
                .build();

        logger.info("Saving Etudiant for deletion test: {}", etudiant);
        createdEtudiant = etudiantRepository.save(etudiant);

        logger.info("Deleting Etudiant: {}", createdEtudiant);
        etudiantService.delete(createdEtudiant);

        // Verify that the Etudiant was deleted
        assertThrows(EntityNotFoundException.class, () -> {
            etudiantService.findById(createdEtudiant.getIdEtudiant());
        });
    }
}
