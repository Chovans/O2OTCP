package com.dotnar.dao;

import com.dotnar.bean.paymch.UnifiedorderKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

/**
 * Created by chovans on 15/7/17.
 */
@Component("UnifiedorderKeyRepository")
public interface UnifiedorderKeyRepository extends CrudRepository<UnifiedorderKey,String>{
    public UnifiedorderKey findByAppIdAndMchIdAndOutTradeNoAndOpenId(String appId,String mchId,String outTradeNo,String openId);
    @Query(value = "from UnifiedorderKey where outTradeNo = :outTradeNo")
    public UnifiedorderKey findByOutTradeNo(@Param("outTradeNo")String outTradeNo);
}
