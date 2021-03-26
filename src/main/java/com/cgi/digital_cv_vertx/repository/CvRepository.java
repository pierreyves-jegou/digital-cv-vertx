package com.cgi.digital_cv_vertx.repository;


import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.ext.mongo.MongoClient;

public class CvRepository extends AbstractVerticle {

  MongoClient mongoClient;
  EventBus eventBus;

  public CvRepository(Vertx vertx, JsonObject config){
    mongoClient = MongoClient.create(vertx, config);
    eventBus = vertx.eventBus();
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    JsonObject query = new JsonObject("{}");


   });

    private void registerEvent(){
      this.eventBus.consumer("cv-repository-find", this::handleFindByLogin);
    }

  }

  private <T> void handleFindByLogin(Message<T> tMessage) {
  String userName = tMessage.body().toString();
  JsonObject query = new JsonObject("{ \"userName\": \""+ userName +"\" }");
  mongoClient.find("test", query, res -> {
      if(res.succeeded()){
        System.out.println(res.result());
        startPromise.complete();
      }else{
        System.out.println(res.cause());
        startPromise.fail(res.cause());
      }
  }


}
