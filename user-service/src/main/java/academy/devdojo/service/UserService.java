package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<User> findAll(String firstName) {
        return firstName == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(firstName);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save (User user) {
        assertEmailDoesNotExist(user.getEmail());
        return repository.save(user);
    }

    public void delete (Long id) {
        var userToDelete = findByIdOrThrowNotFound(id);
        repository.delete(userToDelete);
    }

    public void update (User userToUpdate) {
        assertEmailDoesNotExist(userToUpdate.getEmail(), userToUpdate.getId());
        var savedUser = findByIdOrThrowNotFound(userToUpdate.getId());
        userToUpdate.setRoles(savedUser.getRoles());
        if (userToUpdate.getPassword() == null) {
            userToUpdate.setPassword(savedUser.getPassword());
        }
        repository.save(userToUpdate);
    }

    public void assertUserExists(Long id){
        findByIdOrThrowNotFound(id);
    }

    public void assertEmailDoesNotExist(String email) {
        repository.findByEmail(email)
                .ifPresent(this::throwEmailExistsException);
    }


    public void assertEmailDoesNotExist(String email, Long id) {
        repository.findByEmailAndIdNot(email, id)
                .ifPresent(this::throwEmailExistsException);
    }

    private void throwEmailExistsException(User user) {
        throw new EmailAlreadyExistException("Email %s already exist".formatted(user.getEmail()));
    }
}
