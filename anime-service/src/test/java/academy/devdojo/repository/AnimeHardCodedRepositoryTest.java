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
    void findById() {
        var expectedAnime = animeList.getFirst();

        var anime = repository.findById(expectedAnime.getId());

        Assertions.assertThat(anime).isPresent().contains(expectedAnime);
    }

    @Test
    void findByName() {
    }

    @Test
    void save() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }
}