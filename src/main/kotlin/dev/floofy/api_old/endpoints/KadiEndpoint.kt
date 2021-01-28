/**
 * Copyright (c) 2020-2021 August
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.floofy.api_old.endpoints

import dev.floofy.api_old.core.Endpoint
import dev.floofy.api_old.core.Image
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.io.IOException

class KadiEndpoint: Endpoint(HttpMethod.GET, "/kadi/random", 0) {
    override fun run(ctx: RoutingContext) {
        val res = ctx.response()
        val kadi = File("/var/www/cdn/kadi")
        val files = mutableListOf<File>()
        val listed = kadi.listFiles() ?: emptyArray()

        for (l in listed) {
            if (l.isDirectory) continue

            files.add(l)
        }

        val file = files.random()
        res.setStatusCode(200).sendFile(file.canonicalPath)

        return
    }
}

class RandomKadiEndpoint: Endpoint(HttpMethod.GET, "/kadi", 0) {
    override fun run(ctx: RoutingContext) {
        val res = ctx.response()
        val kadi = File("/var/www/cdn/kadi")
        val files = mutableListOf<File>()
        val listed = kadi.listFiles() ?: emptyArray()

        for (l in listed) {
            if (l.isDirectory) continue

            files.add(l)
        }

        val file = files.random()
        val converter = Image(file)

        val dimensions = try {
            converter.dimensions()
        } catch (ex: IOException) {
            null
        }

        return res.setStatusCode(200).end(JsonObject().apply {
            put("photographer", "auguwu")
            put("height", dimensions?.height ?: 0)
            put("width", dimensions?.width ?: 0)
            put("url", "https://cdn.floofy.dev/kadi/${file.name}")
        })
    }
}