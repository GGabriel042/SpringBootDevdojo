package academy.devdojo.controller;

import academy.devdojo.response.ProfileGetResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProfileControllerIT {
    private static final String URL = "/v1/profiles";
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("GET v1/profiles returns a list with all profiles")
    @Sql(value = "/sql/init_two_profiles.sql")
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() throws Exception {
        var typeReference = new ParameterizedTypeReference<List<ProfileGetResponse>>(){};
        var responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isNotNull().doesNotContainNull();

        responseEntity.getBody()
                .forEach(profileGetResponse -> Assertions.assertThat(profileGetResponse).hasNoNullFieldsOrProperties());
    }
}
