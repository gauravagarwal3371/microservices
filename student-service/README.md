# Student Management System

## This is a RESTful API for managing students in a school system.

### Endpoints

#### Students

##### Add a Student

- **URL**: POST /students/student
- **Description**: Add a new student to the system.
- **Request Body**:
  ```json
  {
    "name": "John Doe",
    "age": 18,
    "grade": 12,
    "school": "Example High School"
  }
- **Response**: 
- **Status Code**:201 Created 
- **Body**:
    ```json
  {
  "id": 1,
  "name": "John Doe",
  "age": 18,
  "grade": 12,
  "school": "Example High School"
  }
#####  Get All Students
- **URL**: GET /students
- **Description**: Get a list of all students in the system.


- **Response**:
- **Status Code**:200 OK
- **Body**:
    ```json
  [
  {
    "id": 1,
    "name": "John Doe",
    "age": 18,
    "grade": 12,
    "school": "Example High School"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "age": 17,
    "grade": 11,
    "school": "Example High School"
  },
  ...
  ]

#####  Get Student by ID
- **URL**: GET /students/student/{id}
- **Description**: Get a specific student by their ID.
- **Path Parameter**: id - The ID of the student.
  

- **Response**:
- **Status Code**:200 OK
- **Body**:
    ```json
  {
  "id": 1,
  "name": "John Doe",
  "age": 18,
  "grade": 12,
  "school": "Example High School"
  }


#####  Update Student by ID
- **URL**: PUT /students/student/{id}
- **Description**: Update an existing student.
- **Path Parameter**: id - The ID of the student.
  ```json
  {
  "name": "Updated Name",
  "age": 19,
  "grade": 13,
  "school": "Updated School"
  }
  
- **Response**:
- **Status Code**:200 OK
- **Body**:
    ```json
  {
  "id": 1,
  "name": "Updated Name",
  "age": 19,
  "grade": 13,
  "school": "Updated School"
  }

#####  Delete Student by ID
- **URL**: DELETE /students/student/{id}
- **Description**: Delete a student from the system.
- **Path Parameter**: id - The ID of the student.


- **Response**:
- **Status Code**:204 NO CONTENT

  
