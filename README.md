# BullishAPI

Bullish demo project

1. Download codebase at
   https://github.com/seanzhu0925/BullishAPI
2. Once downloaded, unzip it in your local workspace, open it with your preferred IDE tool
3. Making sure you have Java JDK installed on your machine Java 11 and above is required
   https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html
4. Download IDE at https://www.jetbrains.com/idea/download
5. Follow the instruction to build the project
   https://stackoverflow.com/questions/31256356/how-to-import-gradle-projects-in-intellij
6. To start the app, navigate to the main class from the root directory, BullishAPI -> src/main/java/org.api.bullish ->
   ElectronicStoreApplication(right click here), select the green error run main class "Run ElectronicStoreApplication"
7. To start the tests, navigate to the main class from the root directory, 6. To start the app, navigate to the main
   class from the root directory, BullishAPI -> src/main/test (right click here), select the green error run all test
   classes "Run Tests in 'BullishAPI'"
8. Manual testing could also achieve through curl commands, have the local server running follow step 6, then go to your
   preferred tool to execute the following commands

Step 1, create a new inventory
curl --location --request POST 'localhost:8080/v1/api/product/create' \
--header 'Content-Type: application/json' \
--data-raw '{
"productName" : "productName",
"price": "30",
"quantity" :  "30",
"productType" : "TV",
"description" : "Nice TV set2"
}'

Step 2, add to shopping cart for current user
curl --location --request POST 'localhost:8080/v1/api/checkout/addToCart' \
--header 'Content-Type: application/json' \
--data-raw '{
"productName" : "productName",
"userId": "dummyUser",
"quantity" :  "10"
}'

Step 3 (Optional), create new discount code
curl --location --request POST 'localhost:8080/v1/api/promocode/create' \
--header 'Content-Type: application/json' \
--data-raw '{
"promocodeName" : "promodummy",
"promocodeType": "HALF_PRICE",
"maxUseTime" :  "30"
}'

Step 4 (Optional), apply discount code
curl --location --request POST 'localhost:8080/v1/api/promocode/apply' \
--header 'Content-Type: application/json' \
--data-raw '{
"userId" : "dummyUser",
"promocodeName": "promodummy"
}'

Step 5, checkout the current shopping cart
curl --location --request GET 'localhost:8080/v1/api/checkout/userId/dummyUser' \
--header 'Content-Type: application/json' \
--data-raw '{
"productName" : "test2",
"userId": "dummyUser",
"quantity" :  20
}'

Final Result will be similar like below response object

```json
{
"orderId": "2fad402e-f04e-4948-8254-8799a1004445",
"userId": "dummyUser",
"totalPrice": 450.0,
"products": [
    {
    "productId": "97a1ec2e-80cc-4f3d-9529-06fef28abc68",
    "productName": "productName1",
    "productType": "TV",
    "description": "Nice TV set2",
    "price": 30.0,
    "quantity": 5,
    "createDate": "2022-11-17T00:50:37.288+00:00",
    "lastModifiedDate": "2022-11-17T00:50:37.288+00:00",
    "totalPriceAfterDiscount": null
    },
    {
    "productId": "48616e2d-b3e8-4e76-a16e-ae6c27e4aa0e",
    "productName": "productName2",
    "productType": "TV",
    "description": "Nice TV set2",
    "price": 30.0,
    "quantity": 10,
    "createDate": "2022-11-17T00:47:26.420+00:00",
    "lastModifiedDate": "2022-11-17T00:47:26.420+00:00",
    "totalPriceAfterDiscount": null
    }
],
"createDate": "2022-11-17T00:50:54.295+00:00"
}
```