#🗺️ Capital Quest – Learn World Capitals with Stats
"Master the map, one capital at a time – track your progress, conquer your weaknesses!"

📋 Table of Contents
✨ Features

📁 Repository Structure

🚀 Quick Start

💻 Language Implementations

📊 Data Format

🤝 Contributing

📄 License

✨ Features
Feature	Description
🧠 Interactive Quiz	Answer 10 random questions per round – 4 multiple‑choice options
📈 Detailed Statistics	Track total attempts, correct answers, accuracy %, and current streak
🏆 Weakness Detection	Automatically identifies countries you struggle with most
🌍 Regional Filtering	Practice capitals from specific continents (optional)
📅 Session History	See your last 5 sessions with scores
💾 Persistence	All stats saved locally in JSON format
🎨 Colorful CLI	Beautiful terminal output with progress bars and emojis
⚡ Cross-Platform	Works on Windows, macOS, and Linux
📁 Repository Structure
text
capital-quest/
├── README.md
├── python/
│   └── capital_quest.py
├── javascript/
│   └── capital_quest.js
├── typescript/
│   └── capital_quest.ts
├── go/
│   └── capital_quest.go
├── rust/
│   └── capital_quest.rs
├── cpp/
│   └── capital_quest.cpp
├── java/
│   └── CapitalQuest.java
└── csharp/
    └── CapitalQuest.cs
🚀 Quick Start
Prerequisites
Each language requires its respective runtime/compiler (see individual sections)

Clone & Run
bash
git clone https://github.com/yourusername/capital-quest.git
cd capital-quest
# Navigate to your language folder and run
💻 Language Implementations
1. 🐍 Python
bash
cd python
python capital_quest.py
Requires: Python 3.8+

2. 🟨 JavaScript (Node.js)
bash
cd javascript
node capital_quest.js
Requires: Node.js 14+

3. 🟦 TypeScript
bash
cd typescript
npm install -g ts-node
ts-node capital_quest.ts
Requires: Node.js 14+, TypeScript

4. 🟩 Go
bash
cd go
go run capital_quest.go
Requires: Go 1.18+

5. 🦀 Rust
bash
cd rust
cargo run
Requires: Rust 1.70+

6. ⚙️ C++
bash
cd cpp
g++ -std=c++17 capital_quest.cpp -o capital_quest
./capital_quest
Requires: C++17 compatible compiler

7. ☕ Java
bash
cd java
javac CapitalQuest.java
java CapitalQuest
Requires: JDK 17+

8. 🔷 C#
bash
cd csharp
dotnet run
Requires: .NET 6.0+

📊 Data Format
All implementations use a unified JSON schema for statistics:

json
{
  "total_questions": 150,
  "correct_answers": 97,
  "wrong_answers": 53,
  "current_streak": 5,
  "best_streak": 12,
  "sessions": [
    {
      "date": "2026-08-16",
      "correct": 8,
      "wrong": 2
    }
  ],
  "country_stats": {
    "France": { "asked": 5, "correct": 4 },
    "Germany": { "asked": 3, "correct": 1 }
  }
}
Data is stored in the user's home directory under .capital_quest/stats.json.

🤝 Contributing
Contributions are welcome! Please:

Fork the repository

Create a feature branch

Commit your changes

Open a Pull Request

📄 License
MIT © 2026 Capital Quest Team
