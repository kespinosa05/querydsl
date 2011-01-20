package com.mysema.query.jdoql;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mysema.query.jdoql.testdomain.Product;
import com.mysema.query.jdoql.testdomain.QProduct;
import com.mysema.query.jdoql.testdomain.QStore;

public class FetchPlanTest extends AbstractJDOTest{
    
    private JDOQLQuery query;
    
    @After
    public void tearDown() {
        if (query != null){
            try {
                query.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        super.tearDown();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void listProducts() throws Exception{
        QProduct product = QProduct.product;
        query = query();
        query.from(product)
             .where(product.name.startsWith("A"))
             .addFetchGroup("myfetchgroup1")
             .addFetchGroup("myfetchgroup2")
             .setMaxFetchDepth(2)
             .list(product);
        query.close();
        
        Field queriesField = AbstractJDOQLQuery.class.getDeclaredField("queries");
        queriesField.setAccessible(true);
        List<Query> queries = (List<Query>)queriesField.get(query);
        Query jdoQuery = queries.get(0);
        assertEquals(new HashSet<String>(Arrays.asList("myfetchgroup1","myfetchgroup2")), jdoQuery.getFetchPlan().getGroups());
        assertEquals(2, jdoQuery.getFetchPlan().getMaxFetchDepth());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void listStores() throws Exception{
        QStore store = QStore.store;
        query = query(); 
        query.from(store)
            .addFetchGroup("products")
            .list(store);
        
        Field queriesField = AbstractJDOQLQuery.class.getDeclaredField("queries");
        queriesField.setAccessible(true);
        List<Query> queries = (List<Query>)queriesField.get(query);
        Query jdoQuery = queries.get(0);
        assertEquals(new HashSet<String>(Arrays.asList("products")), jdoQuery.getFetchPlan().getGroups());
        assertEquals(1, jdoQuery.getFetchPlan().getMaxFetchDepth());
    }
    
    @BeforeClass
    public static void doPersist() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < 10; i++) {
                pm.makePersistent(new Product("C" + i, "F", 200.00, 2));
                pm.makePersistent(new Product("B" + i, "E", 400.00, 4));
                pm.makePersistent(new Product("A" + i, "D", 600.00, 6));
            }
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
        System.out.println("");

    }

}
