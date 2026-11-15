# 🎵 Music Shop

![Java](https://img.shields.io/badge/Java-21-ed8b00?style=flat&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-6DB33F?style=flat&logo=spring-boot&logoColor=white) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-336791?style=flat&logo=postgresql&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?style=flat&logo=apache-maven&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white) ![Podman](https://img.shields.io/badge/Podman-7C2A3A?style=flat&logo=podman&logoColor=white) ![Stripe](https://img.shields.io/badge/Stripe-008CDD?style=flat&logo=stripe&logoColor=white) ![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=jsonwebtokens&logoColor=white)

A production-ready REST backend for an online music store built with **Spring Boot 3**, **Java 21**, and **PostgreSQL**.  
Features role-based access control (USER / ADMIN), JWT authentication, shopping cart, order placement, and Stripe Checkout integration.

---



## 🚀 Tech Stack


| Layer      | Technology                                           |
| ---------- | ---------------------------------------------------- |
| Language   | **Java 21**                                          |
| Framework  | **Spring Boot 3.5.3**                                |
| Security   | **Spring Security + JWT**                            |
| Data       | **Spring Data JPA**, **Hibernate 6**, **PostgreSQL** |
| Validation | **Spring Validation (Jakarta Bean Validation)**      |
| Payments   | **Stripe API**                                       |
| Build      | **Apache Maven 3.9**                                 |
| Container  | **Docker** / **Podman** (multi-stage build)          |
| Testing    | **JUnit 5**, **Spring Boot Test**, **Mockito**       |


---



## ✨ Features



### 👤 Customer (USER)

- User registration & login
- JWT-secured endpoints
- Browse all available music CDs
- Add / remove items in shopping cart
- View cart contents and total price
- Place orders and pay via **Stripe Checkout**
- View order history



### 🛡️ Administrator (ADMIN)

- Full user management (create, delete, assign/revoke roles)
- Product management (add / delete music CDs)
- View all carts, orders, and users
- Update order status



### 🔐 Security

- Stateless JWT authentication
- Role-based endpoint protection (`ADMIN` vs `USER`)
- Password encoding (BCrypt)

---



## 📦 Project Structure

```
src/main/java/com/pawel/musicshop/
├── controller          # REST controllers
├── dto                 # Request/Response DTOs
├── model               # JPA entities
├── repository          # Spring Data JPA repositories
├── security            # JWT util, auth filter, security config
├── service             # Business logic interfaces
└── service/impl        # Service implementations
```

---



## 🛠️ Getting Started



### Prerequisites

- **Java 21+**
- **Maven 3.9+** (or use the included `mvnw` wrapper)
- **PostgreSQL** database
- **Stripe** account (test keys are sufficient for development)



### 1. Clone the repository

```bash
git clone https://github.com/your-username/MusicShop.git
cd MusicShop
```



### 2. Configure environment variables

Create a `.env` file in the project root (it is already gitignored):

```env
DB_CONNECT_URL=jdbc:postgresql://localhost:5432/musicdb
DB_USER=your_db_user
DB_PASSWORD=your_db_password
JWT_SECRET=your_jwt_secret_key_min_256_bits
STRIPE_API_KEY=sk_test_your_stripe_secret_key
WEBHOOK_SECRET=whsec_your_stripe_webhook_secret
APP_BASE_URL=http://localhost:8080
```



### 3. Run with Maven

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---



## 🐳 Docker / Podman

Build and run the containerized application:

### Docker

```bash
docker build -t music-shop .
docker run -p 8080:8080 --env-file .env music-shop
```



### Podman

```bash
podman build -t music-shop -f Containerfile .
podman run -p 8080:8080 --env-file .env music-shop
```

> ⚠️ Make sure PostgreSQL is reachable from the container. If Postgres runs on the same host, use `host.docker.internal` (Docker Desktop) or your host's LAN IP for `DB_CONNECT_URL`. `localhost` inside the container refers to the container itself.

---



## 🧪 Running Tests

```bash
# Run all tests
./mvnw test

# Run a specific test class
./mvnw test -Dtest=PaymentServiceImplTest
```

Unit tests use **Mockito** to mock repositories and external services (Stripe, database), so no real DB or payment transactions are made during test execution.

---



## 📊 Example API Endpoints


| Method | Endpoint                        | Description                    | Access      |
| ------ | ------------------------------- | ------------------------------ | ----------- |
| `POST` | `/api/auth/signup`              | Register a new user            | Public      |
| `POST` | `/api/auth/login`               | Authenticate & receive JWT     | Public      |
| `GET`  | `/api/products`                 | List all music CDs             | USER, ADMIN |
| `POST` | `/api/cart/add`                 | Add item to cart               | USER        |
| `GET`  | `/api/cart`                     | View shopping cart             | USER        |
| `POST` | `/api/orders`                   | Place an order                 | USER        |
| `POST` | `/api/payments/create-checkout` | Create Stripe checkout session | USER        |
| `GET`  | `/api/admin/users`              | Manage users                   | ADMIN       |
| `GET`  | `/api/admin/orders`             | View all orders                | ADMIN       |


---



## 🔮 Future Improvements

- Build frontend to consume this API and provide a full e-commerce shopping experience
- Add integration tests with Testcontainers (PostgreSQL)
- Add email notifications on order status change

