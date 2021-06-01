package za.co.dearx.leave.config;

import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.ProcessEngineService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class CamundaConfiguration {

    @Bean
    @DependsOn("liquibase")
    public ProcessEngineService processEngineService() {
        return BpmPlatform.getProcessEngineService();
    }
}
