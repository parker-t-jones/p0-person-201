---
name: java-to-python
description: >
  Translates a Duke CS 201 Java assignment into equivalent Python programs.
  Use this skill whenever a student asks to convert their Java code to Python,
  wants a Python equivalent of their assignment, mentions generating Python from
  their src/ folder, or asks "what would this look like in Python". Triggers on
  any request involving Java-to-Python translation, Python equivalents of Java
  classes, or generating python/ or pythonic/ folders from Java source files.
---

# Java-to-Python Skill for CS 201

## Purpose

Given a completed CS 201 Java assignment, generate two Python translations:

1. **`python/`** — A Java-mirror translation. One `.py` file per `.java` file.
   Same class/method names (converted to `snake_case`), same logic structure,
   same data flow. A Java programmer should be able to read it and recognize
   exactly what each part does.

2. **`pythonic/`** — A more idiomatic Python version. Uses Python's strengths:
   list comprehensions, dictionaries, sets, `dataclasses`, built-in sorting,
   `pathlib`, etc. Still readable, but written the way a Python programmer
   would naturally write it.

Both versions must produce identical output to the original Java programs.

---

## Repo Layout (assumed)

```
repo-root/
├── src/         ← Java .java files (read-only, do not modify)
├── data/        ← text/CSV/JSON data files (read-only)
├── python/      ← generate one .py per .java file here
├── pythonic/    ← generate idiomatic Python versions here
└── .claude/skills/java-to-python/SKILL.md
```

Create `python/` and `pythonic/` directories if they don't exist.

---

## Step-by-Step Process

### 1. Inventory the source

Read every `.java` file in `src/`. Classify each file as:
- **Data class** — holds fields, getters, constructor, `toString`, `equals`
  (e.g., `Person201.java`, `Restaurant.java`)
- **Utilities class** — static methods only, no instance state
  (e.g., `Person201Utilities.java`)
- **Driver/demo class** — has a `main()` method, orchestrates other classes
  (e.g., `Person201Demo.java`, `CountEateries.java`, `Person201Nearby.java`,
  `Person201Farthest.java`)
- **Network class** — reads from URLs, posts data
  (e.g., `PeopleDownloader.java`, `PostPerson.java`)

Also note the data files in `data/`: their format (CSV, JSON, space-separated)
and what fields they contain.

### 2. Generate `python/` (Java-mirror)

Rules for the Java-mirror translation:

**Naming:**
- `CamelCase` class names → `snake_case` for functions/variables,
  but keep class names in `CamelCase` (Python convention matches Java here)
- `camelCase` method names → `snake_case` (e.g., `readFile` → `read_file`,
  `countEateries` → `count_eateries`)
- Constants stay `UPPER_SNAKE_CASE`

**Class structure:**
- Java data classes → Python classes with `__init__`, properties, `__eq__`,
  `__str__`. Mirror the Java field names exactly.
- Java `record` types (e.g., `Restaurant`) → Python `@dataclass`
- Static utility methods → module-level functions in the corresponding `.py`
  file, grouped under a class only if the Java has a class

**Control flow:**
- Mirror Java's loops, conditionals, and logic directly — do not compress
  into comprehensions or built-ins yet
- `for (Person201 p : people)` → `for p in people:`
- `ArrayList` → `list`, `TreeSet` → `sorted(set(...))`

**I/O:**
- `Scanner` reading CSV → `open()` + `line.split(',')`
- `System.out.printf(...)` → `print(f"...")`  (use f-strings)
- JSON reading (Gson) → `import json` + `json.load()`

**Comments:**
Add a comment above each method explaining the Java original's name if it
differs, e.g.:
```python
# Java: countEateries(Person201[] people, String eatery)
def count_eateries(people, eatery):
```

**One file per Java class.** File name = `snake_case` of class name:
- `Person201.java` → `python/person201.py`
- `Person201Utilities.java` → `python/person201_utilities.py`
- `CountEateries.java` → `python/count_eateries.py`

Each driver file should be runnable: `python python/count_eateries.py`

### 3. Generate `pythonic/` (idiomatic Python)

Only generate a `pythonic/` version for files where Python offers a
meaningfully different and cleaner approach. Skip files that would be nearly
identical to the `python/` version.

