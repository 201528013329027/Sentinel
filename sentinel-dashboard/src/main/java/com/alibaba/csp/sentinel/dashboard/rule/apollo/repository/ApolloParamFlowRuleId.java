package com.alibaba.csp.sentinel.dashboard.rule.apollo.repository;

import com.alibaba.csp.sentinel.dashboard.repository.rule.InMemParamFlowRuleStore;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author:zhaiyarong
 * @Date:2020/3/4 19:01
 */
@Component
@Primary
public class ApolloParamFlowRuleId extends InMemParamFlowRuleStore {
    @Override
    protected long nextId() {
        return System.currentTimeMillis();
    }
}
