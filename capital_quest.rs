# capital_quest.rs
/**
 * 🗺️ Capital Quest – Learn World Capitals (Rust Edition)
 * Advanced: interactive quiz, stats tracking, weakness detection, colored output
 */

use chrono::Local;
use rand::seq::SliceRandom;
use rand::thread_rng;
use serde::{Deserialize, Serialize};
use std::collections::HashMap;
use std::fs;
use std::io::{self, Write, BufRead};
use std::path::PathBuf;

// ─── Types ──────────────────────────────────────────────────────────────────

#[derive(Debug, Serialize, Deserialize, Clone)]
struct CountryStats {
    asked: u32,
    correct: u32,
}

#[derive(Debug, Serialize, Deserialize, Clone)]
struct SessionData {
    date: String,
    correct: u32,
    wrong: u32,
}

#[derive(Debug, Serialize, Deserialize)]
struct StatsData {
    total_questions: u32,
    correct_answers: u32,
    wrong_answers: u32,
    current_streak: u32,
    best_streak: u32,
    sessions: Vec<SessionData>,
    country_stats: HashMap<String, CountryStats>,
}

// ─── Data ────────────────────────────────────────────────────────────────────

const COUNTRIES: &[(&str, &str)] = &[
    ("Afghanistan", "Kabul"),
    ("Albania", "Tirana"),
    ("Algeria", "Algiers"),
    ("Andorra", "Andorra la Vella"),
    ("Angola", "Luanda"),
    ("Argentina", "Buenos Aires"),
    ("Armenia", "Yerevan"),
    ("Australia", "Canberra"),
    ("Austria", "Vienna"),
    ("Azerbaijan", "Baku"),
    ("Bahamas", "Nassau"),
    ("Bahrain", "Manama"),
    ("Bangladesh", "Dhaka"),
    ("Barbados", "Bridgetown"),
    ("Belarus", "Minsk"),
    ("Belgium", "Brussels"),
    ("Belize", "Belmopan"),
    ("Benin", "Porto-Novo"),
    ("Bhutan", "Thimphu"),
    ("Bolivia", "Sucre"),
    ("Bosnia", "Sarajevo"),
    ("Botswana", "Gaborone"),
    ("Brazil", "Brasilia"),
    ("Brunei", "Bandar Seri Begawan"),
    ("Bulgaria", "Sofia"),
    ("Burkina Faso", "Ouagadougou"),
    ("Burundi", "Gitega"),
    ("Cambodia", "Phnom Penh"),
    ("Cameroon", "Yaounde"),
    ("Canada", "Ottawa"),
    ("Cape Verde", "Praia"),
    ("Central African Republic", "Bangui"),
    ("Chad", "N'Djamena"),
    ("Chile", "Santiago"),
    ("China", "Beijing"),
    ("Colombia", "Bogota"),
    ("Comoros", "Moroni"),
    ("Congo", "Brazzaville"),
    ("Costa Rica", "San Jose"),
    ("Croatia", "Zagreb"),
    ("Cuba", "Havana"),
    ("Cyprus", "Nicosia"),
    ("Czech Republic", "Prague"),
    ("Denmark", "Copenhagen"),
    ("Djibouti", "Djibouti"),
    ("Dominican Republic", "Santo Domingo"),
    ("Ecuador", "Quito"),
    ("Egypt", "Cairo"),
    ("El Salvador", "San Salvador"),
    ("Eritrea", "Asmara"),
    ("Estonia", "Tallinn"),
    ("Eswatini", "Mbabane"),
    ("Ethiopia", "Addis Ababa"),
    ("Fiji", "Suva"),
    ("Finland", "Helsinki"),
    ("France", "Paris"),
    ("Gabon", "Libreville"),
    ("Gambia", "Banjul"),
    ("Georgia", "Tbilisi"),
    ("Germany", "Berlin"),
    ("Ghana", "Accra"),
    ("Greece", "Athens"),
    ("Guatemala", "Guatemala City"),
    ("Guinea", "Conakry"),
    ("Guyana", "Georgetown"),
    ("Haiti", "Port-au-Prince"),
    ("Honduras", "Tegucigalpa"),
    ("Hungary", "Budapest"),
    ("Iceland", "Reykjavik"),
    ("India", "New Delhi"),
    ("Indonesia", "Jakarta"),
    ("Iran", "Tehran"),
    ("Iraq", "Baghdad"),
    ("Ireland", "Dublin"),
    ("Israel", "Jerusalem"),
    ("Italy", "Rome"),
    ("Jamaica", "Kingston"),
    ("Japan", "Tokyo"),
    ("Jordan", "Amman"),
    ("Kazakhstan", "Nur-Sultan"),
    ("Kenya", "Nairobi"),
    ("Kuwait", "Kuwait City"),
    ("Kyrgyzstan", "Bishkek"),
    ("Laos", "Vientiane"),
    ("Latvia", "Riga"),
    ("Lebanon", "Beirut"),
    ("Lesotho", "Maseru"),
    ("Liberia", "Monrovia"),
    ("Libya", "Tripoli"),
    ("Liechtenstein", "Vaduz"),
    ("Lithuania", "Vilnius"),
    ("Luxembourg", "Luxembourg"),
    ("Madagascar", "Antananarivo"),
    ("Malawi", "Lilongwe"),
    ("Malaysia", "Kuala Lumpur"),
    ("Maldives", "Male"),
    ("Mali", "Bamako"),
    ("Malta", "Valletta"),
    ("Marshall Islands", "Majuro"),
    ("Mauritania", "Nouakchott"),
    ("Mauritius", "Port Louis"),
    ("Mexico", "Mexico City"),
    ("Micronesia", "Palikir"),
    ("Moldova", "Chisinau"),
    ("Monaco", "Monaco"),
    ("Mongolia", "Ulaanbaatar"),
    ("Montenegro", "Podgorica"),
    ("Morocco", "Rabat"),
    ("Mozambique", "Maputo"),
    ("Myanmar", "Naypyidaw"),
    ("Namibia", "Windhoek"),
    ("Nauru", "Yaren"),
    ("Nepal", "Kathmandu"),
    ("Netherlands", "Amsterdam"),
    ("New Zealand", "Wellington"),
    ("Nicaragua", "Managua"),
    ("Niger", "Niamey"),
    ("Nigeria", "Abuja"),
    ("North Korea", "Pyongyang"),
    ("Norway", "Oslo"),
    ("Oman", "Muscat"),
    ("Pakistan", "Islamabad"),
    ("Palau", "Ngerulmud"),
    ("Panama", "Panama City"),
    ("Papua New Guinea", "Port Moresby"),
    ("Paraguay", "Asuncion"),
    ("Peru", "Lima"),
    ("Philippines", "Manila"),
    ("Poland", "Warsaw"),
    ("Portugal", "Lisbon"),
    ("Qatar", "Doha"),
    ("Romania", "Bucharest"),
    ("Russia", "Moscow"),
    ("Rwanda", "Kigali"),
    ("Saint Kitts", "Basseterre"),
    ("Saint Lucia", "Castries"),
    ("Samoa", "Apia"),
    ("San Marino", "San Marino"),
    ("Sao Tome", "Sao Tome"),
    ("Saudi Arabia", "Riyadh"),
    ("Senegal", "Dakar"),
    ("Serbia", "Belgrade"),
    ("Seychelles", "Victoria"),
    ("Sierra Leone", "Freetown"),
    ("Singapore", "Singapore"),
    ("Slovakia", "Bratislava"),
    ("Slovenia", "Ljubljana"),
    ("Solomon Islands", "Honiara"),
    ("Somalia", "Mogadishu"),
    ("South Africa", "Pretoria"),
    ("South Korea", "Seoul"),
    ("South Sudan", "Juba"),
    ("Spain", "Madrid"),
    ("Sri Lanka", "Sri Jayawardenepura Kotte"),
    ("Sudan", "Khartoum"),
    ("Suriname", "Paramaribo"),
    ("Sweden", "Stockholm"),
    ("Switzerland", "Bern"),
    ("Syria", "Damascus"),
    ("Taiwan", "Taipei"),
    ("Tajikistan", "Dushanbe"),
    ("Tanzania", "Dodoma"),
    ("Thailand", "Bangkok"),
    ("Timor-Leste", "Dili"),
    ("Togo", "Lome"),
    ("Tonga", "Nuku'alofa"),
    ("Trinidad and Tobago", "Port of Spain"),
    ("Tunisia", "Tunis"),
    ("Turkey", "Ankara"),
    ("Turkmenistan", "Ashgabat"),
    ("Tuvalu", "Funafuti"),
    ("Uganda", "Kampala"),
    ("Ukraine", "Kyiv"),
    ("United Arab Emirates", "Abu Dhabi"),
    ("United Kingdom", "London"),
    ("United States", "Washington"),
    ("Uruguay", "Montevideo"),
    ("Uzbekistan", "Tashkent"),
    ("Vanuatu", "Port Vila"),
    ("Vatican City", "Vatican City"),
    ("Venezuela", "Caracas"),
    ("Vietnam", "Hanoi"),
    ("Yemen", "Sana'a"),
    ("Zambia", "Lusaka"),
    ("Zimbabwe", "Harare"),
];

