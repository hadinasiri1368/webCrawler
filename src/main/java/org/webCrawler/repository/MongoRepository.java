package org.webCrawler.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class MongoRepository<ENTITY> {
    @Autowired
    MongoTemplate mongoTemplate;

    public ENTITY save(ENTITY entity) {
        return mongoTemplate.save(entity);
    }

    public ENTITY get(String id, Class<ENTITY> clazz) {
        return mongoTemplate.findById(id, clazz);
    }

    public ENTITY remove(String id, Class<ENTITY> clazz) {
        Query query = new Query(new Criteria("id").is(id));
        return mongoTemplate.findAndRemove(query, clazz);
    }
}