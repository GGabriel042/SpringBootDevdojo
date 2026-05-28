package academy.devdojo.producer;

import academy.devdojo.commons.FileUtils;
import academy.devdojo.commons.ProducerUtils;
import academy.devdojo.domain.Producer;
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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"academy.devdojo.producer", "academy.devdojo.commons"})
//@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodedRepository.class, ProducerData.class})
class ProducerControllerTest {

    private final static String url = "/v1/producers";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProducerRepository repository;
    private List<Producer> producersList;
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProducerUtils producerUtils;

    @BeforeEach
    void init() {
        producersList = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("GET v1/producers returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(producersList);
        var response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));


    }

    @Test
    @DisplayName("GET v1/producers?name=Ufotable returns a list with found object when name exist")
    @Order(2)
    void findAll_ReturnsFoundProducerInList_WhenNameIsFound() throws Exception {
        var response = fileUtils.readResourceFile("producer/get-producer-ufotable-name-200.json");
        var name = "ufotable";
        var ufotable = producersList.stream().filter(producer -> producer.getName().equals(name)).findFirst().orElse(null);

        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.singletonList(ufotable));
        mockMvc.perform(MockMvcRequestBuilders.get(url).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));


    }

    @Test
    @DisplayName("GET v1/producers?name=x returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("producer/get-producer-x-name-200.json");
        var name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get(url).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/1 returns a producer with given id")
    @Order(4)
    void findById_ReturnsProducerById_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("producer/get-producer-by-id-200.json");
        var id = 1L;
        var foundProducer = producersList.stream().filter(producer -> producer.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundProducer);
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/99 throws NotFoundException 404 when producer is not found")
    @Order(5)
    void findById_ThrowsNotFoundException_WhenProducerIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("producer/get-producer-by-id-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get(url + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/producer creates a producer")
    @Order(6)
    void save_CreateAProducer_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("producer/post-request-producer-200.json");
        var response = fileUtils.readResourceFile("producer/post-response-producer-201.json");
        var producerToSave = Producer.builder().id(99L).name("MAPPA").createdAt(LocalDateTime.now()).build();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .content(request)
                        .header("x-api-key", "v1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/producers updates a producer")
    @Order(7)
    void update_UpdateAProducer_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("producer/put-request-producer-200.json");
        var id = 1L;
        var foundProducer = producersList.stream().filter(producer -> producer.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundProducer);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/producers throws NotFoundException when producer is not found")
    @Order(8)
    void update_ThrowsNotFoundException_WhenProducerIsNotFound() throws Exception {

        var request = fileUtils.readResourceFile("producer/put-request-producer-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("DELETE v1/producer/1 removes a producer")
    @Order(9)
    void delete_RemoveProducer_WhenSuccessful() throws Exception {

        var id = producersList.getFirst().getId();
        var foundProducer = producersList.stream().filter(producer -> producer.getId().equals(id)).findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(foundProducer);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(url + "/{id}", id)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/producer throws NotFoundException when producer is not found")
    @Order(10)
    void delete_ThrowsNotFoundException_WhenProducerIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("producer/delete-producer-by-id-404.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(url + "/{id}", id)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    @ParameterizedTest
    @MethodSource("postProducerBadRequestSource")
    @DisplayName("POST v1/producers returns bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> errors) throws Exception {
        var request = fileUtils.readResourceFile("producer/%s".formatted(fileName));

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
    @MethodSource("putProducerBadRequestSource")
    @DisplayName("PUT v1/producers returns bad request when fields are empty")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreInvalid(String fileName, List<String> erros)throws Exception{
        var request = fileUtils.readResourceFile("producer/%s".formatted(fileName));

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




    private static Stream<Arguments> postProducerBadRequestSource() {

        var allRequiredErros = allRequiredErros();

        return Stream.of(
                Arguments.of("post-request-producer-empty-fields-400.json", allRequiredErros),
                Arguments.of("post-request-producer-blank-fields-400.json", allRequiredErros),
                Arguments.of("post-request-producer-null-fields-400.json", allRequiredErros)
        );
    }


    private static Stream<Arguments> putProducerBadRequestSource() {

        var allRequiredErrors = allRequiredErros();
        allRequiredErrors.add("The field 'id' cannot be null");

        return Stream.of(
                Arguments.of("put-request-producer-empty-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-producer-blank-fields-400.json", allRequiredErrors),
                Arguments.of("put-request-producer-null-fields-400.json", allRequiredErrors)
        );
    }

    private static List<String> allRequiredErros(){
        var nameRequiredError = "The field 'name' is required";
        return new ArrayList<>(List.of(nameRequiredError));
    }


}