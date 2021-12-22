package com.catcher.request

import com.catcher.request.dto.CaughtHeader
import com.catcher.request.dto.CaughtRequest
import com.catcher.request.models.RequestCatcherPageViewModel
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.rxjava3.subjects.PublishSubject
import org.http4k.core.*
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.Method.*
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import org.http4k.routing.websockets
import org.http4k.server.PolyHandler
import org.http4k.server.Undertow
import org.http4k.server.asServer
import org.http4k.template.ThymeleafTemplates
import org.http4k.template.viewModel
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun Request.toDto(): CaughtRequest = CaughtRequest(
    timestamp = ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
    path = this.uri.path,
    body = this.bodyString(),
    method = this.method.name,
    source = this.source?.address ?: "Unknown",
    headers = this.headers.toList().map { CaughtHeader(it.first, it.second) },
)

val publisher: PublishSubject<CaughtRequest> = PublishSubject.create()
val objectMapper = ObjectMapper()

private fun catcherHandler(request: Request): Response {
    publisher.onNext(request.toDto())
    return Response(OK)
}

val app: HttpHandler = routes(
    "/{name}" bind routes(
        "" bind GET to {
            val name = it.path("name")
            println("Viewing $name...")
            val renderer = ThymeleafTemplates().CachingClasspath()
            val view = Body.viewModel(renderer, TEXT_HTML).toLens()
            val viewModel = RequestCatcherPageViewModel(name)
            Response(OK).with(view of viewModel)
        },
        "" bind POST to ::catcherHandler,
        "" bind PUT to ::catcherHandler,
        "" bind PATCH to ::catcherHandler,
        "" bind DELETE to ::catcherHandler,
    )
)

val ws = websockets(
    "/{name}" bind { ws: Websocket ->
        val name = ws.upgradeRequest.path("name")!!
        val disposable = publisher.subscribe {
            if (it.path == "/$name") {
                ws.send(WsMessage(objectMapper.writeValueAsString(it)))
            }
        }
        ws.onClose {
            println("$name is closing")
            disposable.dispose()
        }
    }
)

fun main() {
    val printingApp = PolyHandler(app, ws)

    val server = printingApp.asServer(Undertow(9000)).start()

    println("Server started on " + server.port())
}
