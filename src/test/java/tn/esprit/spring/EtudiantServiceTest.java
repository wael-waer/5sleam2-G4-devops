
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Assertions;
import tn.esprit.spring.Entities.Etudiant; // Replace with the actual package of Etudiant
import tn.esprit.spring.Repositories.EtudiantRepository; // Replace with the actual package of EtudiantRepository
import tn.esprit.spring.Services.EtudiantService; // Replace with the actual package of EtudiantService




@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EtudiantServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(EtudiantServiceTest.class);

    private static final String ECOLE_NAME = "ESPRIT"; // Define a constant for "ESPRIT"

    @Autowired
    private EtudiantRepository etudiantRepository;

    private EtudiantService etudiantService;
    private Etudiant createdEtudiant; // Variable to hold the created Etudiant for cleanup

    @BeforeEach
    void setUp() {
        etudiantService = new EtudiantService(etudiantRepository);
    }

    @AfterEach // Cleanup after each test
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
                .ecole(ECOLE_NAME) // Use the constant
                .dateNaissance(LocalDate.of(2000, 5, 10))
                .build();

        logger.info("Adding or updating Etudiant: {}", etudiant);
        createdEtudiant = etudiantService.addOrUpdate(etudiant);

        Assertions.assertTrue(createdEtudiant.getIdEtudiant() > 0, "ID should be generated and greater than 0");
        Assertions.assertTrue(createdEtudiant.getNomEt().equals("Alice"), "Nom should be 'Alice'");
    }

    @Test
    void testFindAll() {
        Etudiant etudiant1 = Etudiant.builder()
                .nomEt("Bob")
                .prenomEt("Brown")
                .cin(112233445)
                .ecole(ECOLE_NAME) // Use the constant
                .dateNaissance(LocalDate.of(2001, 3, 15))
                .build();

        logger.info("Saving Etudiant: {}", etudiant1);
        createdEtudiant = etudiantRepository.save(etudiant1);

        List<Etudiant> etudiants = etudiantService.findAll();
        Assertions.assertTrue(etudiants.size() == 1, "There should be one etudiant");
        Assertions.assertTrue(etudiants.get(0).getNomEt().equals("Bob"), "Nom should be 'Bob'");
    }

    @Test
    void testFindById() {
        Etudiant etudiant = Etudiant.builder()
                .nomEt("Charlie")
                .prenomEt("Johnson")
                .cin(556677889)
                .ecole(ECOLE_NAME) // Use the constant
                .dateNaissance(LocalDate.of(1999, 11, 22))
                .build();

        logger.info("Saving Etudiant: {}", etudiant);
        createdEtudiant = etudiantRepository.save(etudiant);
        Etudiant foundEtudiant = etudiantService.findById(createdEtudiant.getIdEtudiant());

        Assertions.assertTrue(foundEtudiant != null, "Found etudiant should not be null");
        Assertions.assertTrue(foundEtudiant.getNomEt().equals("Charlie"), "Nom should be 'Charlie'");
    }

    @Test
    void testDeleteById() {
        Etudiant etudiant = Etudiant.builder()
                .nomEt("David")
                .prenomEt("Wilson")
                .cin(123456789)
                .ecole(ECOLE_NAME) // Use the constant
                .dateNaissance(LocalDate.of(2002, 7, 25))
                .build();

        logger.info("Saving Etudiant for deletion test: {}", etudiant);
        createdEtudiant = etudiantRepository.save(etudiant);

        logger.info("Deleting Etudiant with ID: {}", createdEtudiant.getIdEtudiant());
        etudiantService.deleteById(createdEtudiant.getIdEtudiant());

        // Verify that the Etudiant was deleted
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            etudiantService.findById(createdEtudiant.getIdEtudiant());
        });
    }

    @Test
    void testDelete() {
        Etudiant etudiant = Etudiant.builder()
                .nomEt("Emma")
                .prenomEt("Davis")
                .cin(998877665)
                .ecole(ECOLE_NAME) // Use the constant
                .dateNaissance(LocalDate.of(1998, 12, 30))
                .build();

        logger.info("Saving Etudiant for deletion test: {}", etudiant);
        createdEtudiant = etudiantRepository.save(etudiant);

        logger.info("Deleting Etudiant: {}", createdEtudiant);
        etudiantService.delete(createdEtudiant);

        // Verify that the Etudiant was deleted
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            etudiantService.findById(createdEtudiant.getIdEtudiant());
        });
    }
}
