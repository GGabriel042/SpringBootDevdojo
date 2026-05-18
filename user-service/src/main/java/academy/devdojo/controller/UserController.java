package academy.devdojo.controller;

import academy.devdojo.mapper.UserMapper;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import academy.devdojo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserMapper mapper;
    private final UserService service;


    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findUser(@RequestParam (required = false) String name) {

        var users = service.findAll(name);
        var userGetResponseList = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(userGetResponseList);
    }


    @GetMapping("{id}")
    public ResponseEntity<UserGetResponse> findUserById(@PathVariable Long id) {
        var user = service.findByIdOrThrowNotFound(id);
        var userGetResponse = mapper.toUserGetResponse(user);

        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> saveUser(@RequestBody UserPostRequest request) {

        var user = mapper.toUser(request);
        var userToBeSaved = service.saveUser(user);
        var userSaved = mapper.toUserPostResponse(userToBeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser (@PathVariable Long id) {

        service.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateUser (@RequestBody UserPutRequest request) {
        var user = mapper.toUser(request);
        service.updateUser(user);

        return ResponseEntity.noContent().build();
    }
}
