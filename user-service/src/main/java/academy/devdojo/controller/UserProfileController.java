package academy.devdojo.controller;

import academy.devdojo.domain.UserProfile;
import academy.devdojo.mapper.ProfileMapper;
import academy.devdojo.mapper.UserProfileMapper;
import academy.devdojo.request.ProfilePostRequest;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import academy.devdojo.response.UserProfileGetResponse;
import academy.devdojo.service.ProfileService;
import academy.devdojo.service.UserProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user-profiles")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class UserProfileController {

    private final UserProfileService service;
    private final UserProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll() {
        log.debug("Request received to list all user profile");

        var userProfiles = service.findAll();
        var userProfileGetResponses = mapper.toUserProfileGetResponse(userProfiles);

        return ResponseEntity.ok(userProfileGetResponses);
    }


    @GetMapping("profiles/{id}")
    public ResponseEntity<List<UserProfileGetResponse>> findAll(@PathVariable Long id) {
        log.debug("Request received to list all user by profiles id '{}'", id);

        var users = service.findAllUsersByProfileId(id);
        var userProfileGetResponseList = mapper.toUserProfileGetResponseList(users);

        return ResponseEntity.ok(userProfileGetResponseList);
    }

}
