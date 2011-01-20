/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query._hsqldb;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;

import com.mysema.query.Connections;
import com.mysema.query.Target;
import com.mysema.query.UpdateBaseTest;
import com.mysema.query.sql.HSQLDBTemplates;
import com.mysema.testutil.Label;

@Label(Target.HSQLDB)
public class UpdateHsqldbTest extends UpdateBaseTest{

    @BeforeClass
    public static void setUpClass() throws Exception {
        Connections.initHSQL();
    }

    @Before
    public void setUp() throws SQLException {
        templates = new HSQLDBTemplates().newLineToSingleSpace();
        super.setUp();
    }

}
