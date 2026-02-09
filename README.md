# 📚 Library Management System

Hệ thống quản lý thư viện đầy đủ tính năng với **Backend Spring Boot** và **Frontend React**.

---

## 🌟 Tổng quan

Library Management System là một ứng dụng web toàn diện giúp quản lý các hoạt động của thư viện, bao gồm:
- Quản lý sách và thể loại
- Quản lý người dùng và phân quyền
- Mượn/trả sách
- Đặt trước sách
- Hệ thống phạt và thanh toán
- Đăng ký gói thành viên (Subscription)
- Danh sách yêu thích (Wishlist)
- Đánh giá sách

---

## 🏗️ Kiến trúc dự án

```
LibraryManagement/
├── LibraryManagementBackend/    # Spring Boot Backend API
└── library-frontend/             # React Frontend
```

---

## 🔧 Backend (Spring Boot)

### Công nghệ sử dụng

| Công nghệ | Phiên bản | Mô tả |
|-----------|-----------|-------|
| Java | 17 | Ngôn ngữ lập trình chính |
| Spring Boot | 3.2.5 | Framework backend |
| PostgreSQL | - | Cơ sở dữ liệu |
| Razorpay | 1.4.8 | Tích hợp thanh toán |

### Cấu trúc Backend

```
src/main/java/com/example/LibraryManagementSystem/
├── config/           # Cấu hình ứng dụng (Security, JWT, OpenAPI)
├── controller/       # REST API Controllers
├── domain/           # Enums và constants
├── exception/        # Xử lý exception tùy chỉnh
├── mapper/           # Chuyển đổi Entity - DTO
├── model/            # JPA Entities
├── payload/          # Request/Response DTOs
├── repository/       # JPA Repositories
└── service/          # Business Logic
```

### Các Entity chính

| Entity | Mô tả |
|--------|-------|
| `Book` | Thông tin sách (ISBN, tên, tác giả, thể loại, số lượng...) |
| `Users` | Thông tin người dùng (email, password, role) |
| `Genre` | Thể loại sách |
| `BookLoan` | Giao dịch mượn sách |
| `Reservation` | Đặt trước sách |
| `Fine` | Phạt trả sách trễ/hư hỏng |
| `Payment` | Thanh toán |
| `SubscriptionPlan` | Gói đăng ký thành viên |
| `Subscription` | Đăng ký của người dùng |
| `WishList` | Danh sách yêu thích |
| `BookReview` | Đánh giá sách |

### API Endpoints

| Controller | Chức năng |
|------------|-----------|
| `AuthController` | Đăng nhập/Đăng ký/Quên mật khẩu |
| `BookController` | CRUD sách (public) |
| `AdminBookController` | Quản lý sách (admin) |
| `BookLoanController` | Mượn/trả sách |
| `ReservationController` | Đặt trước sách |
| `FineController` | Quản lý phạt |
| `PaymentController` | Thanh toán |
| `SubscriptionController` | Đăng ký gói thành viên |
| `SubscriptionPlanController` | Quản lý gói thành viên |
| `GenreController` | Quản lý thể loại |
| `UserController` | Quản lý người dùng |
| `WishListController` | Danh sách yêu thích |
| `BookReviewController` | Đánh giá sách |

### Cấu hình Backend

Tạo file `application.yaml` hoặc sửa cấu hình có sẵn:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/library_management_system
    username: postgres
    password: your_password
  jpa:
    hibernate:
      ddl-auto: update
```

### Chạy Backend

```bash
cd LibraryManagementBackend
./mvnw spring-boot:run
```

Hoặc trên Windows:
```cmd
mvnw.cmd spring-boot:run
```

---

## 💻 Frontend (React + Vite)

### Công nghệ sử dụng

| Công nghệ | Phiên bản | Mô tả |
|-----------|-----------|-------|
| React | 19.2.0 | UI Library |
| Material UI (MUI) | 7.3.7 | UI Components |
| Tailwind CSS | 4.1.18 | Utility-first CSS |
| Axios | 1.13.4 | HTTP Client |

### Cấu trúc Frontend

```
library-frontend/
├── public/           # Static files
├── src/
│   ├── api/          # API calls
│   ├── assets/       # Images, icons
│   ├── pages/
│   │   ├── Dashboard/      # Dashboard chính
│   │   ├── Reservation/    # Đặt trước
│   │   └── Subcription/    # Đăng ký gói
│   ├── App.jsx       # Root component
│   ├── App.css       # Global styles
│   └── main.jsx      # Entry point
├── package.json
└── vite.config.js
```

### Tính năng Dashboard

- **Stats Cards**: Hiển thị thống kê (số sách đang mượn, đặt trước, reading streak)
- **Reading Progress**: Theo dõi tiến độ đọc sách năm
- **Current Loans Tab**: Danh sách sách đang mượn
- **Reservations Tab**: Danh sách đặt trước
- **Reading History Tab**: Lịch sử đọc
- **Recommendations Tab**: Gợi ý sách

### Chạy Frontend

```bash
cd library-frontend
pnpm install    # hoặc npm install
pnpm dev        # hoặc npm run dev
```

Frontend sẽ chạy tại: `http://localhost:5173`

---

## 🚀 Hướng dẫn cài đặt

### Yêu cầu

- **Java 17+**
- **Node.js 18+**
- **PostgreSQL 13+**
- **pnpm** hoặc **npm**

### Bước 1: Clone repository

```bash
git clone https://github.com/your-username/LibraryManagement.git
cd LibraryManagement
```

### Bước 2: Cấu hình Database

1. Tạo database PostgreSQL:
```sql
CREATE DATABASE library_management_system;
```

2. Cập nhật thông tin kết nối trong `LibraryManagementBackend/src/main/resources/application.yaml`

### Bước 3: Chạy Backend

```bash
cd LibraryManagementBackend
./mvnw spring-boot:run
```

### Bước 4: Chạy Frontend

```bash
cd library-frontend
pnpm install
pnpm dev
```

---

## 🔐 Bảo mật

- **JWT Authentication**: Token-based authentication
- **BCrypt Password Encoding**: Mã hóa mật khẩu
- **Role-based Access Control**: Phân quyền USER/ADMIN
- **CORS Configuration**: Cấu hình cross-origin

---

## 💳 Tích hợp thanh toán

Hệ thống tích hợp **Razorpay** để xử lý thanh toán:
- Thanh toán tiền phạt
- Đăng ký gói thành viên

---

## 📧 Email Service

Hệ thống hỗ trợ gửi email thông qua SMTP:
- Xác nhận đăng ký
- Reset mật khẩu
- Thông báo mượn/trả sách

---

## 🛠️ Scripts hữu ích

### Backend
```bash
# Build
./mvnw clean package

# Run tests
./mvnw test

# Skip tests khi build
./mvnw clean package -DskipTests
```

### Frontend
```bash
# Development
pnpm dev

# Build production
pnpm build

# Preview production build
pnpm preview

# Lint
pnpm lint
```

---

## 📄 License

MIT License

---

## 👥 Đóng góp

Mọi đóng góp đều được hoan nghênh! Vui lòng tạo Pull Request hoặc Issue.

---

## 📞 Liên hệ

- **Email**: trung8d2005@gmail.com

