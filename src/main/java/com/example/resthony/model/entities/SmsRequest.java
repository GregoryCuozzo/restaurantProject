package com.example.resthony.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;


@Data
@Builder
@AllArgsConstructor
public class SmsRequest {

    private final String phoneNumber;
    private final String message;

    @Override
    public String toString() {
        return "SmsRequest{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ",message='" + message + '\'' +
                '}';
    }
}







