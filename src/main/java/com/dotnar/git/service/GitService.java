package com.dotnar.git.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dotnar.bean.git.GitConfigRequest;
import com.dotnar.bean.git.GitInfoResponse;
import com.dotnar.bean.git.GitProject;
import com.dotnar.dao.GitProjectRepository;
import com.dotnar.enums.GitProjectState;
import com.dotnar.util.GitUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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

            //save in mysql,and add in memory,cause of check
            String id = saveGitProject(gitHttps, projectName);
            httpurl.add(gitHttps);

            System.out.println("正在Clone....");

            //pull project from url
            String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "shell" + File.separator + "buildGit.sh";
            process = Runtime.getRuntime().exec("sh " + basePath + " " + baseGitRepository + " " + gitHttps + " " + projectName);
            process.waitFor();

            System.out.println("Clone完成....");

            //clear from memory,cause of check
            httpurl.remove(gitHttps);
            updateGitProject(id, null);

            //if has error message,then do in error handler
            String msg = GitUtil.getShellResult(process.getErrorStream());
            if (!StringUtils.isEmpty(msg) && msg.indexOf("fatal") > 0) {
                process.destroy();
                System.err.println(msg);
                return msg;
            } else {
                System.out.println(GitUtil.getShellResult(process.getInputStream()));
                updateGitProject(id, projectName);
                process.destroy();
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.print(e.getMessage());
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
            System.err.println("地址：" + url + " 正在进行。。。");
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
     * @param name
     * @return
     */
    public String saveGitProject(String url, String name) {
        GitProject gitProject = new GitProject();
        gitProject.setName(name);
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
    public void updateGitProject(String id, String projectName) {
        GitProject gitProject = gitProjectRepository.findOne(id);
        gitProject.setUpdateOn(new Date());
        gitProject.setState(GitProjectState.done.name());
        //若传入项目名，则尝试读取json内容
        if (!org.springframework.util.StringUtils.isEmpty(projectName)) {
            try {
                File file = new File(baseGitRepository + File.separator + projectName + File.separator + "package.json");
                //读取package.json文件正常的情况下，以字符串的形式读取其中内容
                if (file.exists() && file.isFile()) {
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

            System.out.println("pulling....");

            //update git,use order: git pull
            String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "shell" + File.separator + "updateGit.sh";
            process = Runtime.getRuntime().exec("sh " + basePath + " " + baseGitRepository + " " + projectName);
            process.waitFor();

            updateGitProject(id,gitProject.getName());

            gitProject.setState(GitProjectState.done.name());
            gitProjectRepository.save(gitProject);

            System.out.println("pull done");
            System.out.println(GitUtil.getShellResult(process.getInputStream()));
            System.out.println(GitUtil.getShellResult(process.getErrorStream()));

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
     * get packageInformation,repositoryPath and other extend relative
     * @param name
     * @return
     */
    public GitInfoResponse getInfoByName(String name){
        GitInfoResponse gitInfoResponse = new GitInfoResponse();
        List<GitProject> projects = gitProjectRepository.findByName(name);
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
        gitInfoResponse.setTemplateName(name);
        gitInfoResponse.set_package(gitProject.get_package());
        gitInfoResponse.setRelation(GitUtil.sortRelations((List<GitProject>) gitProjectRepository.findAll(),gitProject));

        return gitInfoResponse;
    }


}
