package com.cgi.digital_cv_vertx;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    ConfigRetriever retriever = ConfigRetriever.create(this.vertx);
    retriever.getConfig(ar -> {
      if (ar.failed()) {
        // Failed to retrieve the configuration
      } else {
        JsonObject config = ar.result();
        System.out.println(config);
      }
    });

    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
