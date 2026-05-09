package academy.devdojo.repository;

import academy.devdojo.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProducerHardCodedRepositoryTest {

    @InjectMocks
    private ProducerHardCodedRepository repository;

    @Mock
    private ProducerData producerData;
    private final List<Producer> producersList = new ArrayList<>();

    @BeforeEach
    void init() {
        var ufotable = Producer.builder().id(1L).name("ufotable").createdAt(LocalDateTime.now()).build();
        var witStudio = Producer.builder().id(2L).name("witStudio").createdAt(LocalDateTime.now()).build();
        var studioGhibli = Producer.builder().id(3L).name("studioGhibli").createdAt(LocalDateTime.now()).build();
        producersList.addAll(List.of(ufotable, witStudio, studioGhibli));
        BDDMockito.when(producerData.getProducers()).thenReturn(producersList);
    }

    @Test
    @DisplayName("findAll returns a list with all producers")
    void findAll_ReturnsAllProducers_WhenSuccessful() {

        var producers = repository.findAll();
        Assertions.assertThat(producers).isNotNull().hasSize(producers.size());
    }

    @Test
    @DisplayName("findById returns a producer with given id")
    void findById_ReturnsAProducer_WhenSuccessful() {
        var expectedProducer = producersList.getFirst();

        var producers = repository.findById(expectedProducer.getId());

        Assertions.assertThat(producers).isPresent().contains(expectedProducer);
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists")
    void findByName_ReturnsFoundProducerInList_WhenNameIsFound() {
        var expectedProducer = producersList.getFirst();

        var producers = repository.findByName(expectedProducer.getName());

        Assertions.assertThat(producers).isNotNull().contains(expectedProducer);
    }

    @Test
    @DisplayName("findByName returns empty lit when name is null")
    void findByName_ReturnsEmptyList_WhenNameIsNull() {

        var producers = repository.findByName(null);

        Assertions.assertThat(producers).isNotNull().isEmpty();
    }


    @Test
    @DisplayName("save creates a producer")
    void save_CreateAProducer_WhenSuccessful() {
        var producerToSave = Producer.builder()
                .id(99L)
                .name("Mappa")
                .createdAt(LocalDateTime.now()).build();

        var producer = repository.save(producerToSave);

        Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();

        var producerSavedOptional = repository.findById(producerToSave.getId());
        Assertions.assertThat(producerSavedOptional).isPresent().contains(producerToSave);
    }


    @Test
    @DisplayName("delete removes a producer")
    void delete_RemovesProducer_WhenNameIsNull() {

        var producerToDelete = producersList.getFirst();
        repository.delete(producerToDelete);

        var producers = repository.findAll();
        Assertions.assertThat(producers).isNotEmpty().doesNotContain(producerToDelete);
    }


    @Test
    @DisplayName("update updates a producer")
    void update_UpdateAProducer_WhenSuccessful() {
        var producerToUpdate = producersList.getFirst();
        producerToUpdate.setName("Aniplex");

        repository.update(producerToUpdate);

        Assertions.assertThat(this.producersList).contains(producerToUpdate);

        var producerUpdatedOptional = repository.findById(producerToUpdate.getId());
        Assertions.assertThat(producerUpdatedOptional).isPresent();
        Assertions.assertThat(producerUpdatedOptional.get().getName()).isEqualTo(producerToUpdate.getName());

    }

}