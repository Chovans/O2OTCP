package com.dotnar.bean.mongo;

import java.util.Set;

/**
 * @author chovans on 15/8/27.
 */
public class MongoCollections {
    private String dbName;
    private Set<String> collections;

    public MongoCollections(String dbName, Set<String> collections) {
        this.dbName = dbName;
        this.collections = collections;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Set<String> getCollections() {
        return collections;
    }

    public void setCollections(Set<String> collections) {
        this.collections = collections;
    }
}
