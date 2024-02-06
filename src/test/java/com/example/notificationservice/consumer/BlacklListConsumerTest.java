package com.example.notificationservice.consumer;


import com.example.notificationservice.entity.dto.BlackListApiRequest;
import com.example.notificationservice.entity.dto.BlackListApiResponse;
import com.example.notificationservice.entity.events.BlackListKafkaEvent;
import com.example.notificationservice.kafkaConsumers.BlackListConsumer;
import com.example.notificationservice.services.BlackListService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BlacklListConsumerTest {

    @Autowired
    private BlackListConsumer blackListConsumer;

    @MockBean
    private BlackListService blackListService;


    @Test
    public void testConsumePostOperation(){
        BlackListKafkaEvent event = new BlackListKafkaEvent("POST", new BlackListApiRequest(List.of("12345565", "22343455")));
        BlackListApiResponse expectedResponse = new BlackListApiResponse("Successfully BlackListed");
        when(blackListService.save(any(BlackListApiRequest.class))).thenReturn(expectedResponse);
        blackListConsumer.consume(event);

        verify(blackListService, times(1)).save(any(BlackListApiRequest.class));
    }

    @Test
    public void testConsumeDeleteOperation(){
        BlackListKafkaEvent event = new BlackListKafkaEvent("DELETE", new BlackListApiRequest(List.of("12345565", "22343455")));
        BlackListApiResponse expectedResponse = new BlackListApiResponse("Successfully BlackListed");
        when(blackListService.delete(any(BlackListApiRequest.class))).thenReturn(expectedResponse);
        blackListConsumer.consume(event);

        verify(blackListService, times(1)).delete(any(BlackListApiRequest.class));
    }
    @Test
    public void testConsumeException() {
        // Arrange
        BlackListKafkaEvent event = new BlackListKafkaEvent("POST", new BlackListApiRequest(/* add required parameters */));
        when(blackListService.save(any(BlackListApiRequest.class))).thenThrow(new RuntimeException("Test Exception"));

        // Act and Assert
        // Ensure that the RuntimeException is thrown
        Assertions.assertThrows(RuntimeException.class, () -> blackListConsumer.consume(event));
    }

}
