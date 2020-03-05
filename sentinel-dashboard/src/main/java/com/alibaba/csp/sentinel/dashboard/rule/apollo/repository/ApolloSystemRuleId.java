package com.alibaba.csp.sentinel.dashboard.rule.apollo.repository;

import com.alibaba.csp.sentinel.dashboard.repository.rule.InMemParamFlowRuleStore;
import com.alibaba.csp.sentinel.dashboard.repository.rule.InMemSystemRuleStore;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author:zhaiyarong
 * @Date:2020/3/4 19:01
 */
@Component
@Primary
public class ApolloSystemRuleId extends InMemSystemRuleStore {
    @Override
    protected long nextId() {
        return System.currentTimeMillis();
    }
}
