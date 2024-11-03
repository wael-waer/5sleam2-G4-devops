import tn.esprit.spring.Services.Etudiant.EtudiantService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class EtudiantServiceTest {

    @Mock
    private EtudiantRepository repo;

    @InjectMocks
    EtudiantService etudiantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(1L);
        when(repo.save(etudiant)).thenReturn(etudiant);

        Etudiant result = etudiantService.addOrUpdate(etudiant);

        assertEquals(etudiant.getId(), result.getId());
        verify(repo, times(1)).save(etudiant);
    }

    @Test
    void testFindAll() {
        Etudiant etudiant1 = new Etudiant();
        Etudiant etudiant2 = new Etudiant();
        List<Etudiant> etudiants = Arrays.asList(etudiant1, etudiant2);
        when(repo.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.findAll();

        assertEquals(2, result.size());
        verify(repo, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        long id = 1L;

        etudiantService.deleteById(id);

        verify(repo, times(1)).deleteById(id);
    }
}
