Hello I have developed this called **E-Commerce-Backend-WebServices-Restful-APIs** using **Spring Boot, Spring JPA, Spring Security, MySQL.**

This Spring Boot project is a E-Commerce Backend Webservices designed for functionalities of E-Commerce. It features endpoints for adding, updating, listing, and deleting orders, products, order-items, reviews, users and cart-items. The application supports authentication for Admin/Users, ensuring secure access to each functionality of respective roles.

I have used entities Orders, Order-Item, Products, Cart-Items, Users and Reviews. Since ORDER is a reserved keyword in MySQL we can’t use it so we use ORDERS.


`STEPS TO RUN THE PROJECT:`
1. Download the zip file and extract the file. Open in either VS Code or Eclipse or IntelliJ.
2. navigate to `src/main/resources/application.properties`.

    2.1 Create a Database on your wish (`spring.datasource.url=jdbc:mysql://localhost:PORT_NUMBER/DBname`)
   
    2.2 Change username and password (`spring.datasource.username= USERNAME`), (`spring.datasource.password= PASSWORD`)

    2.3 Check out the PlayerDetails file in root folder. You can use that to upload in database for Player and Ranks table.

4. Open terminal and enter this command (`./mvnw spring-boot:run`) or (`mvn spring-boot:run`)




`ENDPOINTS:`

`UserController Endpoints`

   
   1    POST /register
   
        URL: `http://localhost:8080/api/user/register`
        Method: POST       
        Description: User can register their details.
        Requires Authentication: No

   2   GET /{email}
   
       URL: `http://localhost:8080/api/user/{email}`
   
       Method: GET
       
       Description: User can view their profile details.
       
       Requires Authentication: No
   
   3    POST /login
       
        URL: `http://localhost:8080/api/user/login`
   
        Method: POST
       
        Description: User/Admin can login.
   
        Requires Authentication: No
   
   4    DELETE /delete
   
        URL: `http://localhost:8080/api/user/delete`
       
        Method: DELETE
       
        Description: User/Admin can delete their profile.
        
        Requires Authentication: No
   
      
`AdminBasedController Endpoints`

`PRODUCT (Authentication Required):`

   GET /getAllProducts
   URL: `http://localhost:8080/api/admin/getAllProducts`
   Method: GET
   Description: Displays all the Products.

   GET /getProductById/{id}
   URL: `http://localhost:8080/api/admin/getProductById/{id}`
   Method: GET
   Description: Display the Products using Product Id.

   POST /createProduct
   URL: `http://localhost:8080/api/admin/createProduct`
   Method: POST
   Description: Adds Product to the list.

   PUT /updateProduct/{id}
   URL: `http://localhost:8080/api/admin/updateProduct/{id}`
   Method: PUT
   Description: Updates the product details.

   DELETE /deleteProduct/{id}
   URL: `http://localhost:8080/api/admin/deleteProduct/{id}`
   Method: DELETE
   Description: Deletes the product from the list.



`ORDERS (Authentication Required):`

   GET /getAllOrdersByUserId/{id}
   URL: `http://localhost:8080/api/admin/getAllOrdersByUserId/{id}`
   Method: GET
   Description: Admin can view all the users placed order using user’s id.
   
   GET /getOrderById/{id}
   URL: `http://localhost:8080/api/admin/getOrderById/{id}`
   Method: GET
   Description: Displays the order using Order Id.
   
   PUT /updateOrderStatus/{id}
   URL: `http://localhost:8080/api/admin/updateOrderStatus/{id}`
   Method: POST
   Description: Updates the Order’s details.


`ORDER-ITEM (Authentication Required):`

   GET /getAllOrderItems
   URL: `http://localhost:8080/api/admin/getAllOrderItems`
   Method: GET
   Description: Displays all Order-Items.

   GET /getAllOrderItemsByUserId/{id}
   URL: `http://localhost:8080/api/admin/getAllOrderItemsByUserId/{id}`
   Method: GET
   Description: Displays the Order-Items placed by each Users.

   GET /getOrderItemById/{id}
   URL: `http://localhost:8080/api/admin/getOrderItemById/{id}`
   Method: GET
   Description: Displays the Order-Item using Order-Item’s Id.


