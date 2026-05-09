package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnimeHardCodedRepositoryTest {

    @InjectMocks
    private AnimeHardCodedRepository repository;

    @Mock
    private AnimeData animeData;
    private final List<Anime> animeList = new ArrayList<>();

    @BeforeEach
    void init() {
        var steinsGate = Anime.builder().id(1L).name("Steins Gate").build();
        var toradora = Anime.builder().id(2L).name("Toradora").build();
        var pokemon = Anime.builder().id(3L).name("Pokemon").build();
        animeList.addAll(List.of(steinsGate, toradora, pokemon));
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
    }

    @Test
    @DisplayName("findAll returns a list with all animes")
    void findAll_ReturnsAllAnime_WhenSuccessful() {
        var animes = repository.findAll();
        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findById returns a anime with given id")
    void findById_ReturnsAnime_WhenSuccessful() {
        var expectedAnime = animeList.getFirst();

        var anime = repository.findById(expectedAnime.getId());

        Assertions.assertThat(anime).isPresent().contains(expectedAnime);
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists")
    void findByName_ReturnsFoundAnime_WhenNameIsFound() {
        var expectedAnime = animeList.getFirst();

        var anime = repository.findByName(expectedAnime.getName());

        Assertions.assertThat(anime).isNotNull().contains(expectedAnime);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    void findByName_ReturnsEmptyList_WhenIsNull() {

        var anime = repository.findByName(null);

        Assertions.assertThat(anime).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save creates a anime")
    void save_CreateAnime_WhenSuccessful() {
        var animeToSave = Anime.builder().id(99L).name("Digimon").build();

        var anime = repository.save(animeToSave);
        Assertions.assertThat(anime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();

        var animeOptionalSaved = repository.findById(animeToSave.getId());
        Assertions.assertThat(animeOptionalSaved).isPresent().contains(animeToSave);
    }



    @Test
    void update() {
    }
}