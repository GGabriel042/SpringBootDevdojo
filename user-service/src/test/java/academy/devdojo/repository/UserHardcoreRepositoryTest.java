package academy.devdojo.repository;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
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
class UserHardcoreRepositoryTest {

    @InjectMocks
    private UserHardcoreRepository repository;
    @InjectMocks
    private UserUtils userUtils;

    @Mock
    private UserData userData;
    private List<User> userList;

    @BeforeEach
    void init(){
        userList = userUtils.newUserList();
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
    }

    @Test
    @DisplayName("findAll returns a list with all users")
    @Order(1)
    void findAll_ReturnsAllUser_WhenSuccessful() {
        var users = repository.findAll();
        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findById returns a user with given id")
    @Order(2)
    void findById_ReturnsUser_WhenSuccessful() {
        var expectedUser = userList.getFirst();
        var user = repository.findById(expectedUser.getId());

        Assertions.assertThat(user).isPresent().contains(expectedUser);
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists")
    @Order(3)
    void findByName_ReturnsFoundUser_WhenNameIsFound() {
        var expectedUser = userList.getFirst();
        var user = repository.findByFirstName(expectedUser.getFirstName());

        Assertions.assertThat(user).isNotNull().contains(expectedUser);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(4)
    void findByName_ReturnsEmptyList_WhenIsNull() {
        var user = repository.findByFirstName(null);

        Assertions.assertThat(user).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save creates a user")
    @Order(5)
    void save_CreateUser_WhenSuccessful() {
        var id = userList.getLast().getId() + 1;
        var userToBeSaved = User.builder().id(id).firstName("Rodolfo").lastName("Afonso").email("Rodolfonso@teste.com").build();

        var userSaved = repository.saveUser(userToBeSaved);
        Assertions.assertThat(userSaved).isEqualTo(userToBeSaved).hasNoNullFieldsOrProperties();

        var userOptionalSaved = repository.findById(id);
        Assertions.assertThat(userOptionalSaved).isPresent().contains(userToBeSaved);
    }

    @Test
    @DisplayName("delete removes a user")
    @Order(6)
    void delete_RemoveUser_WhenSuccessful() {
        var userToDelete = userList.getLast();
        repository.deleteUser(userToDelete);

        var users = repository.findAll();

        Assertions.assertThat(users).isNotEmpty().doesNotContain(userToDelete);
    }

    @Test
    @DisplayName("update updates a user")
    @Order(7)
    void update_UpdateUser_WhenSuccessful() {

        var userToUpdate = userList.getFirst();
        userToUpdate.setFirstName("Rodolfo");

        repository.updateUser(userToUpdate);

        var users = repository.findAll();
        Assertions.assertThat(users).contains(userToUpdate);

        var userOptionalUpdate = repository.findById(userToUpdate.getId());
        Assertions.assertThat(userOptionalUpdate).isPresent();
        Assertions.assertThat(userOptionalUpdate.get().getFirstName()).isEqualTo(userToUpdate.getFirstName());
    }
}