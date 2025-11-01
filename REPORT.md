# Lab 4 WebSocket -- Project Report

## Description of Changes

Implemented the `onChat()` test in `ElizaServerTest.kt` by removing the `@Disabled` annotation and adding the required logic. The test validates bidirectional WebSocket communication with the ELIZA server.

Key changes:
- Added `val size = list.size` to capture message count before assertions, preventing race conditions
- Implemented `assertTrue(size in 4..5)` for interval-based validation 
- Added `assertEquals("Can you think of a specific example?", list[3])` to verify ELIZA's response
- Completed `ComplexClient.onMessage()` with conditional `if (list.size == 3)` and `session.basicRemote.sendText("always")`
- Removed `@Suppress("UNUSED_PARAMETER")` annotation since the session parameter is now used

## Technical Decisions

Used `CountDownLatch(4)` to synchronize welcome message, client messages, and ELIZA responses. Implemented interval-based assertions `[4, 5]` instead of exact equality because WebSocket communication is asynchronous and network characteristics can vary, causing the client to receive either 4 or 5 messages. The conditional `if (list.size == 3)` ensures the client sends "always" after receiving three messages (welcome + first exchange), triggering ELIZA's response "Can you think of a specific example?".

## Learning Outcomes

Learned how WebSocket lifecycle works with bidirectional communication, understood asynchronous testing patterns using `CountDownLatch`, and realized why flexible assertions are necessary for async systems due to concurrency problems. Gained practical experience with Spring Boot WebSocket integration, Jakarta WebSocket API, and Kotlin features. Understood the importance of handling race conditions in network operations.

## AI Disclosure

### AI Tools Used
- Github Copilot

### AI-Assisted Work
- Documentation: syntax and explanation of the changes done on the tests.

### Original Work
- Documentation: 95% written by me, 5% AI formatting suggestions
