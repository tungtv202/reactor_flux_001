# Purchases API
### Technical 
- Java 11, Maven, Lombok
- Spring boot, Spring WebFlux: for reactive web
    - Webclient: reactive client to perform HTTP requests, non-blocking
- ProjectReactor: for reactive programming
- Jackson: JSON serialization/deserialization library

### Step-by-step instructions for run source code
1. Maven clean & package    
```./mvnw clean package```
2. Change config file
    - `src/main/resources/application.properties`
        - Change api base_url (nodejs server, that run at `daw-purchases-master`)
        - Change port web server (default is 8080)
    
3. Run spring-boot 
```./mvnw spring-boot:run ```
4. Test api - get recent purchases 
```bash
curl --request GET 'http://127.0.0.1:8080/api/recent_purchases/Jasen64'
```
// can test with my example data in `daw-purchases-master/.data`

### Other note
- I use local-in-memory for cache popular purchase data. I think this is sample solution for exercise. `(LocalCacheServiceImpl.class)`
- In the real word, I think I have to handler error more careful.
- Core reactor solution at `PurchaseHandler.class > getPopularPurchasesDto()`
