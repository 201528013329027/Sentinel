package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.rule.apollo.config.ApolloResourceConfig;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import com.ctrip.framework.apollo.openapi.client.exception.ApolloOpenApiException;
import com.ctrip.framework.apollo.openapi.dto.NamespaceReleaseDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenAppNamespaceDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenItemDTO;
import com.ctrip.framework.apollo.openapi.dto.OpenNamespaceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author:zhaiyarong
 * @Date:2020/2/26 11:07
 */
@Service
@Slf4j
public class ApolloCommonService {
    private static final int NO_FOUND_ERROR_CODE = 404;
    @Autowired
    private ApolloOpenApiClient apolloOpenApiClient;

    @Autowired
    private ApolloResourceConfig apolloResourceConfig;
    /**
     * @description: 获得规则类型
     * @param: [appName, flowDataIdSuffix, ruleClass]
     * @return: java.util.List<T>
     * @author: zhaiyarong
     * @date: 2020/2/26 11:21
     * @throws
    */
    public <T> List<T> getRules(String appName, String flowDataIdSuffix, Class<T> ruleClass) throws Exception{
        String flowDataId = appName + flowDataIdSuffix;
        String namespace = appName + apolloResourceConfig.getNamespaceSuffix();
        OpenNamespaceDTO openNamespaceDTO = new OpenNamespaceDTO();
        try{
            openNamespaceDTO = apolloOpenApiClient.getNamespace(apolloResourceConfig.getAppId(),
                    apolloResourceConfig.getEnv(), apolloResourceConfig.getClusterName(), namespace);
        }catch (Exception e) {
            if (e.getCause() instanceof ApolloOpenApiException) {
                ApolloOpenApiException apolloOpenApiException = (ApolloOpenApiException) e.getCause();
                if (Integer.valueOf(NO_FOUND_ERROR_CODE).equals(apolloOpenApiException.getStatus())) {
                    log.error("未找到该namespace", e);
                    return new ArrayList<>();
                }
            }
            throw e;
        }

        String rules = openNamespaceDTO.getItems().stream().filter(p -> p.getKey().equals(flowDataId)).map(OpenItemDTO::getValue).findFirst().orElse("");
        if(StringUtil.isBlank(rules)){
            return new ArrayList<>();
        }

        List<T> flow = JSON.parseArray(rules, ruleClass);
        if(Objects.isNull(flow)){
            return new ArrayList<>();
        }
        return flow;
    }

    public void publishRules(String appName, String flowDataIdSuffix, String rules) throws Exception {
        String flowDataId = appName + flowDataIdSuffix;
        String namespaceName = appName + apolloResourceConfig.getNamespaceSuffix();
        String appId = apolloResourceConfig.getAppId();
        String clusterName = apolloResourceConfig.getClusterName();
        String env = apolloResourceConfig.getEnv();
        AssertUtil.notEmpty(appName, "app name cannot be empty");
        if(StringUtil.isBlank(rules)){
            return;
        }
        OpenItemDTO openItemDTO = new OpenItemDTO();
        openItemDTO.setKey(flowDataId);
        openItemDTO.setValue(rules);
        openItemDTO.setComment(apolloResourceConfig.getModifyCommnet());
        openItemDTO.setDataChangeCreatedBy(apolloResourceConfig.getModifyUser());
        try{
            apolloOpenApiClient.createOrUpdateItem(appId, env,
                    clusterName, namespaceName, openItemDTO);
        }catch (Exception e){
            if(e.getCause() instanceof  ApolloOpenApiException){
                ApolloOpenApiException apolloOpenApiException = (ApolloOpenApiException) e.getCause();
                if(Integer.valueOf(NO_FOUND_ERROR_CODE).equals(apolloOpenApiException.getStatus())){
                    OpenAppNamespaceDTO openNamespaceDTO = new OpenAppNamespaceDTO();
                    openNamespaceDTO.setAppId(appId);
                    openNamespaceDTO.setAppendNamespacePrefix(false);
                    openNamespaceDTO.setComment(apolloResourceConfig.getModifyCommnet());
                    openNamespaceDTO.setName(namespaceName);
                    openNamespaceDTO.setPublic(true);
                    openNamespaceDTO.setDataChangeCreatedBy(apolloResourceConfig.getModifyUser());
                    openNamespaceDTO.setDataChangeLastModifiedBy(apolloResourceConfig.getModifyUser());
                    openNamespaceDTO.setDataChangeCreatedTime(new Date());
                    openNamespaceDTO.setDataChangeLastModifiedTime(new Date());
                    OpenAppNamespaceDTO openAppNamespaceDTO = apolloOpenApiClient.createAppNamespace(openNamespaceDTO);
                    apolloOpenApiClient.createItem(appId, env, clusterName, namespaceName, openItemDTO);
                    log.info("初始化应用配置 -> {}", flowDataId);
                }else {
                    log.error("修改规则失败", e);
                    throw new Exception("修改配置失败");
                }
            }
            log.error("修改规则失败", e);
            throw new Exception("修改配置失败");
        }

        //发布配置
        NamespaceReleaseDTO namespaceReleaseDTO = new NamespaceReleaseDTO();
        namespaceReleaseDTO.setEmergencyPublish(true);
        namespaceReleaseDTO.setReleaseComment(apolloResourceConfig.getReleaseCommnet());
        namespaceReleaseDTO.setReleasedBy(apolloResourceConfig.getReleaseUser());
        namespaceReleaseDTO.setReleaseTitle(apolloResourceConfig.getReleaseCommnet());
        try{
            apolloOpenApiClient.publishNamespace(appId, env, clusterName, namespaceName, namespaceReleaseDTO);
        }catch (Exception e){
            log.error("修改配置文件发布失败", e);
            throw new Exception("修改配置文件发布失败");
        }

    }

    public void deleteRules(String ruleKey, String operator) throws Exception{
        String appId = apolloResourceConfig.getAppId();
        String env = apolloResourceConfig.getEnv();
        String clusterName = apolloResourceConfig.getClusterName();
        String namesapceName = apolloResourceConfig.getNamesapceName();
        try {
            apolloOpenApiClient.removeItem(appId, env, clusterName, namesapceName, ruleKey, operator);
        }catch (Exception e){
            log.error("刪除配置失败 -> {}", ruleKey);
            throw new Exception("删除配置文件失败");
        }

        //发布配置
        NamespaceReleaseDTO namespaceReleaseDTO = new NamespaceReleaseDTO();
        namespaceReleaseDTO.setEmergencyPublish(true);
        namespaceReleaseDTO.setReleaseComment(apolloResourceConfig.getReleaseCommnet());
        namespaceReleaseDTO.setReleasedBy(apolloResourceConfig.getReleaseUser());
        namespaceReleaseDTO.setReleaseTitle(apolloResourceConfig.getReleaseCommnet());
        try{
            apolloOpenApiClient.publishNamespace(appId, env, clusterName, namesapceName, namespaceReleaseDTO);
        }catch (Exception e){
            log.error("删除配置文件，发布失败", e);
            throw new Exception("删除配置文件发布失败");
        }

    }
}
