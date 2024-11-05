package tn.esprit.spring;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Bloc.BlocService;
import tn.esprit.spring.Services.Universite.UniversiteService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class JunitFoyerServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(JunitFoyerServiceTest.class);

    // Constants for duplicated literals
    private static final String FOYER_SAVED_LOG_TEMPLATE = "Saved Foyer with ID {}: {}";
    private static final String FOYER_NAME_1 = "test foyer 1";
    private static final String FOYER_NAME_2 = "test foyer 2";
    private static final String TEST_FOYER_NAME = "Test Foyer";
    private static final String FOYER_FOR_FIND_BY_ID = "test foyer for findById";
    private static final String FOYER_FOR_DELETION = "test foyer for deletion";

    @Autowired
    private FoyerRepository foyerRepository;
    @Autowired
    private UniversiteRepository universiteRepository;
    @Autowired
    private BlocRepository blocRepository;

    private FoyerService foyerService;
    private UniversiteService universiteService;
    private BlocService blocService;
    private Foyer createdFoyer;

    @BeforeEach
    void setUp() {
        foyerService = new FoyerService(foyerRepository, universiteRepository, blocRepository);
        universiteService = new UniversiteService(universiteRepository);

        universiteRepository.deleteAll();
        foyerRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        if (createdFoyer != null) {
            logger.info("Deleting Foyer: {}", createdFoyer);
            foyerService.delete(createdFoyer);
            createdFoyer = null;
        }
        universiteRepository.deleteAll();
        foyerRepository.deleteAll();
    }

    @Test
    void testAddOrUpdate() {
        Foyer foyer = Foyer.builder()
                .nomFoyer(FOYER_NAME_1)
                .capaciteFoyer(20)
                .build();

        logger.info("Adding or updating Foyer: {}", foyer);

        Foyer createdFoyer = foyerService.addOrUpdate(foyer);
        logger.info(FOYER_SAVED_LOG_TEMPLATE, createdFoyer.getIdFoyer(), createdFoyer);

        Assertions.assertNotNull(createdFoyer.getIdFoyer(), "Created Foyer ID should not be null.");
        Assertions.assertTrue(createdFoyer.getNomFoyer().equals(FOYER_NAME_1), "Nom should be '" + FOYER_NAME_1 + "'");
    }

    @Test
    void testFindAll() {
        Foyer foyer1 = Foyer.builder()
                .nomFoyer(FOYER_NAME_1)
                .capaciteFoyer(20)
                .build();

        Foyer foyer2 = Foyer.builder()
                .nomFoyer(FOYER_NAME_2)
                .capaciteFoyer(30)
                .build();

        logger.info("Adding Foyer 1: {}", foyer1);
        foyerService.addOrUpdate(foyer1);
        logger.info("Adding Foyer 2: {}", foyer2);
        foyerService.addOrUpdate(foyer2);

        List<Foyer> allFoyers = foyerService.findAll();

        logger.info("Retrieved all Foyers: {}", allFoyers);
        logger.info("Number of Foyers retrieved: {}", allFoyers.size());

        Assertions.assertNotNull(allFoyers, "List of Foyers should not be null.");
        Assertions.assertEquals(2, allFoyers.size(), "There should be 2 Foyers in the list.");
        Assertions.assertTrue(allFoyers.stream().anyMatch(f -> f.getNomFoyer().equals(FOYER_NAME_1)), "Foyer '" + FOYER_NAME_1 + "' should be present.");
        Assertions.assertTrue(allFoyers.stream().anyMatch(f -> f.getNomFoyer().equals(FOYER_NAME_2)), "Foyer '" + FOYER_NAME_2 + "' should be present.");
    }

    @Test
    void testFindById() {
        Foyer foyer = Foyer.builder()
                .nomFoyer(FOYER_FOR_FIND_BY_ID)
                .capaciteFoyer(25)
                .build();

        logger.info("Creating and saving Foyer for findById test: {}", foyer);
        Foyer createdFoyer = foyerService.addOrUpdate(foyer);
        Long foyerId = createdFoyer.getIdFoyer();

        logger.info(FOYER_SAVED_LOG_TEMPLATE, foyerId, createdFoyer);

        Foyer foundFoyer = foyerService.findById(foyerId);
        logger.info("Found Foyer by ID {}: {}", foyerId, foundFoyer);

        Assertions.assertNotNull(foundFoyer, "Foyer should not be null.");
        Assertions.assertEquals(foyerId, foundFoyer.getIdFoyer(), "Foyer ID should match the created Foyer ID.");
        Assertions.assertEquals(FOYER_FOR_FIND_BY_ID, foundFoyer.getNomFoyer(), "Foyer name should be '" + FOYER_FOR_FIND_BY_ID + "'.");
        Assertions.assertEquals(25, foundFoyer.getCapaciteFoyer(), "Foyer capacity should be 25.");
    }

    @Test
    void testDeleteById() {
        Foyer foyer = Foyer.builder()
                .nomFoyer(FOYER_FOR_DELETION)
                .capaciteFoyer(15)
                .build();

        logger.info("Creating and saving Foyer for deleteById test: {}", foyer);
        Foyer createdFoyer = foyerService.addOrUpdate(foyer);
        Long foyerId = createdFoyer.getIdFoyer();

        logger.info(FOYER_SAVED_LOG_TEMPLATE, foyerId, createdFoyer);

        foyerService.deleteById(foyerId);
        logger.info("Deleted Foyer with ID {}", foyerId);

        try {
            foyerService.findById(foyerId);
            fail("Expected NoSuchElementException, but it was not thrown.");
        } catch (NoSuchElementException e) {
            logger.info("Verified deletion: Foyer with ID {} does not exist.", foyerId);
        }
    }

    @Test
    void testDeleteFoyer() {
        Foyer foyer = Foyer.builder()
                .nomFoyer(FOYER_FOR_DELETION)
                .capaciteFoyer(10)
                .build();

        logger.info("Creating and saving Foyer for delete test: {}", foyer);
        Foyer createdFoyer = foyerService.addOrUpdate(foyer);
        Long foyerId = createdFoyer.getIdFoyer();

        logger.info(FOYER_SAVED_LOG_TEMPLATE, foyerId, createdFoyer);

        foyerService.delete(createdFoyer);
        logger.info("Deleted Foyer with ID {}", foyerId);

        try {
            foyerService.findById(foyerId);
            fail("Expected NoSuchElementException, but it was not thrown.");
        } catch (NoSuchElementException e) {
            logger.info("Verified deletion: Foyer with ID {} does not exist.", foyerId);
        }
    }

    @Test
    void testAffecterFoyerAUniversite() {
        String universiteName = "Test Universite 22";

        Foyer foyer = Foyer.builder()
                .nomFoyer(TEST_FOYER_NAME)
                .capaciteFoyer(50)
                .build();
        foyerService.addOrUpdate(foyer);

        Universite universite = Universite.builder()
                .nomUniversite(universiteName)
                .adresse("Test Address 22")
                .build();
        universiteService.addOrUpdate(universite);

        Universite updatedUniversite = foyerService.affecterFoyerAUniversite(foyer.getIdFoyer(), universiteName);

        Assertions.assertNotNull(updatedUniversite, "Updated Universite should not be null.");
        Assertions.assertEquals(universiteName, updatedUniversite.getNomUniversite(), "Universite name should match.");
        Assertions.assertNotNull(updatedUniversite.getFoyer(), "Foyer should be associated with Universite.");
        Assertions.assertEquals(foyer.getIdFoyer(), updatedUniversite.getFoyer().getIdFoyer(), "Foyer ID should match.");
    }

    @Test
    void testDesaffecterFoyerAUniversite() {
        Foyer foyer = Foyer.builder()
                .nomFoyer(TEST_FOYER_NAME)
                .capaciteFoyer(50)
                .build();

        Foyer createdFoyer = foyerService.addOrUpdate(foyer);
        Universite universite = Universite.builder()
                .nomUniversite("Test Universite")
                .adresse("Test Address")
                .foyer(createdFoyer)
                .build();
        universiteService.addOrUpdate(universite);

        universiteService.desaffecterFoyerAUniversite(universite.getNomUniversite());

        Universite updatedUniversite = universiteService.findById(universite.getIdUniversite());

        Assertions.assertNotNull(updatedUniversite, "Updated Universite should not be null.");
        Assertions.assertNull(updatedUniversite.getFoyer(), "Foyer should be disassociated from Universite.");
    }
}
