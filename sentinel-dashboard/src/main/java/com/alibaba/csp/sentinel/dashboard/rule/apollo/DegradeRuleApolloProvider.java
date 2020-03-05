package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.apollo.config.ApolloResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author:zhaiyarong
 * @Date:2020/3/4 18:46
 */
@Component("degradeRuleApolloProvider")
@Primary
public class DegradeRuleApolloProvider implements DynamicRuleProvider<List<DegradeRuleEntity>> {
    @Autowired
    private ApolloCommonService apolloCommonService;

    @Autowired
    private ApolloResourceConfig apolloResourceConfig;

    @Override
    public List<DegradeRuleEntity> getRules(String appName) throws Exception {
        return apolloCommonService.getRules(appName, apolloResourceConfig.getDegradeDataIdSuffix(), DegradeRuleEntity.class);
    }
}
