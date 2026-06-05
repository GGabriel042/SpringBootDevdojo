package academy.devdojo.anime;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.commons.FileUtils;
import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"academy.devdojo.anime", "academy.devdojo.commons", "academy.devdojo.config"})
@WithMockUser
class AnimeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnimeRepository repository;
    private List<Anime> animeList;
    @Autowired
    private ResourceLoader resourceLoader;
    @InjectMocks
    private AnimeUtils animeUtils;
    private static final String url = "/v1/animes";
    @Autowired
    private FileUtils fileUtils;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }


    @Test
    @DisplayName("GET v1/animes returns a list with all animes when argument is null")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("anime/get-anime-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes returns 403 when role is not User")
    @Order(1)
    @WithMockUser(roles = "ADMIN")
    void findAll_Returns403_WhenRoleIsNotUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("GET v1/animes/paginated returns a paginated list of animes")
    @Order(1)
    void findAllPaginated_ReturnsPaginatedAnimes_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-paginated-200.json");
        var pageRequest = PageRequest.of(0, animeList.size());
        var pageAnime = new PageImpl<>(animeList, pageRequest, animeList.size());

        BDDMockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(pageAnime);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/paginated"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes?name=Toradora returns a list with found object when name exist")
    @Order(2)
    void findAll_ReturnsFoundAnimesInList_WhenNameIsFound() throws Exception {

        var response = fileUtils.readResourceFile("anime/get-anime-toradora-name-200.json");
        var name = "Toradora";
        var toradora = animeList.stream().filter(anime -> anime.getName().equals(name)).findFirst().orElse(null);

        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.singletonList(toradora));

        mockMvc.perform(MockMvcRequestBuilders.get(url).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes?name=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(url).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/2 returns a anime with given id")
    @Order(4)
    void findById_ReturnsAnimeById_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-by-id-200.json");
        var id = 2L;
        var foundAnime = animeList.stream().filter(anime -> anime.getId().equals(id)).findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/99 throws NotFoundException 404 when anime is not found")
    @Order(5)
    void findById_ThrowsNotFoundException_WhenAnimeIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-by-id-404.json");
        var id = (animeList.getLast().getId()) + 1;

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/animes creates a anime")
    @Order(6)
    void save_CreateAnime_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("anime/post-request-anime-200.json");
        var response = fileUtils.readResourceFile("anime/post-response-anime-201.json");
        var animeToBeSaved = Anime.builder().id(99L).name("Digimon").build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToBeSaved);

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
    @DisplayName("PUT v1/animes updates a animes")
    @Order(7)
    void update_UpdateAnime_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("anime/put-request-anime-200.json");
        var id = 1L;
        var foundAnime = animeList.stream().filter(anime -> anime.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @DisplayName("PUT v1/animes throws NotFoundException when anime is not found")
    @Order(8)
    void update_ThrowsNotFoundException_WhenAnimeIsNotFound() throws Exception {

        var request = fileUtils.readResourceFile("anime/put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("DELETE v1/animes/1 removes a anime")
    @Order(9)
    void delete_RemoveAnime_WhenSuccessful() throws Exception {
        var id = animeList.getFirst().getId();
        var foundAnime = animeList.stream().filter(anime -> anime.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundAnime);

        mockMvc.perform(MockMvcRequestBuilders.delete(url + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/animes throws NotFoundException when anime is not found")
    @Order(10)
    void delete_ThrowsNotFoundException_WhenAnimeIsNotFound() throws Exception {

        var response = fileUtils.readResourceFile("anime/delete-anime-by-id-404.json");
        var id = (animeList.getLast().getId()) + 1;

        mockMvc.perform(MockMvcRequestBuilders.delete(url + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @ParameterizedTest
    @MethodSource("postAnimeBadRequestSource")
    @DisplayName("POST v1/animes returns bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(request)
                        .header("x-api-key", "v1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        org.assertj.core.api.Assertions.assertThat(resolvedException).isNotNull();


        org.assertj.core.api.Assertions.assertThat(resolvedException.getMessage())
                .contains(errors);
    }


    @ParameterizedTest
    @MethodSource("putAnimeBadRequestSource")
    @DisplayName("PUT v1/animes returns bad request when fields are empty")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> erros)throws Exception{
        var request = fileUtils.readResourceFile("anime/%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        org.assertj.core.api.Assertions.assertThat(resolvedException).isNotNull();

        Assertions.assertThat(resolvedException.getMessage()).contains(erros);
    }




    private static Stream<Arguments> postAnimeBadRequestSource() {

        var allRequiredErros = allRequiredErros();

        return Stream.of(
                Arguments.of("post-request-anime-empty-fields-400.json", allRequiredErros),
                Arguments.of("post-request-anime-blank-fields-400.json", allRequiredErros),
                Arguments.of("post-request-anime-null-fields-400.json", allRequiredErros)
        );
    }


    private static Stream<Arguments> putAnimeBadRequestSource() {

        var allRequiredErrors = allRequiredErros();
        allRequiredErrors.add("The field 'id' cannot be null");

        return Stream.of(
                Arguments.of("put-request-anime-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-anime-blank-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-anime-null-fields-400.json", allRequiredErrors)
        );
    }

    private static List<String> allRequiredErros(){
        var nameRequiredError = "The field 'name' is required";
        return new ArrayList<>(List.of(nameRequiredError));
    }


}