# Spring Security with JWT Implementation

## Basic Project Diagram

![image](https://github.com/MuhammedKaraaslan/springsecurity/assets/39337485/0a7b81d0-5419-4018-9fad-cdf21bc43403)

## Project Information
### Overview
This project is developed using Spring framework, focusing on Spring Security implementation. It includes two user types: Admin and Customer, each with their own authentication and authorization capabilities based on their roles.

### Features
- Authentication: Both Admin and Customer users can authenticate themselves using their respective credentials.
- Authorization: Role-based access control is implemented, allowing Admin and Customer users to access specific resources based on their roles.
- DemoController: A controller class named DemoController is created to demonstrate role-based access control through different endpoints:
  - Public endpoint accessible to all users.
  - Endpoint restricted to Admin user only.
  - Endpoint restricted to Customer user only.
  - Endpoint accessible to both Admin and Customer users.

## Run Project Locally
```
git clone https://github.com/MuhammedKaraaslan/springsecurity.git
cd springsecurity
docker-compose up -d
```

#### Define environment variables or use default values in application.yml

```
spring.datasource
  url={SPRING_DATASOURCE_URL}
  username={SPRING_DATASOURCE_USERNAME}
  password={SPRING_DATASOURCE_PASSWORD}

application.security.jwt
  secret-key={JWT_SECRET_KEY}
  expiration={JWT_EXPIRATION}
  expiration={REFRESH_TOKEN_EXPIRATION}
```

## Endpoints

  <table style="width:500000">
        <thead>
            <tr>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Method</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Url</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Description</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Request Body</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Header</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>POST</td>
                <td>/api/v1/admins/register</td>
                <td>Admin Register</td>
                <td>AdminRegistrationRequest</td>
                <td></td>
            </tr>
            <tr>
                <td>POST</td>
                <td>/api/v1/admins/login</td>
                <td>Admin Login</td>
                <td>AdminLoginRequest</td>
                <td></td>
            </tr>
            <tr>
                <td>POST</td>
                <td>/api/v1/admins/refresh-token</td>
                <td>Admin Refresh Token</td>
                <td></td>
                <td>Bearer Token</td>
            </tr>
             <tr>
                <td>POST</td>
                <td>/api/v1/customers/register</td>
                <td>Customer Register</td>
                <td>CustomerRegistrationRequest</td>
                <td></td>
            </tr>
            <tr>
                <td>POST</td>
                <td>/api/v1/customers/login</td>
                <td>Customer Login</td>
                <td>CustomerLoginRequest</td>
                <td></td>
            </tr>
            <tr>
                <td>POST</td>
                <td>/api/v1/customers/refresh-token</td>
                <td>Customer Refresh Token</td>
                <td></td>
                <td>Bearer Token</td>
            </tr>
                      <tr>
                <td>GET</td>
                <td>/api/v1/demo/public-endpoint</td>
                <td>Public Endpoint</td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td>GET</td>
                <td>/api/v1/demo/customer-secure-endpoint</td>
                <td>Customer Secure Endpoint</td>
                <td></td>
                <td>Bearer Token</td>
            </tr>
            <tr>
                <td>GET</td>
                <td>/api/v1/demo/admin-secure-endpoint</td>
                <td>Admin Secure Endpoint</td>
                <td></td>
                <td>Bearer Token</td>
            </tr>
             <tr>
                <td>GET</td>
                <td>/api/v1/demo/admin-or-customer-secure-endpoint</td>
                <td>Admin or Customer Secure Endpoint</td>
                <td></td>
                <td>Bearer Token</td>
            </tr>
        </tbody>
  </table>
