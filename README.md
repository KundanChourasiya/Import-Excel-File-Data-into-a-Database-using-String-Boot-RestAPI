# Import-Excel-File-Data-into-a-Database-using-String-Boot-RestAPI

> [!NOTE]
> ### In this Api we import excel file data into a database using spring boot RestApi.
> 1. Postman for testing endpoint
> 2. For Database we will use Mysql
> 3. Store excel data into database using Apcahe POI.

## Tech Stack
- Java-17
- Spring Boot-3
- Spring Data JPA
- lombok
- MySQL
- Apache POI API 
- Postman

## Modules
* Import Excel Data into mysql Database Model

## Installation & Run
Before running the API server, you should update the database config inside the application.properties file.
Update the port number, username and password as per your local database config and storage file path configuration.
    
```
spring.datasource.url=jdbc:mysql://localhost:3306/mydb;
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root
```
## API Root Endpoint

```
https://localhost:8080/
user this data for checking purpose.
```
## Step To Be Followed
> 1. Create Rest Api will return to FileData Details.
>    
>    **Project Documentation**
>    - **Entity** - Product (class)
>    - **Repository** - ProductRepository (interface)
>    - **Service** - ImportService (class)
>    - **Controller** - ImportController (Class)
>
> 2. Add Apache POI API Dependency in pom.xml file.    
> 3. Configure Mysql configuration in application.properties file.
> 4. Create Method in Import Service class for import Excel data into a Database.
> 5. Create Import Controller to use import file data.

## Important Dependency to be used
```
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>

		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>

		<!-- https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml -->
		<dependency>
			<groupId>org.apache.poi</groupId>
			<artifactId>poi-ooxml</artifactId>
			<version>5.4.0</version>
		</dependency>

		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
```

## Configure Mysql configuration in application.properties file.
```
#Mysql Configuration
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/importdata
spring.datasource.username=root
spring.datasource.password=test
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto= create-drop
```

## Create Product class in Entity Package.
```
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "product_no")
    private Integer product_no;

    @Column(name = "product_name")
    private String product_name;

    @Column(name = "product_price")
    private Long product_price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "category")
    private String category;

}
```

## Create ProductRepository interface in repository package.

```
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
```

## Create Method in Import Service class for import Excel data into a Database.

### *ImportService*
```
@Service
public class ImportService {

    private ProductRepository repository;

    public ImportService(ProductRepository repository) {
        this.repository = repository;
    }

    // create import method
    public void importExcelToDatabase(InputStream file) throws IOException {

        LinkedList<Product> productList = new LinkedList<>();

        // open excel file
        Workbook workbook = WorkbookFactory.create(file);

        // select worksheet in excel file
        Sheet sheet = workbook.getSheetAt(0);

        sheet.forEach(row -> {
            Product product = new Product();

            // skip first row in excel file
            if (row.getRowNum() > 0) {

                // get data from cell 1
                product.setProduct_no((int) row.getCell(0).getNumericCellValue());

                // get data from cell 2
                product.setProduct_name(row.getCell(1).getStringCellValue());

                // get data from cell 3
                product.setProduct_price((long) row.getCell(2).getNumericCellValue());

                // get data from cell 4
                product.setQuantity((int) row.getCell(3).getNumericCellValue());

                // get data from cell 5
                product.setCategory(row.getCell(4).getStringCellValue());

                // add all data into linkList
                productList.add(product);
            }

        });

        // save all data into Database
        repository.saveAll(productList);
    }

    // Find all method to retrieve all data from database
    public List<Product> findAll() {
        List<Product> productList = repository.findAll();
        return productList;
    }

}
```


### *Create ImportController class inside the Controller Package.* 

```
@RestController
@RequestMapping("/product")
public class ImportController {

    private ImportService service;

    public ImportController(ImportService service) {
        this.service = service;
    }

    // URL: http://localhost:8080/customer/upload
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> importExcelToDatabase(@RequestParam("file") MultipartFile file) throws IOException {

        // check file is excel file or not
        String filename = file.getOriginalFilename();
        if (file == null || file.isEmpty() || (filename == null || (!filename.toLowerCase().endsWith(".xls") && !filename.toLowerCase().endsWith(".xlsx")))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only Import Excel file");
        }
        service.importExcelToDatabase(file.getInputStream());
        return ResponseEntity.ok("Excel File Data saved into Database");
    }

    // URL: http://localhost:8080/customer/read-data
    @GetMapping("/read-data")
    public ResponseEntity<List<Product>> findAll() {
        List<Product> productList = service.findAll();
        return ResponseEntity.ok(productList);
    }

}
```

### Following pictures will help to understand flow of API

### *PostMan Test Cases*

### *Note- Please use the Excel file inside into ExcelFile Folder. 
Url - http://localhost:8080/product/upload
![image](https://github.com/user-attachments/assets/2cdc90c0-0a14-4c73-9719-a8bdbd45cae5)

Url - http://localhost:8080/product/read-data
![image](https://github.com/user-attachments/assets/7d344d6d-f09a-4636-a369-6a56e63ed3ab)

### Mysql 

![image](https://github.com/user-attachments/assets/3553fdf6-28d6-4e32-8997-b393d282f594)




