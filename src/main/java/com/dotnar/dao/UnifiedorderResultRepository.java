package com.dotnar.dao;

import com.dotnar.bean.paymch.Unifiedorder;
import com.dotnar.bean.paymch.UnifiedorderResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Created by chovans on 15/7/17.
 */
@Component("UnifiedorderResultRepository")
public interface UnifiedorderResultRepository extends CrudRepository<UnifiedorderResult,String>{
}
