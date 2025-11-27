# LAB_ASSIGNMENT 3  
## 📘 Student Management System – Exception Handling, Multithreading & Wrapper Classes  


This assignment enhances the existing **Student Management System** by integrating **Exception Handling**, **Multithreading**, and **Wrapper Classes** to ensure safe, robust, and responsive execution.

The system validates user inputs, throws custom exceptions when required, and simulates loading using multithreading for a better user experience. The use of wrapper classes enables type conversion and autoboxing wherever applicable.

---

## 🎯 Problem Statement

Enhance the Student Management System by:

- Implementing **exception handling** using `try-catch-finally` and **custom exceptions** such as `StudentNotFoundException`.
- Using **multithreading** to simulate loading when adding or saving student data.
- Implementing **wrapper classes** (`Integer`, `Double`) for safe data conversion and autoboxing.
- Ensuring the program remains **responsive** and handles invalid input (blank fields, marks out of range, etc.).

---

## 🧭 Objective

To **handle runtime exceptions**, use **multithreading**, and apply **wrapper classes** for efficient and safe student data management.

---

## 📝 Learning Outcomes

After completing this assignment, students will be able to:

1. Implement `try-catch-finally` for runtime exception handling.  
2. Use **multithreading** to simulate delays and maintain responsiveness.  
3. Work with wrapper classes (`Integer`, `Double`) and demonstrate **autoboxing/unboxing**.  

---

## 🧩 Class Hierarchy & Data Types

### **Class Hierarchy**
1. **StudentManager** – Implements `RecordActions`  
2. **Loader** – Implements `Runnable` for multithreading/loading simulation  
3. **StudentNotFoundException** – Custom exception class  

### **Data Types Used**
- `Integer`, `Double` – Wrapper classes for numeric data  
- `Thread` – For multithreading  
- Primitive ↔ Wrapper conversions (Autoboxing/Unboxing)  

---

## 🛠️ Detailed Instructions

### **1. Exception Handling**
- Validate user inputs (marks, name, course, empty fields).
- Throw custom exceptions like `StudentNotFoundException`.
- Use `try-catch-finally` for safe program execution.

### **2. Multithreading**
- Create a `Loader` class implementing `Runnable`.
- Simulate loading using:
  ```java
  Thread t = new Thread(new Loader());
  t.start();
