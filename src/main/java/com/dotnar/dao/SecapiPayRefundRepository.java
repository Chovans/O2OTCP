package com.dotnar.dao;

import com.dotnar.bean.paymch.SecapiPayRefund;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Created by chovans on 15/7/18.
 */
@Component("SecapiPayRefundRepository")
public interface SecapiPayRefundRepository extends CrudRepository<SecapiPayRefund,String> {
}
