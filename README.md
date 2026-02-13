# 📚 Library Management System

Hệ thống quản lý thư viện đầy đủ tính năng với **Backend Spring Boot** và **Frontend React + Vite**.

---

## 🌟 Tổng quan

Library Management System là một ứng dụng web toàn diện giúp quản lý các hoạt động của thư viện hiện đại, bao gồm:

- 📖 **Quản lý sách và thể loại** - CRUD sách, phân loại theo thể loại
- 👥 **Quản lý người dùng** - Phân quyền USER/ADMIN, xác thực JWT
- 📚 **Mượn/trả sách** - Theo dõi trạng thái mượn, lịch sử đọc
- 🔖 **Đặt trước sách** - Hệ thống đặt trước khi sách hết
- 💰 **Hệ thống phạt** - Tự động tính phạt trễ hạn/hư hỏng
- 💳 **Thanh toán trực tuyến** - Tích hợp Razorpay
- 🎫 **Gói thành viên** - Đăng ký các gói subscription
- ❤️ **Danh sách yêu thích** - Lưu sách yêu thích
- ⭐ **Đánh giá sách** - Review và rating

---

## 🏗️ Kiến trúc dự án

```
LibraryManagement/
├── LibraryManagementBackend/    # Spring Boot REST API
│   ├── src/main/java/           # Source code Java
│   ├── src/main/resources/      # Configuration files
│   └── pom.xml                  # Maven dependencies
│
└── library-frontend/             # React + Vite SPA
    ├── src/
    │   ├── pages/               # Page components
    │   ├── api/                 # API integration
    │   └── assets/              # Static resources
    └── package.json             # NPM dependencies
```

---

## 🔧 Backend (Spring Boot)

### 🛠️ Công nghệ sử dụng

| Công nghệ             | Phiên bản | Mô tả                       |
| --------------------- | --------- | --------------------------- |
| **Java**              | 17        | Ngôn ngữ lập trình          |
| **Spring Boot**       | 3.2.5     | Framework backend           |
| **Spring Security**   | 3.2.5     | Bảo mật và JWT              |
| **Spring Data JPA**   | 3.2.5     | ORM & Database access       |
| **PostgreSQL**        | Latest    | Cơ sở dữ liệu quan hệ       |
| **Lombok**            | 1.18.32   | Giảm boilerplate code       |
| **Razorpay**          | 1.4.8     | Payment gateway             |
| **SpringDoc OpenAPI** | 2.5.0     | API documentation (Swagger) |
| **JWT (jjwt)**        | 0.13.0    | JSON Web Token              |
| **Spring Mail**       | 3.2.5     | Email service               |
| **Maven**             | -         | Build tool                  |

### 📁 Cấu trúc Backend

```
src/main/java/com/example/LibraryManagementSystem/
├── config/           # Security, JWT, CORS, Swagger configuration
├── controller/       # REST API Controllers (14 controllers)
├── domain/           # Enums (BookStatus, LoanStatus, ReservationStatus...)
├── event/            # Domain events
├── exception/        # Custom exceptions & handlers
├── mapper/           # Entity ↔ DTO mappers
├── model/            # JPA Entities (Book, User, Loan...)
├── payload/          # Request/Response DTOs
├── repository/       # JPA Repositories
└── service/          # Business logic & services
```

### 📊 Database Entities

| Entity               | Thuộc tính chính                                         | Quan hệ                           |
| -------------------- | -------------------------------------------------------- | --------------------------------- |
| **Book**             | ISBN, title, author, genre, totalCopies, availableCopies | ManyToOne → Genre                 |
| **Users**            | email, password, firstName, lastName, role               | OneToMany → BookLoan, Reservation |
| **Genre**            | name, description                                        | OneToMany → Book                  |
| **BookLoan**         | loanDate, dueDate, returnDate, status                    | ManyToOne → Book, User            |
| **Reservation**      | reservationDate, expiryDate, status                      | ManyToOne → Book, User            |
| **Fine**             | amount, reason, paidDate, status                         | ManyToOne → BookLoan, User        |
| **Payment**          | amount, paymentDate, method, status                      | ManyToOne → User                  |
| **SubscriptionPlan** | name, duration, price, features                          | OneToMany → Subscription          |
| **Subscription**     | startDate, endDate, status                               | ManyToOne → User, Plan            |
| **WishList**         | addedDate                                                | ManyToOne → Book, User            |
| **BookReview**       | rating, comment, reviewDate                              | ManyToOne → Book, User            |

