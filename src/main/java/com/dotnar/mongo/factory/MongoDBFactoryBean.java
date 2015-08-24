package com.dotnar.mongo.factory;

import com.mongodb.*;
import com.sun.media.jfxmediaimpl.MediaDisposer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author chovans on 15/8/22.
 */
@Component
public class MongoDBFactoryBean implements FactoryBean<Mongo>,InitializingBean,MediaDisposer.Disposable {

    private MongoClient mongoClient;
    @Value("${mongo.host}")
    private String host;
    @Value("${mongo.db.name}")
    private String defauleDbName;
    //线程队列数，它以上面 connectionsPerHost值相乘的结果就是线程队列最大值。如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
    private int threadsAllowedToBlockForConnectionMultiplier = 20;
    private int maxWaitTime=60000;//ms
    private int connectTimeout=10000;


    @Override
    public void dispose() {

    }

    @Override
    public Mongo getObject(){
        try{
            return this.mongoClient;
        }catch (Exception e){
            System.out.println("The mongoFactory has error!");
        }
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return MongoClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 配置mongo客户端基本信息，更多参数请查看源码
         */
        ServerAddress server = new ServerAddress(host);

        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
        builder.connectTimeout(this.connectTimeout);
        builder.maxWaitTime(this.maxWaitTime);
        builder.threadsAllowedToBlockForConnectionMultiplier(this.threadsAllowedToBlockForConnectionMultiplier);

        this.mongoClient = new MongoClient(server, builder.build());
    }

    public void setHost(String host) {
        this.host = host;
    }

}
