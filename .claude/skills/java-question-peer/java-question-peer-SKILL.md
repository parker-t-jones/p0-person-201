---
name: java-question-peer
description: >
  Generates one question per level (surface, medium, deep) for a student to use in a
  weekly peer discussion section. Each student runs this independently; randomness ensures
  variety across a group. Use this skill when a user asks for "my discussion questions",
  "questions for my group", "peer session questions", or "one question per level".
  Also trigger when context suggests a student running the skill for a lab or discussion
  section, especially with mentions of groups, turns, or a TA. This skill is designed
  for repeated weekly use across different assignments — the format stays consistent so
  students and TAs know what to expect each session. Do NOT use this skill for
  instructor-facing question sets with full answer keys — use java-question-gen for that.
---

# Java Project Peer Discussion Question Generator

## Purpose

Generate **one question per level** (surface, medium, deep) for a single student
to use in a small-group peer discussion session. Each student runs this independently;
randomness across runs ensures groups get variety across question sets with some
acceptable overlap.

The student:
- Receives all three questions and answers at once
- Poses one question per round (assigned by rotation: surface → medium → deep)
- Guides group discussion using only their answer for that question
- Can re-run for a fresh set if the session continues to a second round

---

## Step-by-Step Process

### 1. Inventory the Project

Read the README.md (if present) and every `.java` file in `src/`. Build a mental model:

- **Problem domain**: What real-world thing is this about?
- **Key classes**: What are the main classes and what do they represent?
- **Core algorithms**: What does the code *compute*?
- **I/O pattern**: Where does data come from and where does it go?
- **Design patterns**: Any notable structure? (utility classes, separation of concerns, etc.)

### 2. Build a Question Pool

Internally generate a pool of **at least 3 candidates per level** before selecting.
This is the randomness mechanism — draw one from each pool rather than always
generating the "most obvious" question for each level.

Vary candidates across:
- Different classes and methods (don't cluster on the same file)
- Different question types within a level (tracing vs. vocabulary vs. syntax at surface;
  design choice vs. data flow vs. edge case at medium; scaling vs. analogy vs. assumption at deep)

Then **select one candidate per level** that together form a coherent but varied set —
ideally touching different parts of the codebase across the three questions.

### 3. Apply the `[reason]` / `[run]` Tag

Every question carries one tag:

- **`[reason]`** — Answerable by reading and thinking. A conceptual answer suffices;
  running the code is not required.
- **`[run]`** — Requires executing the code. A specific value or output is expected;
  a vague "more/less" answer is not sufficient.

Guidelines:
- Surface: may be `[run]` or `[reason]`
- Medium: prefer `[reason]`; `[run]` only if a concrete result genuinely illuminates design
- Deep: always `[reason]`

Never write a question that accepts both a vague and a specific answer — pick one and tag accordingly.

---

### 4. Write the Answer

Answer format differs by level:

#### Surface answer — Confident and complete `[A-style]`
Give the correct answer directly and completely. The student should be able to
confirm or correct the group's response with authority.
- For `[run]` questions: include the exact expected output or value
- For `[reason]` questions: 2–3 sentences, enough to settle any disagreement

#### Medium answer — Confident and complete `[A-style]`
Give a clear, complete answer the student can use to guide discussion.
Include the key insight and one sentence on why it matters.
If there are multiple valid answers, name the best one and briefly acknowledge alternatives.

#### Deep answer — Guidepost format `[B-style]`
Do NOT give a single authoritative answer. Instead give 3–4 bullet points:
- What a strong answer would include
- What a weak or incomplete answer typically misses
- One follow-up prompt the student can use if discussion stalls

Deep questions are discussion-oriented; the student's job is to draw out reasoning,
not deliver a verdict.

---

### 5. Output Format

Output exactly this structure — nothing more, nothing less:

```
## Your Question Set

Everyone in your group has one question at each level — surface, medium, and deep.
You each lead one level per round, rotating so every level gets covered each round.

**How the rotation works:**
- The group assigns each person a starting level (e.g., person 1 → surface,
  person 2 → medium, person 3 → deep, person 4 → surface).
- In Round 1, each person poses their assigned level's question.
- In Round 2, everyone shifts up one level (surface → medium → deep → surface).
- In Round 3, shift again.

This means by the end of three rounds, every level has been led by multiple
people with different questions — no re-running needed. The group already has
[N × 3] questions from a single run, where N is the number of students.

**When it's your turn:** pose your question, let the group discuss, then share
your answer to confirm, correct, or deepen what they said. For deep questions,
use your guidepost bullets to listen and follow up — don't just read the answer aloud.

Keep your answers hidden until after your group has discussed each question.

---

### 🔵 Surface Question
*[reason] or [run] tag + question text*

**Your answer:**
*[Complete answer — specific enough to confirm or correct the group]*

---

### 🟡 Medium Question
*[reason] or [run] tag + question text*

**Your answer:**
*[Complete answer with key insight and why it matters]*

---

### 🔴 Deep Question
*[reason] tag + question text*

**Your answer — what to listen for:**
- *[What a strong answer includes]*
- *[What weak answers typically miss]*
- *[A follow-up prompt if discussion stalls]*

---

### Session notes
- Your group has plenty of questions from a single run — no need to re-run unless
  you finish all rounds and want more.
- If your question comes up before your turn, you can still pose it — your answer stays useful.
- The TA can help if your group disagrees and your answer doesn't settle it.
```

---

### 6. Randomness Discipline

This skill must NOT always generate the same question for a given project.
Actively vary across runs by:

- Rotating which class or method each level's question focuses on
- Alternating question types (tracing / vocabulary / syntax / design / scaling / analogy)
- Occasionally choosing a less obvious but still valid question over the most prominent one
- For `[run]` surface questions: vary the specific value or file being queried

The goal is that in a group of 4 students, all running this skill on the same project,
at least 2–3 of the 4 surface questions, 2–3 of the 4 medium questions, and
2–3 of the 4 deep questions are meaningfully different.

Some overlap is fine and even useful — the same question discussed from two different
students' perspectives often goes deeper the second time.

---

## Calibration Guidelines

- **Surface**: answerable in 1–2 sentences by anyone who read the code carefully.
  Should not require understanding the whole system — just the relevant method or class.
- **Medium**: requires a paragraph of genuine reasoning. A student who only skimmed will struggle.
  The answer should give the student enough to guide discussion, not just confirm a yes/no.
- **Deep**: open-ended, no single right answer. The guidepost answer gives the student
  3–4 things to listen for, plus a follow-up if discussion stalls. The student's job
  is facilitator, not judge.

---

## What NOT to Do

- Don't generate the same question every run — vary the question pool selection
- Don't give deep questions an authoritative single answer — use guidepost format
- Don't give surface/medium questions vague answers — students need enough to settle disagreements
- Don't write questions answerable without reading the code (trivially Googleable, pure Java trivia)
- Don't write ambiguous questions that accept both vague and specific answers — tag and commit
- Don't generate more than one question per level — this is a peer session, not a quiz
- Don't frame re-running as necessary — a group of N students already has N×3 questions from a single run; re-running is only for bonus rounds after all questions are exhausted
- Don't include instructor-only content (grade rubrics, Gradescope notes, full answer keys for all questions) — this output goes directly to students
