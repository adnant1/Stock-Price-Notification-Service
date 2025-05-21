# 📈 TickrAlert — Stock Price Notification Service

TickrAlert is a full-stack stock price alert system that allows users to create custom notifications based on real-time stock data. When a stock price crosses a user's target threshold, an alert is triggered and delivered via email using AWS SNS.

---

## 🚀 Features

- 🔐 **Google OAuth 2.0 Authentication**
- 📬 **Real-time Stock Alerts via Email** (powered by AWS SNS)
- 📊 **Live Stock Prices** using the Finnhub API
- 📅 **Scheduled Price Checks** via EventBridge
- 📦 **DynamoDB Integration** for storing user alerts
- 🧼 **Full-Stack App** with a protected frontend dashboard
- 🌐 **Deployed on AWS Elastic Beanstalk** using Docker and frontend **hosted on AWS S3**

---

## 🛠️ Tech Stack

### Backend

- Java 21
- Spring Boot 3 using Maven
- AWS SDK v2 (DynamoDB, SNS)
- OAuth 2.0 Login (Google)

### Frontend

- React (Vite)
- React Router
- Axios

### Cloud & Infra

- AWS Elastic Beanstalk (Dockerized)
- AWS SNS
- AWS DynamoDB
- AWS EventBridge
- AWS S3
- Finnhub Stock API

---

## ⚙️ Setup Instructions

### 1. Clone the Repo

```bash
git clone https://github.com/adnant1/TickrAlert.git
cd TickrAlert
```

### 2. Backend Setup

```bash
cd stock-alert-service
```

#### Configure `application.yml`

```yaml
spring:
  application:
    name: stock_alert_service
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLCLIENTID}
            client-secret: ${GOOGLSECRET}
            scope:
              - email
              - profile

server:
  error:
    include-message: always

frontend:
  url: ${FRONTEND_URL}

aws:
  region: us-east-2

dynamodb:
  table-name: StockAlertsTable
  user-table-name: UsersTable
  region: us-east-2

finnhub:
  api-key: ${FINNHUB_API_KEY}
  base-url: https://finnhub.io/api/v1

scheduler:
  secret-key: ${SCHEDULER_SECRET_KEY}

jwt:
  secret: ${JWT_SECRET}
```

#### Run Backend

```bash
mvn spring-boot:run
```

### 3. Frontend Setup

```bash
cd ../react-frontend
npm install
npm run dev
```

Ensure your frontend `.env` has the correct backend URL:

```env
VITE_BACKEND_URL=http://localhost:8080
```

---

## 🧪 Scheduled Task

The backend checks all alerts every 5 minutes using an AWS EventBridge rule. This rule triggers an AWS Lambda function that makes an HTTP request to the backend's `/scheduler/run` endpoint.

You can also trigger this manually via:

```httphttp
POST /scheduler/run
```

---

## 📬 Email Alerts

- Users are subscribed to an SNS topic based on their email.
- Email verification is required before alerts are delivered.
- Notifications are only sent when the target price condition is met.

---

## 🐳 Deployment

### Backend — Elastic Beanstalk (Docker)

1. Create JAR file:

```bash
mvn clean package
```

2. Docker image built and deployed via AWS Elastic Beanstalk Console or CLI using `eb deploy`.

Ensure EB environment variables are set for:

- `GOOGLCLIENTID`
- `GOOGLSECRET`
- `FRONTEND_URL`
- `FINNHUB_API_KEY`
- `SCHEDULER_SECRET_KEY`
- `JWT_SECRET`

### Frontend — AWS S3 + CloudFront

1. Build the frontend:

```bash
npm run build
```

2. Upload the contents of the `dist` folder to your S3 bucket.

3. Enable static website hosting on the bucket and optionally connect it to a CloudFront distribution for CDN support.

Ensure your frontend `.env` is configured to point to the deployed backend URL.

---

## 🔐 Authentication Flow

1. User logs in using Google OAuth.
2. Backend retrieves user email via `@AuthenticationPrincipal`.
3. Alerts are stored and evaluated per authenticated user.
4. SNS topics are scoped per user to isolate email notifications.

---

## 👨‍💻 Author

**Adnan T.** — [@adnant1](https://github.com/adnant1)
