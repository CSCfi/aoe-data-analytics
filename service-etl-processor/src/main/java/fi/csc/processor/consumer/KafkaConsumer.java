package fi.csc.processor.consumer;

import fi.csc.processor.model.SearchRequest;
import fi.csc.processor.model.document.SearchRequestDocument;
import fi.csc.processor.repository.SearchRequestRepository;
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
    private final SearchRequestRepository searchRequestRepository;

    @Autowired
    public KafkaConsumer(SearchRequestRepository searchRequestRepository) {
        this.searchRequestRepository = searchRequestRepository;
    }

    @KafkaListener(
        topics = "search_requests",
        groupId = "aoe-analytics",
        containerFactory = "kafkaListener",
        properties = {"enable.auto.commit:false", "auto.offset.reset:latest"})
    public void consume(
        @Payload SearchRequest searchRequest, // byte[] payload
        @Header(KafkaHeaders.OFFSET) int offset) {
        SearchRequestDocument searchRequestDocument = new SearchRequestDocument();
        searchRequestDocument.setTimestamp(LocalDateTime.parse(searchRequest.getTimestamp(), formatter));
        searchRequestDocument.setSessionId(searchRequest.getSessionId());
        searchRequestDocument.setKeywords(searchRequest.getKeywords());
        searchRequestDocument.setFilters(searchRequest.getFilters());
        searchRequestRepository.save(searchRequestDocument);
        LOG.info(String.format("Consumed message -> %s [offset=%d]", searchRequest, offset));
    }
}