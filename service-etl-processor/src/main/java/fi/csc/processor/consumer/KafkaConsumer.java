package fi.csc.processor.consumer;

import fi.csc.processor.model.request.MaterialActivity;
import fi.csc.processor.model.request.SearchRequest;
import fi.csc.processor.model.document.MaterialActivityDocument;
import fi.csc.processor.model.document.SearchRequestDocument;
import fi.csc.processor.repository.primary.MaterialActivityPrimaryRepository;
import fi.csc.processor.repository.primary.SearchRequestPrimaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class KafkaConsumer implements ConsumerSeekAware {
    private final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class.getSimpleName());
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        .withZone(ZoneId.of("UTC"));
    private final MaterialActivityPrimaryRepository materialActivityPrimaryRepository;
    private final SearchRequestPrimaryRepository searchRequestPrimaryRepository;

    @Autowired
    public KafkaConsumer(
        MaterialActivityPrimaryRepository materialActivityPrimaryRepository,
        SearchRequestPrimaryRepository searchRequestPrimaryRepository) {
        this.materialActivityPrimaryRepository = materialActivityPrimaryRepository;
        this.searchRequestPrimaryRepository = searchRequestPrimaryRepository;
    }

    @KafkaListener(
        topics = "${kafka.topic.material-activity}",
        groupId = "${kafka.group-id.material-activity}",
        containerFactory = "kafkaListenerMaterialActivity",
        autoStartup = "true",
        properties = {"enable.auto.commit:false", "auto.offset.reset:latest"})
    public void consume(
        @Payload MaterialActivity materialActivity, // byte[] payload
        @Header(KafkaHeaders.OFFSET) int offset) {
        MaterialActivityDocument materialActivityDocument = new MaterialActivityDocument();
        materialActivityDocument.setTimestamp(LocalDateTime.parse(materialActivity.getTimestamp(), formatter));
        materialActivityDocument.setSessionId(materialActivity.getSessionId());
        materialActivityDocument.setEduMaterialId(materialActivity.getEduMaterialId());
        materialActivityDocument.setInteraction(materialActivity.getInteraction());
        materialActivityDocument.setMetadata(materialActivity.getMetadata());
        materialActivityPrimaryRepository.save(materialActivityDocument);
        LOG.info(String.format("Consumed message -> %s [offset=%d]", materialActivity, offset));
    }

    @KafkaListener(
        topics = "${kafka.topic.search-requests}",
        groupId = "${kafka.group-id.search-requests}",
        containerFactory = "kafkaListenerSearchRequests",
        autoStartup = "true",
        properties = {"enable.auto.commit:false", "auto.offset.reset:latest"})
    public void consume(
        @Payload SearchRequest searchRequest, // byte[] payload
        @Header(KafkaHeaders.OFFSET) int offset) {
        SearchRequestDocument searchRequestDocument = new SearchRequestDocument();
        searchRequestDocument.setTimestamp(LocalDateTime.parse(searchRequest.getTimestamp(), formatter));
        searchRequestDocument.setSessionId(searchRequest.getSessionId());
        searchRequestDocument.setKeywords(searchRequest.getKeywords());
        searchRequestDocument.setFilters(searchRequest.getFilters());
        searchRequestPrimaryRepository.save(searchRequestDocument);
        LOG.info(String.format("Consumed message -> %s [offset=%d]", searchRequest, offset));
    }
}
