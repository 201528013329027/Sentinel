package com.alibaba.csp.sentinel.dashboard.rule.apollo.repository;

import com.alibaba.csp.sentinel.dashboard.repository.rule.InMemDegradeRuleStore;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author:zhaiyarong
 * @Date:2020/3/4 18:58
 */
@Component
@Primary
public class ApolloDegradeRuleId extends InMemDegradeRuleStore {
    @Override
    protected long nextId() {
        return System.currentTimeMillis();
    }
}
