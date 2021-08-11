package za.co.dearx.leave.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Properties specific to the Calendarific API Integration.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 */
@Configuration
@ConfigurationProperties(prefix = "calendarific", ignoreUnknownFields = false)
public class CalendarificProperties {

    public String baseUrl;

    public String apiKey;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
