package com.dotnar.dao;

import com.dotnar.bean.paymch.Unifiedorder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Created by chovans on 15/7/17.
 */
@Component("UnifiedorderRepository")
public interface UnifiedorderRepository extends CrudRepository<Unifiedorder,String>{
}
