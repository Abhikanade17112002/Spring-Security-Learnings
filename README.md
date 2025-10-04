# Hospital Management System

A comprehensive Spring Boot application for managing hospital operations with secure authentication and role-based access control.

## Features

### Core Functionality
- **Patient Management**: Complete CRUD operations for patient records with insurance information
- **Doctor Management**: Manage doctor profiles and assignments
- **Department Management**: Organize departments with head doctors
- **Appointment System**: Schedule and manage patient appointments
- **Insurance Management**: Track patient insurance policies

### Security Features
- **JWT Authentication**: Secure token-based authentication
- **OAuth2 Integration**: Modern authentication protocol support
- **Role-Based Access Control (RBAC)**: Granular permission management
- **Custom Authorities**: Fine-grained access control with custom permissions

## Technology Stack

- **Framework**: Spring Boot
- **Security**: Spring Security with JWT & OAuth2
- **Database**: JPA/Hibernate (MySQL/PostgreSQL)
- **Authentication**: JWT Tokens
- **Authorization**: RBAC with custom authorities
- **API Testing**: Postman

## API Endpoints

### Public Endpoints (No Authentication Required)

#### Authentication
```
POST /api/v1/auth/register - Register new user
POST /api/v1/auth/login - User login
GET  /api/v1/public/doctors - View all doctors
```

### User Endpoints (Requires Authentication)

#### Patients
```
GET    /api/patients - Get all patients
POST   /api/patients - Create new patient
PATCH  /api/patients/assigninsurance/{id} - Assign insurance to patient
DELETE /api/patients/deleteinsurance - Remove patient insurance
DELETE /api/patients/deleteappointment - Cancel appointment
```

#### Doctors
```
GET  /api/doctors - Get all doctors
POST /api/doctors - Add new doctor
```

#### Departments
```
GET   /api/departments - Get all departments
POST  /api/departments - Create new department
PATCH /api/departments/addheaddoctor - Assign head doctor
POST  /api/departments/adddepartmentdoctor - Add doctor to department
```

#### Appointments
```
GET  /api/appointments - Get all appointments
POST /api/appointments - Schedule new appointment
```

### Admin Endpoints (Admin Role Required)

```
GET  /api/v1/admin/patients - Get all patients (admin)
POST /api/v1/admin/addauthority - Create new authority
POST /api/v1/admin/addrole - Create new role
GET  /api/v1/admin/authorities - List all authorities
GET  /api/v1/admin/roles - List all roles
```

## Authentication Flow

### 1. User Registration
```json
POST /api/v1/auth/register
{
  "firstName": "John",
  "lastName": "Doe",
  "emailId": "john.doe@example.com",
  "password": "password123",
  "roles": ["ROLE_USER"]
}
```

### 2. User Login
```json
POST /api/v1/auth/login
{
  "userName": "John.Doe@1234",
  "password": "password123"
}
```

**Response**: JWT token valid for 5 minutes

## Security Architecture

### Authentication Flow

```
User Registration → Auto-generate Username → Hash Password → Assign Default Role
                                                                      ↓
User Login → Validate Credentials → Generate JWT Token (5 min expiry)
                                                ↓
Protected Endpoint Access → Validate JWT → Check User Roles → Check Authorities → Grant/Deny Access
```

### JWT Token Details

- **Algorithm**: HS256 (HMAC with SHA-256)
- **Expiration**: 5 minutes (300 seconds)
- **Payload includes**:
    - `sub`: Username
    - `userId`: User ID
    - `iat`: Issued at timestamp
    - `exp`: Expiration timestamp

### Username Generation

The system automatically generates unique usernames in the format:
```
FirstName.LastName@RandomNumber
Example: Madhuri.Kanade@8816
```

### Password Security

- Passwords are hashed using BCrypt
- Original passwords are never stored
- Minimum password requirements can be configured

### Authorization Hierarchy

```
User
  └─ Roles (Many-to-Many)
      └─ Authorities (Many-to-Many)
          └─ Permissions (e.g., USER_READ, USER_WRITE)
```

Example:
- **User**: Madhuri Kanade
    - **Role**: ROLE_USER
        - **Authority**: USER_READ

## Role-Based Access Control

