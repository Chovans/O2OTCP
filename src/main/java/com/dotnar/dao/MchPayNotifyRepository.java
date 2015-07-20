package com.dotnar.dao;

import com.dotnar.bean.paymch.MchPayNotify;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Created by chovans on 15/7/19.
 */
@Component("MchPayNotifyRepository")
public interface MchPayNotifyRepository extends CrudRepository<MchPayNotify,String>{
}
