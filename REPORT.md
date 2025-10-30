# Lab 4 WebSocket -- Project Report

## Description of Changes

Implemented the `onChat()` test in `ElizaServerTest.kt` by removing the `@Disabled` annotation and adding the required logic. The test validates bidirectional WebSocket communication with the ELIZA server.

Key changes:
- Added `val size = list.size` to capture message count before assertions
- Implemented `assertTrue(size >= 2 && size <= 4)` for interval-based validation
- Added `assertEquals("The doctor is in.", list[0])` to verify welcome message
- Completed `ComplexClient.onMessage()` with conditional `if (list.size == 1)` and `session.basicRemote.sendText("I am feeling sad")`
- Added six explanatory comments as required
- Removed `@Suppress("UNUSED_PARAMETER")` annotation since the session parameter is now used

## Technical Decisions

Used `CountDownLatch(4)` to synchronize welcome message, client message, and ELIZA responses. Implemented interval-based assertions `[2, 4]` instead of exact equality because WebSocket communication is asynchronous and timing can vary. The conditional `if (list.size == 1)` ensures the client waits for the server's welcome message before sending test input, maintaining proper message sequencing.

## Learning Outcomes

Learned how WebSocket lifecycle works with bidirectional communication, understood asynchronous testing patterns using `CountDownLatch`, and realized why flexible assertions are necessary for async systems. Gained practical experience with Spring Boot WebSocket integration, Jakarta WebSocket API, and Kotlin features like extension functions and lambdas.

## AI Disclosure

### AI Tools Used
- ChatGPT and Copilot

### AI-Assisted Work
- Test implementation: 100% AI-generated code including all logic, assertions, and comments
- Code analysis: 100% AI assistance in understanding existing structure

### Original Work
- Documentation: 80% written by me, 20% AI formatting suggestions
- Testing and verification: Manually running tests and validating implementation
- Understanding: Independent study of WebSocket concepts and ELIZA history
