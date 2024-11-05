package tn.esprit;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class Junit_test {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(Junit_test.class);


    private EtudiantRepository etudiantRepository;

    private EtudiantService etudiantService;
    private Etudiant createdEtudiant; // Variable to hold the created Etudiant for cleanup

    @BeforeEach
    void setUp() {
        etudiantService = new EtudiantService(etudiantRepository);
    }

    @AfterEach
        // Cleanup after each test
    void tearDown() {
        if (createdEtudiant != null) {
            logger.info("Deleting Etudiant: {}");
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
                .ecole("ESPRIT")
                .dateNaissance(LocalDate.of(2000, 5, 10))
                .build();

        logger.info("Adding or updating Etudiant: {}");
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
                .ecole("ESPRIT")
                .dateNaissance(LocalDate.of(2001, 3, 15))
                .build();

        logger.info("Saving Etudiant: {}");
        createdEtudiant = etudiantRepository.save(etudiant1);

        List<Etudiant> etudiants = etudiantService.findAll();
        Assertions.assertTrue(etudiants.size() == 1, "There should be one etudiant");
        Assertions.assertTrue(etudiants.get(0).getNomEt().equals("Bob"), "Nom should be 'Bob'");
    }


    @Test
    void testDeleteById() {
        Etudiant etudiant = Etudiant.builder()
                .nomEt("David")
                .prenomEt("Wilson")
                .cin(123456789)
                .ecole("ESPRIT")
                .dateNaissance(LocalDate.of(2002, 7, 25))
                .build();

        logger.info("Saving Etudiant for deletion test: {}");
        createdEtudiant = etudiantRepository.save(etudiant);

        logger.info("Deleting Etudiant with ID: {}");
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
                .ecole("ESPRIT")
                .dateNaissance(LocalDate.of(1998, 12, 30))
                .build();

        logger.info("Saving Etudiant for deletion test: {}");
        createdEtudiant = etudiantRepository.save(etudiant);

        logger.info("Deleting Etudiant: {}");
        etudiantService.delete(createdEtudiant);

        // Verify that the Etudiant was deleted
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            etudiantService.findById(createdEtudiant.getIdEtudiant());
        });
    }
}
