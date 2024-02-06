package com.example.notificationservice.Repositories;

import com.example.notificationservice.entity.domain.ElasticSearchSchema;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticSearchRepository extends ElasticsearchRepository<ElasticSearchSchema,Integer> {

   public List<ElasticSearchSchema> findByTimeBetween(Long start,Long end);

   @Query("{\"match\": {\"message\": \"*?0*\"}}")
   List<ElasticSearchSchema> findByMessageContainingMessage(String searchTerm);

}
