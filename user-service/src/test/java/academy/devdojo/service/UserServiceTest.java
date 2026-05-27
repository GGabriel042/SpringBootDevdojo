package academy.devdojo.service;

import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistException;
import academy.devdojo.repository.UserRepository;
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
    private UserRepository repository;
    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("findAll returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var users = service.findAll(null);

        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }

    @Test
    @DisplayName("findAll returns list with found object when firstName exists")
    @Order(2)
    void findAll_ReturnsFoundUserInList_WhenNameIsFound() {
        var expectedUser = userList.getFirst();
        List<User> usersListFound = Collections.singletonList(expectedUser);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(expectedUser.getFirstName())).thenReturn(usersListFound);

        var userFound = service.findAll(expectedUser.getFirstName());

        Assertions.assertThat(userFound).containsAll(usersListFound);
    }

    @Test
    @DisplayName("findAll returns empty list when firstName is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        var firstName = "not-Found";
        BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(Collections.emptyList());

        var userNotFound = service.findAll(firstName);

        Assertions.assertThat(userNotFound).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns a user with given id")
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() {
        var expectedUser = userList.getFirst();
        var id = expectedUser.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(expectedUser));

        var users = service.findByIdOrThrowNotFound(id);
        Assertions.assertThat(users).isEqualTo(expectedUser);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when user is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var expectedUser = userList.getLast();
        var nonExistentId = expectedUser.getId() + 1;

        BDDMockito.when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(nonExistentId))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a user")
    @Order(6)
    void save_CreateAUser_WhenSuccessful() {
        var userToBeSaved = userUtils.newUserToSave();

        BDDMockito.when(repository.save(userToBeSaved)).thenReturn(userToBeSaved);
        BDDMockito.when(repository.findByEmail(userToBeSaved.getEmail())).thenReturn(Optional.empty());

        var savedUser = service.save(userToBeSaved);
        Assertions.assertThat(savedUser).isEqualTo(userToBeSaved).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("Delete removes a user")
    @Order(7)
    void delete_RemoveUser_WhenSuccessful() {
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));

        BDDMockito.doNothing().when(repository).delete(userToDelete);
        Assertions.assertThatNoException()
                .isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @DisplayName("Delete throws ResponseStatusException when user is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userToDelete = userList.getLast();
        var nonExistingId = userToDelete.getId() + 1;

        BDDMockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(nonExistingId))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a user")
    @Order(9)
    void update_UpdateAUser_WhenSuccessful() {
        var userToBeUpdate = userList.getFirst().withFirstName("Rodolfinho");
        var email = userToBeUpdate.getEmail();
        var id = userToBeUpdate.getId();

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(userToBeUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.empty());
        BDDMockito.when(repository.save(userToBeUpdate)).thenReturn(userToBeUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(userToBeUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when user is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenUserIsNotFound() {
        var userToBeUpdate = userList.getLast();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.update(userToBeUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }


    @Test
    @DisplayName("update throws EmailAlreadyExistException when email belongs to another user")
    @Order(11)
    void update_EmailAlreadyExistException_WhenEmailBelongsToAnotherUser() {
        var savedUser = userList.getLast();
        var userToBeUpdate = userList.getFirst().withEmail(savedUser.getEmail());
        var email = userToBeUpdate.getEmail();
        var id = userToBeUpdate.getId();


        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(userToBeUpdate));
        BDDMockito.when(repository.findByEmailAndIdNot(email, id)).thenReturn(Optional.of(savedUser));


        Assertions.assertThatException().isThrownBy(() -> service.update(userToBeUpdate))
                .isInstanceOf(EmailAlreadyExistException.class);
    }


    @Test
    @DisplayName("save throws EmailAlreadyExistException when email exists")
    @Order(12)
    void save_ThrowsEmailAlreadyExistException_WhenEmailAlreadyExists() {
        var savedUser = userList.getLast();
        var userToBeSaved = userUtils.newUserToSave().withEmail(savedUser.getEmail());
        var email = userToBeSaved.getEmail();


        BDDMockito.when(repository.findByEmail(email)).thenReturn(Optional.of(savedUser));


        Assertions.assertThatException()
                .isThrownBy(() -> service.save(userToBeSaved))
                .isInstanceOf(EmailAlreadyExistException.class);

    }
}