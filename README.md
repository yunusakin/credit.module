# Credit Module API 🚀

This project provides a backend API for managing customers, loans, and loan installments.  
It includes role-based security, Swagger documentation, and an H2 in-memory database.
---
## **🔹 Features**
- **Customer Management**: Create and retrieve customers.
- **Loan Management**: Create loans with interest rates and installment plans.
- **Installment Management**: List loan installments and process payments.
- **Security**: Role-based access control with Spring Security.
- **Database**: In-memory H2 database with JPA and Hibernate.
- **API Documentation**: Fully documented using Swagger UI.
---
## **🔹 Technology Stack**
| Technology       | Description |
|-----------------|------------|
| **Java 21**     | Programming language |
| **Spring Boot 3.4.2** | Backend framework |
| **Spring Security** | Authentication & Authorization |
| **Spring Data JPA** | Database interaction |
| **H2 Database** | In-memory database for development/testing |
| **Lombok** | Reduces boilerplate code |
| **Maven 3.9.9** | Dependency and build management |
| **Swagger (Springdoc OpenAPI 2.1.0)** | API Documentation |
| **JUnit & Mockito** | Unit testing framework |


## **🔹 Project Setup & Installation**
### **1️⃣ Prerequisites**
Before running the application, ensure you have installed:
- **Java 21**
- **Maven 3.9.9**
- **An IDE (IntelliJ, VS Code, Eclipse, etc.)**

### **2️⃣ Clone the repository**
```bash
git clone https://github.com/your-repo-name.git
cd credit.module
```

---

## **🔹 Configuration**
### **3️⃣ Set Active Profile**
The application supports different profiles:
- `dev` (Development - Default)
- `preprod` (Pre-Production)
- `prod` (Production)
- `test` (Testing)

Ensure the correct profile is set in `application.properties`:
```properties
spring.profiles.active=dev
```

You can also specify the profile when running the application.

---

## **🔹 Build and Run the Application**
### **4️⃣ Build the Project**
```bash
mvn clean install
```

### **5️⃣ Run the Application**
Run with the default profile:
```bash
mvn spring-boot:run
```

Run with a specific profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev      # Development
mvn spring-boot:run -Dspring-boot.run.profiles=preprod  # Pre-Production
mvn spring-boot:run -Dspring-boot.run.profiles=prod     # Production
mvn spring-boot:run -Dspring-boot.run.profiles=test     # Testing
```

Run as a standalone JAR:
```bash
java -jar target/credit.module-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

---

## **🔹 API Documentation**
### **6️⃣ Access Swagger UI**
1. Start the application.
2. Open your browser and go to:
   ```
   http://localhost:8080/swagger-ui/index.html
   ```
3. Click **Authorize**, enter credentials, and explore the API.

---

## **🔹 Database Access**
### **7️⃣ H2 Console**
| **Property**  | **Value** |
|--------------|---------------------------------|
| **URL**      | `http://localhost:8080/h2-console` |
| **JDBC URL** | `jdbc:h2:mem:credit-module` |
| **Username** | `sa` |
| **Password** | (leave empty) |
| **Mode**     | In-Memory |

---

## **🔹 Authentication & Security**
### **8️⃣ Role-Based Access Control**
- **Public Endpoints**: Customer creation and retrieval.
- **Restricted Endpoints**: Loans and Installments require **ADMIN** role.

### **🔑 Default Admin Credentials**
| Username | Password  |
|----------|----------|
| `admin`  | `admin123` |

---

## **🔹 API Endpoints**
### **9️⃣ Customer API**
| Method | Endpoint                 | Description                | Access  |
|--------|---------------------------|----------------------------|---------|
| `POST` | `/api/v1/customers`       | Create a new customer      | Open    |
| `GET`  | `/api/v1/customers/{id}`  | Retrieve customer by ID    | Open    |
| `GET`  | `/api/v1/customers`       | List all customers         | Open    |

### **🔹 Loan API**
| Method | Endpoint          | Description                  | Access  |
|--------|-------------------|------------------------------|---------|
| `POST` | `/api/v1/loans`   | Create a new loan            | Admin   |
| `GET`  | `/api/v1/loans`   | List loans with filters      | Admin   |

### **🔹 Installment API**
| Method | Endpoint                         | Description                        | Access  |
|--------|----------------------------------|------------------------------------|---------|
| `GET`  | `/api/v1/installments/{loanId}` | Get all installments for a loan   | Admin   |
| `POST` | `/api/v1/installments/pay`      | Process installment payments      | Admin   |

---

## **🔹 Running Tests**
### **🔟 Execute Unit Tests**
```bash
mvn test
```

### **✅ Test Coverage**
- Loan Service Tests (Create Loan, Exceed Credit Limit, Pay Installments)
- Installment Service Tests (Pay Installments, Overpayment, No Installments)
- Global Exception Handling Tests

---