# Matrix Multiplication Benchmarking Project

This project aims to benchmark various matrix multiplication implementations in Java. Different parallelization techniques and strategies are explored to find the most efficient approach.

## Table of Contents

- [Introduction](#introduction)
- [Matrix Multiplication Implementations](#matrix-multiplication-implementations)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [Branches](#branches)
- [Contributing](#contributing)
- [License](#license)

## Introduction

Matrix multiplication is a fundamental operation in linear algebra and various algorithms and parallelization techniques can be applied to optimize its performance. This project investigates and benchmarks different matrix multiplication implementations to identify the most efficient one based on various criteria.

## Matrix Multiplication Implementations

The project includes several matrix multiplication implementations for both dense and sparse matrices:

- **Dense Matrix Multiplications:**
  - Atomic Multiplication (`DenseMatrixAtomicMultiplication.java`)
  - Parallel Multiplication (`DenseMatrixParallelMultiplication.java`)
  - Parallel Stream Multiplication (`DenseMatrixParallelStreamMultiplication.java`)
  - Parallel Synchronized Multiplication (`DenseMatrixParallelSynchronizedMultiplication.java`)
  - Semaphore Multiplication (`DenseMatrixSemaphoreMultiplication.java`)
  - ThreadPool Multiplication (`DenseMatrixThreadPoolMultiplication.java`)

- **Sparse Matrix Multiplications:**
  - Atomic Multiplication (`SparseMatrixAtomicMultiplication.java`)
  - Parallel Multiplication (`SparseMatrixParallelMultiplication.java`)
  - Parallel Synchronized Multiplication (`SparseMatrixParallelSynchronizedMultiplication.java`)
  - Semaphore Multiplication (`SparseMatrixSemaphoreMultiplication.java`)
  - ThreadPool Multiplication (`SparseMatrixThreadPoolMultiplication.java`)

## Project Structure

### Test-Driven Development (TDD)

**Definition:**
TDD is a software development approach where tests are written before the actual code is implemented. This process involves a cycle of writing a failing test, implementing the minimum amount of code to make the test pass, and then refactoring the code while ensuring that the test still passes. This cycle is often referred to as the "Red-Green-Refactor" cycle.

**Advantages:**
- Ensures a comprehensive suite of tests.
- Improves code quality and maintainability.
- Provides a safety net for making changes by quickly identifying regressions.

### SOLID Principles

#### Single Responsibility Principle (SRP)
A class should have only one reason to change, meaning that it should have only one responsibility or job.

#### Open/Closed Principle (OCP)
Software entities should be open for extension but closed for modification. This encourages the use of interfaces and abstract classes.

#### Liskov Substitution Principle (LSP)
Subtypes should be substitutable for their base types without altering the correctness of the program.

#### Interface Segregation Principle (ISP)
Clients should not be forced to depend on interfaces they do not use. This principle encourages creating smaller, specific interfaces.

#### Dependency Inversion Principle (DIP)
High-level modules should not depend on low-level modules. Both should depend on abstractions, and abstractions should not depend on details; details should depend on abstractions.

**Advantages:**
- Improves code maintainability, scalability, and flexibility.
- Facilitates easier code reuse and extension.
- Promotes a modular and clean code structure.
- Reduces the impact of changes and makes the codebase more resilient to modifications.
  
## How to Run

Execute the `BenchmarkRunner` class to obtain benchmark results tailored to your computer.


