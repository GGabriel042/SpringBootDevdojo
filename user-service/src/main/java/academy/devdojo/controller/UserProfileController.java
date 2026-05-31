package academy.devdojo.controller;

import academy.devdojo.domain.UserProfile;
import academy.devdojo.mapper.ProfileMapper;
import academy.devdojo.request.ProfilePostRequest;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import academy.devdojo.service.ProfileService;
import academy.devdojo.service.UserProfileService;
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
public class UserProfileController {

    private final UserProfileService service;


    @GetMapping
    public ResponseEntity<List<UserProfile>> findProfile() {

        var userProfiles = service.findAll();

        return ResponseEntity.ok(userProfiles);
    }

}
