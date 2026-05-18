package academy.devdojo.service;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.UserHardcoreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;
    @InjectMocks
    private UserUtils userUtils;

    @Mock
    private UserHardcoreRepository repository;
    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns a list with all animes when argument is null")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var users = service.findAll(null);

        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findAll returns list with found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundAnimeInList_WhenNameIsFound() {
        var expectedUser = userList.getFirst();
        List<User> usersListFound = Collections.singletonList(expectedUser);

        BDDMockito.when(repository.findByFirstName(expectedUser.getFirstName())).thenReturn(usersListFound);

        var userFound = service.findAll(expectedUser.getFirstName());

        Assertions.assertThat(userFound).containsAll(usersListFound);
    }

    @Test
    @DisplayName("findAll returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        var name = "not-Found";
        BDDMockito.when(repository.findByFirstName(name)).thenReturn(Collections.emptyList());

        var userNotFound = service.findAll(name);

        Assertions.assertThat(userNotFound).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns a anime with given id")
    @Order(4)
    void findById_ReturnsAnimeById_WhenSuccessful() {
        var expectedUser = userList.getFirst();
        var id = expectedUser.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(expectedUser));

        var users = service.findByIdOrThrowNotFound(id);
        Assertions.assertThat(users).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when anime is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var expectedUser = userList.getLast();
        var nonExistentId = expectedUser.getId() + 1;

        BDDMockito.when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(nonExistentId))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a anime")
    @Order(5)
    void save_CreateAAnime_WhenSuccessful() {
        var id = userList.getLast().getId() + 1;
        var userToBeSaved = User.builder().id(id).firstName("Rodolfo").lastName("Afonso").email("Rodolfonso@teste.com").build();

        BDDMockito.when(repository.saveUser(userToBeSaved)).thenReturn(userToBeSaved);

        var savedUser = service.saveUser(userToBeSaved);
        Assertions.assertThat(savedUser).isEqualTo(userToBeSaved).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("Delete removes a anime")
    @Order(6)
    void delete_RemoveAnime_WhenSuccessful() {
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));

        BDDMockito.doNothing().when(repository).deleteUser(userToDelete);
        Assertions.assertThatNoException().isThrownBy(() -> service.deleteUser(userToDelete.getId()));
    }

    @Test
    @DisplayName("Delete throws ResponseStatusException when anime is not found")
    @Order(7)
    void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var userToDelete = userList.getLast();
        var nonExistingId = userToDelete.getId() + 1;

        BDDMockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.deleteUser(nonExistingId))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a anime")
    @Order(8)
    void update_UpdateAAnime_WhenSuccessful() {
        var userToBeUpdate = userList.getFirst();
        userToBeUpdate.setFirstName("Rodolfinho");

        BDDMockito.when(repository.findById(userToBeUpdate.getId())).thenReturn(Optional.of(userToBeUpdate));
        BDDMockito.doNothing().when(repository).updateUser(userToBeUpdate);

        service.updateUser(userToBeUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.updateUser(userToBeUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when anime is not found")
    @Order(9)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var userToBeUpdate = userList.getLast();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.updateUser(userToBeUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}