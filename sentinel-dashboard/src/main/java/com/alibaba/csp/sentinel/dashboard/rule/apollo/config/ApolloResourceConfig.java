package com.alibaba.csp.sentinel.dashboard.rule.apollo.config;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import com.ctrip.framework.apollo.openapi.client.ApolloOpenApiClient;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
 * @description:
 * @author:zhaiyarong
 * @Date:2020/2/26 11:09
 */
@Configuration
@Data
@PropertySource("classpath:application.properties")
public class ApolloResourceConfig {
    @Value("${env: DEV}")
    private String env;

    @Value("${apollo.app.id:sentinel-apollo}")
    private String appId;

    @Value("${apollo.cluster.name: default}")
    private String clusterName;

    @Value("${apollo.namespace.name:application}")
    private String namesapceName;

    @Value("${apollo.modify.user}")
    private String modifyUser;

    @Value("${apollo.modify.comment:modify by sentinel-dashboard}")
    private String modifyCommnet;

    @Value("${apollo.release.comment:release by sentinel-dashboard}")
    private String releaseCommnet;

    @Value("${apollo.release.user}")
    private String releaseUser;

    @Value("${apollo.portal.url}")
    private String apolloPortalUrl;

    @Value("${apollo.application.token}")
    private String apolloApplicationToken;

    /**
     * 流控规则前缀标示
     */
    @Value("${flow.key.suffix:-flow}")
    String flowDataIdSuffix;

    /**
     * 熔断降级规则前缀标示
     */
    @Value("${degrade.key.suffix:-degrade}")
    String degradeDataIdSuffix;

    /**
     * 热点规则前缀标示
     */
    @Value("${paramFlow.key.suffix:-paramFlow}")
    String paramFlowDataIdSuffix;

    /**
     * 系统规则前缀标示
     */
    @Value("${system.key.suffix:-system}")
    String systemDataIdSuffix;

    /**
     * 授权规则前缀标示
     */
    @Value("${authority.key.suffix:-authority}")
    String authorityDataIdSuffix;

    @Value("${namespace.name.suffix:-sentinel-rule}")
    String namespaceSuffix;

    @Bean
    public Converter<List<FlowRuleEntity>, String> flowRuleEntityEncoder(){
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder(){
        return s -> JSON.parseArray(s, FlowRuleEntity.class);
    }

    @Bean
    public ApolloOpenApiClient apolloOpenApiClient(){
        ApolloOpenApiClient client = ApolloOpenApiClient.newBuilder().withPortalUrl(apolloPortalUrl)
                .withToken(apolloApplicationToken)
                .build();
        return client;
    }
}
