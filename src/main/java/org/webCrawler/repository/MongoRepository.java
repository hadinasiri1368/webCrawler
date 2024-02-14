package org.webCrawler.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<ENTITY> list(Class<ENTITY> clazz) {
        return mongoTemplate.findAll(clazz);
    }

    public List<ENTITY> list(Class<ENTITY> clazz, Query query) {
        return mongoTemplate.find(query, clazz);
    }

    public ENTITY remove(String id, Class<ENTITY> clazz) {
        Query query = new Query(new Criteria("id").is(id));
        return mongoTemplate.findAndRemove(query, clazz);
    }
}