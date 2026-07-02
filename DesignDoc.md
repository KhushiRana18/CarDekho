# CarDekho Take-Home Assessment — Explainable Car Recommendation Engine

## Objective

Build an explainable recommendation engine that helps a confused car buyer reach a confident shortlist.

The system is **not** a chatbot. Instead, it combines:
- Rule-based preference extraction
- Hard constraint filtering
- Personalized multi-criteria ranking using **TOPSIS**
- Semantic similarity matching
- Explainable recommendations with trade-offs for regret minimization

---

# High-Level Architecture

```text
                 User
                   │
        ┌──────────┴──────────┐
        │                     │
   Structured Form      Natural Language
        │                     │
        └──────────┬──────────┘
                   │
        Preference Extraction
                   │
        Buyer Preference Profile
                   │
      Dynamic Weight Generation
                   │
        Hard Constraint Filtering
                   │
         Candidate Car Set
        ┌──────────┴──────────┐
        │                     │
   TOPSIS Ranking      Semantic Similarity
        │                     │
        └──────────┬──────────┘
                   │
          Score Fusion Layer
                   │
        Top N Recommendations
                   │
 Explainability + Trade-offs Layer
```

---

# Tech Stack

## Frontend

- React
- TailwindCSS
- Axios

## Backend

- Spring Boot
- REST APIs

## Data

- Kaggle Used/New Car Dataset (CSV)
- Loaded into memory on startup

---

# Step 1 — Load Dataset

Load the dataset during application startup.

Each car should contain normalized structured information.

Example:

```json
{
  "id": 101,
  "brand": "Hyundai",
  "model": "Creta",
  "price": 1250000,
  "fuel": "Petrol",
  "transmission": "Automatic",
  "bodyType": "SUV",
  "mileage": 18.5,
  "power": 115,
  "engine": 1497,
  "bootSpace": 433,
  "groundClearance": 190,
  "safety": 4,
  "maintenanceScore": 8,
  "reliabilityScore": 9
}
```

---

# Step 2 — User Input

Support two input modes.

## Option A

Structured Form

Fields:

- Budget
- Fuel
- Transmission
- Body Type
- Family Size
- Primary Usage
- Priority Sliders

Example

```
Mileage       ████████
Safety        ██████
Performance   ██
Comfort       █████
Maintenance   ███████
```

---

## Option B

Natural Language Prompt

Example

```
Need an automatic SUV under 12 lakh.

Mostly city driving.

Don't care about performance.

Need good mileage and low maintenance.
```

---

# Step 3 — Preference Extraction

Convert both input modes into a common Buyer Profile.

Example

```json
{
  "budget": 1200000,
  "fuel": "Petrol",
  "transmission": "Automatic",
  "bodyType": "SUV",
  "familySize": 5,
  "usage": "City",
  "importance": {
    "mileage": 8,
    "maintenance": 7,
    "safety": 5,
    "comfort": 4,
    "performance": 1
  }
}
```

Natural language extraction can be implemented using:

- Regex
- Keyword matching
- Rule-based parser

No LLM required.

---

# Step 4 — Dynamic Weight Generation

Generate TOPSIS weights dynamically.

Formula

```
weight(feature)
=
importance(feature)
/ totalImportance
```

Example

```
Mileage        8
Safety         5
Maintenance    7
Comfort        4
Performance    1
```

↓

```
Mileage        0.32
Safety         0.20
Maintenance    0.28
Comfort        0.16
Performance    0.04
```

Every buyer gets a unique weight vector.

---

# Step 5 — Hard Constraint Filtering

Remove cars that cannot satisfy mandatory requirements.

Examples

- Price > Budget
- Wrong Fuel Type
- Wrong Transmission
- Wrong Body Type

Result

```
Original Dataset

↓

530 Cars

↓

Budget Filter

↓

220 Cars

↓

Transmission Filter

↓

96 Cars

↓

Body Type

↓

34 Cars
```

TOPSIS only ranks the remaining cars.

---

# Step 6 — Feature Normalization

Normalize every numerical feature.

Example

```
Mileage

12 kmpl

↓

0

30 kmpl

↓

1
```

