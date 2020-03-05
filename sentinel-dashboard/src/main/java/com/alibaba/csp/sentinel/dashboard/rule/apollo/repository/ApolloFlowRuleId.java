package com.alibaba.csp.sentinel.dashboard.rule.apollo.repository;

import com.alibaba.csp.sentinel.dashboard.repository.rule.InMemFlowRuleStore;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @description:
 * @author:zhaiyarong
 * @Date:2020/3/4 18:59
 */
@Component
@Primary
public class ApolloFlowRuleId extends InMemFlowRuleStore {
    @Override
    protected long nextId() {
        return System.currentTimeMillis();
    }
}
