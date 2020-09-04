package com.naskar.fluentquery.metamodel.mappings;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.naskar.fluentquery.QueryBuilder;
import com.naskar.fluentquery.converters.NativeSQL;
import com.naskar.fluentquery.converters.NativeSQLResult;
import com.naskar.fluentquery.metamodel.conventions.MetamodelConvention;
import com.naskar.fluentquery.metamodel.mappings.domain.Customer;

@RunWith(Parameterized.class)
public class MetamodelMappingTest {
	
	private MetamodelConvention mc;
	private EntityManager em;
	
	@Parameters
    public static List<String> data() {
        return Arrays.asList("test-openjpa", "test-eclipselink");
    }
	
	private String unitName;
	
	public MetamodelMappingTest(String unitName) {
		this.unitName = unitName;
	}
	
	@Before
	public void setup() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(unitName);
	    this.em = factory.createEntityManager();
	    this.mc = new MetamodelConvention();
	    this.mc.addAll(em);
	}
	
	@After
	public void tearDown() {
		if(em != null) {
			em.close();
		}
	}
	
	@Test
	public void testSelect() {
		String expected = "select e0.* from TB_CUSTOMER e0";
		
		String actual = new QueryBuilder()
			.from(Customer.class)
			.to(new NativeSQL(mc))
			.sql()
			;
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testMapping() {	
		NativeSQLResult result = new QueryBuilder()
			.from(Customer.class)
			.where(i -> i.getId()).eq(1L)
				.and(i -> i.getName()).like("r%")
			.select(i -> i.getId())
			.select(i -> i.getName())
			.to(new NativeSQL(mc))
			;
		
		Query q = em.createNativeQuery(result.sqlValues());
		for(int i = 0; i < result.values().size(); i++) {
			q.setParameter(i + 1, result.values().get(i));
		}
		
		Assert.assertTrue(q.getResultList().isEmpty());
	}
	
	/*
	// TODO: references
	@Test
	public void testTwoEntities() {
		String expected = 
			"select e0.DS_NAME, e1.VL_BALANCE from TB_CUSTOMER e0, TB_ACCOUNT e1" +
			" where e0.DS_NAME like :p0" +
			" and e1.VL_BALANCE > :p1" +
			" and e1.CD_CUSTOMER = e0.CD_CUSTOMER" +
			" and e1.NU_REGION_CODE = e0.NU_REGION_CODE" +
			" and e1.VL_BALANCE < e0.VL_MIN_BALANCE"
			;
		
		NativeSQLResult result = new QueryBuilder()
			.from(Customer.class)
				.where(i -> i.getName()).like("r%")
				.select(i -> i.getName())
			.from(Account.class, (query, parent) -> {
				
				query
					.where(i -> i.getBalance()).gt(0.0)
						.and(i -> i.getCustomer().getId()).eq(parent.getId())
						.and(i -> i.getCustomer().getRegionCode()).eq(parent.getRegionCode())
						.and(i -> i.getBalance()).lt(parent.getMinBalance())
					.select(i -> i.getBalance());
				
			})
			.to(new NativeSQL(mc))
			;
		
		String actual = result.sql();
		
		Assert.assertEquals(expected, actual);
		Assert.assertEquals(result.params().get(":p0"), "r%");
		Assert.assertEquals(result.params().get(":p1"), 0.0);
		
	}
	*/


}