### 🌐 API Controllers

| Controller                   | Base Path                 | Chức năng                         |
| ---------------------------- | ------------------------- | --------------------------------- |
| `AuthController`             | `/api/auth`               | Đăng ký, đăng nhập, quên mật khẩu |
| `BookController`             | `/api/books`              | Lấy danh sách sách (public)       |
| `AdminBookController`        | `/api/admin/books`        | Quản lý sách (ADMIN only)         |
| `BookLoanController`         | `/api/book-loans`         | Mượn, trả, gia hạn sách           |
| `ReservationController`      | `/api/reservations`       | Đặt trước, hủy đặt trước          |
| `FineController`             | `/api/fines`              | Xem, thanh toán phạt              |
| `PaymentController`          | `/api/payments`           | Xử lý thanh toán Razorpay         |
| `SubscriptionController`     | `/api/subscriptions`      | Đăng ký gói thành viên            |
| `SubscriptionPlanController` | `/api/subscription-plans` | Quản lý gói thành viên            |
| `GenreController`            | `/api/genres`             | CRUD thể loại sách                |
| `UserController`             | `/api/users`              | Quản lý người dùng                |
| `WishListController`         | `/api/wishlists`          | Quản lý danh sách yêu thích       |
| `BookReviewController`       | `/api/book-reviews`       | Đánh giá và review sách           |
| `HomeController`             | `/`                       | Health check                      |

### ⚙️ Cấu hình Backend

File `application.yaml`:

```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/library_management_system
    username: postgres
    password: your_password

  jpa:
    hibernate:
      ddl-auto: update

  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html

razorpay:
  key:
    id: your_razorpay_key
    secret: your_razorpay_secret
  callback:
    base-url: http://localhost:5173
```

### 🚀 Chạy Backend

```bash
cd LibraryManagementBackend

# Trên Linux/Mac
./mvnw spring-boot:run

# Trên Windows
mvnw.cmd spring-boot:run
```

Backend sẽ chạy tại: `http://localhost:8080`
Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 💻 Frontend (React + Vite)

### 🎨 Công nghệ sử dụng

| Công nghệ             | Phiên bản | Mô tả                       |
| --------------------- | --------- | --------------------------- |
| **React**             | 19.2.0    | UI Library                  |
| **Vite**              | 7.2.4     | Build tool & dev server     |
| **React Router**      | 7.13.0    | Client-side routing         |
| **Material UI (MUI)** | 7.3.7     | Component library           |
| **Tailwind CSS**      | 4.1.18    | Utility-first CSS framework |
| **Axios**             | 1.13.4    | HTTP client                 |
| **Emotion**           | 11.14.0   | CSS-in-JS styling           |

### 📁 Cấu trúc Frontend

```
library-frontend/
├── public/                  # Static assets
├── src/
│   ├── api/                # API integration với backend
│   ├── assets/             # Images, icons, media
│   ├── img/                # Image resources
│   ├── pages/              # React components cho từng page
│   │   ├── Book/          # Trang danh sách sách
│   │   │   ├── BookPage.jsx
│   │   │   ├── BookCard.jsx
│   │   │   └── GenreFilter.jsx
│   │   ├── Dashboard/     # Dashboard người dùng
│   │   │   ├── Dashboard.jsx
│   │   │   ├── CurrentLoans.jsx
│   │   │   ├── CurrentLoanCard.jsx
│   │   │   ├── Reservations.jsx
│   │   │   ├── ReadingHistory.jsx
│   │   │   ├── Recommandations.jsx
│   │   │   ├── StatesCard.jsx
│   │   │   ├── StateConfig.jsx
│   │   │   └── GetStatusChip.jsx
│   │   ├── MyLoans/       # Quản lý sách đang mượn
│   │   │   ├── MyLoan.jsx
│   │   │   ├── LoanCard.jsx
│   │   │   ├── loans.js
│   │   │   └── tab.js
│   │   ├── Reservation/   # Quản lý đặt trước
│   │   │   ├── MyReservation.jsx
│   │   │   ├── MyReservationCard.jsx
│   │   │   ├── reservation.js
│   │   │   ├── tab.jsx
│   │   │   └── getStatusColor.js
│   │   ├── Subcription/   # Đăng ký gói thành viên
│   │   └── UserLayout/    # Layout chung
│   │       ├── UserLayout.jsx
│   │       ├── Navbar.jsx
│   │       ├── UserSidebar.jsx
│   │       ├── SidebarDrawer.jsx
│   │       ├── NavigationItems.jsx
│   │       ├── formatDate.js
│   │       └── util.js
│   ├── App.jsx             # Root component & routing
│   ├── App.css             # Global CSS
│   ├── index.css           # Base styles
│   └── main.jsx            # Entry point
├── eslint.config.js        # ESLint configuration
├── jsconfig.json           # JavaScript config
├── vite.config.js          # Vite configuration
└── package.json            # Dependencies
```