Repeat for:

- Mileage
- Power
- Boot Space
- Safety
- Maintenance
- Reliability
- Ground Clearance

---

# Step 7 — TOPSIS Ranking

Use the normalized decision matrix.

Steps

1. Normalize decision matrix
2. Apply dynamic weights
3. Compute Positive Ideal Solution
4. Compute Negative Ideal Solution
5. Compute Euclidean distances
6. Calculate TOPSIS score

```
Score =
DistanceToWorst
/
(
DistanceToBest
+
DistanceToWorst
)
```

Rank cars in descending order.

---

# Step 8 — Semantic Similarity

TOPSIS captures structured preferences.

Semantic similarity captures qualitative intent.

Example user prompt

```
Need something comfortable and reliable.
```

Create a textual profile for every car.

Example

```
Hyundai Creta

Comfortable SUV

Reliable

Good resale

Large cabin

Automatic

Family friendly
```

Use TF-IDF vectorization.

Compute

```
Cosine Similarity

User Prompt

vs

Car Description
```

Result

```
Semantic Score

0–1
```

---

# Step 9 — Score Fusion

Combine objective ranking with semantic similarity.

```
Final Score

=

α × TOPSIS

+

β × Semantic Similarity
```

Example

```
Structured Form

95% TOPSIS

5% Semantic
```

Natural language prompt

```
70% TOPSIS

30% Semantic
```

Fusion weights are adaptive based on input modality.

---

# Step 10 — Recommendation Generation

Return Top 5 recommendations.

Example response

```json
{
  "car": "Hyundai Creta",
  "matchScore": 92,
  "topsisScore": 0.88,
  "semanticScore": 0.91
}
```

---

# Step 11 — Explainability Layer

For every recommendation generate:

## Why Recommended

Example

```
✓ Fits within budget

✓ Excellent mileage

✓ Automatic transmission

✓ High reliability

✓ Spacious cabin
```

---

## Trade-offs

Example

```
⚠ Slightly lower power

⚠ Average boot space

⚠ Higher maintenance than competitors
```

---

## Regret Minimization

Help users understand why one recommendation ranked above another.

Example

```
Compared to Honda City

+ Better mileage

+ Lower maintenance

- Slightly less engine power
```

This reduces uncertainty and increases trust.

---

# API Design

## POST

```
/recommend
```

Request

```json
{
  "budget": 1200000,
  "fuel": "Petrol",
  "transmission": "Automatic",
  "bodyType": "SUV",
  "prompt": "Need a reliable automatic SUV with good mileage."
}
```

Response

```json
{
  "buyerProfile": {},
  "recommendations": [
    {
      "car": "Hyundai Creta",
      "matchScore": 92,
      "pros": [],
      "cons": []
    }
  ]
}
```

---

# Project Structure

```
backend/

├── controller/
│
├── service/
│   ├── PreferenceParser
│   ├── WeightGenerator
│   ├── FilterService
│   ├── NormalizationService
│   ├── TopsisService
│   ├── SemanticSimilarityService
│   ├── ScoreFusionService
│   └── ExplainabilityService
│
├── model/
│
├── repository/
│
└── resources/
    └── cars.csv
```

---

# Future Improvements

- Learn user weights from historical interactions.
- Add brand affinity and resale value as explicit criteria.
- Replace TF-IDF with Sentence Transformers (`all-MiniLM-L6-v2`) for richer semantic matching.
- Incorporate user feedback ("Not interested", "Show more like this") to refine rankings over time.
- Add diversity-aware re-ranking to avoid recommending very similar cars.
- Include uncertainty/confidence estimates for each recommendation.

---

# Why This Design?

This architecture separates recommendation into three complementary stages:

1. **Hard Filters** ensure recommendations satisfy mandatory constraints.
2. **TOPSIS** performs personalized multi-criteria ranking using dynamically generated user preference weights.
3. **Semantic Similarity** captures qualitative intent expressed in natural language that may not be represented by structured attributes.

Finally, an **Explainability Layer** communicates the reasoning behind each recommendation, highlights trade-offs, and compares alternatives to minimize buyer regret and build trust in the recommendations.