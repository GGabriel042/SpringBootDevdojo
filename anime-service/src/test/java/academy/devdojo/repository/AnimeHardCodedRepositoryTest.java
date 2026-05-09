package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void findAll() {

    }

    @Test
    void findById() {
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