//package com.example.notificationservice;
//
//import com.example.notificationservice.entity.domain.ElasticSearchSchema;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
////@WebMvcTest(ElasticSearchService.class)
//@SpringBootTest
//public class ElasticSearchServiceTest {
//
//    @Autowired
//    private ElasticSearchService elasticSearchService;
//
//    @MockBean
//    private ElasticSearchRepository elasticSearchRepository;
//
//    @Test
//    void testFindMessage() {
//        // Arrange
//        Long start = 100L;
//        Long end = 200L;
//        List<ElasticSearchSchema> expectedResult = new ArrayList<>();
//        // Add some expected data to the list if needed
//
//        // Mock the repository method
//        when(elasticSearchRepository.findByTimeBetween(start, end)).thenReturn(expectedResult);
//
//        // Act
//        List<ElasticSearchSchema> result = elasticSearchService.findMessage(start, end);
//
//        // Assert
//        assertEquals(expectedResult, result);
//    }
//
//    @Test
//    void testSave() {
//        // Arrange
//        ElasticSearchSchema schema = new ElasticSearchSchema();
//        // Set up the schema object if needed
//        ElasticSearchSchema expectedResult = schema; // Set the expected result as needed
//
//        // Mock the repository method
//        when(elasticSearchRepository.save(schema)).thenReturn(expectedResult);
//
//        // Act
//        ElasticSearchSchema result = elasticSearchService.save(schema);
//
//        // Assert
//        assertEquals(expectedResult, result);
//    }
//
//    @Test
//    void testFindByMessage() {
//        // Arrange
//        String message = "test message";
//        List<ElasticSearchSchema> expectedResult = new ArrayList<>();
//        // Add some expected data to the list if needed
//
//        // Mock the repository method
//        when(elasticSearchRepository.findByMessageContainingMessage(message)).thenReturn(expectedResult);
//
//        // Act
//        List<ElasticSearchSchema> result = elasticSearchService.findByMessage(message);
//
//        // Assert
//        assertEquals(expectedResult, result);
//    }
//}
