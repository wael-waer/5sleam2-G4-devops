package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
class FoyerServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MockitoFoyerServiceTest.class);

    // Define constants for duplicate string literals
    private static final String TEST_FOYER_NAME = "Test Foyer";
    private static final String TEST_UNIVERSITE_NAME = "Test Universite";

    @Mock
    private FoyerRepository foyerRepository;  // Mocking FoyerRepository

    @Mock
    private UniversiteRepository universiteRepository; // Mocking UniversiteRepository

    @Mock
    private BlocRepository blocRepository;  // Mocking BlocRepository

    @InjectMocks
    private FoyerService foyerService; // Injecting mocks into FoyerService

    @BeforeEach
    void setup() {
        // Optional setup for common behaviors or configurations
        logger.info("Before Testing...");
    }

    @Test
    void testFindById() {
        // Arrange: Define the behavior of the mock
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer(TEST_FOYER_NAME);

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        // Act: Call the method to be tested
        Foyer result = foyerService.findById(1L);

        // Log the name of the Foyer
        logger.info("Testing findById method: Foyer Name: {}", result.getNomFoyer());

        // Assert: Verify the result
        assertEquals(TEST_FOYER_NAME, result.getNomFoyer());
    }

    @Test
    void testFindAll() {
        // Arrange: Create mock data
        Foyer foyer1 = new Foyer();
        foyer1.setIdFoyer(1L);
        foyer1.setNomFoyer("Foyer 1");

        Foyer foyer2 = new Foyer();
        foyer2.setIdFoyer(2L);
        foyer2.setNomFoyer("Foyer 2");

        List<Foyer> mockFoyers = List.of(foyer1, foyer2);

        // Define the behavior of the mock
        when(foyerRepository.findAll()).thenReturn(mockFoyers);

        // Act: Call the method to be tested
        List<Foyer> result = foyerService.findAll();

        // Assert: Verify the result
        assertEquals(2, result.size()); // Check the size of the list
        assertEquals("Foyer 1", result.get(0).getNomFoyer()); // Check the first foyer's name
        assertEquals("Foyer 2", result.get(1).getNomFoyer()); // Check the second foyer's name
    }

    @Test
    void testAddOrUpdate() {
        // Arrange: Create a Foyer object to be saved
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("New Foyer");

        // Define the behavior of the mock to return the same foyer object when saved
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        // Act: Call the method to be tested
        Foyer result = foyerService.addOrUpdate(foyer);

        // Log the action being performed
        logger.info("Testing addOrUpdate method: Foyer being added/updated: {}", foyer.getNomFoyer());

        // Assert: Verify the result
        assertNotNull(result); // Check that the result is not null
        assertEquals("New Foyer", result.getNomFoyer()); // Check the name of the foyer
        assertEquals(1L, result.getIdFoyer()); // Check the ID of the foyer

        // Log the result of the operation
        logger.info("Foyer added/updated successfully: ID: {}, Name: {}", result.getIdFoyer(), result.getNomFoyer());

        // Verify that the save method was called once with the foyer object
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    void testDeleteById() {
        // Arrange: Specify the ID of the foyer to be deleted
        long foyerId = 1L;

        // Act: Call the method to be tested
        foyerService.deleteById(foyerId);

        // Log the action of deleting the foyer by ID
        logger.info("Testing deleteById method: Deleting foyer with ID: {}", foyerId);

        // Assert: Verify that the deleteById method was called once with the specified ID
        verify(foyerRepository, times(1)).deleteById(foyerId);

        // Log the successful deletion
        logger.info("Foyer with ID: {} has been successfully deleted.", foyerId);
    }

    @Test
    void testDelete() {
        // Arrange: Create a Foyer object to be deleted
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Foyer to be Deleted");

        // Act: Call the method to be tested
        foyerService.delete(foyer);

        // Log the action of deleting the foyer
        logger.info("Testing delete method: Deleting foyer with ID: {}", foyer.getIdFoyer());

        // Assert: Verify that the delete method was called once with the specified Foyer object
        verify(foyerRepository, times(1)).delete(foyer);

        // Log the successful deletion
        logger.info("Foyer with ID: {} has been successfully deleted.", foyer.getIdFoyer());
    }

    @Test
    void testAffecterFoyerAUniversite() {
        // Arrange: Create a Foyer and Universite object
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(2L);
        foyer.setNomFoyer(TEST_FOYER_NAME);

        Universite universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite(TEST_UNIVERSITE_NAME);

        // Define the behavior of the mocks
        when(foyerRepository.findById(2L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.findByNomUniversite(TEST_UNIVERSITE_NAME)).thenReturn(universite);
        when(universiteRepository.save(universite)).thenReturn(universite); // Simulate saving the Universite

        // Act: Call the method to be tested
        Universite result = foyerService.affecterFoyerAUniversite(2L, TEST_UNIVERSITE_NAME);

        // Log the action of affecting the foyer to the universite
        logger.info("Testing affecterFoyerAUniversite method: Affecting foyer with ID: {} to Universite: {}", foyer.getIdFoyer(), universite.getNomUniversite());

        // Assert: Verify the result
        assertEquals(universite, result); // Ensure the returned universite is the one we expect
        assertEquals(foyer, result.getFoyer()); // Ensure the foyer is set in the universite

        // Log the successful action
        logger.info("Foyer with ID: {} has been successfully affected to Universite: {}", foyer.getIdFoyer(), universite.getNomUniversite());
    }

    @Test
    void testDesaffecterFoyerAUniversite() {
        // Arrange: Create a Universite object with an associated Foyer
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(2L);
        foyer.setNomFoyer(TEST_FOYER_NAME);

        Universite universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite(TEST_UNIVERSITE_NAME);
        universite.setFoyer(foyer); // Set the Foyer association

        // Define the behavior of the mocks
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite)); // Find the Universite
        when(universiteRepository.save(universite)).thenReturn(universite); // Simulate saving the Universite

        // Act: Call the method to be tested
        Universite result = foyerService.desaffecterFoyerAUniversite(1L);

        // Log the action of desaffecting the foyer from the universite
        logger.info("Testing desaffecterFoyerAUniversite method: Desaffecting Foyer from Universite: {}", universite.getNomUniversite());

        // Assert: Verify the result
        assertEquals(universite, result); // Ensure the returned universite is the one we expect
        assertNull(result.getFoyer()); // Ensure the foyer is removed from the universite

        // Log the successful action
        logger.info("Foyer has been successfully desaffecte from Universite: {}", universite.getNomUniversite());
    }
}
