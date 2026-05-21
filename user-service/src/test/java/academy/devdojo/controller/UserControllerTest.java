package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"academy.devdojo"})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository repository;
    private List<User> userList;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private UserUtils userUtils;
    private static final String URL = "/v1/users";
    @Autowired
    private FileUtils fileUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
    }

    @Test
    @DisplayName("GET v1/users returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(userList);
        var response = fileUtils.readResourceFile("user/get-user-null-first-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?firstName=Gabriel returns list with found object when first name exists")
    @Order(2)
    void findAll_ReturnsFoundUserInList_WhenFirstNameIsFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-gabriel-first-name-200.json");
        var firstName = "Gabriel";
        var gabriel = userList.stream().filter(user -> user.getFirstName().equals(firstName)).findFirst().orElse(null);

        BDDMockito.when(repository.findByFirstNameIgnoreCase(firstName)).thenReturn(Collections.singletonList(gabriel));

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?firstName=x returns empty list when first name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-x-first-name-200.json");
        var firstName = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("firstName", firstName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/1 returns a user with given id")
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        var id = userList.getFirst().getId();
        var foundUser = userList.stream().filter(user -> user.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        mockMvc.perform(MockMvcRequestBuilders.get(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/99 throws NotFoundException 404 when user is not found")
    @Order(5)
    void findById_ThrowsNotFoundException_WhenUserIsNotFound() throws Exception {
        var id = userList.getLast().getId() + 1;

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("POST v1/users creates a user")
    @Order(6)
    void save_CreateUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/post-request-user-200.json");
        var response = fileUtils.readResourceFile("user/post-response-user-201.json");
        var newId = userList.getLast().getId() + 1;
        var userToBeSaved = User.builder().id(newId).firstName("Rodolfo").lastName("Afonso").email("Rodolfonso@gmail.com").build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(userToBeSaved);

        mockMvc.perform(MockMvcRequestBuilders
                    .post(URL)
                    .content(request)
                    .header("x-api-key", "v1")
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("PUT v1/users updates a users")
    @Order(7)
    void update_UpdateUser_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("user/put-request-user-200.json");
        var id = userList.getFirst().getId();
        var foundUser = userList.stream().filter(user -> user.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/users throws NotFoundException when user is not found")
    @Order(8)
    void update_ThrowsNotFoundException_WhenUserIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("user/put-request-user-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("DELETE v1/users/1 removes a user")
    @Order(9)
    void delete_RemoveUser_WhenSuccessful() throws Exception {
        var id = userList.getFirst().getId();
        var foundUser = userList.stream().filter(user -> user.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundUser);

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/users throws NotFoundException when user is not found")
    @Order(10)
    void delete_ThrowsNotFoundException_WhenUserIsNotFound() throws Exception {
        var id = userList.getLast().getId() + 1;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @DisplayName("POST v1/users returns bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .header("x-api-key", "v1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();


        Assertions.assertThat(resolvedException.getMessage())
                .contains(errors);
    }


    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @DisplayName("PUT v1/users returns bad request when fields are empty")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> erros)throws Exception{
        var request = fileUtils.readResourceFile("user/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(URL)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(erros);
    }




    private static Stream<Arguments> postUserBadRequestSource() {

        var allRequiredErros = allRequiredErros();
        var emailRequiredError = invalidEmailErros();

        return Stream.of(
                Arguments.of("post-request-user-empty-fields-400.json", allRequiredErros),
                Arguments.of("post-request-user-blank-fields-400.json", allRequiredErros),
                Arguments.of("post-request-user-null-fields-400.json", allRequiredErros),
                Arguments.of("post-request-user-invalid-email-400.json", emailRequiredError)
        );
    }


    private static Stream<Arguments> putUserBadRequestSource() {

        var allRequiredErrors = allRequiredErros();
        allRequiredErrors.add("The field 'id' cannot be null");
        var emailRequiredError = invalidEmailErros();

        return Stream.of(
                Arguments.of("put-request-user-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-user-blank-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-user-null-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-user-invalid-email-400.json", emailRequiredError)
        );
    }

    private static List<String> allRequiredErros(){
        var firstNameRequiredError = "The field 'firstName' is required";
        var lastNameRequiredError = "The field 'lastName' is required";
        var emailRequiredError = "The field 'email' is required";
        return new ArrayList<>(List.of(firstNameRequiredError, lastNameRequiredError, emailRequiredError));
    }

    private static List<String> invalidEmailErros() {
        var emailInvalidError = "The e-mail is not valid";
        return List.of(emailInvalidError);
    }
}