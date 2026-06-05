package academy.devdojo.anime;

import academy.devdojo.api.AnimeControllerApi;
import academy.devdojo.dto.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class AnimeController implements AnimeControllerApi {

    private final AnimeMapper mapper;
    private final AnimeService service;


    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAllAnimes(@RequestParam(required = false) String name) {

        log.debug("Request received to find all animes, param name {}", name);

        var animes = service.findAll(name);
        var animeGetResponseList = mapper.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(animeGetResponseList);
    }

    @Override
    @GetMapping("/paginated")
    public ResponseEntity<PageAnimeGetResponse> findAllAnimesPaginated(
            @Min(0) @Parameter(name = "page", description = "Zero-based page index (0..N)", in = ParameterIn.QUERY) @Valid @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @Min(1) @Parameter(name = "size", description = "The size of the page to be returned", in = ParameterIn.QUERY) @Valid @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @Parameter(name = "sort", description = "Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.", in = ParameterIn.QUERY) @Valid @RequestParam(value = "sort", required = false) List<String> sort,
            @ParameterObject final Pageable pageable
    ) {
        log.debug("Request received to find all animes paginated");

        var jpaPageAnimeGetResponse = service.findAllPaginated(pageable);

        var pageAnimeGetResponse = mapper.toPageAnimeGetResponse(jpaPageAnimeGetResponse);

        return ResponseEntity.ok(pageAnimeGetResponse);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AnimeGetResponse> findByAnimeId(@PathVariable Long id) {

        log.debug("Request to find anime by id {}", id);

        var anime = service.findByIdOrThrowNotFound(id);
        var animeGetResponse = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> saveAnime(@RequestBody @Valid AnimePostRequest request) {
        log.debug("Request to save anime : {}", request);

        var anime = mapper.toAnime(request);
        var animeSaved = service.save(anime);
        var animeGetResponse = mapper.toAnimePostResponse(animeSaved);


        return ResponseEntity.status(HttpStatus.CREATED).body(animeGetResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimeById(@PathVariable Long id) {
        log.debug("Request to delete anime by id {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }


    @PutMapping
    public ResponseEntity<Void> updateAnime(@RequestBody @Valid AnimePutRequest request) {
        log.debug("Request to update anime {}", request);

        var animeToUpdate = mapper.toAnime(request);
        service.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }

}
