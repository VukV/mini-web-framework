# Mini Web Framework

Java project Advanced Web Programming course at Faculty of Computing.

The goal of the project was to learn reflection and annotations, and then make a mini web framework using those concepts. The framework supports dependency injection and URL path mapping to specified methods using annotations.

Framework annotations and functionality:
* ```@Controller``` - annotates a class which has HTTP methods
* ```@GET``` - annotates a method that is used for specific GET request
* ```@POST``` - annotates a method that is used for specific POST request
* ```@Path``` - annotates which path is being mapped to a method
* ```@Bean``` - annotates a class which will be injectable
* ```@Service``` - behaves like a singleton bean
* ```@Component``` - behaves like a bean which will have different instances
* ```@Qualifier``` - annotates implementation classes, in case of injecting interfaces
* ```@Autowired``` - annotates what to inject in a class
