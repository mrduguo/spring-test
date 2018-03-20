package com.github.mrduguo.spring.test.config

import com.github.mrduguo.spring.test.AbstractSpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus

class HealthSpec extends AbstractSpec {

    @Value('${server.contextPath:}')
    String contextPath

    void "web app should be health"() {
        when:
        def entity = getForEntity(contextPath+'/health', Map.class)

        then:
        entity.statusCode == HttpStatus.OK
        entity.body.status == 'UP'
        entity.body.diskSpace.status == 'UP'

        keepServiceRunningWhenRunTheTestIndividually()
    }

    void keepServiceRunningWhenRunTheTestIndividually() {
        if (System.properties.getProperty('sun.java.command')?.endsWith('HealthSpec') || System.properties.getProperty('test.single')?.endsWith('HealthSpec')) {
            println "\nRun test in service mode, you may access the swagger api spec at:\nhttp://localhost:${port}${contextPath}/swagger-ui.html\n"
            Thread.sleep(Long.MAX_VALUE)
        }
    }

}