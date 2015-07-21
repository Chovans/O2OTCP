package com.dotnar.dao;

import com.dotnar.bean.sns.Oauth2;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Created by chovans on 15/7/21.
 */
@Component("OauthRepository")
public interface OauthRepository extends CrudRepository<Oauth2,String>{
}