lazy_static! {
    static ref COUNTRY_MAP: HashMap<String, String> = COUNTRIES.iter().map(|(k, v)| (k.to_string(), v.to_string())).collect();
}

// ─── Colors ──────────────────────────────────────────────────────────────────

fn c(text: &str, color: &str) -> String {
    format!("{}{}{}", color, text, "\x1b[0m")
}

const RESET: &str = "\x1b[0m";
const BRIGHT: &str = "\x1b[1m";
const DIM: &str = "\x1b[2m";
const RED: &str = "\x1b[31m";
const GREEN: &str = "\x1b[32m";
const YELLOW: &str = "\x1b[33m";
const BLUE: &str = "\x1b[34m";
const MAGENTA: &str = "\x1b[35m";
const CYAN: &str = "\x1b[36m";

// ─── Stats Manager ──────────────────────────────────────────────────────────

struct Stats {
    data_path: PathBuf,
    total_questions: u32,
    correct_answers: u32,
    wrong_answers: u32,
    current_streak: u32,
    best_streak: u32,
    sessions: Vec<SessionData>,
    country_stats: HashMap<String, CountryStats>,
}

impl Stats {
    fn new() -> Self {
        let home = std::env::var("HOME").or_else(|_| std::env::var("USERPROFILE")).unwrap_or_else(|_| ".".to_string());
        let data_dir = PathBuf::from(home).join(".capital_quest");
        fs::create_dir_all(&data_dir).unwrap();
        let data_path = data_dir.join("stats.json");
        let mut s = Stats {
            data_path,
            total_questions: 0,
            correct_answers: 0,
            wrong_answers: 0,
            current_streak: 0,
            best_streak: 0,
            sessions: Vec::new(),
            country_stats: HashMap::new(),
        };
        s.load();
        s
    }

