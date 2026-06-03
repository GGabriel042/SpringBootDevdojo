package academy.devdojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserGetResponse {

    @Schema(description = "User's id", example = "1")
    private Long id;
    @Schema(description = "User's first name", example = "Edward")
    private String firstName;
    @Schema(description = "User's last name", example = "Elric")
    private String lastName;
    @Schema(description = "User's email. Must be unique", example = "EdwardElric@Fullmetal.com")
    private String email;
}
