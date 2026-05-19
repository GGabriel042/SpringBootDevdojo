package academy.devdojo.controller;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.UserUtils;
import academy.devdojo.domain.User;
import academy.devdojo.repository.UserData;
import academy.devdojo.repository.UserHardcoreRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "academy.devdojo")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserData userData;
    @SpyBean
    private UserHardcoreRepository repository;
    private List<User> userList;
    @Autowired
    private ResourceLoader resourceLoader;
    @InjectMocks
    private UserUtils userUtils;
    private static final String url = "/v1/user";
    @Autowired
    private FileUtils fileUtils;

    @BeforeEach
    void init() {
        userList = userUtils.newUserList();
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
    }

    @Test
    @DisplayName("GET v1/users returns a list with all users when argument is null")
    @Order(1)
    void findAll_ReturnsAllUsers_WhenArgumentIsNull() throws Exception {

        var response = fileUtils.readResourceFile("user/get-user-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?name=Toradora returns a list with found object when name exist")
    @Order(2)
    void findAll_ReturnsFoundUsersInList_WhenNameIsFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-gabriel-name-200.json");
        var name = "gabriel";

        mockMvc.perform(MockMvcRequestBuilders.get(url).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users?name=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(url).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/2 returns a user with given id")
    @Order(4)
    void findById_ReturnsUserById_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("user/get-user-by-id-200.json");
        var id = userList.getFirst().getId();

        mockMvc.perform(MockMvcRequestBuilders.get(url+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/users/99 throws ResponseStatusException 404 when user is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenUserIsNotFound() throws Exception {
        var id = userList.getLast().getId() + 1;

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/{id}", id))
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

        BDDMockito.when(repository.saveUser(ArgumentMatchers.any())).thenReturn(userToBeSaved);

        mockMvc.perform(MockMvcRequestBuilders
                    .post(url)
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

        mockMvc.perform(MockMvcRequestBuilders
                .put(url)
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/users throws ResponseStatusException when user is not found")
    @Order(8)
    void update_ThrowsResponseStatusException_WhenUserIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("user/put-request-user-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
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

        mockMvc.perform(MockMvcRequestBuilders.delete(url + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/users throws ResponseStatusException when user is not found")
    @Order(10)
    void delete_ThrowsResponseStatusException_WhenUserIsNotFound() throws Exception {
        var id = userList.getLast().getId() + 1;

        mockMvc.perform(MockMvcRequestBuilders.delete(url + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}