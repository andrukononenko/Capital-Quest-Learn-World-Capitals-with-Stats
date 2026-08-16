# capital_quest.py
#!/usr/bin/env python3
"""
🗺️ Capital Quest – Learn World Capitals (Python Edition)
Advanced: interactive quiz, detailed stats, weakness detection, colored output
"""

import json
import os
import random
import sys
from datetime import datetime
from pathlib import Path
from typing import Dict, List, Tuple, Optional
from collections import defaultdict

try:
    from rich.console import Console
    from rich.table import Table
    from rich.progress import Progress, BarColumn, TextColumn
    from rich.panel import Panel
    from rich.prompt import Prompt, IntPrompt, Confirm
    from rich import box
    RICH_AVAILABLE = True
except ImportError:
    RICH_AVAILABLE = False
    print("⚠️  Install 'rich' for enhanced UI: pip install rich")


# ─── Data ────────────────────────────────────────────────────────────────────

COUNTRIES = {
    "Afghanistan": "Kabul",
    "Albania": "Tirana",
    "Algeria": "Algiers",
    "Andorra": "Andorra la Vella",
    "Angola": "Luanda",
    "Argentina": "Buenos Aires",
    "Armenia": "Yerevan",
    "Australia": "Canberra",
    "Austria": "Vienna",
    "Azerbaijan": "Baku",
    "Bahamas": "Nassau",
    "Bahrain": "Manama",
    "Bangladesh": "Dhaka",
    "Barbados": "Bridgetown",
    "Belarus": "Minsk",
    "Belgium": "Brussels",
    "Belize": "Belmopan",
    "Benin": "Porto-Novo",
    "Bhutan": "Thimphu",
    "Bolivia": "Sucre",
    "Bosnia": "Sarajevo",
    "Botswana": "Gaborone",
    "Brazil": "Brasilia",
    "Brunei": "Bandar Seri Begawan",
    "Bulgaria": "Sofia",
    "Burkina Faso": "Ouagadougou",
    "Burundi": "Gitega",
    "Cambodia": "Phnom Penh",
    "Cameroon": "Yaounde",
    "Canada": "Ottawa",
    "Cape Verde": "Praia",
    "Central African Republic": "Bangui",
    "Chad": "N'Djamena",
    "Chile": "Santiago",
    "China": "Beijing",
    "Colombia": "Bogota",
    "Comoros": "Moroni",
    "Congo": "Brazzaville",
    "Costa Rica": "San Jose",
    "Croatia": "Zagreb",
    "Cuba": "Havana",
    "Cyprus": "Nicosia",
    "Czech Republic": "Prague",
    "Denmark": "Copenhagen",
    "Djibouti": "Djibouti",
    "Dominican Republic": "Santo Domingo",
    "Ecuador": "Quito",
    "Egypt": "Cairo",
    "El Salvador": "San Salvador",
    "Eritrea": "Asmara",
    "Estonia": "Tallinn",
    "Eswatini": "Mbabane",
    "Ethiopia": "Addis Ababa",
    "Fiji": "Suva",
    "Finland": "Helsinki",
    "France": "Paris",
    "Gabon": "Libreville",
    "Gambia": "Banjul",
    "Georgia": "Tbilisi",
    "Germany": "Berlin",
    "Ghana": "Accra",
    "Greece": "Athens",
    "Guatemala": "Guatemala City",
    "Guinea": "Conakry",
    "Guyana": "Georgetown",
    "Haiti": "Port-au-Prince",
    "Honduras": "Tegucigalpa",
    "Hungary": "Budapest",
    "Iceland": "Reykjavik",
    "India": "New Delhi",
    "Indonesia": "Jakarta",
    "Iran": "Tehran",
    "Iraq": "Baghdad",
    "Ireland": "Dublin",
    "Israel": "Jerusalem",
    "Italy": "Rome",
    "Jamaica": "Kingston",
    "Japan": "Tokyo",
    "Jordan": "Amman",
    "Kazakhstan": "Nur-Sultan",
    "Kenya": "Nairobi",
    "Kuwait": "Kuwait City",
    "Kyrgyzstan": "Bishkek",
    "Laos": "Vientiane",
    "Latvia": "Riga",
    "Lebanon": "Beirut",
    "Lesotho": "Maseru",
    "Liberia": "Monrovia",
    "Libya": "Tripoli",
    "Liechtenstein": "Vaduz",
    "Lithuania": "Vilnius",
    "Luxembourg": "Luxembourg",
    "Madagascar": "Antananarivo",
    "Malawi": "Lilongwe",
    "Malaysia": "Kuala Lumpur",
    "Maldives": "Male",
    "Mali": "Bamako",
    "Malta": "Valletta",
    "Marshall Islands": "Majuro",
    "Mauritania": "Nouakchott",
    "Mauritius": "Port Louis",
    "Mexico": "Mexico City",
    "Micronesia": "Palikir",
    "Moldova": "Chisinau",
    "Monaco": "Monaco",
    "Mongolia": "Ulaanbaatar",
    "Montenegro": "Podgorica",
    "Morocco": "Rabat",
    "Mozambique": "Maputo",
    "Myanmar": "Naypyidaw",
    "Namibia": "Windhoek",
    "Nauru": "Yaren",
    "Nepal": "Kathmandu",
    "Netherlands": "Amsterdam",
    "New Zealand": "Wellington",
    "Nicaragua": "Managua",
    "Niger": "Niamey",
    "Nigeria": "Abuja",
    "North Korea": "Pyongyang",
    "Norway": "Oslo",
    "Oman": "Muscat",
    "Pakistan": "Islamabad",
    "Palau": "Ngerulmud",
    "Panama": "Panama City",
    "Papua New Guinea": "Port Moresby",
    "Paraguay": "Asuncion",
    "Peru": "Lima",
    "Philippines": "Manila",
    "Poland": "Warsaw",
    "Portugal": "Lisbon",
    "Qatar": "Doha",
    "Romania": "Bucharest",
    "Russia": "Moscow",
    "Rwanda": "Kigali",
    "Saint Kitts": "Basseterre",
    "Saint Lucia": "Castries",
    "Samoa": "Apia",
    "San Marino": "San Marino",
    "Sao Tome": "Sao Tome",
    "Saudi Arabia": "Riyadh",
    "Senegal": "Dakar",
    "Serbia": "Belgrade",
    "Seychelles": "Victoria",
    "Sierra Leone": "Freetown",
    "Singapore": "Singapore",
    "Slovakia": "Bratislava",
    "Slovenia": "Ljubljana",
    "Solomon Islands": "Honiara",
    "Somalia": "Mogadishu",
    "South Africa": "Pretoria",
    "South Korea": "Seoul",
    "South Sudan": "Juba",
    "Spain": "Madrid",
    "Sri Lanka": "Sri Jayawardenepura Kotte",
    "Sudan": "Khartoum",
    "Suriname": "Paramaribo",
    "Sweden": "Stockholm",
    "Switzerland": "Bern",
    "Syria": "Damascus",
    "Taiwan": "Taipei",
    "Tajikistan": "Dushanbe",
    "Tanzania": "Dodoma",
    "Thailand": "Bangkok",
    "Timor-Leste": "Dili",
    "Togo": "Lome",
    "Tonga": "Nuku'alofa",
    "Trinidad and Tobago": "Port of Spain",
    "Tunisia": "Tunis",
    "Turkey": "Ankara",
    "Turkmenistan": "Ashgabat",
    "Tuvalu": "Funafuti",
    "Uganda": "Kampala",
    "Ukraine": "Kyiv",
    "United Arab Emirates": "Abu Dhabi",
    "United Kingdom": "London",
    "United States": "Washington",
    "Uruguay": "Montevideo",
    "Uzbekistan": "Tashkent",
    "Vanuatu": "Port Vila",
    "Vatican City": "Vatican City",
    "Venezuela": "Caracas",
    "Vietnam": "Hanoi",
    "Yemen": "Sana'a",
    "Zambia": "Lusaka",
    "Zimbabwe": "Harare",
}

