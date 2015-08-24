import com.dotnar.bean.mongo.MongoResult;
import com.dotnar.contant.TCPConfig;
import com.dotnar.mongo.service.MongoService;
import com.dotnar.util.JsonUtil;
import hprose.client.HproseClient;
import hprose.client.HproseTcpClient;
import hprose.server.HproseTcpServer;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author chovans on 15/8/22.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/conf/spring.xml" })
public class Test {

    @Autowired
    private MongoService mongoService;

    @org.junit.Test
    public void mainTest(){
        String mongoResult = null;
//        mongoService.insert("test1", "dummyColl", "{'id':'55d80f32016868315397cd6f','name':'chovans','email':'4@','phone':'18759718701'}");
//        mongoResult = mongoService.insert("test1", "dummyColl", "{'_id':'55d80f32016868315397cd6e','name':'chovans2','email':'4@','phone':'18759718702'}");
//        mongoResult = mongoService.findById("test1","dummyColl","55d83f7101682e2e0b5a0807");
//        mongoResult = mongoService.findOne("test1","dummyColl","{'name':'chovans2'}");
//        mongoResult = mongoService.findList("test1","dummyColl","2","3");
//        mongoResult = mongoService.update("test1","dummyColl","55d8436101687c99f48244c5","{'email':'5@','name':'jijuyuan','user':'guest'}");
        mongoResult = mongoService.remove("test1","dummyColl","55d8436101687c99f48244c5");
        System.out.println(mongoResult);
    }

    @org.junit.Test
    public void HproseTest(){
        try{
            HproseTcpClient client = new HproseTcpClient(TCPConfig.MONGODBTCP);
            Object string = client.invoke("insert", new Object[]{"test2", "doc", "{'id':'55d80f32016868315397cd6f','name':'chovans','email':'4@','phone':'18759718701'}"});
            System.out.println(string);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
