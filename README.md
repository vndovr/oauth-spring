# Example project to check how the OpenID protocol works

## How to use

1. Run the keycloak using docker 
```sh
     docker run -p 8081:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:18.0 start-dev
```
2. Create new realm called **TEST**
3. Create new client **ibagroup_test**
4. Create new user **test/test**
5. Create new role **MANAGER**
6. Assign role to user **test** using mapper to map list of ream roles to claim **roles** in JWT

## Play with the code

Check the description from Swagger at:  http://localhost:8080/swagger-ui/index.html

Switch from **opaque** to **jwt**  type of token validation (and back). In logs you will see what endpoints are called and how in different types.