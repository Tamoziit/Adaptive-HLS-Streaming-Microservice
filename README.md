# Adaptive-HLS-Streaming-Microservice
Adaptive Bitrate HLS Streaming Architecture in Springboot Microservices Ecosystem

## Important Pointers
**User Media Access Flow**

User --> requests for a media --> Streaming Service --> Private S3 Bucket (not accessible in public) --> Streaming service returns *Pre-signed URLs* with *fixed TTLs* --> User can only access the media via the signed URLs, not independently. 