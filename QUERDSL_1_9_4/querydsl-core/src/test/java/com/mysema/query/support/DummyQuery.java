/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import com.mysema.query.Query;

public class DummyQuery extends QueryBase<DummyQuery> implements Query<DummyQuery>{

    public DummyQuery() {
        super(new QueryMixin<DummyQuery>());
    }

}
