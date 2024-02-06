package com.example.notificationservice.consumer;


import com.example.notificationservice.Repositories.BlackListRepository;
import com.example.notificationservice.entity.Exceptions.InvalidEntryException;
import com.example.notificationservice.entity.domain.ElasticSearchSchema;
import com.example.notificationservice.entity.domain.SmsRequests;
import com.example.notificationservice.entity.events.SmsRequestKafkaEvent;
import com.example.notificationservice.kafkaConsumers.SmsRequestConsumer;
import com.example.notificationservice.services.ElasticSearchService;
import com.example.notificationservice.services.Message3rdPartyService;
import com.example.notificationservice.services.SmsRequestService;
import com.example.notificationservice.utils.FunctionsAndConditions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

@SpringBootTest
public class SmsRequestConsumerTest {


    @Autowired
    private SmsRequestConsumer smsRequestConsumer;
    @MockBean
    private SmsRequestService smsRequestService;

    @MockBean
    private ElasticSearchService elasticSearchService;

    @Autowired
    private FunctionsAndConditions functionsAndConditions;

    @MockBean
    private Message3rdPartyService message3rdPartyService;

    @MockBean
    private BlackListRepository blackListRepository;


    @Test
    public void testConsume_Success() {
        SmsRequests smsRequest = new SmsRequests();
        smsRequest.setId(123);
        smsRequest.setNumber("+919876543210"); // Valid phone number
        when(smsRequestService.findById(123)).thenReturn(smsRequest);

        // Mock Message3rdPartyService.postDataToAPI
        ResponseEntity<String> response = new ResponseEntity<>("OK", HttpStatus.OK);
        when(message3rdPartyService.postDataToAPI(smsRequest.getMessage(), smsRequest.getNumber(), smsRequest.getId())).thenReturn(response);
        when(blackListRepository.isPresent(any())).thenReturn(false);
        smsRequestConsumer.consume(new SmsRequestKafkaEvent(123));

        ElasticSearchSchema elasticSearchData = new ElasticSearchSchema();

        when(elasticSearchService.save(any(ElasticSearchSchema.class))).thenReturn(new ElasticSearchSchema());
        when(smsRequestService.save(any(SmsRequests.class))).thenReturn(new SmsRequests());


        verify(smsRequestService, times(1)).findById(123);
        verify(message3rdPartyService, times(1)).postDataToAPI(smsRequest.getMessage(), smsRequest.getNumber(), smsRequest.getId());

        verify(elasticSearchService, times(1)).save(any(ElasticSearchSchema.class));
        verify(smsRequestService, times(1)).save(any(SmsRequests.class));
    }
    @Test
    public void testConsume_InvalidPhoneNumber() {
        // Test for processing with an invalid phone number

        // Mock SmsRequestService.findById
        SmsRequests smsRequest = new SmsRequests();
        smsRequest.setId(123);
        smsRequest.setNumber("123456"); // Invalid phone number
        when(smsRequestService.findById(123)).thenReturn(smsRequest);
        when(smsRequestService.save(smsRequest)).thenReturn(smsRequest);
        // Call consume method
        smsRequestConsumer.consume(new SmsRequestKafkaEvent(123));

        // Verify interactions
        verify(smsRequestService, times(1)).findById(123);
        verifyNoInteractions(message3rdPartyService); // No interaction with third-party service
        verifyNoInteractions(elasticSearchService); // No interaction with ElasticSearch service
        verify(smsRequestService, times(1)).save(any());
    }

    @Test
    public void testConsume_BlackListedNumber() {
        // Test for processing with a blacklisted phone number

        // Mock SmsRequestService.findById
        SmsRequests smsRequest = new SmsRequests();
        smsRequest.setId(123);
        smsRequest.setNumber("+919876543210"); // Valid phone number
        when(smsRequestService.findById(123)).thenReturn(smsRequest);
        when(smsRequestService.save(smsRequest)).thenReturn(smsRequest);
        // Mock BlackListRepository.isPresent
        when(blackListRepository.isPresent("+919876543210")).thenReturn(true);

        // Call consume method
        smsRequestConsumer.consume(new SmsRequestKafkaEvent(123));

        // Verify interactions
        verify(smsRequestService, times(1)).findById(123);
        verifyNoInteractions(message3rdPartyService); // No interaction with third-party service
        verifyNoInteractions(elasticSearchService); // No interaction with ElasticSearch service
        verify(smsRequestService, times(1)).save(any());
    }
    @Test
    public void testConsume_ThirdPartyApiFailure() {
        // Test for processing when the third-party API returns an error response

        // Mock SmsRequestService.findById
        SmsRequests smsRequest = new SmsRequests();
        smsRequest.setId(123);
        smsRequest.setNumber("+919876543210"); // Valid phone number
        when(smsRequestService.findById(123)).thenReturn(smsRequest);

        // Mock Message3rdPartyService.postDataToAPI to simulate failure response
        ResponseEntity<String> response = new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        when(message3rdPartyService.postDataToAPI(smsRequest.getMessage(), smsRequest.getNumber(), smsRequest.getId())).thenReturn(response);

        // Call consume method
        smsRequestConsumer.consume(new SmsRequestKafkaEvent(123));

        // Verify interactions
        verify(smsRequestService, times(1)).findById(123);
        verify(message3rdPartyService, times(1)).postDataToAPI(smsRequest.getMessage(), smsRequest.getNumber(), smsRequest.getId());
//        verify(functionsAndConditions, never()).handleSuccessfulRequest(any(), any()); // No successful request handling
        verifyNoInteractions(elasticSearchService); // No interaction with ElasticSearch service
//        verify(functionsAndConditions, times(1)).handleApiFailure(any(), any(), anyInt()); // Verify handling of API failure
        verify(smsRequestService, times(1)).save(any());
    }
    @Test
    public void testConsume_ExceptionDuringProcessing() {
        // Test for processing when an exception occurs during processing

        // Mock SmsRequestService.findById to throw an exception
        when(smsRequestService.findById(123)).thenThrow(new RuntimeException("Simulated exception"));

        // Call consume method

        Exception exception = Assertions.assertThrows(Exception.class,()->  smsRequestConsumer.consume(new SmsRequestKafkaEvent(123)));
        verify(smsRequestService, times(1)).findById(123);
        verifyNoInteractions(message3rdPartyService); // No interaction with third-party service
        verifyNoInteractions(elasticSearchService); // No interaction with ElasticSearch service
        verify(smsRequestService, never()).save(any()); // No save should be attempted due to exception
    }



}
