package academy.devdojo.service;

import academy.devdojo.commons.ProfileUtils;
import academy.devdojo.domain.Profile;
import academy.devdojo.repository.ProfileRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileServiceTest {

    @InjectMocks
    private ProfileService service;
    @InjectMocks
    private ProfileUtils profileUtils;

    @Mock
    private ProfileRepository repository;
    private List<Profile> profileList;

    @BeforeEach
    void init() {
        profileList = profileUtils.newProfileList();
    }

    @Test
    @DisplayName("findAll returns a list with all profiles")
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(profileList);

        var profiles = service.findAll();

        Assertions.assertThat(profiles).isNotNull().hasSameElementsAs(profileList);
    }


    @Test
    @DisplayName("save creates a profile")
    @Order(2)
    void save_CreateAProfile_WhenSuccessful() {
        var profileToBeSaved = profileUtils.newProfileToSave();
        var profileSaved = profileUtils.newProfileSaved();

        BDDMockito.when(repository.save(profileToBeSaved)).thenReturn(profileSaved);

        var savedProfile = service.save(profileToBeSaved);
        Assertions.assertThat(savedProfile).isEqualTo(profileSaved).hasNoNullFieldsOrProperties();
    }


}