**When to write a pythonic version:**
- Data classes → use `@dataclass` with `__post_init__` for validation
- Utility/counting logic → use list comprehensions, `sum()`, `Counter`
- Sorting → use `sorted()` with `key=` lambdas
- File reading → use `pathlib.Path`, generator expressions
- Collection building → use dict/set comprehensions

**Key idioms to introduce (with comments explaining them):**

```python
# Python list comprehension — like a for-loop that builds a list in one line
# Java equivalent: for(Person201 p : people) { if(...) list.add(p); }
nearby = [p for p in people if distance(p, query) < threshold]
```

```python
# Counter does the counting Java's TreeSet+loop pattern does manually
from collections import Counter
eatery_counts = Counter(p.eatery for p in people)
```

```python
# @dataclass auto-generates __init__, __repr__, __eq__
from dataclasses import dataclass

@dataclass
class Person201:
    name: str
    latitude: float
    longitude: float
    eatery: str
```

Add a comment block at the top of each `pythonic/` file:
```python
# PYTHONIC VERSION
# Compare with python/person201.py to see Java-style vs Python-style.
# Key differences noted inline with # PYTHON: comments.
```

**Import paths:** `pythonic/` files that need shared classes (like `Person201`)
should add `python/` to the path at the top:
```python
import sys, os
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'python'))
```
This lets both folders share the `Person201` class without duplication.

### 4. Handle data files

- CSV files (`name,lat,lon,eatery` format): read with `open()` and
  `line.strip().split(',')` in the mirror version; use a generator or
  list comprehension in the pythonic version.
- JSON files: use `json.load()` in both versions.
- Data file paths: use relative paths from repo root, same as Java
  (e.g., `"data/foodlarge.txt"`). Do not hardcode absolute paths.

### 5. Verify

After generating, do a quick sanity check:
- Run the driver scripts if Python is available: 
  `python python/count_eateries.py`
- Confirm output structure matches what the Java would produce
  (same counts, same formatting where feasible)
- Note any differences (e.g., floating point formatting) in a comment

---

## Conventions for Comments in Generated Files

The goal is that a Java student can read the Python and understand it.
Use these comment patterns:

```python
# JAVA→PYTHON: Java arrays (Person201[]) become Python lists
# JAVA→PYTHON: Java's double → Python's float  
# JAVA→PYTHON: String.format("%06.2f", x) → f"{abs(x):06.2f}"
# JAVA→PYTHON: Math.abs() → abs(), Math.toRadians() → math.radians()
# JAVA→PYTHON: TreeSet keeps items sorted; sorted(set(...)) does the same
```

---

## Example: This Assignment (p0-person201)

For reference, the Java classes in this assignment map as follows:

| Java file | Role | python/ file | pythonic/ file |
|---|---|---|---|
| `Person201.java` | Data class | `person201.py` | `person201.py` (use `@dataclass`) |
| `Restaurant.java` | Record (data) | `restaurant.py` | same as mirror |
| `Person201Utilities.java` | Static utils | `person201_utilities.py` | `person201_utilities.py` (use `math`, comprehensions) |
| `CountEateries.java` | Driver | `count_eateries.py` | `count_eateries.py` (use `Counter`) |
| `Person201Demo.java` | Demo driver | `person201_demo.py` | skip (nearly identical) |
| `Person201Nearby.java` | Driver | `person201_nearby.py` | `person201_nearby.py` (use list comprehension) |
| `Person201Farthest.java` | Driver | `person201_farthest.py` | `person201_farthest.py` (use `max()` with key) |
| `PeopleDownloader.java` | Network | `people_downloader.py` | skip (network code similar) |
| `PostPerson.java` | Network/POST | `post_person.py` | skip (network code similar) |
| `ReadRestaurants.java` | JSON reader | `read_restaurants.py` | `read_restaurants.py` (use `json`, `dataclass`) |

Data file format (`foodsmall.txt`, `foodlarge.txt`):
```
name,latitude,longitude,eatery
xamir,46.9994,-122.3921,Guasaca
malka,47.2507,-121.0989,Playa Bowls
```

---

## What NOT to Do

- Do not use numpy, pandas, or other non-standard libraries — standard library only
- Do not use type annotations in the mirror version (they'd look unfamiliar to Java students)
- Do use type annotations in the pythonic version
- Do not restructure the logic significantly in the mirror version —
  if Java does a nested loop, Python should too
- Do not skip files — every `.java` in `src/` gets a `.py` in `python/`
