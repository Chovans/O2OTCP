package com.dotnar.mongo.service;

import com.dotnar.bean.mongo.MongoResult;
import com.dotnar.util.JsonUtil;
import com.dotnar.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chovans on 15/8/22.
 */
@Service
public class MongoService {
    @Autowired
    private static Mongo mongo;

    private static Boolean checkDBName = true;
    private static Boolean checkDocumentName = true;

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
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        //若保存过程中出现不可知错误，抛出错误
        try {
            mongo.getDB(dbName).getCollection(documentName).save(basicDBObject);
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
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        try {
            mongoResult.setContent(mongo.getDB(dbName).getCollection(documentName).findOne(new ObjectId(id)));
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
        DBCursor dbCursor = null;
        //检测db名称和document名
        if (checkName(dbName, documentName) != null) {
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        try {
            //根据条件转换成query对象
            BasicDBObject basicDBObject = MongoUtil.transProperties(jsonObj);
            //通过游标的方式逐条读取
            dbCursor = mongo.getDB(dbName).getCollection(documentName).find(basicDBObject);
            List<DBObject> objs = new ArrayList<>();
            while (dbCursor.hasNext()) {
                objs.add(dbCursor.next());
            }
            mongoResult.setContent(objs);
        } catch (Exception e) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        } finally {
            //关闭游标
            if (dbCursor != null) {
                dbCursor.close();
            }
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
     * @return
     */
    public static String findList(String dbName, String documentName, String num, String page) {
        MongoResult mongoResult = new MongoResult();
        DBCursor cursor = null;

        //检测db名称和document名
        if (checkName(dbName, documentName) != null) {
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        try {
            Integer pageNum = Integer.parseInt(num);
            Integer pageSize = Integer.parseInt(page);
            Integer skipNum = (pageNum - 1) * pageSize;

            cursor = mongo.getDB(dbName).getCollection(documentName).find().skip(skipNum).limit(pageSize);
            List<DBObject> objs = new ArrayList<>();
            while (cursor.hasNext()) {
                objs.add(cursor.next());
            }
            mongoResult.setContent(objs);

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
        return findList(dbName, documentName, "1", String.valueOf(Integer.MAX_VALUE));
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
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        try {
            //转换新对象
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.append("$set", MongoUtil.transProperties(jsonObj));

            mongo.getDB(dbName).getCollection(documentName).updateMulti(new BasicDBObject().append("_id", new ObjectId(id)), newDocument);
        } catch (Exception e) {
            mongoResult.setResult("fail");
            mongoResult.setMsg(e.getMessage());
        }
        return JsonUtil.toJSONString(mongoResult);
    }

    /**
     * 删除
     * @param dbName
     * @param documentName
     * @param id
     * @return
     */
    public static String remove(String dbName, String documentName, String id) {
        MongoResult mongoResult = new MongoResult();

        //检测db名称和document名
        if (checkName(dbName, documentName) != null) {
            mongoResult.setMsg(checkName(dbName, documentName));
            return JsonUtil.toJSONString(mongoResult);
        }

        try {
            mongo.getDB(dbName).getCollection(documentName).remove(new BasicDBObject().append("_id",new ObjectId(id)));
        } catch (Exception e) {
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

        if (mongo != null && checkDBName && mongo.getDatabaseNames().contains(dbName)) {
            if (checkDocumentName && !mongo.getDB(dbName).collectionExists(documentName)) {
                System.out.println("mo document" + documentName);
                return "mo document" + documentName;
            }
        } else {
            System.out.println("mo db" + documentName);
            return "no db " + dbName;
        }
        return null;
    }

}
