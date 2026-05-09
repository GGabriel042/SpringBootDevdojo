package academy.devdojo.repository;

import academy.devdojo.domain.Anime;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class AnimeData {

    private  final List<Anime> animes = new ArrayList<>();
     {
        var Fullmetal = new Anime(1L, "Fullmetal");
        var Cavaleiros = new Anime(2L, "Cavaleiros");
        var DragonBall = new Anime(3L, "DragonBall");
        animes.addAll(List.of(Fullmetal, Cavaleiros, DragonBall));
    }


}
