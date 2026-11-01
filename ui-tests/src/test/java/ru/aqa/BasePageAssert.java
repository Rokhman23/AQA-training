package ru.aqa;

import org.assertj.core.api.AbstractAssert;

/** Shared AssertJ base for page-specific assertion objects. */
public abstract class BasePageAssert<SELF extends BasePageAssert<SELF, PAGE>, PAGE extends BasePage>
        extends AbstractAssert<SELF, PAGE> {
    protected BasePageAssert(PAGE actual, Class<SELF> selfType) {
        super(actual, selfType);
    }
}
