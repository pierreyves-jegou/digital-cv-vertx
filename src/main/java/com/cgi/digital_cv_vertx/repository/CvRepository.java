package com.cgi.digital_cv_vertx.repository;


import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.mongo.MongoClient;

public class CvRepository extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigRetriever retriever = ConfigRetriever.create(this.vertx);
    retriever.getConfig(ar -> {
      if (ar.failed()) {
        // Failed to retrieve the configuration
      } else {
        JsonObject config = ar.result();
        System.out.println(config);
        MongoClient client = MongoClient.createShared(vertx, config);
        JsonObject query = new JsonObject();
        client.count("myCollection" , query , rs -> {
          if(rs.succeeded()){
            System.out.println(rs.result());
          }else{
            System.out.println(rs.cause());
          }
        });
      }
    });


  }


}
