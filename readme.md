Hay veces en que las campos sobre los que restringir una consulta pueden variar en tiempo de ejecución. En ese caso si queremos usar JPA no podemos usar una sentencia **@Query** definida en nuestro repositorio pues no sabemos los campos sobre los que se aplicaran condiciones en la consulta. Además es bastante común que el usuario pueda elegir  el criterio de búsqueda sobre un campo, deseando que el valor de un campo deba ser igual, mayor o menor, respecto al valor introducido .

En **Spring Boot** podemos dar una  solución a este problema usando un la clase **CriteriaBuilder** de nuestro **EntityManager** . En esta entrada os mostrare como hacerlo fácilmente.

Para ello he creado un proyecto que he dejado en https://github.com/chuchip/CustomJpaQuery  

En este programa podremos hacer una petición REST a la URL http://localhost:8080/get donde podremos pasar los siguientes parámetros, todos ellos opcionales:

- Identificador del cliente: **idCustomer**
- Nombre del Cliente: **nameCustomer**
- Dirección del cliente: **addressCustomer**
- Fecha creación del registro: **createdDate**. La fecha se debera mandar en formato español, es decir: "dd-MM-yyyy". Por ejemplo: 31-01-2018.
- Condición del campo anterior: **dateCondition**. Tiene que ser una de estas tres cadenas de texto:  **"greater","less", "equal"** En caso de no poner ninguna condición o poner una condición no valida se usara **greater** 
	

URLs de búsqueda podrían ser:

http://localhost:8080/get?createdDate=21-01-2018&dateCondition=equal

http://localhost:8080/get?createdDate=21-01-2018&dateCondition=greater

http://localhost:8080/get?nameCustomer=Smith&createdDate=21-01-2018

El programa usa una base de datos **H2** para crear una tabla simple de clientes (**customers**) con los campos: **id**,**name**,**address**,**email** y **created_date**. Llena después la tabla con los datos que podemos ver en el fichero **data.sql**

Para realizar nuestra QUERY personalizada, en primer lugar, se crea un interface en **CustomersRepository**  que extiende de **JpaRepository** . En este interface definimos la función **getData** como se ve en el siguiente código:

```java
public interface CustomersRepository extends JpaRepository<CustomersEntity, Integer> {
	
	public List<CustomersEntity> getData(HashMap<String, Object> conditions);
}

```

La función **getData** recibirá un **HashMap**  donde iremos poniendo las condiciones de búsqueda. Así si queremos buscar los clientes cuyo código de cliente sea igual a  1, añadiremos una  la llave 'id' y el valor '1"

````java
HashMap<String,Object> hm= new HashMap<>();
hm.put("id",1);
````

Si deseamos que el nombre sea como 'Smith', añadiríamos este elemento al **HashMap**:

```java
hm.put("name","Smith");
```

 Y así sucesivamente con todos los campos o condiciones deseadas.

Una vez definido nuestro repositorio  creamos una clase a la que obligatoriamente deberemos llamar **CustomersRepositoryImpl** es decir se debe llamar igual que nuestro interface del repositorio pero añadiendo la terminación **impl** (de implementación).  En esta clase  deberemos tener una función igual que la definida en el repositorio pues es la función que **Spring Boot**  ejecutara cuando llamemos a la función definida en el interface.

Este es el código de la clase que permitirá personalizar nuestra query:

```
public class CustomersRepositoryImpl{
@PersistenceContext
private EntityManager entityManager;
	
public List<CustomersEntity> getData(HashMap<String, Object> conditions)
{
	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	CriteriaQuery<CustomersEntity> query= cb.createQuery(CustomersEntity.class);
	Root<CustomersEntity> root = query.from(CustomersEntity.class);
		
	List<Predicate> predicates = new ArrayList<>();
	conditions.forEach((field,value) ->
	{
		switch (field)
		{
			case "id":
				predicates.add(cb.equal (root.get(field), (Integer)value));
				break;
			case "name":
				predicates.add(cb.like(root.get(field),"%"+(String)value+"%"));
				break;
			case "address":
				predicates.add(cb.like(root.get(field),"%"+(String)value+"%"));
				break;
			case "created":
				String dateCondition=(String) conditions.get("dateCondition");					
				switch (dateCondition)
				{
					case GREATER_THAN:
						predicates.add(cb.greaterThan(root.<Date>get(field),(Date)value));
						break;
					case LESS_THAN:
						predicates.add(cb.lessThan(root.<Date>get(field),(Date)value));
						break;
					case EQUAL:
						predicates.add(cb.equal(root.<Date>get(field),(Date)value));
                        break;
				}
				break;
			}
		});
		query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
		return entityManager.createQuery(query).getResultList(); 		
	}
}
```



Como se ve, lo primero es inyectar una referencia al objeto **EntityManager** con la etiqueta **@PersistenceContext**. En la función sobre el **EntityManager**  crearemos un objeto **CriteriaBuilder** y sobre este objeto creamos un **CriteriaQuery** donde iremos poniendo las diferentes condiciones de nuestra **Query**. Para poder buscar las columnas sobre las que queremos realizar la consulta necesitaremos un objeto **Root** , que crearemos a partir del anterior objeto **CriteriaQuery** 

Ahora creamos una lista de objeto **Predicate** . En esa lista irán todos los **Predicate** que no son sino las condiciones de nuestra query.

Utilizando Lambdas y Streams para hacer el código mas limpio y sencillo, vamos recorriendo el **HashMap** y añadiendo a la lista de **Predicates**  las condiciones definidas.

Partiendo del objeto   **CriteriaQuery**  se ira llamando a la función deseada según el criterio a aplicar. De esta manera, si queremos establecer como condición que un campo sea igual a un valor llamaremos a la función **equal**(),  pasando como primer parámetro la **Expresion** que hace referencia al campo de la entidad, y después el valor deseado. El objeto  **Expresion** se creara simplemente cogiendo del objeto **Root** anteriormente definido, el nombre de la columna sobre el que se establecerá la condición.

Si deseamos añadir una condición donde un campo sea *como* a un texto introducido se llamara a la función **like**(). En caso de que deseemos que el campo tenga un valor superior al introducido se usara	**greaterThan**() y así sucesivamente.

Si el campo es de tipo **Date**, es necesario especificar el tipo de dato del campo como se muestra en el código `root.<Date>get(field)`, pues de otra manera no sabrá parsear correctamente la fecha.

Resaltar que el nombre del campo es el definido en nuestra entity que lógicamente no tiene porque ser el de la columna en la base de datos. Por ejemplo, el campo de fecha en la entity del proyecto de ejemplo, esta creada con las siguientes sentencias:

```
@Column(name="created_date")
@Temporal(TemporalType.DATE)
Date created;
```

De tal manera que en la base de datos la columna se llamara `created_date` pero todas las referencias a la entidad se harán a través del nombre `created` y es por ello que cuando busquemos el nombre del campo deberemos en **Root** deberemos buscar el campo `created` y no el campo `created_date`que no lo encontraría y nos daría error.

Una vez tenemos las condiciones de la consulta establecidas no tenemos más que preparar la consulta llamando a la función **select** a la que primero le indicaremos el **Root** con la entidad a consultar y después, las condiciones establecidas en el ArrayList de **Predicate**, el cual deberemos  convertir previamente a un simple Array. Esto lo haremos con la sentencia: '`query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));`

Ahora ejecutaremos la select y recogeremos los resultados en un objeto **List** con el comando `entityManager.createQuery(query).getResultList()`

Como siempre no dudéis en hacer cualquier consulta  o mandar feedbacks. ¡¡ Hasta otra!!