### 🎯 Trang và Tính năng Frontend

#### 1. **Dashboard** (`/`)

- **Stats Cards**:
  - Tổng số sách đang mượn
  - Số sách đặt trước
  - Reading streak (chuỗi ngày đọc)
  - Tiến độ đọc trong năm
- **Tabs Navigation**:
  - 📚 Current Loans: Danh sách sách đang mượn
  - 🔖 Reservations: Sách đã đặt trước
  - 📖 Reading History: Lịch sử đọc
  - 💡 Recommendations: Gợi ý sách phù hợp

#### 2. **Books** (`/books`)

- Danh sách tất cả sách có sẵn
- Lọc theo thể loại (Genre Filter)
- Card hiển thị thông tin sách
- Chức năng tìm kiếm

#### 3. **My Loans** (`/my-loans`)

- Xem sách đang mượn
- Trạng thái mượn (ACTIVE, OVERDUE, RETURNED)
- Ngày mượn, ngày hết hạn
- Lịch sử mượn trả
- Status chips với màu sắc

#### 4. **My Reservations** (`/my-reservations`)

- **Statistics Cards**:
  - 📚 Total Reservations: Tổng số đặt trước
  - ⏰ Active Reservations: Đang active
  - ✅ Ready To Pick Up: Sẵn sàng nhận
- **Tabs**:
  - All: Tất cả reservations
  - Active: PENDING & AVAILABLE
  - Fulfilled: Đã hoàn thành
- **Reservation Cards**: Chi tiết từng đặt trước với status colors

#### 5. **Other Routes** (Planned)

- `/my-fines`: Quản lý phạt
- `/subscriptions`: Đăng ký gói thành viên
- `/wishlist`: Danh sách yêu thích
- `/profile`: Thông tin cá nhân
- `/settings`: Cài đặt

### 🎨 UI/UX Features

- **Material Design**: Sử dụng MUI components
- **Responsive Design**: Tương thích mọi thiết bị
- **Tailwind Utility Classes**: Styling nhanh chóng
- **Gradient Effects**: Màu gradient hiện đại
- **Hover Effects**: Tương tác mượt mà
- **Status Chips**: Trạng thái với màu sắc rõ ràng
- **Card-based Layout**: Giao diện dạng thẻ

### 🚀 Chạy Frontend

```bash
cd library-frontend

# Cài đặt dependencies
pnpm install
# hoặc npm install

# Chạy development server
pnpm dev
# hoặc npm run dev
```

Frontend sẽ chạy tại: `http://localhost:5173`

---

## 🚀 Hướng dẫn cài đặt

### ✅ Yêu cầu hệ thống

- **Java**: JDK 17 trở lên
- **Node.js**: 18.x trở lên
- **PostgreSQL**: 13 trở lên
- **Maven**: 3.6+ (hoặc dùng wrapper đi kèm)
- **pnpm** hoặc **npm**: Package manager

### 📥 Bước 1: Clone Project

```bash
git clone https://github.com/your-username/LibraryManagement.git
cd LibraryManagement
```

### 🗄️ Bước 2: Cấu hình Database

1. Tạo PostgreSQL database:

```sql
CREATE DATABASE library_management_system;
```

2. Cập nhật file `LibraryManagementBackend/src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/library_management_system
    username: postgres
    password: your_password # Thay đổi password của bạn
```

### ⚙️ Bước 3: Cấu hình Email (Optional)

