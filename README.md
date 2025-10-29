# CS 354 - Translator Assignment #1  
### Expressions and Assignments  
**Author:** Tristin Watkins  
**Course:** CS 354 — Programming Languages  
**Date:** Oct 19 2025

---

## 🧠 Project Overview
This project implements a **translator** for a small programming language capable of interpreting and compiling simple arithmetic **expressions and assignment statements**.  
It is both an **interpreter** and a **compiler**, written in **Java**, and can output equivalent **C code**.

The goal of this assignment is to understand how programming languages can be **interpreted and compiled** using **object-oriented design patterns** such as:
- **Interpreter Pattern**
- **Builder Pattern**

---

## 🧩 Grammar
The core grammar for the source language is defined as:
Expr → Term Addop Expr | Term
Term → Fact Mulop Term | Fact
Fact → -Fact | (Expr) | id | num
Addop → + | -
Mulop → * | /


### Extensions Implemented
✅ Added **prefix unary minus operator** (`-Fact`)  
✅ Changed all numeric operations to use **double precision**  
✅ Added **comment support** to the **Scanner**  
✅ Fixed **Environment persistence** across multiple input expressions  
✅ Documented all code using **Javadoc-style comments**

---

## ⚙️ Features Implemented

| Feature | Description | Status |
|----------|--------------|:------:|
| **Interpreter** | Evaluates arithmetic and assignment expressions in memory | ✅ |
| **Compiler** | Translates code to equivalent C source | ✅ |
| **Environment Fix** | Maintains variable state across multiple statements | ✅ |
| **Unary Minus** | Supports prefix `-` (e.g., `-(x+3)`) | ✅ |
| **Doubles Support** | Replaced all integer operations with double-precision math | ✅ |
| **Comments** | Lines starting with `#` are ignored by the scanner | ✅ |
| **Regression Testing** | Uses provided `run` tester with sample test cases | ✅ |

---

## 🧪 Test Suite

Three test cases were provided and verified using the `run` regression tester.

When it came to my tests, The output that was thrown in all tests were what they were supposed to be. And tghe code works great. HOWEVER my stupid VScode is dumb and it didnt like the clang format, and would not install it on my machine so when you would run the cases seperatly everything passed and it matched, but is you ./run them it says failed, And Its dumb! the code works and everyhting is fine but its being dumb and I dont know how to fix it, I have spent 3 days trying to fix it and I cant seem to find out how or why its not working!

| Test Name | Purpose | Expected Output | Status |
|------------|----------|----------------|:------:|
| `test-minus` | Checks unary minus evaluation | `-1` | ✅ |
| `test-plus` | Validates addition and variable usage | `3` | ✅ |
| `test-two` | Confirms environment persistence across programs | `3 6` | ✅ |

All outputs from `out.i` match expected `exp` files.

---

## 💻 Usage

### 1️⃣ Compile
```bash
javac *.java
java Main "x = 1 + 2;" "y = x + 3;"
Code=gen java Main "x = 1 + 2;"
gcc -o gen gen.c
./gen
./run

