package academy.devdojo.anime;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService service;
    @InjectMocks
    private AnimeUtils animeUtils;

    @Mock
    private AnimeRepository repository;
    private List<Anime> animeList;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("findAll returns a list with all animes when argument is null")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var animes = service.findAll(null);

        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findAllPaginates returns a paginated list of animes")
    @Order(1)
    void findAllPaginates_ReturnsPaginatedAnimes_WhenSuccessful() {
        var pageRequest = PageRequest.of(0, animeList.size());
        var pageAnime = new PageImpl<>(animeList, pageRequest, animeList.size());

        BDDMockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(pageAnime);

        var animesFound = service.findAllPaginated(pageRequest);
        Assertions.assertThat(animesFound).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findAll returns list with found object when name exists")
    @Order(2)
    void findAll_ReturnsFoundAnimeInList_WhenNameIsFound() {
        var expectedAnime = animeList.getFirst();
        List<Anime> animeListFound = Collections.singletonList(expectedAnime);

        BDDMockito.when(repository.findByName(expectedAnime.getName())).thenReturn(animeListFound);

        var animesFound = service.findAll(expectedAnime.getName());
        Assertions.assertThat(animesFound).containsAll(animeListFound);
    }

    @Test
    @DisplayName("findAll returns empty list when name is not found")
    @Order(3)
    void findAll_ReturnsEmptyList_WhenNameIsNotFound() {
        var name = "not-found";
        BDDMockito.when(repository.findByName(name))
                .thenReturn(Collections.emptyList());

        var animes = service.findAll(name);
        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns a anime with given id")
    @Order(4)
    void findById_ReturnsAnimeById_WhenSuccessful() {
        var expectedAnime = animeList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));

        var animes = service.findByIdOrThrowNotFound(expectedAnime.getId());
        Assertions.assertThat(animes).isEqualTo(expectedAnime);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when anime is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var expectedAnime = animeList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedAnime.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a anime")
    @Order(5)
    void save_CreateAAnime_WhenSuccessful() {
        var animeToSave = Anime.builder().id(99L).name("Re Zero").build();
        BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);

        var savedAnime = service.save(animeToSave);
        Assertions.assertThat(savedAnime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
    }


    @Test
    @DisplayName("Delete removes a anime")
    @Order(6)
    void delete_RemoveAnime_WhenSuccessful() {
        var animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));

        BDDMockito.doNothing().when(repository).delete(animeToDelete);
        Assertions.assertThatNoException()
                .isThrownBy(() -> service.delete(animeToDelete.getId()));
    }


    @Test
    @DisplayName("Delete throws ResponseStatusException when anime is not found")
    @Order(7)
    void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a anime")
    @Order(8)
    void update_UpdateAAnime_WhenSuccessful() {
        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setName("Re Zero");

        BDDMockito.when(repository.findById(animeToUpdate.getId()))
                .thenReturn(Optional.of(animeToUpdate));
        BDDMockito.when(repository.save(animeToUpdate)).thenReturn(animeToUpdate);

        service.update(animeToUpdate);
        Assertions.assertThatNoException()
                .isThrownBy(() -> service.update(animeToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when anime is not found")
    @Order(9)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToUpdate = animeList.getFirst();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}