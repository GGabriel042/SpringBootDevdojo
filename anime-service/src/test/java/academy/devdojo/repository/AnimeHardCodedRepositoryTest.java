package academy.devdojo.repository;

import academy.devdojo.commons.AnimeUtils;
import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeHardCodedRepositoryTest {

    @InjectMocks
    private AnimeHardCodedRepository repository;
    @InjectMocks
    private AnimeUtils animeUtils;

    @Mock
    private AnimeData animeData;
    private List<Anime> animeList;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
    }

    @Test
    @DisplayName("findAll returns a list with all animes")
    @Order(1)
    void findAll_ReturnsAllAnime_WhenSuccessful() {
        var animes = repository.findAll();
        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findById returns a anime with given id")
    @Order(2)
    void findById_ReturnsAnime_WhenSuccessful() {
        var expectedAnime = animeList.getFirst();

        var anime = repository.findById(expectedAnime.getId());

        Assertions.assertThat(anime).isPresent().contains(expectedAnime);
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists")
    @Order(3)
    void findByName_ReturnsFoundAnime_WhenNameIsFound() {
        var expectedAnime = animeList.getFirst();

        var anime = repository.findByName(expectedAnime.getName());

        Assertions.assertThat(anime).isNotNull().contains(expectedAnime);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(4)
    void findByName_ReturnsEmptyList_WhenIsNull() {

        var anime = repository.findByName(null);

        Assertions.assertThat(anime).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save creates a anime")
    @Order(5)
    void save_CreateAnime_WhenSuccessful() {
        var animeToSave = Anime.builder().id(99L).name("Digimon").build();

        var anime = repository.save(animeToSave);
        Assertions.assertThat(anime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();

        var animeOptionalSaved = repository.findById(animeToSave.getId());
        Assertions.assertThat(animeOptionalSaved).isPresent().contains(animeToSave);
    }

    @Test
    @DisplayName("delete removes a anime")
    @Order(6)
    void delete_RemoveAnime_WhenSuccessful() {
        var animeToDelete = animeList.getFirst();

        repository.delete(animeToDelete);

        var animes = repository.findAll();
        Assertions.assertThat(animes).isNotEmpty().doesNotContain(animeToDelete);

    }

    @Test
    @DisplayName("update updates a anime")
    @Order(7)
    void update_UpdateAnime_WhenSuccessful() {
        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setName("Digimon");

        repository.update(animeToUpdate);

        var animes = repository.findAll();
        Assertions.assertThat(animes).contains(animeToUpdate);

        var animeUpdateOptional = repository.findById(animeToUpdate.getId());
        Assertions.assertThat(animeUpdateOptional).isPresent();
        Assertions.assertThat(animeUpdateOptional.get().getName()).isEqualTo(animeToUpdate.getName());
    }
}