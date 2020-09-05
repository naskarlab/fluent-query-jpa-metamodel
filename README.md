# Fluent Query JPA Metamodel

Create Queries using only POJO classes of the JPA mappings.  

See more example in: [Fluent Query](https://github.com/naskarlab/fluent-query)

## Features

* Configuration over code: independence business code of the infrastructure code;
* Intrusive-less: zero or less changes for your code;
* Glue code: it’s only a small and simple classes set;
* Fluent Builder: code complete is your friend!


## Examples

```

private MetamodelConvention mc;
private EntityManager em;

@Before
public void setup() {
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("test");
    this.mc = new MetamodelConvention();
    this.mc.addAll(factory);
    this.em = factory.createEntityManager();
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
```

## Usage with Maven

```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
    <groupId>com.github.naskarlab</groupId>
    <artifactId>fluent-query-jpa-metamodel</artifactId>
    <version>0.0.1</version>
</dependency>
```

