# Adaptive-HLS-Streaming-Microservice
Adaptive Bitrate HLS Streaming Architecture in Springboot Microservices Ecosystem

## HLD System Design
![HLD](HLD.png)

## Important Pointers
**User Media Access Flow**

- User requests for a media
- Streaming Service --> Private S3 Bucket (not accessible in public)
- Streaming service returns *Pre-signed URLs* with *fixed TTLs*
- User can only access the media via the signed URLs, not independently. 

**Video S3 Upload & Encoding Flow**
- Receive multipart video file
- Generate unique S3 Key
- Upload file to S3
- Publish VideoUploadedEvent to `Kafka`
- Encoding service picks up & starts ffmpeg encoding

**Encoding Pipeline Flow**
- Download raw video from `S3`
- Encode to multiple qualities using `ffmpeg`
- Generate HLS playlist (`.ts segments`) for each quality
- Create master playlist (`master.m3u8` playlist) having pointers to segments for each quality
- Upload all encoded files back to S3
- Publish `video.encoded` event to Kafka

**Getting streaming URL for a movie**
- Check `redis` cache for existing pre-signed URL
- If cached - return immediately
- If not cached - generate new pre-signed URL from S3
- Caching the URL in Redis with TTL - *5 mins less than actual expiry time* to avoid overloaded cache misses [avoids `Cache Stampede/Dog-piling`]
- Return streaming URL