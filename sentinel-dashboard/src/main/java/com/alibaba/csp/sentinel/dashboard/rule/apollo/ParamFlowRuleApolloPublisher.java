package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.apollo.config.ApolloResourceConfig;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author:zhaiyarong
 * @Date:2020/2/26 14:01
 */
@Component("paramRuleApolloPublisher")
@Primary
public class ParamFlowRuleApolloPublisher implements DynamicRulePublisher<List<ParamFlowRuleEntity>> {
    @Autowired
    private ApolloCommonService apolloCommonService;

    @Autowired
    private ApolloResourceConfig apolloResourceConfig;

    @Override
    public void publish(String app, List<ParamFlowRuleEntity> rules) throws Exception {
        apolloCommonService.publishRules(app, apolloResourceConfig.getParamFlowDataIdSuffix(), JSON.toJSONString(rules));
    }
}
