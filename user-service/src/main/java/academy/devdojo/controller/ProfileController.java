package academy.devdojo.controller;

import academy.devdojo.mapper.ProfileMapper;
import academy.devdojo.request.ProfilePostRequest;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import academy.devdojo.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profiles")
@Slf4j
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileMapper mapper;
    private final ProfileService service;


    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> findProfile() {

        var profiles = service.findAll();
        var profileGetResponseList = mapper.toProfileGetResponseList(profiles);

        return ResponseEntity.ok(profileGetResponseList);
    }



    @PostMapping
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest request) {

        var profile = mapper.toProfile(request);
        var profileToBeSaved = service.save(profile);
        var profileSaved = mapper.toProfilePostResponse(profileToBeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(profileSaved);
    }

}
