@file:Suppress("NoWildcardImports")

package websockets

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.websocket.ClientEndpoint
import jakarta.websocket.ContainerProvider
import jakarta.websocket.OnMessage
import jakarta.websocket.Session
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import java.net.URI
import java.util.concurrent.CountDownLatch

private val logger = KotlinLogging.logger {}

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ElizaServerTest {
    @LocalServerPort
    private var port: Int = 0

    @Test
    fun onOpen() {
        logger.info { "This is the test worker" }
        val latch = CountDownLatch(3)
        val list = mutableListOf<String>()
        val client = SimpleClient(list, latch)
        client.connect("ws://localhost:$port/eliza")
        latch.await()
        assertEquals(3, list.size)
        assertEquals("The doctor is in.", list[0])
    }

    @Test
    fun onChat() {
        logger.info { "Test thread" }
        val latch = CountDownLatch(4)
        val list = mutableListOf<String>()
        val client = ComplexClient(list, latch)
        client.connect("ws://localhost:$port/eliza")
        latch.await()
        val size = list.size
        // 1. EXPLAIN WHY size = list.size IS NECESSARY
        // This assignment is necessary because the list size can change between the moment
        // latch.await() is called and when list.size is accessed. This is due to network operations
        // and WebSocket messages that may arrive at any moment, affecting the list size.
        // 2. REPLACE BY assertXXX expression that checks an interval; assertEquals must not be used;
        assertTrue(size in 4..5, "Expected between 4 and 5 messages, but got $size")
        // 3. EXPLAIN WHY assertEquals CANNOT BE USED AND WHY WE SHOULD CHECK THE INTERVAL
        // Because depending on the characteristics of the network and the speed of the network,
        // there are occasions when the client can receive 4 or 5 messages.
        // This entire exercise is to solve concurrency problems.
        // 4. COMPLETE assertEquals(XXX, list[XXX])
        assertEquals("Can you think of a specific example?", list[3])
    }
}

@ClientEndpoint
class SimpleClient(
    private val list: MutableList<String>,
    private val latch: CountDownLatch,
) {
    @OnMessage
    fun onMessage(message: String) {
        logger.info { "Client received: $message" }
        list.add(message)
        latch.countDown()
    }
}

@ClientEndpoint
class ComplexClient(
    private val list: MutableList<String>,
    private val latch: CountDownLatch,
) {
    @OnMessage
    fun onMessage(
        message: String,
        session: Session,
    ) {
        logger.info { "Client received: $message" }
        list.add(message)
        latch.countDown()
        // 5. COMPLETE if (expression) {
        if (list.size == 3) {
            // 6. COMPLETE sentence
            session.basicRemote.sendText("always")
        }
    }
}

fun Any.connect(uri: String) {
    ContainerProvider.getWebSocketContainer().connectToServer(this, URI(uri))
}
