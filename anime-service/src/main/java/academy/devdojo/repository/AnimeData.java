package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {

    private final List<Anime> animes = new ArrayList<>();

    {
        var Fullmetal = Anime.builder().id(1L).name("Fullmetal").build();
        var Cavaleiros = Anime.builder().id(2L).name("Cavaleiros").build();
        var DragonBall = Anime.builder().id(3L).name("DragonBall").build();
        animes.addAll(List.of(Fullmetal, Cavaleiros, DragonBall));
    }

    public List<Anime> getAnimes() {
        return animes;
    }
}
