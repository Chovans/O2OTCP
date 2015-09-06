package com.dotnar.dao;

import com.dotnar.bean.git.GitProject;
import com.dotnar.bean.sns.Oauth2;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chovans on 15/7/21.
 */
@Component("GitProjectRepository")
public interface GitProjectRepository extends CrudRepository<GitProject,String>{
    public List<GitProject> findByUrl(String url);
    public List<GitProject> findByName(String name);
    public List<GitProject> findByUserNameAndName(String userName,String name);
}
