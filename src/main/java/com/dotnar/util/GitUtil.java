package com.dotnar.util;

import com.dotnar.bean.git.GitProject;
import org.springframework.util.*;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chovans on 15/8/11.
 */
public class GitUtil {

    public static String getShellResult(InputStream is) {

        if (org.springframework.util.StringUtils.isEmpty(is)) {
            return null;
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String temp = "";
            StringBuffer result = new StringBuffer();
            while ((temp = br.readLine()) != null) {
                result.append(temp);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 整理依赖关系
     * @param gitProjects
     * @return
     */
    public static String[] sortRelations(List<GitProject> gitProjects,GitProject gitProject){
        List<String> sorted = new ArrayList<>();
        sorted.add(gitProject.getUserName() + File.separator + gitProject.getName());
        findByName(gitProjects,sorted,gitProject);
        return sorted.toArray(new String[sorted.size()]);
    }

    public static void findByName(List<GitProject> gitProjects,List<String> sortedGitProjects,GitProject gp){
        for(GitProject gitProject:gitProjects){

            if(StringUtils.isEmpty(gp.getParentTemplateName()))
                return;

            String[] parentInfos = gp.getParentTemplateName().split("/");

            if(parentInfos.length > 1 && parentInfos[0].equals(gitProject.getUserName()) && parentInfos[1].equals(gitProject.getName()) ){

                if(sortedGitProjects.size() == 1)
                    sortedGitProjects.add(gp.getParentTemplateName());
                if(!org.springframework.util.StringUtils.isEmpty(gitProject.getParentTemplateName()))
                    sortedGitProjects.add(gitProject.getParentTemplateName());
                findByName(gitProjects,sortedGitProjects,gitProject);

            }
        }
    }

    /**
     * 返回加上路径后
     * @param basePath
     * @param relations
     * @return
     */
    public static String[] sortRelationsPath(String basePath,String[] relations){
        List<String> sorted = new ArrayList<>();
        for(String relation:relations){
            sorted.add(basePath+relation);
        }
        return sorted.toArray(new String[sorted.size()]);
    }

}