`REVIEW (Authentication Required):`

   DELETE /deleteReview/{id}
   URL: `http://localhost:8080/api/admin/deleteReview/{id}`
   Method: DELETE
   Description: Deletes the reviews from the list.

   GET /getReviewById/{id}
   URL: `http://localhost:8080/api/admin/getReviewByUserId/{id}`
   Method: GET
   Description: Displays the reviews by Review’s ID.
   
   GET /getAllReviews
   URL: `http://localhost:8080/api/admin/getAllReviews`
   Method: GET
   Description: Displays all the reviews in the list.


`UserAccessController Endpoints`

`PRODUCT (Authentication Required):`

   GET /getAllProducts
   URL: `http://localhost:8080/api/user/getAllProducts`
   Method: GET
   Description: Displays all the Products.

   GET /getProductById/{id}
   URL: `http://localhost:8080/api/user/getProductById/{id}`
   Method: GET
   Description: Display the Products using Product Id.


`ORDERS (Authentication Required):`

   POST /createOrder
   URL: `http://localhost:8080/api/user/createOrder`
   Method: POST
   Description: User can create an order which also creates an order-items.

   GET /getAllMyOrdersUserId/{id}
   URL: `http://localhost:8080/api/user/getAllOrdersByUserId/{id}`
   Method: GET
   Description: Users can view all the placed order using user’s id.
   
   GET /getOrderById/{id}
   URL: `http://localhost:8080/api/user/getOrderById/{id}`
   Method: GET
   Description: Displays the order using Order Id.
   
   DELETE /deleteOrder/{id}
   URL: `http://localhost:8080/api/user/deleteOrder/{id}`
   Method: DELETE
   Description: Deletes the Order from the list.


`ORDER-ITEM (Authentication Required):`

   GET /getAllOrderItems
   URL: `http://localhost:8080/api/user/getAllOrderItems`
   Method: GET
   Description: Displays all Order-Items.

   GET /getOrderItemById/{id}
   URL: `http://localhost:8080/api/user /getOrderItemById/{id}`
   Method: GET
   Description: Displays the Order-Item using Order-Item’s Id.


`REVIEW (Authentication Required):`

   POST /createReview
   URL: `http://localhost:8080/api/user/ createReview`
   Method: POST
   Description: User can drop a review for the product they have Ordered.

   DELETE /deleteReview/{id}
   URL: `http://localhost:8080/api/user/deleteReview/{id}`
   Method: DELETE
   Description: Deletes the reviews from the list.

   GET /getReviewById/{id}
   URL: `http://localhost:8080/api/user/getReviewByUserId/{id}`
   Method: GET
   Description: Displays the reviews by Review’s ID.
   
   GET /getReviewByUserId/{id}
   URL: `http://localhost:8080/api/user/getReviewByUserId/{id}`
   Method: GET
   Description: Displays all the reviews in the list by a user.

   PUT /updateReview/{id}
   URL: `http://localhost:8080/api/user/updateReview/{id}`
   Method: PUT
   Description: User can update their review for the product.
   
   GET /getReviewByUserId/{id}
   URL: `http://localhost:8080/api/user/getReviewByUserId/{id}`
   Method: GET
   Description: Displays all the reviews in the list by a user.


`CART-ITEMS (Authentication Required):`

   POST /addcartItems
   URL: `http://localhost:8080/api/user/addcartItems`
   Method: POST
   Description: User can add products to the cart.

   DELETE /deleteCartItemById/{id}
   URL: `http://localhost:8080/api/user/deleteCartItemById/{id}`
   Method: DELETE
   Description: User can delete the products from the cart.

   GET /{userId}/orders
   URL: `http://localhost:8080/api/user/{userId}/orders`
   Method: GET
   Description: Displays the cart items of the users.

   PUT /updatecartItemsById/{id}
   URL: `http://localhost:8080/api/user/updatecartItemsById/{id}`
   Method: PUT
   Description: User can update their cart-items for the product.

