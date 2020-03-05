package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.dashboard.rule.apollo.config.ApolloResourceConfig;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author:zhaiyarong
 * @Date:2020/2/26 14:01
 */
@Component("flowRuleApolloPublisher")
public class FlowRuleApolloPublisher implements DynamicRulePublisher<List<FlowRuleEntity>> {
    @Autowired
    private ApolloCommonService apolloCommonService;

    @Autowired
    private ApolloResourceConfig apolloResourceConfig;

    @Override
    public void publish(String app, List<FlowRuleEntity> rules) throws Exception {
        apolloCommonService.publishRules(app, apolloResourceConfig.getFlowDataIdSuffix(), JSON.toJSONString(rules));
    }
}
