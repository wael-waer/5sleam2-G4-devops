package tn.esprit;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MokitoTest {



    private static final Logger logger = LoggerFactory.getLogger(MokitoTest.class);

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        // Create a new Etudiant instance for testing
        etudiant = Etudiant.builder()
                .idEtudiant(1L) // Set a sample ID for testing purposes
                .nomEt("Alice")
                .prenomEt("Smith")
                .cin(987654321)
                .ecole("ESPRIT")
                .dateNaissance(LocalDate.of(2000, 5, 10))
                .build();
        logger.info("Set up new Etudiant for testing: {}", etudiant);
    }

    @Test
    void testAddOrUpdate() {
        // Mock the repository to return an Etudiant with an ID
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // Call the method to test
        Etudiant createdEtudiant = etudiantService.addOrUpdate(etudiant);

        // Assertions
        Assertions.assertNotNull(createdEtudiant);
        Assertions.assertEquals("Alice", createdEtudiant.getNomEt());

        // Verify that the save method was called
        verify(etudiantRepository, times(1)).save(any(Etudiant.class));
        logger.info("Test addOrUpdate: Successfully added or updated Etudiant: {}", createdEtudiant);
    }

    @Test
    void testFindAll() {
        // Create a list of Etudiant for testing
        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiant);

        // Mock the repository to return the list
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        // Call the method to test
        List<Etudiant> foundEtudiants = etudiantService.findAll();

        // Assertions
        Assertions.assertNotNull(foundEtudiants);
        Assertions.assertEquals(1, foundEtudiants.size());
        Assertions.assertEquals("Alice", foundEtudiants.get(0).getNomEt());

        // Verify that findAll was called
        verify(etudiantRepository, times(1)).findAll();
        logger.info("Test findAll: Found {} Etudiants", foundEtudiants.size());
    }

    @Test
    void testFindById() {
        // Mock the repository to return an Optional containing the Etudiant
        when(etudiantRepository.findById(anyLong())).thenReturn(Optional.of(etudiant));

        // Call the method to test
        Etudiant foundEtudiant = etudiantService.findById(1L);

        // Assertions
        Assertions.assertNotNull(foundEtudiant);
        Assertions.assertEquals("Alice", foundEtudiant.getNomEt());

        // Verify that findById was called
        verify(etudiantRepository, times(1)).findById(anyLong());
        logger.info("Test findByIdNotFound: Expected EntityNotFoundException was thrown.");
    }

    @Test
    void testFindByIdNotFound() {
        // Mock the repository to return an empty Optional
        when(etudiantRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            etudiantService.findById(1L);
        });
        // Call the method to test and assert that it throws an EntityNotFoundException
        Assertions.assertEquals("Etudiant not found with ID: 1", exception.getMessage());

        // Verify that findById was called
        verify(etudiantRepository, times(1)).findById(anyLong());
        logger.info("Test findByIdNotFound: No Etudiant found with the given ID");
    }

    @Test
    void testDeleteById() {
        // Call the method to test
        etudiantService.deleteById(1L);

        // Verify that deleteById was called
        verify(etudiantRepository, times(1)).deleteById(1L);
        logger.info("Test deleteById: Deleted Etudiant with ID: 1");
    }

    @Test
    void testDelete() {
        // Call the method to test
        etudiantService.delete(etudiant);

        // Verify that delete was called
        verify(etudiantRepository, times(1)).delete(etudiant);
        logger.info("Test delete: Deleted Etudiant: {}", etudiant);
    }
}

