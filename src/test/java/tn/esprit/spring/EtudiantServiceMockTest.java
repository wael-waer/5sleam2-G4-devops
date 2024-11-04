@ExtendWith(MockitoExtension.class)
public class EtudiantServiceMockTest {

    private static final Logger logger = LoggerFactory.getLogger(EtudiantServiceMockTest.class);

    private static final String TEST_NAME = "Alice"; // Define a constant for the name "Alice"

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
        // Mock the repository to return an Etudiant with an ID
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        // Call the method to test
        Etudiant createdEtudiant = etudiantService.addOrUpdate(etudiant);

        // Assertions
        Assertions.assertNotNull(createdEtudiant);
        Assertions.assertEquals(TEST_NAME, createdEtudiant.getNomEt()); // Use the constant

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
        Assertions.assertEquals(TEST_NAME, foundEtudiants.get(0).getNomEt()); // Use the constant

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
        Assertions.assertEquals(TEST_NAME, foundEtudiant.getNomEt()); // Use the constant

        // Verify that findById was called
        verify(etudiantRepository, times(1)).findById(anyLong());
        logger.info("Test findById: Successfully found Etudiant with ID: {}", foundEtudiant.getIdEtudiant());
    }

    // Other tests remain unchanged
}
