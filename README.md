# mybank-app
# How to setup

  ```git
 https://github.com/ramveer93/mybank-app.git
  ```
  ```mvn
  mvn clean install
  ```
- install postgres version 11 in your local machine
- Go to src/main/resources initalization.sql and run the commands mentioned in this file on psql cmd prompt
- Run the application as spring boot application

# Swagger documents
```git
 http://localhost:8091/swagger-ui.html
 ```
 
 # Run the APIs in steps
 - default Users created on server startup
    - username: AdmAdm1
    - password: Admin
    - role : ADMIN
 
 - First create a jwt token with admin role POST /v1/authentication/token 
 ```json
       {
        "username":"AdmAdm1",
        "password":"Admin"
      }
```
 ## APIs supported with Role as ADMIN

 - Add a employee using POST /v1/admin/addEmployee
 ```json
  {
    "bank": {
      "bankAddress": "SBI IIT PACL INDORE CAMPUS Indore MP",
      "bankName": "SBI Indore",
      "ifscCode": "SBIIND00054"
    },
    "designation": "Senior Manager",
    "employeeFirstName": "Man",
    "employeeLastName": "Tomar",
    "employeeMiddleName": "Singh",
    "password": "addmin",
    "roles": [
      {
        "name": "EMPLOYEE"
      }
    ]
  }
```
- Delete employee DELETE /v1/admin/deleteEmployee?employeeId=4

## APIs supported with Role as EMPLOYEE

- add a new customer to bank POST /v1/employee/addCustomer
```json
{
  "accounts": [],
  "bank": 
    {
     "bankId":1
    },
  "customerAddress": "ALWAR-34433",
  "customerFirstName": "atul",
  "customerLastName": "Mohan",
  "customerMiddleName": "singh",
  "kyc": {
  }
}
```
- Deleted existing customer DELETE /v1/employee/deleteCustomer?customerId=1
- Get customer details GET /v1/employee/getCustomer?customerId=1
- Create account POST /v1/account/createAccount
```json
{
  "accType": {
    "name": "saving ACCOUNT"
  },
  "balance": 300,
  "bank": {
    "bankId": 1
  },
  "interestRate": 4.8,
  "status": "PENDING"
}
```
- Link customer with account PUT /v1/employee/linkCustomerWithAccount?customerId=1&accountIds=1
- Update customer KYC PUT /v1/employee/updateKyc

```json
  {
      "documents": [
          {
              "docFormat": "jpeg",
              "docName": "licence_ramveer",
              "docNumber": "rj05123jkks22",
              "docType": "driving licence",
              "verificationStatus": "Approved"
          }
      ],
      "status": "Success"
  }
```
- Get Account balance GET /v1/account/getBalance?accountId=1
- Transfer money from one account to another PUT /v1/account/transferMoney?sourceAccountId=1&targetAccountId=2&money=14
- Get account statement GET /v1/account/accountStatement??accountId=1&startDate=2021-01-27T00:35:15.896&endDate=2021-01-27T00:35:15.896
- Generate report GET /v1/account/generateReport?accountId=1&startDate=2021-01-27T00:35:15.896&endDate=2021-01-27T00:35:15.896


# Authorization
- Via JWT token 
- First create admin/employees to generate jwt token , Use below end point to generate jwt token 
- POST /v1/authentication/token
```json
  {
    "username":"AdmAdm1",
    "password":"Admin"
  }
```

# License 
MIT License 2021


