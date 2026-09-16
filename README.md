# COMP3011 Assignment 1

--- 
- Student name: Dang Thi Thanh Nhan
- Student ID: a2943401
- Student email: a2943401@adelaide.edu.au

---

## I. Project Overview
This project is a Spring Boot web application that converts recorded
audio into text using a cloud-based Speech-to-Text API.
## II. Architecture
**The application is divided into different components**

**1. Controllers**

- AdministrativeController manages administrative tasks such as checking server uptime and requesting a graceful server shutdown.

- GlobalStatController provides an endpoint for retrieving the application's global token usage statistics.

- STTController handles audio upload requests from the frontend and returns the transcription result or an error response.

> The controllers are separated by  responsibility, which helps each one to focus on a specific type of API request.

**2. Services**

- GlobalStatService tracks the total number of input and output tokens used by the application.

- STTService manages interactions with the Cloud STT API by transmitting audio files, retrieving the resulting transcriptions, and updating global token usage.

> The services contain the application's main business logic. This helps the controllers focused on handling HTTP requests, and the services handle the application logic and communication with external services.

**3. Models**

- AudioTranscriptionResponse describes the response from the STT API (text and token usage).

- ErrorsResponse stores information returned to the client when an error occurs.
- GlobalStatsResponse contains the total input and output token returned by the global statistics endpoint.

- ServerShutdownResponse the response message returned when a graceful server shutdown is requested.

- ServerUptimeResponse consists of the server start time, current UTC time, and server uptime.

**4. Configuration**
- RestClientConfig configures the RestClient used to communicate with the Cloud STT API.

- VirtualThreadConfig configures Tomcat to use Java virtual threads for handling HTTP requests.

## III. Concurrency
- LongAdder is used for token statistics because multiple requests may update the token counts at the same time. It safely handles these concurrent updates so that the total token counts remain accurate. 

- Because the STT service waits for a response from the external API, virtual threads provide a lightweight way to handle blocking I/O. When a virtual thread is blocked while waiting for I/O, its underlying carrier thread can be used to perform other work. Therefore, the application to handle a large number of concurrent requests with minimal memory overhead.

## IV. Security

The Cloud STT API key is obtained from the **OPENAI_API_KEY** environment
variable at runtime. The API key is not stored in source code and is not logged or exposed to the client.

## V. Regression Testing	
**1. STT Controller Tests**

> These tests check successful transcription requests and error handling.

> Expected results:
- Successful requests return HTTP 200.
- STT service failures return HTTP 500.

> These tests provide assurance that the controller handles both normal and failed STT requests correctly.

**2. Administrative Controller Tests**

> These tests check the server uptime response and graceful shutdown behaviour.

> Expected results:
- Uptime response contains valid time values, non-negative server uptime.
- The first shutdown request returns HTTP 202 Accepted.
- A repeated shutdown request returns HTTP 409 Conflict.

> These tests provide assurance that the administrative endpoints return the correct responses and handle repeated shutdown requests correctly.

**3. Global Statistics Controller Tests**

> These tests check if the controller correctly retrieves and returns the global input and output token counts.

> Expected results:
- The controller returns HTTP 200 OK.
- The returned input token and output token count matches the values provided by the service.

> These tests make sure that the controller returns the correct token statistics from the service.

**4. Global Statistics Service Test**

> These tests check the initial token values, normal token updates, and concurrent token updates to detect potential race conditions.

> Expected results:
- Initial token counts are zero.
- Token updates are accumulated correctly.
- Concurrent updates do not lose token counts.

>These tests provide assurance that global token statistics are initialised correctly, updated, and remain consistent when multiple requests update the statistics concurrently.

**5. STT Service Tests**

> These tests check successful transcription requests, token usage updates, and STT API failures.

> Expected results:
- The transcription response is returned with right format and input and output token counts are updated.
- An STT API failure throws the expected exception and token statistics are not updated when the STT request fails.

> These tests make sure that the STT service handles successful and failed API requests correctly and maintains accurate token statistics.

**6. Concurrency Test**

> These tests check the application's ability to handle more than 200 concurrent blocking HTTP requests.

> Expected results:
- Successful execution of over 200 simultaneous requests without concurrency errors.
- No requests fail due to concurrent processing.
- The requests are handled using virtual threads.

> These tests make sure that the application can handle a high number of concurrent blocking requests without losing responses and requests are handled by virtual threads. 

## VI. Logging Approach

- In the application components, logs are used to record events such as audio upload, transcription success or failure, server shutdown, and whether requests are handled by virtual threads. Errors are logged to help identify failures during execution.

- In tests, logs are used to record test same information in the application components and number of concurrent requests with the total execution time.

## VII. AI Assistant

- In STTControllerConcurrencyTest, there is one method called 'sendConcurrentRequests()'. Because I was unsure how to create one HTTP request task for each concurrent request. I could not find a suitable example on Google, so I used AI to help understand and implement this approach.
