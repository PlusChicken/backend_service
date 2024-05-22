package com.dji.sample.component.oss.service.impl;

import com.dji.sample.component.oss.model.OssConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sean
 * @version 1.1
 * @date 2022/6/20
 */
@Component
@Aspect
@Slf4j
public class OssAspectHandler {

    @Autowired
    private OssServiceContext ossServiceContext;

    @Before("execution(public * com.dji.sample.component.oss.service.impl.OssServiceContext.*(..))")
    public void before() {
        log.info("OssServiceContext::OssServiceContext,provider={},enable={},endpoint={}," +
                "accessKey={},secretKey={},region={},roleArn={}",OssConfiguration.provider,
                OssConfiguration.enable,OssConfiguration.endpoint,OssConfiguration.accessKey,OssConfiguration.secretKey,
                OssConfiguration.region,OssConfiguration.roleArn);

        if (!OssConfiguration.enable) {
            throw new IllegalArgumentException("Please enable OssConfiguration.");
        }
        if (this.ossServiceContext.getOssService() == null) {
            throw new IllegalArgumentException("Please check the OssConfiguration configuration.");
        }
        this.ossServiceContext.createClient();
    }
}
