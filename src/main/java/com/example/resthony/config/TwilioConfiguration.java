package com.example.resthony.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties("twilio")
public class TwilioConfiguration {

    @Value("AC1a10644486a6fc6d0768e06143c54477")
    private String accountSID;

    @Value("07712c436086119bb6173ba8e05c8ab0")
    private String authToken;

    @Value("+12076790846")
    private String trialNumber;

}