### Default Roles
- **ROLE_USER**: Standard user access
- **ROLE_ADMIN**: Administrative privileges

### Custom Authorities
Authorities provide granular permissions such as:
- `USER_READ`
- `USER_WRITE`
- `USER_DELETE`
- `PATIENT_MANAGE`
- `DOCTOR_MANAGE`

### Creating Roles with Authorities
```json
POST /api/v1/admin/addrole
{
  "roleName": "ROLE_USER",
  "authorityIds": [
    "authority-uuid-1",
    "authority-uuid-2"
  ]
}
```

## Database Schema

![Database ERD](path/to/database-schema.png)

The application uses a relational database with the following entities:

### Patient
- `id` (BIGINT, Primary Key)
- `name` (VARCHAR(40))
- `birth_date` (DATE)
- `gender` (VARCHAR(255))
- `blood_group` (ENUM)
- `email` (VARCHAR(255))
- `created_at` (DATETIME(6))
- `patient_insurance_id` (BIGINT, Foreign Key)

### Doctor
- `id` (BIGINT, Primary Key)
- `name` (VARCHAR(100))
- `email` (VARCHAR(100))
- `specialization` (VARCHAR(100))

### Department
- `id` (BIGINT, Primary Key)
- `name` (VARCHAR(100))
- `head_doctor_id` (BIGINT, Foreign Key)

### Appointment
- `id` (BIGINT, Primary Key)
- `appointment_time` (DATETIME(6))
- `reason` (VARCHAR(500))
- `doctor_id` (BIGINT, Foreign Key)
- `patient_id` (BIGINT, Foreign Key)

### Insurance
- `id` (BIGINT, Primary Key)
- `policy_number` (VARCHAR(50))
- `provider` (VARCHAR(100))
- `valid_until` (DATE)
- `created_at` (DATETIME(6))

### User (Authentication)
- `user_id` (VARCHAR(255), Primary Key)
- `email_id` (VARCHAR(255))
- `first_name` (VARCHAR(255))
- `last_name` (VARCHAR(255))
- `user_name` (VARCHAR(255))
- `password` (VARCHAR(255))
- `provider_id` (VARCHAR(255))
- `provider_type` (ENUM)

### Roles
- `role_id` (VARCHAR(255), Primary Key)
- `role_name` (VARCHAR(255))

### Authorities
- `authorities_id` (VARCHAR(255), Primary Key)
- `authority_name` (VARCHAR(255))

### Mapping Tables
- **user_roles_mapping**: Links users to their roles
- **role_authority_mapping**: Links roles to their authorities
- **my_dpt_doctors**: Links departments to their doctors

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- MySQL/PostgreSQL database

### Configuration
1. Clone the repository
```bash
git clone <repository-url>
cd hospital-management-system
```

2. Configure database connection in `application.properties`:
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Configuration
jwt.secret=your-secret-key-min-256-bits
jwt.expiration=300000

