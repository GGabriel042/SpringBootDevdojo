package academy.devdojo.producer;

import academy.devdojo.domain.Producer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {

    Producer toProducer(ProducerPostRequest postRequest);

    ProducerGetResponse toProducerGetResponse(Producer producer);

    List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producers);

    Producer toProducer(ProducerPutRequest request);
}
