package com.dotnar.mongo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dotnar.bean.mongo.MongoCollections;
import com.dotnar.bean.mongo.MongoResult;
import com.dotnar.util.JsonUtil;
import com.dotnar.util.MongoUtil;
import com.mongodb.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chovans on 15/8/22.
 */
@Service
public class MongoService {
    @Autowired
    private static Mongo mongo;

    private static Set<String> dbs = new HashSet<>();

    final static Logger logger = Logger.getLogger(MongoService.class);
    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public MongoService(Mongo mongo) {
        this.mongo = mongo;
    }

    /**
     * 插入数据
     *
     * @param dbName
     * @param documentName
     * @param jsonObj
     * @return
     */
    public static String insert(String dbName, String documentName, String jsonObj) {
        MongoResult mongoResult = new MongoResult();
        BasicDBObject basicDBObject = MongoUtil.transProperties(jsonObj);

        //检测db名称和document名
        if (checkName(dbName, documentName) != null) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        //若保存过程中出现不可知错误，抛出错误
        try {

            if(basicDBObject.get("_id") == null){
                basicDBObject.put("_id",UUID.randomUUID().toString());
            }

            mongo.getDB(dbName).getCollection(documentName).save(basicDBObject);

            logger.info("==== (" + sdf.format(System.currentTimeMillis()) + ")" + dbName + "." + documentName + " ====");
            logger.info("Mongo.insert:" + jsonObj);

        } catch (Exception e) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        }
        return JsonUtil.toJSONString(mongoResult);
    }

    /**
     * find by id
     *
     * @param dbName
     * @param documentName
     * @param id
     * @return
     */
    public static String findById(String dbName, String documentName, String id) {
        MongoResult mongoResult = new MongoResult();

        //检测db名称和document名
        if (checkName(dbName, documentName) != null) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        try {

            if(StringUtils.isEmpty(id)){
                throw new Exception("id is null");
            }

            mongoResult.setContent(mongo.getDB(dbName).getCollection(documentName).findOne(id));

            logger.info("==== (" + sdf.format(System.currentTimeMillis()) + ")" + dbName + "." + documentName + " ====");
            logger.info("Mongo.findById:" + id);

        } catch (Exception e) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        }
        return JsonUtil.toJSONString(mongoResult);
    }

    /**
     * 根据 json的条件去搜索符合条件的结果
     *
     * @param dbName
     * @param documentName
     * @param jsonObj
     * @return
     */
    public static String findOne(String dbName, String documentName, String jsonObj) {
        MongoResult mongoResult = new MongoResult();

        //检测db名称和document名
        if (checkName(dbName, documentName) != null) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        try {
            //根据条件转换成query对象
            BasicDBObject basicDBObject = MongoUtil.transProperties(jsonObj);
            mongoResult.setContent(mongo.getDB(dbName).getCollection(documentName).findOne(basicDBObject));

            logger.info("==== (" + sdf.format(System.currentTimeMillis()) + ")" + dbName + "." + documentName + " ====");
            logger.info("Mongo.findOne:" + jsonObj);

        } catch (Exception e) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        }

        return JsonUtil.toJSONString(mongoResult);
    }

    /**
     * 获取列表咯
     *
     * @param dbName
     * @param documentName
     * @param num
     * @param page
     * @param jsonObj
     * @param sortBy
     * @return
     */
    public static String findList(String dbName, String documentName, String num, String page, String jsonObj,String sortBy) {
        MongoResult mongoResult = new MongoResult();
        DBCursor cursor = null;

        //检测db名称和document名
        if (checkName(dbName, documentName) != null) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        try {
            Integer pageNum = Integer.parseInt(num);
            Integer pageSize = Integer.parseInt(page);
            Integer skipNum = (pageNum - 1) * pageSize;
            BasicDBObject basicDBObject = new BasicDBObject();
            BasicDBObject sortDBObject = new BasicDBObject();

            if (!StringUtils.isEmpty(jsonObj))
                basicDBObject = MongoUtil.transProperties(jsonObj);

            if(!StringUtils.isEmpty(sortBy))
                sortDBObject = MongoUtil.transProperties(sortBy);

            cursor = mongo.getDB(dbName).getCollection(documentName).find(basicDBObject).sort(sortDBObject).skip(skipNum).limit(pageSize);
            List<DBObject> objs = new ArrayList<>();
            while (cursor.hasNext()) {
                objs.add(cursor.next());
            }
            mongoResult.setContent(objs);

            logger.info("==== (" + sdf.format(System.currentTimeMillis()) + ")" + dbName + "." + documentName + " ====");
            logger.info("Mongo.findList:num=" + num + ",page=" + page);

        } catch (Exception e) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return JsonUtil.toJSONString(mongoResult);
    }

    /**
     * 获取某个集合下所有记录
     *
     * @param dbName
     * @param documentName
     * @return
     */
    public static String findAll(String dbName, String documentName) {
        logger.info("Mongo.findAll");


        return findList(dbName, documentName, "1", String.valueOf(Integer.MAX_VALUE), null, null);

    }


    /**
     * 根据id更新对象
     *
     * @param dbName
     * @param documentName
     * @param id
     * @param jsonObj
     * @return
     */
    public static String update(String dbName, String documentName, String id, String jsonObj) {
        MongoResult mongoResult = new MongoResult();

        //检测db名称和document名
        if (checkName(dbName, documentName) != null) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        try {
            //转换新对象
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", MongoUtil.transProperties(jsonObj));

            mongo.getDB(dbName).getCollection(documentName).updateMulti(new BasicDBObject().append("_id", id), newDocument);

            logger.info("==== (" + sdf.format(System.currentTimeMillis()) + ")" + dbName + "." + documentName + " ====");
            logger.info("Mongo.update:id=" + id + ",jsonObj=" + jsonObj);

        } catch (Exception e) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        }
        return JsonUtil.toJSONString(mongoResult);
    }

    /**
     * 删除
     *
     * @param dbName
     * @param documentName
     * @param id
     * @return
     */
    public static String remove(String dbName, String documentName, String id) {
        MongoResult mongoResult = new MongoResult();

        //检测db名称和document名
        if (checkName(dbName, documentName) != null) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        try {
            mongo.getDB(dbName).getCollection(documentName).remove(new BasicDBObject().append("_id", id));

            logger.info("==== (" + sdf.format(System.currentTimeMillis()) + ")" + dbName + "." + documentName + " ====");
            logger.info("Mongo.remove:id=" + id);

        } catch (Exception e) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        }
        return JsonUtil.toJSONString(mongoResult);

    }

    /**
     * 创建数据库
     *
     * @param dbName
     * @return
     */
    public static String createDB(String dbName) {
        MongoResult mongoResult = new MongoResult();


        System.out.println("==== createDB ====");
        logger.info("==== (" + sdf.format(System.currentTimeMillis()) + ")" + dbName + " ====");
        logger.info("==== createDB ====");

        try {
            if (!dbs.contains(dbName))
                dbs.add(dbName);

            System.out.println("==== createDB:" + dbName + "  successfully! ====");
            logger.info("==== createDB:" + dbName + " successfully! ====");

        } catch (Exception e) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        }

        return JsonUtil.toJSONString(mongoResult);
    }

    /**
     * 创建集合，需要已存在或者createDB之后
     *
     * @param dbName
     * @param collectionName
     * @return
     */
    public static String createCollection(String dbName, String collectionName) {
        MongoResult mongoResult = new MongoResult();

        System.out.println("==== createCollections ====");
        logger.info("==== (" + sdf.format(System.currentTimeMillis()) + ")" + dbName + "." + collectionName + " ====");
        logger.info("==== createCollections ====");

        //检测是否有db？
        if (!mongo.getDatabaseNames().contains(dbName) && !dbs.contains(dbName)) {
            mongoResult.setResult("fail");
            mongoResult.setMsg("no db " + dbName);
            return JsonUtil.toJSONString(mongoResult);
        }
        try {
            mongo.getDB(dbName).createCollection(collectionName, new BasicDBObject());

            System.out.println("==== createCollection successfully! ====");
            logger.info("==== createCollection:" + dbName + "." + collectionName + " successfully! ====");
        } catch (Exception e) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        }

        return JsonUtil.toJSONString(mongoResult);
    }

    /**
     * 创建数据库和集合
     *
     * @param dbName
     * @param collectionName
     * @return
     */
    public static String createDBAndCollection(String dbName, String collectionName) {
        MongoResult mongoResult = new MongoResult();

        System.out.println("==== createDBAndCollection ====");
        logger.info("==== (" + sdf.format(System.currentTimeMillis()) + ")" + dbName + "." + collectionName + " ====");
        logger.info("==== createDBAndCollection ====");

        try {
            if (!dbs.contains(dbName)) {
                dbs.add(dbName);
            }
            if (!mongo.getDB(dbName).collectionExists(collectionName)) {
                mongo.getDB(dbName).createCollection(collectionName, new BasicDBObject());
            }

            System.out.println("==== createDBAndCollection successfully! ====");
            logger.info("==== createDBAndCollection:" + dbName + "." + collectionName + " successfully! ====");

        } catch (Exception e) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        }

        return JsonUtil.toJSONString(mongoResult);
    }


    /**
     * 获取所有的db和collections
     * @return
     */
    public static String getDBAndCollection(){

        MongoResult mongoResult = new MongoResult();

        try{

            Set<String> mongoDbs = new HashSet<>();
            mongo.getDatabaseNames().stream().filter(dbName -> !mongoDbs.contains(dbName)).forEach(mongoDbs::add);
            mongoDbs.addAll(dbs);
            mongoDbs.addAll(mongo.getDatabaseNames());

            List<MongoCollections> collections =
                    mongoDbs.stream().map(dbName -> new MongoCollections(dbName, mongo.getDB(dbName).getCollectionNames())).collect(Collectors.toList());

            mongoResult.setContent(collections);
        }catch (Exception e){
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        }

        return JsonUtil.toJSONString(mongoResult);

    }


    /**
     * 检测数据库名和集合名是否正确
     * 如果不需要检测集合名，那就取消if语句
     *
     * @param dbName
     * @param documentName
     * @return
     */
    public static String checkName(String dbName, String documentName) {

        if ((mongo != null && mongo.getDatabaseNames().contains(dbName)) || dbs.contains(dbName)) {

            if (!mongo.getDB(dbName).collectionExists(documentName)) {
                System.out.println("no document " + documentName);
                logger.error("no document " + documentName);
                return "no document " + documentName;
            }

        } else {
            System.out.println("mo db " + documentName);
            logger.error("mo db " + documentName);
            return "no db " + dbName;
        }
        return null;
    }


}
