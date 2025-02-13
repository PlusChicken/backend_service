package com.dji.sample.storage.service.impl;

import com.alibaba.fastjson.JSON;
import com.dji.sample.component.oss.model.OssConfiguration;
import com.dji.sample.component.oss.service.impl.OssServiceContext;
import com.dji.sample.storage.service.IStorageService;
import com.dji.sdk.cloudapi.media.StorageConfigGet;
import com.dji.sdk.cloudapi.media.api.AbstractMediaService;
import com.dji.sdk.cloudapi.storage.StsCredentialsResponse;
import com.dji.sdk.mqtt.MqttReply;
import com.dji.sdk.mqtt.requests.TopicRequestsRequest;
import com.dji.sdk.mqtt.requests.TopicRequestsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

/**
 * @author sean
 * @version 0.3
 * @date 2022/3/9
 */
@Service
@Slf4j
public class StorageServiceImpl extends AbstractMediaService implements IStorageService {

    @Autowired
    private OssServiceContext ossService;

    @Override
    public StsCredentialsResponse getSTSCredentials() {

        log.info("StorageServiceImpl getSTSCredentials start..., " +
                        "endPoint={},bucket={},provider={},objectDirPrefix={},region={}",
                OssConfiguration.endpoint,OssConfiguration.bucket,
                OssConfiguration.provider,OssConfiguration.objectDirPrefix,
                OssConfiguration.region
                );
        return new StsCredentialsResponse()
                .setEndpoint(OssConfiguration.endpoint)
                .setBucket(OssConfiguration.bucket)
                .setCredentials(ossService.getCredentials()) // 此处需要验证
                .setProvider(OssConfiguration.provider)
                .setObjectKeyPrefix(OssConfiguration.objectDirPrefix)
                .setRegion(OssConfiguration.region);
    }

    @Override
    public TopicRequestsResponse<MqttReply<StsCredentialsResponse>> storageConfigGet(TopicRequestsRequest<StorageConfigGet> response, MessageHeaders headers) {

        log.info("StorageServiceImpl::storageConfigGet, StorageConfigGet={}",response.getData().toString());
        return new TopicRequestsResponse<MqttReply<StsCredentialsResponse>>().setData(MqttReply.success(getSTSCredentials()));
    }
}
