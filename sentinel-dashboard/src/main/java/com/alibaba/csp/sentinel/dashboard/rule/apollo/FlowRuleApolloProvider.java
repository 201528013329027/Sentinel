package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.apollo.config.ApolloResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author:zhaiyarong
 * @Date:2020/2/26 11:04
 */
@Component("flowRuleApolloProvider")
public class FlowRuleApolloProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {
    @Autowired
    private ApolloCommonService apolloCommonService;

    @Autowired
    private ApolloResourceConfig apolloResourceConfig;

    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
        return apolloCommonService.getRules(appName, apolloResourceConfig.getFlowDataIdSuffix(), FlowRuleEntity.class);
    }
}
