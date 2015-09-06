import com.dotnar.mongo.service.MongoReflectService;
import com.dotnar.mongo.service.MongoService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chovans on 15/8/22.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/conf/spring.xml" })
public class Test {

    @Autowired
    private MongoService mongoService;

    @Autowired
    private MongoReflectService mongoReflectService;

    @org.junit.Test
    public void mainTest(){
        String mongoResult = null;
//        mongoResult = mongoService.insert("Gaubee_test_1", "user", "{'_id':'kz是是','name':'chovans','email':'4@','phone':'18759718701'}");
//        mongoResult = mongoService.insert("Gaubee_test_1", "user", "{'id':'55d80f32016868315397cd6e','name':'chovans2','email':'4@','phone':'18759718702'}");
//        mongoResult = mongoService.findById("Gaubee_test_1","user","55dd8a460168e443d13aab1e");
//        mongoResult = mongoService.findOne("Gaubee_test_1","user","{'_id':'55dd8a460168e443d13aab1e'}");
//        mongoResult = mongoService.findOne("Gaubee_test_1","user","{'age':23}");
//        mongoResult = mongoService.findList("Gaubee_test_1","user","1","30",null,"{'name':1}");
//        mongoResult = mongoService.update("test1","test","55d80f32016868315397cd6f","{'email':'5@','name':'jijuyuan','user':'guest'}");
//        mongoResult = mongoService.remove("test1","test","55d8436101687c99f48244c5");
//        mongoResult = mongoService.createCollection("test112","ji");
//        mongoResult = mongoService.creeateDB("test2");
//        mongoResult = mongoService.createCollection("test2","test");
//        mongoResult = mongoService.createDBAndCollection("test3","test");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
//        mongoService.createDB("jijuyuan");
//        mongoResult = mongoService.createCollection("test1","chovans");
//        mongoResult = mongoService.getDBAndCollection();

        mongoResult = mongoReflectService.reflectMethod("findAll", "[['Gaubee_test_1','user']]");
        System.out.println(mongoResult);
    }

    @org.junit.Test
    public void HproseTest(){
        try{
//            HproseTcpClient client = new HproseTcpClient("121.40.72.93:7074");
//            Object string = client.invoke("insert", new Object[]{"test2", "doc", "{'id':'55d80f32016868315397cd6f','name':'chovans','email':'4@','phone':'18759718701'}"});
//            System.out.println(client);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void matchTest(){


    }

    public static void main(String[] args){
        String url = "https://git.oschina.net/xuezi/pc_base_version.git";

//        Pattern pattern = Pattern.compile("/[0-9a-zA-Z]+/");

        System.out.println(Pattern.matches("/[0-9a-zA-Z]+/",url));
    }


}
