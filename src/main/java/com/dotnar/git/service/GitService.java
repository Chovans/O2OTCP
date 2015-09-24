package com.dotnar.git.service;

import com.alibaba.fastjson.JSONObject;
import com.dotnar.bean.git.GitInfoResponse;
import com.dotnar.bean.git.GitProject;
import com.dotnar.dao.GitProjectRepository;
import com.dotnar.enums.GitProjectState;
import com.dotnar.util.GitUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chovans on 15/8/11.
 */
@Service
public class GitService {

    private static Logger logger = LoggerFactory.getLogger(GitService.class);

    private String baseGitRepository = null;
    private List<String> httpurl = new ArrayList<String>();

    private Process process;

    @Autowired
    private GitProjectRepository gitProjectRepository;

    @Value("${git.username}")
    private String username;

    @Value("${git.email}")
    private String email;

    @Value("${git.dir.linux}")
    private String basePathLinux;
    @Value("${git.dir.mac}")
    private String basePathMac;
    @Value("${git.dir.windows}")
    private String baseWindows;


    public String configGit(String gitHttps) {

        try {
            //根据操作系统初始化git地址
            if (baseGitRepository == null)
                configBasePath();

            //检测是否合法
            String checkResult = checkIsVaild(gitHttps);
            if (!"success".equals(checkResult)) {
                return checkResult;
            }

            //检测出项目名
            Pattern pattern = Pattern.compile("\\w*\\.git");
            Matcher matcher = pattern.matcher(gitHttps);
            String projectName = (gitHttps.replace(matcher.replaceAll(""), "").split("\\.")[0]);

            //检测出用户名
            String userName = gitHttps.split("/")[gitHttps.split("/").length-2];

            //clear git repository
            deleteGitProject(null,userName,projectName);

            //save in mysql,and add in memory,cause of check
            String id = saveGitProject(gitHttps,userName, projectName);
            httpurl.add(gitHttps);

            logger.info("==== 正在Clone(" + userName + "  " + projectName + ") ====");

            //pull project from url
            String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "shell" + File.separator + "buildGit.sh";
            process = Runtime.getRuntime().exec("sh " + basePath + " " + baseGitRepository + " " + userName + " " + gitHttps + " " + projectName);
            process.waitFor();

            logger.info("==== Clone完成.... ====");

            //clear from memory,cause of check
            httpurl.remove(gitHttps);
            updateGitProject(id, null);

            //if has error message,then do in error handler
            String msg = GitUtil.getShellResult(process.getErrorStream());
            if (!StringUtils.isEmpty(msg) && msg.indexOf("fatal") > 0) {
                process.destroy();
                logger.error(msg);
                return msg;
            } else {
                logger.info(GitUtil.getShellResult(process.getInputStream()));
                updateGitProject(id, projectName);
                process.destroy();
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * 设置不同操作系统中git的目录
     *
     * @throws Exception
     */
    public void configBasePath() throws Exception {
        Properties properties = System.getProperties();
        String osName = properties.getProperty("os.name").toLowerCase();

        if (osName.indexOf("linux") > -1) {
            baseGitRepository = basePathLinux;
        } else if (osName.indexOf("mac") > -1) {
            baseGitRepository = basePathMac;
        } else if (osName.indexOf("windows") > -1) {
            baseGitRepository = baseWindows;
        } else
            throw new Exception("无法识别当前操作系统，git仓库初始化失败");
    }

    /**
     * 检测输入是否合法
     * 1.是否正在进行
     * 2.是否已经完成过
     *
     * @return true    合法
     * false   不合法
     */
    public String checkIsVaild(String url) {
        //1.是否正在进行
        if (httpurl.indexOf(url) > -1) {
            logger.error("地址：" + url + " 正在进行。。。");
            return "正在Clone中...";
        }
        //2.是否已完成过
        if (gitProjectRepository.findByUrl(url).size() > 0) {
            return "已存在相同url的项目";
        }

        return "success";
    }

    /**
     * 保存git项目
     *
     * @param url
     * @param userName
     * @param projectName
     * @return
     */
    public String saveGitProject(String url, String userName ,String projectName) {
        GitProject gitProject = new GitProject();
        gitProject.setName(projectName);
        gitProject.setUserName(userName);
        gitProject.setState(GitProjectState.downing.name());
        gitProject.setUrl(url);
        gitProject.setCreateOn(new Date());
        gitProject.setUpdateOn(new Date());
        gitProjectRepository.save(gitProject);
        return gitProject.getId();
    }

    /**
     * update git project state
     *
     * @param id
     */
    public GitProject updateGitProject(String id, String projectName) {
        GitProject gitProject = gitProjectRepository.findOne(id);
        gitProject.setUpdateOn(new Date());
        gitProject.setState(GitProjectState.done.name());
        //若传入项目名，则尝试读取json内容
        if (!org.springframework.util.StringUtils.isEmpty(projectName)) {
            try {
                File file = new File(baseGitRepository + File.separator +gitProject.getUserName() + File.separator + projectName + File.separator + "package.json");
                //读取package.json文件正常的情况下，以字符串的形式读取其中内容
                if (file.exists() && !file.isDirectory()) {
                    String json = FileUtils.readFileToString(file);
                    JSONObject jsonObject = JSONObject.parseObject(json);
                    gitProject.setParentTemplateName(jsonObject.getString("parent_template_name"));
                    gitProject.set_package(json);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gitProjectRepository.save(gitProject);

        return gitProject;

    }


    /**
     * delete git from server
     * @param id
     */
    public String deleteGitProject(String id,String userName, String projectName){
        projectName = projectName == null?gitProjectRepository.findOne(id).getName():projectName;
        userName = userName == null? gitProjectRepository.findOne(id).getUserName():userName;
        try{
            configBasePath();
            String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "shell" + File.separator + "deleteGit.sh";
            Process process = Runtime.getRuntime().exec("sh " + basePath + " " + baseGitRepository+File.separator+userName + " " + projectName);
            if(id != null)
                gitProjectRepository.delete(id);

            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * get All GitProject
     *
     * @return
     */
    public List<GitProject> findAll() {
        return (List<GitProject>) gitProjectRepository.findAll();
    }


    /**
     * 更新git仓库
     *
     * @param id
     * @return
     */
    public String updateGitProject(String id) {

        if (org.springframework.util.StringUtils.isEmpty(id)) {
            return "id is empty";
        }
        GitProject gitProject = gitProjectRepository.findOne(id);

        try {
            //根据操作系统初始化git地址
            if (baseGitRepository == null)
                configBasePath();


            gitProject.setUpdateOn(new Date());
            gitProject.setState(GitProjectState.updating.name());
            gitProjectRepository.save(gitProject);
            String projectName = gitProject.getName();


            //update git,use order: git pull
            String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "shell" + File.separator + "updateGit.sh";
            process = Runtime.getRuntime().exec("sh " + basePath + " " + baseGitRepository + File.separator + gitProject.getUserName() + File.separator + projectName);
            process.waitFor();

            gitProject = updateGitProject(id, gitProject.getName());

            gitProject.setState(GitProjectState.done.name());
            gitProjectRepository.save(gitProject);

            return "success";

        } catch (Exception e) {
            e.printStackTrace();
            gitProject.setState(GitProjectState.done.name());
            gitProjectRepository.save(gitProject);

            System.err.println("pull error" + e.getMessage());

            return e.getMessage();
        }

    }

    /**
     * git hook address
     * @param projectName
     * @return
     */
    public String hookGit(String projectName){
        List<GitProject> projects = gitProjectRepository.findByName(projectName);
        return updateGitProject(projects.size()>0?projects.get(0).getId():null);
    }

    /**
     * git hook address
     * @param projectName
     * @return
     */
    public String hookGitV2(String projectName,String userName){
        List<GitProject> projects = gitProjectRepository.findByUserNameAndName(userName,projectName);
        return updateGitProject(projects.size()>0?projects.get(0).getId():null);
    }


    /**
     * get packageInformation,repositoryPath and other extend relative
     * @param userName
     * @param templateName
     * @return
     */
    public GitInfoResponse getInfoByName(String userName,String templateName){
        GitInfoResponse gitInfoResponse = new GitInfoResponse();

        List<GitProject> projects ;
        if(userName == null)
            projects = gitProjectRepository.findByName(templateName);
        else
            projects = gitProjectRepository.findByUserNameAndName(userName, templateName);

        if(projects.size() == 0)
            return gitInfoResponse;

        try{
            //根据操作系统初始化git地址
            if (baseGitRepository == null)
                configBasePath();
        }catch (Exception e){
            e.printStackTrace();
            return gitInfoResponse;
        }

        GitProject gitProject = projects.get(0);
        gitInfoResponse.set_package(gitProject.get_package());
        gitInfoResponse.setPath(baseGitRepository + File.separator + gitProject.getName() + File.separator);
        gitInfoResponse.setTemplateName(templateName);
        gitInfoResponse.set_package(gitProject.get_package());
        gitInfoResponse.setRelation(GitUtil.sortRelations((List<GitProject>) gitProjectRepository.findAll(), gitProject));
        gitInfoResponse.setRelationPaths(GitUtil.sortRelationsPath(baseGitRepository+File.separator,gitInfoResponse.getRelation()));

        return gitInfoResponse;
    }


}
