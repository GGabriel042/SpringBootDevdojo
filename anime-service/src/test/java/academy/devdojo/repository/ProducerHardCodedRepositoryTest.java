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
    @DisplayName("findByName returns a producer with given name")
    void findByName_ReturnsAProducer_WhenSuccessful() {
        var expectedProducer = producersList.getFirst();

        var producers = repository.findByName(expectedProducer.getName());

        Assertions.assertThat(producers).isNotNull().contains(expectedProducer);
    }


}