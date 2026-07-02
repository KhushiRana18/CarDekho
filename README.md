# CarDekho Recommendation Engine (Hackathon Project)

## What we built (and why)
We built an **explainable car recommendation engine** that turns a buyer’s preferences (budget, fuel, transmission, body type + a short “prompt”) into a **ranked shortlist of cars** with **human-readable reasons** (pros/cons + match reasons).

The goal was to make trade-offs visible and help users quickly converge on a few good options instead of scrolling endlessly through listings.

## Brief solution approach (high level)
- **Load dataset**: CSV data is loaded in-memory on backend startup.
- **Preference intake**: frontend collects structured fields + a free-text prompt.
- **Filter first**: apply hard constraints (budget/fuel/transmission/body type) to avoid irrelevant results.
- **Score & rank**:
  - compute a **TOPSIS-inspired** multi-criteria score from the structured attributes
  - add a **lightweight prompt similarity** score
  - combine them into a final match score
- **Explainability**: return “why this car” bullets and pros/cons so the ranking isn’t a black box.

## What we deliberately cut
- **No LLM-based parsing / advanced context understanding**: we avoided full natural-language preference extraction and conversational refinement due to **time + cost constraints**.
- **No embeddings/LLM-powered RAG pipeline**: we kept similarity lightweight instead of production-grade semantic retrieval.
- **No dataset curation pipeline**: we did not build a robust cleaning/merging process for multiple sources.

## Tech stack (and why)
- **Frontend**: React + Vite  
  - picked for **ease of understanding**, fast iteration, and quick UI prototyping
- **Backend**: Java + Spring Boot  
  - picked for **familiarity** and fast API development
- **Serving/Deployment**: Docker (single image)  
  - easiest way to share a consistent demo environment for a hackathon
- **Data**: CSV loaded into memory  
  - simple, fast to iterate, no DB setup needed for demo

## What we delegated to AI tools vs. did manually
- **AI-assisted**
  - the **initial design document** was generated with ChatGPT, but **guided and constrained by us**
  - we produced a **work breakdown** from that design
  - we then implemented features **sequentially using GitHub Copilot** (component scaffolding, API wiring, iteration on logic)
- **Manual**
  - defining the overall problem framing, scope cuts, and trade-offs
  - deciding the scoring/explainability approach and what’s “good enough” for a hackathon demo
  - integrating pieces end-to-end and validating the UX/data flow

## Where AI tools helped most
- **Speed**: quickly generating boilerplate (React components, DTO-like payload shapes, service scaffolding).
- **Iteration**: accelerating small refactors and “try this variant” changes during integration.
- **Unblocking**: turning the work breakdown into working code without getting stuck on syntax/mechanics.

## Where they got in the way
- **Overconfidence**: sometimes suggested code that compiled but didn’t match the intended runtime behavior.
- **Integration edges**: the hardest parts were still glue code, routing, and end-to-end correctness (which required manual verification).
- **Scope pressure**: it’s easy to generate “more features” than a hackathon timeline allows—so we had to actively prune.

## If we had another 4 hours, what we’d add
- **LLM-based preference parsing** (robustly extract constraints and priorities from natural language).
- **Better context understanding** (follow-up questions, ambiguity resolution, conversational refinement).
- **Stronger UI/UX** (clearer comparison, sorting controls, richer drill-down).
- **Dataset work**: clean/merge multiple public datasets to create a **customized, higher-quality dataset** with consistent fields and fewer missing values.

## API
- **GET** `/cars`
- **POST** `/recommend`

Example request:

```json
{
  "budget": 1200000,
  "fuel": "Petrol",
  "transmission": "Automatic",
  "bodyType": "SUV",
  "prompt": "Need a reliable automatic SUV with good mileage."
}
```

## Run locally (dev)
### Backend

```bash
cd Backend
mvn spring-boot:run
```

### Frontend

```bash
cd Frontend
npm install
npm run dev
```

## Run with Docker (shareable)
We publish a single container image that runs **both frontend + backend**.

```bash
docker run --rm -p 8081:80 khushirana8600/cardekho:1.0
```

- The app will be available at **`http://localhost:8081`**
- You can change the port by changing the left side of `-p`, for example:
  - `-p 3000:80` → open at `http://localhost:3000`