    fn load(&mut self) {
        if !self.data_path.exists() { return; }
        if let Ok(raw) = fs::read_to_string(&self.data_path) {
            if let Ok(data) = serde_json::from_str::<StatsData>(&raw) {
                self.total_questions = data.total_questions;
                self.correct_answers = data.correct_answers;
                self.wrong_answers = data.wrong_answers;
                self.current_streak = data.current_streak;
                self.best_streak = data.best_streak;
                self.sessions = data.sessions;
                self.country_stats = data.country_stats;
            }
        }
    }

    fn save(&self) {
        let data = StatsData {
            total_questions: self.total_questions,
            correct_answers: self.correct_answers,
            wrong_answers: self.wrong_answers,
            current_streak: self.current_streak,
            best_streak: self.best_streak,
            sessions: self.sessions.clone(),
            country_stats: self.country_stats.clone(),
        };
        if let Ok(json) = serde_json::to_string_pretty(&data) {
            let _ = fs::write(&self.data_path, json);
        }
    }

    fn record_answer(&mut self, country: &str, correct: bool) {
        self.total_questions += 1;
        if correct {
            self.correct_answers += 1;
            self.current_streak += 1;
            if self.current_streak > self.best_streak {
                self.best_streak = self.current_streak;
            }
        } else {
            self.wrong_answers += 1;
            self.current_streak = 0;
        }
        let entry = self.country_stats.entry(country.to_string()).or_insert(CountryStats { asked: 0, correct: 0 });
        entry.asked += 1;
        if correct { entry.correct += 1; }
        self.save();
    }

    fn add_session(&mut self, correct: u32, wrong: u32) {
        self.sessions.push(SessionData {
            date: Local::now().format("%Y-%m-%d").to_string(),
            correct,
            wrong,
        });
        if self.sessions.len() > 10 {
            self.sessions.drain(0..self.sessions.len()-10);
        }
        self.save();
    }

    fn get_accuracy(&self) -> f64 {
        if self.total_questions == 0 { 0.0 } else { self.correct_answers as f64 / self.total_questions as f64 * 100.0 }
    }

