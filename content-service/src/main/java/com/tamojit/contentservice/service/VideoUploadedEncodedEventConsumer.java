package com.tamojit.contentservice.service;

import com.tamojit.contentservice.model.VideoStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Consumes both video.uploaded & video.encoded Kafka topics
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VideoUploadedEncodedEventConsumer {
    private final ContentService contentService;

    @KafkaListener(
        topics = "video.uploaded"
    )
    public void consumeVideoUploadedEvent(
        @Payload Map<String, Object> payload
    ) {
        String movieId = payload.get("movieId").toString();
        String videoKey =  payload.get("videoKey").toString();

        log.info("Video uploaded for videoKey={}, movieId={}", videoKey, movieId);
        contentService.updateVideoKey(movieId, videoKey);
    }

    @KafkaListener(
        topics = "video.encoded"
    )
    public void consumeVideoEncodedEvent(
        @Payload Map<String, Object> payload
    ) {
        String movieId = payload.get("movieId").toString();
        String hlsUrl =  payload.get("hlsUrl").toString();
        boolean success = (Boolean) payload.get("success");

        if (success) {
            log.info("Video encoded for hlsUrl={}, movieId={}", hlsUrl, movieId);
            contentService.updateHlsUrl(movieId, hlsUrl);
        } else {
            String errorMessage = payload.get("errorMessage").toString();
            contentService.updateVideoStatus(movieId, VideoStatus.FAILED);
        }
    }
}
