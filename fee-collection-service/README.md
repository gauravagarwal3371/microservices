# Fee Collection Management System

## Introduction

The Fee Collection Management System is a RESTful API designed to manage fee collection for students in a school system. It provides endpoints to collect fees, generate receipts, and retrieve fee collection data.

## Endpoints

### Fee Collection

#### Collect Fee and Generate Receipt

- **URL:** POST /fees/collect
- **Description:** Collect fee for a student and generate a receipt.
- **Request Body:**
  ```json
  {
    "studentId": "1",
    "amountPaid": 100.5,
    "cardType": "visa",
    "cardNumber": "1234"
  }

- **Response:**
- Status Code: 200 OK
- Body: Receipt details
  ```json
  {
  "studentId": "1",
  "name": "JOHN DOE",
  "grade": "4",
  "school": "Test High School",
  "reference": "0812cefb-7209-4e52-b161-646f94a695b9",
  "amountPaid": 100.5,
  "timestamp": "2024-04-27T14:52:49.586774",
  "cardNumber": "1234",
  "cardType": "visa"
  }

#### Get Collected Fee by Student ID

- **URL:** GET /fees/{studentId}
- **Description:** Get collected fee details for a specific student.
- **Path Parameter:** studentId - The ID of the student.
- 
- **Response:**
- Status Code: 200 OK
- Body: List of fee collection details for the student
```json
  [
  {
    "studentId": "1",
    "amountPaid": 100.5,
    "timestamp": "2024-04-27T14:52:41.224702",
    "cardType": "visa",
    "cardNumber": "1234"
  }
  ]