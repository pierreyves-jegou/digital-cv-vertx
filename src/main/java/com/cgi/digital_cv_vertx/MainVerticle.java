package com.cgi.digital_cv_vertx;

import com.cgi.digital_cv_vertx.controller.CvController;
import com.cgi.digital_cv_vertx.repository.CvRepository;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.EventBus;

import java.util.List;

public class MainVerticle extends AbstractVerticle {

  private EventBus eventBus;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
     eventBus = vertx.eventBus();
    readConfiguration(this.vertx)
      .compose(config -> deployAllVerticles(config))
      .onSuccess(ar -> startPromise.complete())
      .onFailure(ar -> startPromise.fail(ar));
  }

  private Future<String> deployVerticleRepository(JsonObject config){
    Promise promise = Promise.promise();
    CvRepository cvRepository = new CvRepository(this.vertx, config);
    vertx.deployVerticle(cvRepository, ar -> {
      if(ar.succeeded()){
        promise.complete(ar.result());
      }else{
        promise.fail(ar.cause());
      }
    });
    return promise.future();
  }

  private Future<String> deployVerticleController(JsonObject config){
    Promise promise = Promise.promise();
    CvController cvController = new CvController(config);
    vertx.deployVerticle(cvController, ar -> {
      if(ar.succeeded()){
        promise.complete(ar.result());
      }else{
        promise.fail(ar.cause());
      }
    });
    return promise.future();
  }

  private Future<JsonObject> readConfiguration(Vertx vertx){
    Promise<JsonObject> promise = Promise.promise();
    ConfigRetriever retriever = ConfigRetriever.create(this.vertx);
    retriever.getConfig(ar -> {
      if (ar.failed()) {
        // Failed to retrieve the configuration
        //TODO
        promise.fail("not abble to read the configuration");
      } else {
        promise.complete(ar.result());
      }
    });
    return promise.future();
  }

  private Future<String> deployAllVerticles(JsonObject config){
    return deployVerticleRepository(config)
      .compose(ar -> {
        return deployVerticleController(config);
      });
  }

}


