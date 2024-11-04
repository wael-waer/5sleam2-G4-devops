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
class JunitFoyerServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(EtudiantServiceTest.class);

    private static final String ECOLE_NAME = "ESPRIT";
    private static final String SAVED_FOYER_MESSAGE = "Saved Foyer with ID {}: {}"; // Constant for "Saved Foyer with ID {}: {}"
    private static final String TEST_FOYER_1 = "test foyer 1"; // Constant for "test foyer 1"
    private static final String TEST_FOYER_NAME = "Test Foyer"; // Constant for "Test Foyer"

    @Autowired
    private EtudiantRepository etudiantRepository;

    private EtudiantService etudiantService;
    private Etudiant createdEtudiant;

    @BeforeEach
    void setUp() {
        etudiantService = new EtudiantService(etudiantRepository);
    }

    @AfterEach
    void tearDown() {
        if (createdEtudiant != null) {
            logger.info("Deleting Etudiant: {}", createdEtudiant);
            etudiantService.delete(createdEtudiant);
            createdEtudiant = null;
        }
    }

    // Your existing test methods follow, using the new constants where appropriate...
    // For example:
    @Test
    void testAddOrUpdate() {
        Etudiant etudiant = Etudiant.builder()
                .nomEt("Alice")
                .prenomEt("Smith")
                .cin(987654321)
                .ecole(ECOLE_NAME)
                .dateNaissance(LocalDate.of(2000, 5, 10))
                .build();

        logger.info(SAVED_FOYER_MESSAGE, etudiant.getIdEtudiant(), etudiant); // Use the constant
        createdEtudiant = etudiantService.addOrUpdate(etudiant);

        Assertions.assertTrue(createdEtudiant.getIdEtudiant() > 0, "ID should be generated and greater than 0");
        Assertions.assertTrue(createdEtudiant.getNomEt().equals("Alice"), "Nom should be 'Alice'");
    }

    // Other test methods, such as `testFindAll`, `testFindById`, etc., also using these constants
}