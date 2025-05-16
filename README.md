# USSD Event Processor - Query API Service

This application provides a REST API for querying USSD event records stored in a PostgreSQL database.

## Prerequisites

- Java 21
- PostgreSQL database (can be run via Docker)
- IntelliJ IDEA or VS Code with Spring Boot plugins/extensions

## Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/Phinart98/query-api-service.git
cd query-api-service
```


### 2. Database Setup

This application is designed to connect to the same PostgreSQL database used by the [File Loader Service](https://github.com/Phinart98/file-loader-service.git).
You can check that project for instructions on how to set up the database.


### 3. Run the Application

Open the project in your IDE (IntelliJ IDEA or VS Code) and run the main application class: `QueryApiServiceApplication`

The application will start on port 8081 and connect to the PostgreSQL database.

### 4. Using the API

The API provides a single POST endpoint for querying records:

**Endpoint:** `POST /api/query`

**Request Body:**

{
"recordDateStart": "2023-08-18 10:30:00",
"recordDateEnd": "2023-08-18 10:31:00",
"msisdn": "573228550000",  // Optional
"imsi": "1234567890"       // Optional
}


**Required Parameters:**
- `recordDateStart`: Start of the date range (format: yyyy-MM-dd HH:mm:ss)
- `recordDateEnd`: End of the date range (format: yyyy-MM-dd HH:mm:ss)

**Optional Parameters:**
- `msisdn`: Filter by MSISDN
- `imsi`: Filter by IMSI

**Response:**

[
{
"recordDate": "2023-08-18 10:30:15",
"msisdn": "573228550000",
"imsi": "732101647793504"
},
...
]


### 5. Example Queries

**Date range only:**

{
"recordDateStart": "2023-08-18 10:30:00",
"recordDateEnd": "2023-08-18 10:31:00"
}


**Date range + MSISDN:**

{
"recordDateStart": "2023-08-18 10:30:00",
"recordDateEnd": "2023-08-18 10:31:00",
"msisdn": "573228550000"
}


**Date range + IMSI:**

{
"recordDateStart": "2023-08-18 10:30:00",
"recordDateEnd": "2023-08-18 10:31:00",
"imsi": "1234567890"
}


**Date range + MSISDN + IMSI:**

{
"recordDateStart": "2023-08-18 10:30:00",
"recordDateEnd": "2023-08-18 10:31:00",
"msisdn": "573228550000",
"imsi": "1234567890"
}


## Configuration

Key settings in `application.properties`:
- `server.port`: The port the application runs on (default: 8081)
- Database connection settings (configured for PostgreSQL on localhost:5432)

## Notes

- This application is designed to work alongside the File Loader Service, which populates the database with USSD event records.
- The application uses JPA with Hibernate in validate mode to ensure compatibility with the existing database schema.