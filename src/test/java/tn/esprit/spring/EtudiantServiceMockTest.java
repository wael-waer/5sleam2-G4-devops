import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import tn.esprit.spring.Entities.Etudiant; // Ensure this matches the actual package
import tn.esprit.spring.Repositories.EtudiantRepository; // Ensure this matches the actual package
import tn.esprit.spring.Services.EtudiantService; // Ensure this matches the actual package

@ExtendWith(MockitoExtension.class)
public class EtudiantServiceMockTest {

    private static final Logger logger = LoggerFactory.getLogger(EtudiantServiceMockTest.class);
    private static final String TEST_NAME = "Alice";

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = Etudiant.builder()
                .idEtudiant(1L)
                .nomEt(TEST_NAME)
                .prenomEt("Smith")
                .cin(987654321)
                .ecole("ESPRIT")
                .dateNaissance(LocalDate.of(2000, 5, 10))
                .build();
        logger.info("Set up new Etudiant for testing: {}", etudiant);
    }

    @Test
    void testAddOrUpdate() {
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant createdEtudiant = etudiantService.addOrUpdate(etudiant);

        assertNotNull(createdEtudiant);
        assertEquals(TEST_NAME, createdEtudiant.getNomEt());

        verify(etudiantRepository, times(1)).save(any(Etudiant.class));
        logger.info("Test addOrUpdate: Successfully added or updated Etudiant: {}", createdEtudiant);
    }

    @Test
    void testFindAll() {
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiant);

        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> foundEtudiants = etudiantService.findAll();

        assertNotNull(foundEtudiants);
        assertEquals(1, foundEtudiants.size());
        assertEquals(TEST_NAME, foundEtudiants.get(0).getNomEt());

        verify(etudiantRepository, times(1)).findAll();
        logger.info("Test findAll: Found {} Etudiants", foundEtudiants.size());
    }

    @Test
    void testFindById() {
        when(etudiantRepository.findById(anyLong())).thenReturn(Optional.of(etudiant));

        Etudiant foundEtudiant = etudiantService.findById(1L);

        assertNotNull(foundEtudiant);
        assertEquals(TEST_NAME, foundEtudiant.getNomEt());

        verify(etudiantRepository, times(1)).findById(anyLong());
        logger.info("Test findById: Successfully found Etudiant with ID: {}", foundEtudiant.getIdEtudiant());
    }
}
