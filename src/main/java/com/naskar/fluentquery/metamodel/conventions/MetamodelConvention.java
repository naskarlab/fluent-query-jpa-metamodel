package com.naskar.fluentquery.metamodel.conventions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Table;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;

import com.naskar.fluentquery.impl.Convention;

public class MetamodelConvention implements Convention {
	
	// <className, tableName>
	private Map<String, String> clazzes;
	
	// <getterMethodName, columnName>
	private Map<String, Map<String, String>> methods;
	
	public MetamodelConvention() {
		this.clazzes = new HashMap<String, String>();
		this.methods = new HashMap<String, Map<String, String>>();
	}
	
	public void addAll(EntityManagerFactory factory) {
		EntityManager em = null;
		try {
			
			em = factory.createEntityManager();
			
			for(ManagedType<?> entity : em.getMetamodel().getManagedTypes()) {
				
				Class<?> clazz = entity.getJavaType();
				
				Table table = clazz.getAnnotation(Table.class);
			    String tableName = table == null ? clazz.getName() : table.name();
			    
			    clazzes.put(clazz.getName(), tableName);
			    
				Map<String, String> fields = new HashMap<String, String>();
				methods.put(clazz.getName(), fields);
			    
				for(Attribute<?, ?> attr : entity.getDeclaredAttributes()) {
					
					String attributeName = attr.getJavaMember().getName();
					
					String method = "get" + 
						attributeName.substring(0, 1).toUpperCase() + 
						attributeName.substring(1);
					
					String columnName = attributeName;
					
					try {
						Field field = clazz.getDeclaredField(attributeName);
						if(field != null) {
							Column column = field.getAnnotation(Column.class);	
							columnName = column.name();
						}
					} catch(Exception e) {
						columnName = attributeName;
					}
					
					fields.put(method, columnName);
				}
				
			}
			
		} finally {
			if(em != null) {
				em.close();
			}
		}
		
	}
	
	@Override
	public <T> String getNameFromClass(Class<T> clazz) {
		return clazzes.get(clazz.getName());
	}
	
	@Override
	public String getNameFromMethod(List<Method> methods) {
		if(methods.size() > 1) {
			throw new UnsupportedOperationException(methods.toString());
		}
		
		String name = null;
		
		if(!methods.isEmpty()) {
			name = getNameFromMethod(methods.get(0));
		}
		
		return name;
	}
	
	@Override
	public String getNameFromMethod(Method m) {
		String name = null;
		
		Map<String, String> fields = methods.get(m.getDeclaringClass().getName());
		if(fields != null) {
			name = fields.get(m.getName());
		}
		
		return name;
	}

}