    fn get_weaknesses(&self, top_n: usize) -> Vec<(String, f64)> {
        let mut weak = Vec::new();
        for (country, stats) in &self.country_stats {
            if stats.asked >= 3 {
                let acc = stats.correct as f64 / stats.asked as f64 * 100.0;
                if acc < 60.0 {
                    weak.push((country.clone(), acc));
                }
            }
        }
        weak.sort_by(|a, b| a.1.partial_cmp(&b.1).unwrap());
        weak.truncate(top_n);
        weak
    }

    fn reset(&mut self) {
        self.total_questions = 0;
        self.correct_answers = 0;
        self.wrong_answers = 0;
        self.current_streak = 0;
        self.best_streak = 0;
        self.sessions.clear();
        self.country_stats.clear();
        self.save();
    }
}

// ─── Quiz Engine ────────────────────────────────────────────────────────────

struct CapitalQuiz {
    stats: Stats,
    country_list: Vec<String>,
    rng: rand::rngs::ThreadRng,
}

impl CapitalQuiz {
    fn new() -> Self {
        let country_list: Vec<String> = COUNTRY_MAP.keys().cloned().collect();
        CapitalQuiz {
            stats: Stats::new(),
            country_list,
            rng: thread_rng(),
        }
    }

    fn ask(&self, prompt: &str) -> String {
        print!("{}", prompt);
        io::stdout().flush().unwrap();
        let stdin = io::stdin();
        let mut line = String::new();
        stdin.lock().read_line(&mut line).unwrap();
        line.trim().to_string()
    }

    fn ask_int(&self, prompt: &str, max: u32) -> u32 {
        loop {
            let ans = self.ask(prompt);
            if let Ok(num) = ans.parse::<u32>() {
                if num >= 1 && num <= max {
                    return num;
                }
            }
            println!("{}", c(&format!("Please enter a number between 1 and {}", max), YELLOW));
        }
    }

    fn ask_confirm(&self, prompt: &str) -> bool {
        let ans = self.ask(&format!("{} (yes/no): ", prompt));
        let a = ans.to_lowercase();
        a == "yes" || a == "y"
    }

    fn get_question(&mut self) -> (String, String, Vec<String>) {
        let country = self.country_list.choose(&mut self.rng).unwrap().clone();
        let correct = COUNTRY_MAP[&country].clone();
        let all_capitals: Vec<String> = COUNTRY_MAP.values().cloned().collect();
        let mut wrongs: Vec<String> = all_capitals.into_iter().filter(|c| c != &correct).collect();
        wrongs.shuffle(&mut self.rng);
        wrongs.truncate(3);
        while wrongs.len() < 3 { wrongs.push("Unknown".to_string()); }
        let mut options = vec![correct.clone()];
        options.extend(wrongs);
        options.shuffle(&mut self.rng);
        (country, correct, options)
    }

    fn show_progress(&self, current: u32, total: u32) {
        let bar_len = 30;
        let filled = (current as f64 / total as f64 * bar_len as f64) as usize;
        let bar = "█".repeat(filled) + &"░".repeat(bar_len - filled);
        print!("\r  {} {}/{}", bar, current, total);
        io::stdout().flush().unwrap();
    }

    fn run_quiz(&mut self, rounds: u32) {
        let mut correct_count = 0;
        let mut wrong_count = 0;

        println!("{}", c("\n🧠 Starting Quiz! Answer 10 questions.", &format!("{}{}", BRIGHT, CYAN)));

        for i in 0..rounds {
            let (country, correct, options) = self.get_question();
            println!("\n{} What is the capital of {}?", c(&format!("Q{}", i+1), YELLOW), c(&country, BRIGHT));
            for (idx, opt) in options.iter().enumerate() {
                println!("  {}. {}", idx+1, opt);
            }
            let choice = self.ask_int("Your choice (1-4): ", 4);
            let selected = options[(choice-1) as usize].clone();
            let is_correct = selected == correct;
            if is_correct {
                correct_count += 1;
                println!("{}", c(&format!("✅ Correct! {} → {}", country, correct), GREEN));
            } else {
                wrong_count += 1;
                println!("{}", c(&format!("❌ Wrong! {} → {} (you said {})", country, correct, selected), RED));
                if let Some(stats) = self.stats.country_stats.get(&country) {
                    if stats.asked >= 3 {
                        let acc = stats.correct as f64 / stats.asked as f64 * 100.0;
                        println!("{}", c(&format!("  Your accuracy for {}: {:.1}%", country, acc), DIM));
                    }
                }
            }
            self.stats.record_answer(&country, is_correct);
            self.show_progress(i+1, rounds);
        }

        self.stats.add_session(correct_count, wrong_count);
        println!("\n\n{} Correct: {}, Wrong: {}", c("Quiz finished!", BRIGHT), c(&correct_count.to_string(), GREEN), c(&wrong_count.to_string(), RED));
        if correct_count >= 8 {
            println!("{}", c("🏆 Great job! You're a geography master!", &format!("{}{}", BRIGHT, CYAN)));
        }
    }

