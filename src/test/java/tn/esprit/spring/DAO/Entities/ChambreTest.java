package tn.esprit.spring.DAO.Entities;

import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.Services.Chambre.ChambreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;

public class ChambreTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreService chambreService;

    private Chambre chambre;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre.setReservations(new ArrayList<>()); // Initialiser la liste des réservations
    }

    @Test
    public void testAddOrUpdate() {
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);
        Chambre savedChambre = chambreService.addOrUpdate(chambre);
        assertNotNull(savedChambre);
    }

    @Test
    public void testFindAll() {
        List<Chambre> chambres = new ArrayList<>();
        chambres.add(chambre);
        when(chambreRepository.findAll()).thenReturn(chambres);
        List<Chambre> result = chambreService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetChambresNonReserveParNomFoyerEtTypeChambre() {
        // Créez un mock pour Foyer
        Foyer foyerMock = mock(Foyer.class);
        when(foyerMock.getNomFoyer()).thenReturn("NomFoyer");

        // Créez un mock pour Bloc
        Bloc blocMock = mock(Bloc.class);
        when(blocMock.getFoyer()).thenReturn(foyerMock);

        // Créez un mock pour Chambre
        Chambre chambreMock = mock(Chambre.class);
        when(chambreMock.getBloc()).thenReturn(blocMock);
        when(chambreMock.getTypeC()).thenReturn(TypeChambre.SIMPLE);

        // Utilisez la méthode existante `findAll()`
        when(chambreRepository.findAll()).thenReturn(List.of(chambreMock));

        // Exécution de la méthode à tester
        List<Chambre> result = chambreService.getChambresNonReserveParNomFoyerEtTypeChambre("NomFoyer", TypeChambre.SIMPLE);

        // Vérification des résultats
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("NomFoyer", result.get(0).getBloc().getFoyer().getNomFoyer());
    }
  //mohameddaoud
}

