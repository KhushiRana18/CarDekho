# Implementation Task Breakdown

## Phase 0 — Project Setup

### Task 0.1
- Create Spring Boot project
- Create React app
- Configure CORS
- Verify frontend ↔ backend communication

**Deliverable**
- Clicking a button on React fetches data from Spring Boot.

---

## Phase 1 — Dataset Layer

### Task 1.1
- Download Kaggle dataset
- Clean unnecessary columns
- Add missing derived columns if required
    - Safety Score
    - Maintenance Score
    - Reliability Score

### Task 1.2
- Create Car model

Example

```java
Car
- id
- brand
- model
- price
- fuel
- transmission
- bodyType
- mileage
- engine
- power
- seats
- bootSpace
- safetyScore
- maintenanceScore
- reliabilityScore
```

### Task 1.3
- Load CSV on application startup
- Store cars in memory

**Deliverable**
- GET /cars returns all cars.

---

# Phase 2 — User Input

### Task 2.1

Create recommendation form

Fields

- Budget
- Fuel
- Transmission
- Body Type
- Family Size
- Usage

### Task 2.2

Priority sliders

Example

```
Mileage
██████

Safety
████

Performance
██

Comfort
██████

Maintenance
█████
```

### Task 2.3

Optional text prompt

```
Need a comfortable SUV
Mostly city driving
```

**Deliverable**

React sends request to backend.

---

# Phase 3 — Buyer Profile

### Task 3.1

Create BuyerProfile class

```java
BuyerProfile
```

Contains

```
budget

fuel

transmission

bodyType

importanceMap
```

### Task 3.2

Implement parser

If form

↓

Direct mapping

If prompt

↓

Regex

↓

Keyword mapping

↓

BuyerProfile

**Deliverable**

Input becomes structured profile.

---

# Phase 4 — Dynamic Weight Generator

### Task 4.1

Convert importance values into weights.

Example

```
Mileage

8

Safety

5

Maintenance

7
```

↓

```
Mileage

0.40

Safety

0.25

Maintenance

0.35
```

### Task 4.2

Return weight map

```java
Map<String, Double>
```

**Deliverable**

Every user gets personalized weights.

---

# Phase 5 — Hard Filtering

### Task 5.1

Implement budget filter

### Task 5.2

Fuel filter

### Task 5.3

Transmission filter

### Task 5.4

Body type filter

### Task 5.5

Return candidate cars

**Deliverable**

530 cars

↓

37 cars

---

# Phase 6 — Feature Normalization

### Task 6.1

Normalize every criterion.

Features

- Price
- Mileage
- Power
- Safety
- Maintenance
- Reliability
- Boot Space

### Task 6.2

Store normalized matrix

**Deliverable**

Decision matrix ready for TOPSIS.

---

# Phase 7 — TOPSIS Engine

### Task 7.1

Create decision matrix

### Task 7.2

Normalize matrix

### Task 7.3

Apply dynamic weights

### Task 7.4

Compute

Positive Ideal Solution

### Task 7.5

Compute

Negative Ideal Solution

### Task 7.6

Compute Euclidean distances

### Task 7.7

Calculate TOPSIS score

### Task 7.8

Sort descending

**Deliverable**

Cars ranked by TOPSIS.

---

# Phase 8 — Semantic Similarity

### Task 8.1

Generate textual description for every car.

Example

```
Automatic SUV

Comfortable

Reliable

Good mileage

Family car
```

### Task 8.2

Create TF-IDF vectors

### Task 8.3

Convert user prompt into TF-IDF vector

### Task 8.4

Compute cosine similarity

```
User

vs

Every Car
```

### Task 8.5

Store semantic score

**Deliverable**

Every candidate gets semantic similarity score.

---

# Phase 9 — Score Fusion

### Task 9.1

Determine input type

Form only

↓

95% TOPSIS

Prompt present

↓

70% TOPSIS

30% Semantic

### Task 9.2

Compute

```
Final Score

=

α × TOPSIS

+

β × Semantic
```

### Task 9.3

Sort again

**Deliverable**

Final ranked recommendations.

---

# Phase 10 — Explainability

### Task 10.1

Generate strengths

Example

```
Excellent mileage

Fits budget

Automatic

High safety
```

### Task 10.2

Generate weaknesses

```
Average performance

Small boot

High maintenance
```

### Task 10.3

Compare against next ranked car

Generate

```
Why this ranked higher

+

Trade-offs
```

**Deliverable**

Pros and cons for every recommendation.

---

# Phase 11 — REST API

### Task 11.1

POST

```
/recommend
```

Returns

```
Buyer Profile

↓

Recommendations

↓

Scores

↓

Pros

↓

Cons
```

### Task 11.2

Response DTO

```json
{
  "buyerProfile": {},
  "recommendations": []
}
```

---

# Phase 12 — Frontend

### Task 12.1

Recommendation cards

Display

- Match Score
- Car Name
- Price
- Fuel
- Transmission

### Task 12.2

Pros section

```
✓ Excellent mileage

✓ Spacious

✓ Reliable
```

### Task 12.3

Trade-offs

```
⚠ Average performance

⚠ Slightly expensive
```

### Task 12.4

Comparison modal

Compare

Recommendation #1

vs

Recommendation #2

---

# Phase 13 — README

Include

## Problem Statement

Explain buyer confusion.

## Approach

- Hard filtering
- Dynamic preference extraction
- TOPSIS
- Semantic similarity
- Explainability

## Architecture Diagram

## Recommendation Pipeline

## API Documentation

## Future Improvements

- Feedback loop
- Learning weights
- Transformer embeddings
- Collaborative filtering

---

# Suggested Git Commit Plan

1. Initial Spring Boot + React setup
2. Load and expose car dataset
3. Build recommendation form
4. Parse buyer profile
5. Implement hard filters
6. Add normalization service
7. Implement TOPSIS ranking
8. Add semantic similarity
9. Fuse TOPSIS + semantic scores
10. Build explainability layer
11. Integrate frontend recommendations
12. Polish UI and documentation