    fn show_stats(&self) {
        if self.stats.total_questions == 0 {
            println!("{}", c("📭 No data yet. Take a quiz first!", YELLOW));
            return;
        }

        println!("\n{}", c(&"=".repeat(50), DIM));
        println!("{}", c("📊 YOUR STATISTICS", &format!("{}{}", BRIGHT, MAGENTA)));
        println!("{}", c(&"=".repeat(50), DIM));
        println!("  Total Questions: {}", self.stats.total_questions);
        println!("  Correct Answers: {}", c(&self.stats.correct_answers.to_string(), GREEN));
        println!("  Wrong Answers:   {}", c(&self.stats.wrong_answers.to_string(), RED));
        println!("  Accuracy:        {}", c(&format!("{:.1}%", self.stats.get_accuracy()), CYAN));
        println!("  Current Streak:  {}", c(&self.stats.current_streak.to_string(), YELLOW));
        println!("  Best Streak:     {}", c(&self.stats.best_streak.to_string(), GREEN));
        println!("{}", c(&"-".repeat(50), DIM));

        if !self.stats.sessions.is_empty() {
            println!("  Recent Sessions:");
            let start = if self.stats.sessions.len() > 5 { self.stats.sessions.len() - 5 } else { 0 };
            for s in &self.stats.sessions[start..] {
                let total = s.correct + s.wrong;
                let acc = if total > 0 { s.correct as f64 / total as f64 * 100.0 } else { 0.0 };
                println!("    {}: {}C / {}W ({:.1}%)", s.date, s.correct, s.wrong, acc);
            }
        }

        let weak = self.stats.get_weaknesses(5);
        if !weak.is_empty() {
            println!("{}", c("  ⚠️ Weaknesses (countries with <60% accuracy):", YELLOW));
            for (country, acc) in weak {
                println!("    {}: {:.1}%", country, acc);
            }
        } else {
            println!("{}", c("  🎉 No significant weaknesses!", GREEN));
        }
        println!("{}", c(&"=".repeat(50), DIM));
    }

    fn reset_stats(&mut self) {
        if !self.ask_confirm("⚠️  Delete ALL stats? This cannot be undone!") { return; }
        self.stats.reset();
        println!("{}", c("🗑️  All statistics cleared.", YELLOW));
    }

    fn run(&mut self) {
        println!("{}", c("\n🗺️ Capital Quest – Learn World Capitals", &format!("{}{}", BRIGHT, CYAN)));
        println!("{}", c("Master the map, one capital at a time!", DIM));

        loop {
            self.show_menu();
            let choice = self.ask("Your choice: ");
            match choice.as_str() {
                "1" => self.run_quiz(10),
                "2" => self.show_stats(),
                "3" => self.reset_stats(),
                "0" => {
                    println!("{}", c("👋 Goodbye! Keep learning!", CYAN));
                    break;
                }
                _ => println!("{}", c("❌ Invalid choice.", RED)),
            }
            if choice != "0" {
                print!("\nPress Enter to continue...");
                io::stdout().flush().unwrap();
                let mut _dummy = String::new();
                io::stdin().read_line(&mut _dummy).unwrap();
            }
        }
    }

    fn show_menu(&self) {
        println!("\n{}", c(&"=".repeat(50), CYAN));
        println!("{}", c("🗺️ MAIN MENU", &format!("{}{}", BRIGHT, CYAN)));
        println!("{}", c(&"=".repeat(50), CYAN));
        println!("  1. 🧠 Start Quiz (10 questions)");
        println!("  2. 📊 Show Statistics");
        println!("  3. 🗑️  Reset Statistics");
        println!("  0. 🚪 Exit");
        println!("{}", c(&"=".repeat(50), CYAN));
    }
}

// ─── Main ────────────────────────────────────────────────────────────────────

#[macro_use] extern crate lazy_static;

fn main() -> Result<(), Box<dyn std::error::Error>> {
    let mut app = CapitalQuiz::new();
    app.run();
    Ok(())
}
