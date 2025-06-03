Hello I have developed this called **E-Commerce-Backend-WebServices-Restful-APIs** using **Spring Boot, Spring JPA, Spring Security, MySQL.**

This Spring Boot project is a E-Commerce Backend Webservices designed for functionalities of E-Commerce. It features endpoints for adding, updating, listing, and deleting orders, products, order-items, reviews, users and cart-items. The application supports authentication for Admin/Users, ensuring secure access to each functionality of respective roles.

I have used entities Orders, Order-Item, Products, Cart-Items, Users and Reviews. Since ORDER is a reserved keyword in MySQL we can’t use it so we use ORDERS.


`STEPS TO RUN THE PROJECT:`
1. Download the zip file and extract the file. Open in either VS Code or Eclipse or IntelliJ.
2. navigate to `src/main/resources/application.properties`.

    2.1 Create a Database on your wish (`spring.datasource.url=jdbc:mysql://localhost:PORT_NUMBER/DBname`)
   
    2.2 Change username and password (`spring.datasource.username= USERNAME`), (`spring.datasource.password= PASSWORD`)

    2.3 Check out the PlayerDetails file in root folder. You can use that to upload in database for Player and Ranks table.

4. Open terminal and enter this command (`./mvnw spring-boot:run`)




`ENDPOINTS:`

UserController Endpoints
   
   POST /register
   URL: `http://localhost:8080/api/user/register `
   Method: POST
   Description: User can register their details.
   Requires Authentication: No
   
   GET /{email}
   URL: `http://localhost:8080/api/user/{email} `
   Method: GET
   Description: User can view their profile details.
   Requires Authentication: No
   
   POST /login
   URL: `http://localhost:8080/api/user/login `
   Method: POST
   Description: User/Admin can login.
   Requires Authentication: No
   
   DELETE /delete
   URL: `http://localhost:8080/api/user/delete`
   Method: DELETE
   Description: User/Admin can delete their profile.
   Requires Authentication: No
   
      
AdminLoginController Endpoints
`PRODUCT:`

   GET /
   URL: `http://localhost:8080/`
   Method: GET
   Description: Displays the login page.

`ORDERS:`
   POST /
   URL: `http://localhost:8080/`
   Method: POST
   Description: Processes the admin login.
   Requires Authentication: No (for the login process itself)
   
   GET /index
   URL: `http://localhost:8080/index`
   Method: GET
   Description: Displays the index page after successful login.
   Requires Authentication: Yes
   
   GET /logout
   URL: `http://localhost:8080/logout`
   Method: GET
   Description: Logs out the admin and invalidates the session.
   Requires Authentication: No (ends the session)
   

AdminLoginController Endpoints

   GET /
   URL: `http://localhost:8080/`
   Method: GET
   Description: Displays the login page.
   
   POST /
   URL: `http://localhost:8080/`
   Method: POST
   Description: Processes the admin login.
   Requires Authentication: No (for the login process itself)
   
   GET /index
   URL: `http://localhost:8080/index`
   Method: GET
   Description: Displays the index page after successful login.
   Requires Authentication: Yes
   
   GET /logout
   URL: `http://localhost:8080/logout`
   Method: GET
   Description: Logs out the admin and invalidates the session.
   Requires Authentication: No (ends the session

