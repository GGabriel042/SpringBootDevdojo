package academy.devdojo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserPutRequest {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
