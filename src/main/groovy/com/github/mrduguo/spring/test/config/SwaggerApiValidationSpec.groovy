package com.github.mrduguo.spring.test.config

import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus

class SwaggerApiValidationSpec extends AbstractSpec {

    @Value('${server.contextPath:}')
    String contextPath

    @Value('${api.host:localhost:8080}')
    String serverHost

    void "generated spec should match swagger.json"() {
        given:
        System.properties.setProperty('jdk.map.althashing.threshold','512') // keep json map order
        def swaggerJson = new JsonSlurper().parseText(getClass().getResourceAsStream('/static/swagger.json').text)
        def swaggerSpec=JsonOutput.prettyPrint(new JsonBuilder(swaggerJson).toString())
        def targetFile=new File('build/resources/test/generated/swagger.json')
        targetFile.delete()
        targetFile.parentFile.mkdirs()

        when:
        def entity = getForEntity(contextPath+'/v2/api-docs', String.class)

        then:
        entity.statusCode == HttpStatus.OK
        def apiJson=new JsonSlurper().parseText(entity.body)
        apiJson.put('host', serverHost)
        def generatedSpec=JsonOutput.prettyPrint(new JsonBuilder(apiJson).toString())
        targetFile.write(generatedSpec)
        generatedSpec==swaggerSpec
    }

    void "generated dependencies api should match swagger-mock.json"() {
        given:
        System.properties.setProperty('jdk.map.althashing.threshold','512') // keep json map order
        def swaggerJson = new JsonSlurper().parseText(getClass().getResourceAsStream('/static/swagger-dependencies.json').text)
        def swaggerSpec=JsonOutput.prettyPrint(new JsonBuilder(swaggerJson).toString())
        def targetFile=new File('build/resources/test/generated/swagger-dependencies.json')
        targetFile.delete()
        targetFile.parentFile.mkdirs()

        when:
        def entity = getForEntity(contextPath+'/v2/api-docs?group=dependencies', String.class)

        then:
        entity.statusCode == HttpStatus.OK
        def apiJson=new JsonSlurper().parseText(entity.body)
        apiJson.put('host', serverHost)
        def generatedSpec=JsonOutput.prettyPrint(new JsonBuilder(apiJson).toString())
        targetFile.write(generatedSpec)
        generatedSpec==swaggerSpec
    }

}