# OAuth2 Configuration (if using)
spring.security.oauth2.client.registration.google.client-id=your-client-id
spring.security.oauth2.client.registration.google.client-secret=your-client-secret
```

3. Create the database:
```sql
CREATE DATABASE hospital_db;
```

### Running the Application
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Or run the JAR file
java -jar target/hospital-management-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

### Initial Setup

1. **Create Admin User**: Use the register endpoint to create the first admin user
2. **Create Authorities**: Add necessary authorities using admin endpoint
3. **Create Roles**: Define roles and assign authorities
4. **Assign Roles**: Assign appropriate roles to users

### Environment Variables (Production)

```bash
export JWT_SECRET=your-production-secret-key
export DB_URL=jdbc:mysql://production-db:3306/hospital_db
export DB_USERNAME=prod_user
export DB_PASSWORD=prod_password
```

## API Documentation & Testing

### Postman Screenshots

#### 1. Get All Patients (Admin Endpoint)
![Get All Patients](path/to/get-all-patients.png)

**Endpoint**: `GET /api/v1/admin/patients`

**Authorization**: Bearer Token (JWT)

**Response**: Returns a list of all patients with their details including ID, name, gender, birth date, and blood group.

#### 2. Register User
![Register User](path/to/register-user.png)

**Endpoint**: `POST /api/v1/auth/register`

**Request Body**:
```json
{
  "firstName": "Madhuri",
  "lastName": "Kanade",
  "emailId": "madhuri.kanade@example.com",
  "password": "password123",
  "roles": ["ROLE_USER"]
}
```

**Response**: Returns the created user with auto-generated username, hashed password, and assigned role with authorities.

#### 3. User Login
![User Login](path/to/login-user.png)

**Endpoint**: `POST /api/v1/auth/login`

**Request Body**:
```json
{
  "userName": "Madhuri.Kanade@8816",
  "password": "password123"
}
```

**Response**: Returns JWT token valid for 5 minutes:
```json
{
  "userName": "Madhuri.Kanade@8816",
  "jwtToken": "Bearer eyJhbGciOiJIUzI1NiJ9..."
}
```

#### 4. Get Admin Patients (Protected Route)
![Get Admin Patients](path/to/get-admin-patients.png)

**Endpoint**: `GET /api/v1/admin/patients`

**Authorization**: Bearer Token (Required)

**Headers**:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Response**: Returns patient list only if user has admin role and valid JWT token.

### Testing Workflow

1. **Register a new user** using the `/api/v1/auth/register` endpoint
2. **Login** with the generated username to receive JWT token
3. **Copy the JWT token** from the login response
4. **Add the token** to the Authorization header as `Bearer <token>`
5. **Test protected endpoints** with the authenticated token
6. **Token expires** after 5 minutes - login again to get a new token

### Response Status Codes

- `200 OK`: Request successful
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Missing or invalid JWT token
- `403 Forbidden`: User lacks required permissions
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## Security Notes

- **JWT Token Expiration**: Tokens expire after 5 minutes for enhanced security
- **Password Encryption**: All passwords are hashed using BCrypt algorithm
- **OAuth2 Support**: Additional authentication provider integration available
- **RBAC Implementation**: Fine-grained access control through roles and authorities
- **Admin Endpoints**: Protected by role-based authorization
- **Public Endpoints**: Limited to authentication and basic information retrieval
- **Rate Limiting**: Recommended for production to prevent abuse
- **HTTPS**: Always use HTTPS in production environments
- **Token Storage**: Never store JWT tokens in localStorage (use httpOnly cookies in production)

### Security Best Practices

1. **Environment Variables**: Store sensitive data (JWT secret, DB credentials) in environment variables
2. **Token Refresh**: Implement refresh token mechanism for production
3. **Password Policy**: Enforce strong password requirements
4. **Account Lockout**: Implement account lockout after multiple failed login attempts
5. **Audit Logging**: Log all authentication and authorization events
6. **CORS Configuration**: Configure proper CORS policies for production
7. **SQL Injection Prevention**: Use parameterized queries (handled by JPA)
8. **XSS Protection**: Validate and sanitize all user inputs

## Error Handling

The API returns standard HTTP status codes:
- `200 OK`: Successful request
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Authentication required
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License.

## Contact

For questions or support, please contact the development team.

## Screenshots

### Application Screenshots

Below are screenshots demonstrating the key features and API endpoints:

#### 1. Get All Patients Endpoint
![Get All Patients](screenshots/get-all-patients.png)
*Successfully retrieves list of all patients with their details*

#### 2. User Registration
![Register User](screenshots/register-user.png)
*New user registration with auto-generated username and role assignment*

#### 3. User Authentication
![User Login](screenshots/login-user.png)
*Login endpoint returning JWT token for authenticated access*

#### 4. Protected Admin Endpoint
![Admin Patients](screenshots/admin-patients.png)
*Admin-only endpoint requiring valid JWT token and admin role*

#### 5. Database Schema
![Database ERD](screenshots/database-schema.png)
*Entity-Relationship Diagram showing database structure and relationships*

### Key Features Demonstrated

- **Token-based Authentication**: JWT tokens generated on successful login
- **Role-based Authorization**: Different access levels for users and admins
- **Secure Endpoints**: Protected routes require valid authentication
- **User Management**: Complete user registration and authentication flow
- **Database Relations**: Normalized database design with proper foreign key constraints

---

**Note**: This is a development version. Ensure proper security measures are implemented before deploying to production.