# ─── Stats Manager ───────────────────────────────────────────────────────────

class Stats:
    DATA_DIR = Path.home() / ".capital_quest"
    DATA_FILE = DATA_DIR / "stats.json"

    def __init__(self):
        self.total_questions = 0
        self.correct_answers = 0
        self.wrong_answers = 0
        self.current_streak = 0
        self.best_streak = 0
        self.sessions: List[Dict] = []
        self.country_stats: Dict[str, Dict] = defaultdict(lambda: {"asked": 0, "correct": 0})
        self._load()

    def _load(self) -> None:
        if self.DATA_FILE.exists():
            try:
                with open(self.DATA_FILE, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                    self.total_questions = data.get("total_questions", 0)
                    self.correct_answers = data.get("correct_answers", 0)
                    self.wrong_answers = data.get("wrong_answers", 0)
                    self.current_streak = data.get("current_streak", 0)
                    self.best_streak = data.get("best_streak", 0)
                    self.sessions = data.get("sessions", [])
                    self.country_stats = defaultdict(lambda: {"asked": 0, "correct": 0})
                    for country, stats in data.get("country_stats", {}).items():
                        self.country_stats[country] = stats
            except Exception:
                pass

    def _save(self) -> None:
        self.DATA_DIR.mkdir(parents=True, exist_ok=True)
        data = {
            "total_questions": self.total_questions,
            "correct_answers": self.correct_answers,
            "wrong_answers": self.wrong_answers,
            "current_streak": self.current_streak,
            "best_streak": self.best_streak,
            "sessions": self.sessions,
            "country_stats": dict(self.country_stats),
        }
        with open(self.DATA_FILE, 'w', encoding='utf-8') as f:
            json.dump(data, f, indent=2, ensure_ascii=False)

    def record_answer(self, country: str, correct: bool) -> None:
        self.total_questions += 1
        if correct:
            self.correct_answers += 1
            self.current_streak += 1
            if self.current_streak > self.best_streak:
                self.best_streak = self.current_streak
        else:
            self.wrong_answers += 1
            self.current_streak = 0
        self.country_stats[country]["asked"] += 1
        if correct:
            self.country_stats[country]["correct"] += 1
        self._save()

    def add_session(self, correct: int, wrong: int) -> None:
        self.sessions.append({"date": datetime.now().strftime("%Y-%m-%d"), "correct": correct, "wrong": wrong})
        if len(self.sessions) > 10:
            self.sessions = self.sessions[-10:]
        self._save()

    def get_accuracy(self) -> float:
        if self.total_questions == 0:
            return 0.0
        return self.correct_answers / self.total_questions * 100

    def get_weaknesses(self, top_n: int = 5) -> List[Tuple[str, float]]:
        weak = []
        for country, stats in self.country_stats.items():
            if stats["asked"] >= 3:
                acc = stats["correct"] / stats["asked"] * 100
                if acc < 60:
                    weak.append((country, acc))
        weak.sort(key=lambda x: x[1])
        return weak[:top_n]

    def reset(self) -> None:
        self.total_questions = 0
        self.correct_answers = 0
        self.wrong_answers = 0
        self.current_streak = 0
        self.best_streak = 0
        self.sessions = []
        self.country_stats = defaultdict(lambda: {"asked": 0, "correct": 0})
        self._save()


# ─── Quiz Engine ─────────────────────────────────────────────────────────────

class CapitalQuiz:
    def __init__(self):
        self.stats = Stats()
        self.console = Console() if RICH_AVAILABLE else None
        self.country_list = list(COUNTRIES.keys())

    def _get_question(self) -> Tuple[str, str, List[str]]:
        country = random.choice(self.country_list)
        correct = COUNTRIES[country]
        # Generate wrong options
        wrongs = []
        all_capitals = list(COUNTRIES.values())
        all_capitals.remove(correct)
        wrongs = random.sample(all_capitals, min(3, len(all_capitals)))
        while len(wrongs) < 3:
            # fallback
            wrongs.append("Unknown")
        options = [correct] + wrongs
        random.shuffle(options)
        return country, correct, options

    def _show_progress(self, current: int, total: int) -> None:
        if self.console and RICH_AVAILABLE:
            with Progress(TextColumn("[progress.description]{task.description}"),
                          BarColumn(),
                          TextColumn("{task.percentage:>3.0f}%")) as progress:
                task = progress.add_task("[cyan]Progress", total=total)
                progress.update(task, advance=current)
        else:
            bar_len = 30
            filled = int(current / total * bar_len)
            bar = "█" * filled + "░" * (bar_len - filled)
            print(f"\r  {bar} {current}/{total}", end="")

    def run_quiz(self, rounds: int = 10) -> None:
        correct_count = 0
        wrong_count = 0

        if self.console and RICH_AVAILABLE:
            self.console.print(Panel.fit("[bold cyan]🧠 Starting Quiz![/bold cyan]\nAnswer 10 questions.", border_style="cyan"))
        else:
            print("\n🧠 Starting Quiz! Answer 10 questions.\n")

        for i in range(rounds):
            country, correct, options = self._get_question()
            if self.console and RICH_AVAILABLE:
                self.console.print(f"\n[bold yellow]Q{i+1}.[/bold yellow] What is the capital of [bold]{country}[/bold]?")
                for idx, opt in enumerate(options, 1):
                    self.console.print(f"  [{idx}] {opt}")
                choice = Prompt.ask("Your choice", choices=["1","2","3","4"], default="1")
            else:
                print(f"\nQ{i+1}. What is the capital of {country}?")
                for idx, opt in enumerate(options, 1):
                    print(f"  {idx}. {opt}")
                choice = input("Your choice (1-4): ").strip()
                while choice not in ["1","2","3","4"]:
                    choice = input("Please enter 1-4: ").strip()

            selected = options[int(choice)-1]
            is_correct = (selected == correct)
            if is_correct:
                correct_count += 1
                msg = f"✅ Correct! {country} → {correct}"
                color = "green"
            else:
                wrong_count += 1
                msg = f"❌ Wrong! {country} → {correct} (you said {selected})"
                color = "red"
            self.stats.record_answer(country, is_correct)

            if self.console and RICH_AVAILABLE:
                self.console.print(f"[{color}]{msg}[/{color}]")
                if not is_correct and self.stats.country_stats[country]["asked"] >= 3:
                    acc = self.stats.country_stats[country]["correct"] / self.stats.country_stats[country]["asked"] * 100
                    self.console.print(f"[dim]Your accuracy for {country}: {acc:.1f}%[/dim]")
            else:
                print(msg)
                if not is_correct and self.stats.country_stats[country]["asked"] >= 3:
                    acc = self.stats.country_stats[country]["correct"] / self.stats.country_stats[country]["asked"] * 100
                    print(f"  Your accuracy for {country}: {acc:.1f}%")

            self._show_progress(i+1, rounds)

        self.stats.add_session(correct_count, wrong_count)
        if self.console and RICH_AVAILABLE:
            self.console.print(f"\n[bold]Quiz finished![/bold] Correct: [green]{correct_count}[/green], Wrong: [red]{wrong_count}[/red]")
            if correct_count >= 8:
                self.console.print("[bold cyan]🏆 Great job! You're a geography master![/bold cyan]")
        else:
            print(f"\n\nQuiz finished! Correct: {correct_count}, Wrong: {wrong_count}")
            if correct_count >= 8:
                print("🏆 Great job! You're a geography master!")

    def show_stats(self) -> None:
        if self.stats.total_questions == 0:
            print("📭 No data yet. Take a quiz first!")
            return

        if self.console and RICH_AVAILABLE:
            table = Table(title="📊 Your Statistics", box=box.ROUNDED)
            table.add_column("Metric", style="cyan")
            table.add_column("Value", style="green")
            table.add_row("Total Questions", str(self.stats.total_questions))
            table.add_row("Correct Answers", str(self.stats.correct_answers))
            table.add_row("Wrong Answers", str(self.stats.wrong_answers))
            table.add_row("Accuracy", f"{self.stats.get_accuracy():.1f}%")
            table.add_row("Current Streak", str(self.stats.current_streak))
            table.add_row("Best Streak", str(self.stats.best_streak))
            self.console.print(table)

            # Sessions
            if self.stats.sessions:
                session_table = Table(title="📅 Recent Sessions", box=box.MINIMAL)
                session_table.add_column("Date", style="dim")
                session_table.add_column("Correct", style="green")
                session_table.add_column("Wrong", style="red")
                session_table.add_column("Accuracy")
                for s in self.stats.sessions:
                    total = s["correct"] + s["wrong"]
                    acc = (s["correct"] / total * 100) if total > 0 else 0
                    session_table.add_row(s["date"], str(s["correct"]), str(s["wrong"]), f"{acc:.1f}%")
                self.console.print(session_table)

            # Weaknesses
            weak = self.stats.get_weaknesses()
            if weak:
                weak_table = Table(title="⚠️ Weaknesses (countries with <60% accuracy)", box=box.MINIMAL)
                weak_table.add_column("Country", style="yellow")
                weak_table.add_column("Accuracy", style="red")
                for country, acc in weak:
                    weak_table.add_row(country, f"{acc:.1f}%")
                self.console.print(weak_table)
            else:
                self.console.print("[green]🎉 No significant weaknesses! Keep it up![/green]")
        else:
            print("\n" + "="*50)
            print("📊 YOUR STATISTICS")
            print("="*50)
            print(f"  Total Questions: {self.stats.total_questions}")
            print(f"  Correct Answers: {self.stats.correct_answers}")
            print(f"  Wrong Answers:   {self.stats.wrong_answers}")
            print(f"  Accuracy:        {self.stats.get_accuracy():.1f}%")
            print(f"  Current Streak:  {self.stats.current_streak}")
            print(f"  Best Streak:     {self.stats.best_streak}")
            print("="*50)
            if self.stats.sessions:
                print("  Recent Sessions:")
                for s in self.stats.sessions[-5:]:
                    total = s["correct"] + s["wrong"]
                    acc = (s["correct"] / total * 100) if total > 0 else 0
                    print(f"    {s['date']}: {s['correct']}C / {s['wrong']}W ({acc:.1f}%)")
            weak = self.stats.get_weaknesses()
            if weak:
                print("  ⚠️ Weaknesses:")
                for country, acc in weak:
                    print(f"    {country}: {acc:.1f}%")
            else:
                print("  🎉 No significant weaknesses!")

    def reset_stats(self) -> None:
        if self.console and RICH_AVAILABLE:
            if not Confirm.ask("⚠️  Delete ALL stats? This cannot be undone!", default=False):
                return
        else:
            resp = input("⚠️  Delete ALL stats? (yes/no): ").strip().lower()
            if resp != "yes":
                return
        self.stats.reset()
        print("🗑️  All statistics cleared.")

    def run(self) -> None:
        if self.console and RICH_AVAILABLE:
            self.console.print(Panel.fit("[bold cyan]🗺️ Capital Quest – Learn World Capitals[/bold cyan]\n[dim]Master the map, one capital at a time![/dim]", border_style="cyan"))
        else:
            print("\n" + "="*50)
            print("🗺️ CAPITAL QUEST – Learn World Capitals")
            print("="*50 + "\n")

        while True:
            self._show_menu()
            choice = self._get_choice()

            if choice == "1":
                self.run_quiz()
            elif choice == "2":
                self.show_stats()
            elif choice == "3":
                self.reset_stats()
            elif choice == "0":
                print("👋 Goodbye! Keep learning!")
                break
            else:
                print("❌ Invalid choice.")

            if choice != "0":
                if self.console and RICH_AVAILABLE:
                    self.console.print("\n[dim]Press Enter to continue...[/dim]")
                    input()
                else:
                    input("\nPress Enter to continue...")

    def _show_menu(self) -> None:
        if self.console and RICH_AVAILABLE:
            menu = f"""
[bold cyan]🗺️ Main Menu[/bold cyan]
  [1] 🧠 Start Quiz (10 questions)
  [2] 📊 Show Statistics
  [3] 🗑️  Reset Statistics
  [0] 🚪 Exit
"""
            self.console.print(Panel(menu, border_style="blue"))
        else:
            print("\n" + "-"*50)
            print("🗺️ MAIN MENU")
            print("-"*50)
            print("  1. 🧠 Start Quiz (10 questions)")
            print("  2. 📊 Show Statistics")
            print("  3. 🗑️  Reset Statistics")
            print("  0. 🚪 Exit")
            print("-"*50)

    def _get_choice(self) -> str:
        if self.console and RICH_AVAILABLE:
            return Prompt.ask("[bold]Your choice[/bold]", choices=["0","1","2","3"])
        return input("Your choice: ").strip()


def main():
    try:
        app = CapitalQuiz()
        app.run()
    except KeyboardInterrupt:
        print("\n👋 Goodbye!")
        sys.exit(0)
    except Exception as e:
        print(f"❌ Unexpected error: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()
