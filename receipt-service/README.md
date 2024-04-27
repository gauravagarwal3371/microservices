# Receipt Management System

## Introduction

The Receipt Collection Management System is a RESTful API designed to manage receipt for students in a school system. It provides endpoints to generate receipts, and retrieve receipt data.

## Endpoints

### Receipt

#### Generate Receipt

- **URL:** GET receipts/student/{studentId}
- **Description:** Get receipt based on student Id
- **Path Parameter**: id - The ID of the student.

- **Response:**
- Status Code: 200 OK
- Body: Receipt details
  ```json
  {
  "studentId": "1",
  "name": "JOHN DOE",
  "grade": "4",
  "school": "Test High School",
  "reference": "064d3d97-882b-45bc-81d7-4751c61a8e5f",
  "amountPaid": 100.5,
  "timestamp": "2024-04-27T15:11:35.346938",
  "cardNumber": "1234",
  "cardType": "visa"
  }

- **URL:** GET receipts/reference/{referenceNo}
- **Description:** Get receipt based on referenceNo
- **Path Parameter**: id - The referenceNo of the receipt.

- **Response:**
- Status Code: 200 OK
- Body: Receipt details
  ```json
  {
  "studentId": "1",
  "name": "JOHN DOE",
  "grade": "4",
  "school": "Test High School",
  "reference": "064d3d97-882b-45bc-81d7-4751c61a8e5f",
  "amountPaid": 100.5,
  "timestamp": "2024-04-27T15:11:35.346938",
  "cardNumber": "1234",
  "cardType": "visa"
  }