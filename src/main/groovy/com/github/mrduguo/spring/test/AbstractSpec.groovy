package com.github.mrduguo.spring.test

import com.github.mrduguo.spring.app.App
import com.github.mrduguo.spring.app.config.EnvConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.context.ApplicationContext
import org.springframework.http.ResponseEntity
import org.springframework.boot.test.TestRestTemplate
import spock.lang.Specification

@SpringApplicationConfiguration(classes = App.class)
@WebIntegrationTest
class AbstractSpec extends Specification {

    static{
        EnvConfig.init()
    }

    @Value('${local.server.port}')
    int port

    @Autowired
    ApplicationContext context

    String url(String path){
        "http://localhost:$port$path"
    }

    public <T> ResponseEntity<T> getForEntity(String path, Class<T> responseType, Object... urlVariables){
        new TestRestTemplate().getForEntity(url(path), responseType,urlVariables)
    }

    public <T> ResponseEntity<T> postForEntity(String path, def payload, Class<T> responseType, Object... urlVariables){
        new TestRestTemplate().postForEntity(url(path),payload, responseType,urlVariables)
    }

}