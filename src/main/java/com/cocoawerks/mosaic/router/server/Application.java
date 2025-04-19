package com.cocoawerks.mosaic.router.server;

import com.cocoawerks.mosaic.server.MosaicServletInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application extends MosaicServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
