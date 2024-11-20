# Burger Restaurant Application
## Project description
This application is developed for a chain of burger restaurants. It allows users to:

1. View the burger menu.
2. View one menu product.
3. Add products to cart.
4. Delete products from cart.
5. Log in to the application to receive additional privileges, such as viewing promotions and paying for order with bonus points.
6. Choose a payment method (cash or bonuses).
7. Customize the application interface, including language and theme.
The application supports English and Russian languages, as well as dark and light themes.

## Technologies

1. Kotlin – programming language.
2. Jetpack Components:
* Hilt for dependency injection.
* Room for working with the local database.
* Navigation Component for navigation between screens.
* ViewModel and StateFlow for state management.
3. Retrofit – for interacting with the REST API.
4. Material Design – for UI styling.

  ## The view of my application

1. The preview that greets the user when the application is launched.
   
   ![Preview](/app/src/main/res/drawable/images/app1.png)

2. A menu where everyone will find a dish to their liking. It is possible to filter by type of dish: you can display only burgers, only pizza or all dishes at the same time.
   
   ![Menu](/app/src/main/res/drawable/images/app2.png)

3. An individual product card where the user can read a short and full description of the dish, its price, as well as add the dish to the cart and go directly to the cart page.

   ![Product card](/app/src/main/res/drawable/images/app11.png)

4. From any page of the app, the user can go to the settings page and choose the language (English or Russian) and theme (dark or light) that suits them.

   ![Settings](/app/src/main/res/drawable/images/app3.png)

5. To use all features of the application, the user needs to authorize (current login "user1", current password "user")

   ![Sign in](/app/src/main/res/drawable/images/app4.png)

6. One of the additional features that becomes available to the user after registration: a list of special offers offered by the restaurant.

   ![Spicial offers](/app/src/main/res/drawable/images/app5.png)

7. Also after authorization the user can click on the piggy bank to view his data in his personal cabinet.

   ![Personal cabinet](/app/src/main/res/drawable/images/app6.png)

8. On the cart page the user can see the selected items and adjust their quantity, see the total price of products, if the user is authorized, he can also see the number of his points.

   ![Basket](/app/src/main/res/drawable/images/app7.png)

9. On the payment page the user can choose the payment method that suits him: cash or, if the user is authorized, payment with bonus points.
    
    ![Payment](/app/src/main/res/drawable/images/app8.png)
   
10. After successful payment of the order, the user can return to the menu for further selection of dishes.

    ![Success payment](/app/src/main/res/drawable/images/app9.png)

11. If the user loses internet connection while using the application, there will be displayed a warning message about it.

    ![Lost internet connection](/app/src/main/res/drawable/images/app10.png)

## Installation and launch 
### Requirements: 
* Android 7.0 and above 
* Free space: 50 MB
* Internet: Required for the application to work.
  
### Installation:
* Download the APK file from the release.
* Install the APK on your device.

### Launch from source:
* Clone repository:git clone https://github.com/anka-ka/Burger-application.git
* Open the project in Android Studio.
* Synchronize Gradle and run the app.

## Backend Integration

This application relies on a backend service developed by [Anton Shashkov](https://github.com/z1kman). The backend handles business logic of this application.

You can find the backend service here:

[Swagger API](https://ashashkov.com/project/burgerRestaurant/api/docs)
[Backend Repository](https://github.com/z1kman/burgerRestaurantBackend)