Để sử dụng tính năng gửi email, cập nhật trong `application.yaml`:

```yaml
spring:
  mail:
    username: your-email@gmail.com
    password: your-app-password # Generate password từ Google
```

### 💳 Bước 4: Cấu hình Razorpay (Optional)

Để sử dụng thanh toán, cập nhật:

```yaml
razorpay:
  key:
    id: your_razorpay_key_id
    secret: your_razorpay_secret
```

### 🔧 Bước 5: Chạy Backend

```bash
cd LibraryManagementBackend

# Build project
./mvnw clean package

# Chạy ứng dụng
./mvnw spring-boot:run
```

Backend sẽ chạy tại `http://localhost:8080`

### 🎨 Bước 6: Chạy Frontend

Mở terminal mới:

```bash
cd library-frontend

# Cài đặt dependencies
pnpm install

# Chạy development server
pnpm dev
```

Frontend sẽ chạy tại `http://localhost:5173`

### ✅ Bước 7: Kiểm tra

- Frontend: http://localhost:5173
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

---

## 🔐 Bảo mật và Xác thực

### JWT Authentication

- Token-based authentication
- Access token với thời gian hết hạn
- Secure password encoding với BCrypt

### Role-based Access Control (RBAC)

- **USER**: Mượn sách, đặt trước, xem lịch sử
- **ADMIN**: Quản lý sách, thể loại, người dùng

### Security Features

- CORS configuration
- Password encryption
- JWT token validation
- Secure endpoints

---

## 💳 Tích hợp Thanh toán

### Razorpay Payment Gateway

- Thanh toán tiền phạt
- Đăng ký gói subscription
- Xử lý callback và verification
- Lưu lịch sử thanh toán

---

## 📧 Email Service

### Tính năng Email

- Xác nhận đăng ký tài khoản
- Reset mật khẩu
- Thông báo mượn/trả sách
- Nhắc nhở hết hạn

### Cấu hình

Sử dụng Spring Mail với SMTP (Gmail, SendGrid, v.v.)

---

## 🛠️ Scripts hữu ích

### Backend Commands

```bash
# Clean và build
./mvnw clean package

# Chạy tests
./mvnw test

# Skip tests khi build
./mvnw clean package -DskipTests

# Chạy với profile cụ thể
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Frontend Commands

```bash
# Development
pnpm dev

# Build production
pnpm build

# Preview production build
pnpm preview

# Lint code
pnpm lint

# Clear cache và reinstall
rm -rf node_modules pnpm-lock.yaml
pnpm install
```

---

## 📊 Database Schema

### Quan hệ chính

```
Users (1) -----> (*) BookLoan
Users (1) -----> (*) Reservation
Users (1) -----> (*) Subscription
Users (1) -----> (*) WishList
Users (1) -----> (*) BookReview

Book (1) -----> (*) BookLoan
Book (1) -----> (*) Reservation
Book (*) -----> (1) Genre

BookLoan (1) -----> (*) Fine

SubscriptionPlan (1) -----> (*) Subscription
```

---

## 🧪 Testing

```bash
# Backend tests
cd LibraryManagementBackend
./mvnw test

# Frontend tests (nếu có)
cd library-frontend
pnpm test
```

---

## 📝 API Documentation

Sau khi chạy backend, truy cập:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

---

## 🤝 Đóng góp

Mọi đóng góp đều được hoan nghênh!

1. Fork repository
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

---

## 📄 License

Dự án này được phát hành theo giấy phép [MIT License](LICENSE)

---

## 👨‍💻 Tác giả

**Trung**  
📧 Email: trung8d2005@gmail.com  
🐙 GitHub: [https://github.com/trung1609](https://github.com/your-github-username)

---

## 🙏 Acknowledgments

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [Material UI](https://mui.com)
- [Tailwind CSS](https://tailwindcss.com)
- [Razorpay](https://razorpay.com)

---

## 📞 Liên hệ & Hỗ trợ

Nếu có vấn đề hoặc câu hỏi:

- 📧 Email: trung8d2005@gmail.com
- 🐛 Issues: [GitHub Issues](https://github.com/your-username/LibraryManagement/issues)

---

<div align="center">

### ⭐ Nếu project hữu ích, hãy cho một star nhé! ⭐

Made with ❤️ by Trung

</div>
