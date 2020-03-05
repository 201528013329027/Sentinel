package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.AuthorityRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.ParamFlowRuleEntity;
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
@Component("authorityRuleApolloProvider")
public class AuthorityRuleApolloProvider implements DynamicRuleProvider<List<AuthorityRuleEntity>> {
    @Autowired
    private ApolloCommonService apolloCommonService;

    @Autowired
    private ApolloResourceConfig apolloResourceConfig;

    @Override
    public List<AuthorityRuleEntity> getRules(String appName) throws Exception {
        return apolloCommonService.getRules(appName, apolloResourceConfig.getAuthorityDataIdSuffix(), AuthorityRuleEntity.class);
    }
}
