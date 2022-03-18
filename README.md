# COEN268 Final Project  ---- Final Project
Ce Pang, Honglin Chen, Xinwei Lu, Yulin Zeng, Zhihao Lin

## Overview
EatWhat is a restaurant recommendation application based on Android platform. 

A user can either use the app to search for a restaruant with filters or post a restaurant review.

**Tech Stack:** 

Restaurant search module:  **Yelp API** + **Retrofit with OkHttp** + **Android SDK (Java)**

Post review module: **Firebase database** + **Android SDK (Java)**

## Main functional modules：
### User info mangement module: 
1. A user can login to his or her account with email address and password
2. A user can create an account with email address, username, and a user also can take a photo or choose a photo from gallery as avatar. 
3. After log in, the username abd avatar is shown in the drawer navigation header

![image](https://user-images.githubusercontent.com/29085565/158917243-5612a485-fdf5-4201-b691-fe181f587128.png)
![image](https://user-images.githubusercontent.com/29085565/158917268-8a14ce85-25e7-4109-b796-79149bb7a2c4.png)
![image](https://user-images.githubusercontent.com/29085565/158917420-346cb595-9e9c-4420-813c-3862ef4c24dd.png)

4. A user can manage his or her account by clicking "My Profile" in drawer naviagtion header 
5. A user can change the password

![image](https://user-images.githubusercontent.com/29085565/158917575-ea5b5095-535b-4cbe-b9f6-a5a1ea3cba96.png)
![image](https://user-images.githubusercontent.com/29085565/158917900-f8ebb373-f511-424f-ba7e-1e2de3c3413b.png)
![image](https://user-images.githubusercontent.com/29085565/158917774-c8d3f0e0-8419-46f7-985e-0a10a35ba31d.png)

### Restarunt Search Module
The search funtion is based on location (city）
1. a user can use his or her current location or select a location on map by clicking the location icon on home page. Default is user's current location

![image](https://user-images.githubusercontent.com/29085565/158919034-94754d0b-fb2a-487d-8041-cc041791eb5b.png)
![image](https://user-images.githubusercontent.com/29085565/158919067-594ac0a4-bd04-4659-b7f0-ce0c62054b0b.png)


2. A user can search restaurant with selected category
3. A user also can search a restaurant with the restaurant name

![image](https://user-images.githubusercontent.com/29085565/158918596-5ddfc4ef-decd-4e7a-b739-30dbe94f114b.png)
![image](https://user-images.githubusercontent.com/29085565/158918631-17a4d6dc-803a-461e-bb45-dbeba9a130ec.png)
![image](https://user-images.githubusercontent.com/29085565/158918765-c0c33194-27f5-4bae-b66c-df76765187b2.png)

 
 4. Click on any restaurant card will direct the user to the restaurant detail page, click on the map icon and phone icon will lead the app to external map application and phone dialing application
 
 6. Click on the "Star" icon will let the user collect the restaurnt and a user can find the restaurant in "Collected Restaurant" (from drawer sidebar meun)
 
![image](https://user-images.githubusercontent.com/29085565/158919448-c6783a6f-8400-4c6f-9fad-a2449127cd28.png)
![image](https://user-images.githubusercontent.com/29085565/158919745-1fc1cab8-e2a3-469b-b086-d737bf487784.png)


### Post Review Module

1. A user can write a post by click the floating button on home page
2. After clicking post, the user can see his or her post under POSTS tab. The user can also see other user's posts under this tab
3. A user can manage his or her posts in My Posts page (from drawer sidebar meun)

![image](https://user-images.githubusercontent.com/29085565/158920403-3dd9140f-9d65-481c-9264-40bbe4744ac2.png)
![image](https://user-images.githubusercontent.com/29085565/158920541-95169aa1-d515-4855-bfcd-1816a34f89a7.png)
![image](https://user-images.githubusercontent.com/29085565/158920593-f8ef48c2-8502-4530-aaea-ed7118c1cf77.png)

4. clicking on any review card will direct the user to post detail page
5. clicking on the "heart" icon represents thge user like this post, and the user can see this post in "Liked Post" (from drawer sidebar meun)

![image](https://user-images.githubusercontent.com/29085565/158920801-6b20ef93-a27c-45e2-8707-1ce243c505f4.png)
![image](https://user-images.githubusercontent.com/29085565/158920996-296d809e-7fdf-4b5b-bd96-3bb8c3250560.png)

###  Retaurant of the Day Module 
1. Under TODAY'S Tab, a user can shake the phone for Retaurant of the Day
2. A card will pop out, and the user can click GO to see detail of the restaurant (restaurant detail page)

![image](https://user-images.githubusercontent.com/29085565/158921196-aceeeae1-28ef-4e08-9674-427ff5aabd85.png)
![image](https://user-images.githubusercontent.com/29085565/158921226-c0ebe2dd-a204-44cf-aaa4-959a7ef3fd8c.png)

## Database design
Non-relational database structure for post

![image](https://user-images.githubusercontent.com/29085565/158922161-dabe669d-b92a-44fc-80ea-01692e24d1b4.png)


Non-relational database structure for a user

![image](https://user-images.githubusercontent.com/29085565/158922197-577ec415-2a51-4105-9325-f82bf55d61b0.png)


## External API used
For Restaurant search functionality, we used Yelp Fusion API
https://www.yelp.com/developers/documentation/v3/get_started
