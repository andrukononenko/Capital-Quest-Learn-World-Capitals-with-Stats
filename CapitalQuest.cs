# CapitalQuest.cs
/**
 * 🗺️ Capital Quest – Learn World Capitals (C# Edition)
 * Interactive quiz with stats, weakness detection, colorful CLI
 * Requires: .NET 6.0+
 */

using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;
using System.Text.Json.Serialization;

// ─── Data Classes ──────────────────────────────────────────────────────────

public class CountryStats
{
    [JsonPropertyName("asked")]
    public int Asked { get; set; }
    
    [JsonPropertyName("correct")]
    public int Correct { get; set; }
}

public class SessionData
{
    [JsonPropertyName("date")]
    public string Date { get; set; } = "";
    
    [JsonPropertyName("correct")]
    public int Correct { get; set; }
    
    [JsonPropertyName("wrong")]
    public int Wrong { get; set; }
}

public class StatsData
{
    [JsonPropertyName("totalQuestions")]
    public int TotalQuestions { get; set; }
    
    [JsonPropertyName("correctAnswers")]
    public int CorrectAnswers { get; set; }
    
    [JsonPropertyName("wrongAnswers")]
    public int WrongAnswers { get; set; }
    
    [JsonPropertyName("currentStreak")]
    public int CurrentStreak { get; set; }
    
    [JsonPropertyName("bestStreak")]
    public int BestStreak { get; set; }
    
    [JsonPropertyName("sessions")]
    public List<SessionData> Sessions { get; set; } = new();
    
    [JsonPropertyName("countryStats")]
    public Dictionary<string, CountryStats> CountryStats { get; set; } = new();
}

// ─── Main App ──────────────────────────────────────────────────────────────

public class CapitalQuest
{
    // ─── Data ──────────────────────────────────────────────────────────────

    private static readonly Dictionary<string, string> Countries = new()
    {
        {"Afghanistan", "Kabul"},
        {"Albania", "Tirana"},
        {"Algeria", "Algiers"},
        {"Andorra", "Andorra la Vella"},
        {"Angola", "Luanda"},
        {"Argentina", "Buenos Aires"},
        {"Armenia", "Yerevan"},
        {"Australia", "Canberra"},
        {"Austria", "Vienna"},
        {"Azerbaijan", "Baku"},
        {"Bahamas", "Nassau"},
        {"Bahrain", "Manama"},
        {"Bangladesh", "Dhaka"},
        {"Barbados", "Bridgetown"},
        {"Belarus", "Minsk"},
        {"Belgium", "Brussels"},
        {"Belize", "Belmopan"},
        {"Benin", "Porto-Novo"},
        {"Bhutan", "Thimphu"},
        {"Bolivia", "Sucre"},
        {"Bosnia", "Sarajevo"},
        {"Botswana", "Gaborone"},
        {"Brazil", "Brasilia"},
        {"Brunei", "Bandar Seri Begawan"},
        {"Bulgaria", "Sofia"},
        {"Burkina Faso", "Ouagadougou"},
        {"Burundi", "Gitega"},
        {"Cambodia", "Phnom Penh"},
        {"Cameroon", "Yaounde"},
        {"Canada", "Ottawa"},
        {"Cape Verde", "Praia"},
        {"Central African Republic", "Bangui"},
        {"Chad", "N'Djamena"},
        {"Chile", "Santiago"},
        {"China", "Beijing"},
        {"Colombia", "Bogota"},
        {"Comoros", "Moroni"},
        {"Congo", "Brazzaville"},
        {"Costa Rica", "San Jose"},
        {"Croatia", "Zagreb"},
        {"Cuba", "Havana"},
        {"Cyprus", "Nicosia"},
        {"Czech Republic", "Prague"},
        {"Denmark", "Copenhagen"},
        {"Djibouti", "Djibouti"},
        {"Dominican Republic", "Santo Domingo"},
        {"Ecuador", "Quito"},
        {"Egypt", "Cairo"},
        {"El Salvador", "San Salvador"},
        {"Eritrea", "Asmara"},
        {"Estonia", "Tallinn"},
        {"Eswatini", "Mbabane"},
        {"Ethiopia", "Addis Ababa"},
        {"Fiji", "Suva"},
        {"Finland", "Helsinki"},
        {"France", "Paris"},
        {"Gabon", "Libreville"},
        {"Gambia", "Banjul"},
        {"Georgia", "Tbilisi"},
        {"Germany", "Berlin"},
        {"Ghana", "Accra"},
        {"Greece", "Athens"},
        {"Guatemala", "Guatemala City"},
        {"Guinea", "Conakry"},
        {"Guyana", "Georgetown"},
        {"Haiti", "Port-au-Prince"},
        {"Honduras", "Tegucigalpa"},
        {"Hungary", "Budapest"},
        {"Iceland", "Reykjavik"},
        {"India", "New Delhi"},
        {"Indonesia", "Jakarta"},
        {"Iran", "Tehran"},
        {"Iraq", "Baghdad"},
        {"Ireland", "Dublin"},
        {"Israel", "Jerusalem"},
        {"Italy", "Rome"},
        {"Jamaica", "Kingston"},
        {"Japan", "Tokyo"},
        {"Jordan", "Amman"},
        {"Kazakhstan", "Nur-Sultan"},
        {"Kenya", "Nairobi"},
        {"Kuwait", "Kuwait City"},
        {"Kyrgyzstan", "Bishkek"},
        {"Laos", "Vientiane"},
        {"Latvia", "Riga"},
        {"Lebanon", "Beirut"},
        {"Lesotho", "Maseru"},
        {"Liberia", "Monrovia"},
        {"Libya", "Tripoli"},
        {"Liechtenstein", "Vaduz"},
        {"Lithuania", "Vilnius"},
        {"Luxembourg", "Luxembourg"},
        {"Madagascar", "Antananarivo"},
        {"Malawi", "Lilongwe"},
        {"Malaysia", "Kuala Lumpur"},
        {"Maldives", "Male"},
        {"Mali", "Bamako"},
        {"Malta", "Valletta"},
        {"Marshall Islands", "Majuro"},
        {"Mauritania", "Nouakchott"},
        {"Mauritius", "Port Louis"},
        {"Mexico", "Mexico City"},
        {"Micronesia", "Palikir"},
        {"Moldova", "Chisinau"},
        {"Monaco", "Monaco"},
        {"Mongolia", "Ulaanbaatar"},
        {"Montenegro", "Podgorica"},
        {"Morocco", "Rabat"},
        {"Mozambique", "Maputo"},
        {"Myanmar", "Naypyidaw"},
        {"Namibia", "Windhoek"},
        {"Nauru", "Yaren"},
        {"Nepal", "Kathmandu"},
        {"Netherlands", "Amsterdam"},
        {"New Zealand", "Wellington"},
        {"Nicaragua", "Managua"},
        {"Niger", "Niamey"},
        {"Nigeria", "Abuja"},
        {"North Korea", "Pyongyang"},
        {"Norway", "Oslo"},
        {"Oman", "Muscat"},
        {"Pakistan", "Islamabad"},
        {"Palau", "Ngerulmud"},
        {"Panama", "Panama City"},
        {"Papua New Guinea", "Port Moresby"},
        {"Paraguay", "Asuncion"},
        {"Peru", "Lima"},
        {"Philippines", "Manila"},
        {"Poland", "Warsaw"},
        {"Portugal", "Lisbon"},
        {"Qatar", "Doha"},
        {"Romania", "Bucharest"},
        {"Russia", "Moscow"},
        {"Rwanda", "Kigali"},
        {"Saint Kitts", "Basseterre"},
        {"Saint Lucia", "Castries"},
        {"Samoa", "Apia"},
        {"San Marino", "San Marino"},
        {"Sao Tome", "Sao Tome"},
        {"Saudi Arabia", "Riyadh"},
        {"Senegal", "Dakar"},
        {"Serbia", "Belgrade"},
        {"Seychelles", "Victoria"},
        {"Sierra Leone", "Freetown"},
        {"Singapore", "Singapore"},
        {"Slovakia", "Bratislava"},
        {"Slovenia", "Ljubljana"},
        {"Solomon Islands", "Honiara"},
        {"Somalia", "Mogadishu"},
        {"South Africa", "Pretoria"},
        {"South Korea", "Seoul"},
        {"South Sudan", "Juba"},
        {"Spain", "Madrid"},
        {"Sri Lanka", "Sri Jayawardenepura Kotte"},
        {"Sudan", "Khartoum"},
        {"Suriname", "Paramaribo"},
        {"Sweden", "Stockholm"},
        {"Switzerland", "Bern"},
        {"Syria", "Damascus"},
        {"Taiwan", "Taipei"},
        {"Tajikistan", "Dushanbe"},
        {"Tanzania", "Dodoma"},
        {"Thailand", "Bangkok"},
        {"Timor-Leste", "Dili"},
        {"Togo", "Lome"},
        {"Tonga", "Nuku'alofa"},
        {"Trinidad and Tobago", "Port of Spain"},
        {"Tunisia", "Tunis"},
        {"Turkey", "Ankara"},
        {"Turkmenistan", "Ashgabat"},
        {"Tuvalu", "Funafuti"},
        {"Uganda", "Kampala"},
        {"Ukraine", "Kyiv"},
        {"United Arab Emirates", "Abu Dhabi"},
        {"United Kingdom", "London"},
        {"United States", "Washington"},
        {"Uruguay", "Montevideo"},
        {"Uzbekistan", "Tashkent"},
        {"Vanuatu", "Port Vila"},
        {"Vatican City", "Vatican City"},
        {"Venezuela", "Caracas"},
        {"Vietnam", "Hanoi"},
        {"Yemen", "Sana'a"},
        {"Zambia", "Lusaka"},
        {"Zimbabwe", "Harare"}
    };

    // ─── Colors ─────────────────────────────────────────────────────────────

    private static readonly string Reset = "\u001B[0m";
    private static readonly string Bright = "\u001B[1m";
    private static readonly string Dim = "\u001B[2m";
    private static readonly string Red = "\u001B[31m";
    private static readonly string Green = "\u001B[32m";
    private static readonly string Yellow = "\u001B[33m";
    private static readonly string Blue = "\u001B[34m";
    private static readonly string Magenta = "\u001B[35m";
    private static readonly string Cyan = "\u001B[36m";

    private static string C(string text, string color) => color + text + Reset;

    // ─── Stats Manager ─────────────────────────────────────────────────────

    private class Stats
    {
        private readonly string dataDir;
        private readonly string dataFile;
        public int TotalQuestions { get; set; }
        public int CorrectAnswers { get; set; }
        public int WrongAnswers { get; set; }
        public int CurrentStreak { get; set; }
        public int BestStreak { get; set; }
        public List<SessionData> Sessions { get; set; } = new();
        public Dictionary<string, CountryStats> CountryStats { get; set; } = new();

        public Stats()
        {
            string home = Environment.GetFolderPath(Environment.SpecialFolder.UserProfile);
            dataDir = Path.Combine(home, ".capital_quest");
            dataFile = Path.Combine(dataDir, "stats.json");
            Directory.CreateDirectory(dataDir);
            Load();
        }

        private void Load()
        {
            if (!File.Exists(dataFile)) return;
            try
            {
                string json = File.ReadAllText(dataFile);
                var data = JsonSerializer.Deserialize<StatsData>(json);
                if (data != null)
                {
                    TotalQuestions = data.TotalQuestions;
                    CorrectAnswers = data.CorrectAnswers;
                    WrongAnswers = data.WrongAnswers;
                    CurrentStreak = data.CurrentStreak;
                    BestStreak = data.BestStreak;
                    Sessions = data.Sessions ?? new List<SessionData>();
                    CountryStats = data.CountryStats ?? new Dictionary<string, CountryStats>();
                }
            }
            catch { /* ignore */ }
        }

        private void Save()
        {
            var data = new StatsData
            {
                TotalQuestions = TotalQuestions,
                CorrectAnswers = CorrectAnswers,
                WrongAnswers = WrongAnswers,
                CurrentStreak = CurrentStreak,
                BestStreak = BestStreak,
                Sessions = Sessions,
                CountryStats = CountryStats
            };
            string json = JsonSerializer.Serialize(data, new JsonSerializerOptions { WriteIndented = true });
            File.WriteAllText(dataFile, json);
        }

        public void RecordAnswer(string country, bool correct)
        {
            TotalQuestions++;
            if (correct)
            {
                CorrectAnswers++;
                CurrentStreak++;
                if (CurrentStreak > BestStreak) BestStreak = CurrentStreak;
            }
            else
            {
                WrongAnswers++;
                CurrentStreak = 0;
            }
            if (!CountryStats.ContainsKey(country))
                CountryStats[country] = new CountryStats();
            CountryStats[country].Asked++;
            if (correct) CountryStats[country].Correct++;
            Save();
        }

        public void AddSession(int correct, int wrong)
        {
            Sessions.Add(new SessionData
            {
                Date = DateTime.Now.ToString("yyyy-MM-dd"),
                Correct = correct,
                Wrong = wrong
            });
            if (Sessions.Count > 10) Sessions.RemoveAt(0);
            Save();
        }

        public double GetAccuracy()
        {
            if (TotalQuestions == 0) return 0.0;
            return (double)CorrectAnswers / TotalQuestions * 100.0;
        }

        public List<(string Country, double Accuracy)> GetWeaknesses(int topN)
        {
            var weak = new List<(string, double)>();
            foreach (var kv in CountryStats)
            {
                if (kv.Value.Asked >= 3)
                {
                    double acc = (double)kv.Value.Correct / kv.Value.Asked * 100.0;
                    if (acc < 60.0) weak.Add((kv.Key, acc));
                }
            }
            weak.Sort((a, b) => a.Accuracy.CompareTo(b.Accuracy));
            if (weak.Count > topN) weak = weak.GetRange(0, topN);
            return weak;
        }

        public void Reset()
        {
            TotalQuestions = 0;
            CorrectAnswers = 0;
            WrongAnswers = 0;
            CurrentStreak = 0;
            BestStreak = 0;
            Sessions.Clear();
            CountryStats.Clear();
            Save();
        }
    }

    // ─── Quiz Engine ──────────────────────────────────────────────────────

    private readonly Stats stats;
    private readonly List<string> countryList;
    private readonly Random random;

    public CapitalQuest()
    {
        stats = new Stats();
        countryList = Countries.Keys.ToList();
        random = new Random();
    }

    private string Ask(string prompt)
    {
        Console.Write(prompt);
        return Console.ReadLine()?.Trim() ?? "";
    }

    private int AskInt(string prompt, int max)
    {
        while (true)
        {
            if (int.TryParse(Ask(prompt), out int val) && val >= 1 && val <= max)
                return val;
            Console.WriteLine(C($"Please enter a number between 1 and {max}", Yellow));
        }
    }

    private bool AskConfirm(string prompt)
    {
        string ans = Ask(prompt + " (yes/no): ").ToLower();
        return ans == "yes" || ans == "y";
    }

    private void ShowProgress(int current, int total)
    {
        int barLen = 30;
        int filled = current * barLen / total;
        string bar = new string('█', filled) + new string('░', barLen - filled);
        Console.Write($"\r  {bar} {current}/{total}");
    }

    private (string country, string correct, List<string> options) GetQuestion()
    {
        string country = countryList[random.Next(countryList.Count)];
        string correct = Countries[country];

        var allCapitals = Countries.Values.ToList();
        var wrongs = allCapitals.Where(c => c != correct).ToList();
        wrongs = wrongs.OrderBy(x => random.Next()).Take(3).ToList();
        while (wrongs.Count < 3) wrongs.Add("Unknown");

        var options = new List<string> { correct };
        options.AddRange(wrongs);
        options = options.OrderBy(x => random.Next()).ToList();
        return (country, correct, options);
    }

    private void RunQuiz(int rounds)
    {
        int correctCount = 0;
        int wrongCount = 0;

        Console.WriteLine(C("\n🧠 Starting Quiz! Answer 10 questions.", Bright + Cyan));

        for (int i = 0; i < rounds; i++)
        {
            var (country, correct, options) = GetQuestion();
            Console.WriteLine($"\n{C($"Q{i+1}.", Yellow)} What is the capital of {C(country, Bright)}?");
            for (int j = 0; j < options.Count; j++)
                Console.WriteLine($"  {j+1}. {options[j]}");
            int choice = AskInt("Your choice (1-4): ", 4);
            string selected = options[choice-1];
            bool isCorrect = selected == correct;
            if (isCorrect)
            {
                correctCount++;
                Console.WriteLine(C($"✅ Correct! {country} → {correct}", Green));
            }
            else
            {
                wrongCount++;
                Console.WriteLine(C($"❌ Wrong! {country} → {correct} (you said {selected})", Red));
                if (stats.CountryStats.TryGetValue(country, out var cs) && cs.Asked >= 3)
                {
                    double acc = (double)cs.Correct / cs.Asked * 100.0;
                    Console.WriteLine(C($"  Your accuracy for {country}: {acc:F1}%", Dim));
                }
            }
            stats.RecordAnswer(country, isCorrect);
            ShowProgress(i+1, rounds);
        }

        stats.AddSession(correctCount, wrongCount);
        Console.WriteLine($"\n\n{C("Quiz finished!", Bright)} Correct: {C(correctCount.ToString(), Green)}, Wrong: {C(wrongCount.ToString(), Red)}");
        if (correctCount >= 8)
        {
            Console.WriteLine(C("🏆 Great job! You're a geography master!", Bright + Cyan));
        }
    }

    private void ShowStats()
    {
        if (stats.TotalQuestions == 0)
        {
            Console.WriteLine(C("📭 No data yet. Take a quiz first!", Yellow));
            return;
        }

        Console.WriteLine("\n" + C(new string('═', 50), Dim));
        Console.WriteLine(C("📊 YOUR STATISTICS", Bright + Magenta));
        Console.WriteLine(C(new string('═', 50), Dim));
        Console.WriteLine($"  Total Questions: {stats.TotalQuestions}");
        Console.WriteLine($"  Correct Answers: {C(stats.CorrectAnswers.ToString(), Green)}");
        Console.WriteLine($"  Wrong Answers:   {C(stats.WrongAnswers.ToString(), Red)}");
        Console.WriteLine($"  Accuracy:        {C($"{stats.GetAccuracy():F1}%", Cyan)}");
        Console.WriteLine($"  Current Streak:  {C(stats.CurrentStreak.ToString(), Yellow)}");
        Console.WriteLine($"  Best Streak:     {C(stats.BestStreak.ToString(), Green)}");
        Console.WriteLine(C(new string('─', 50), Dim));

        if (stats.Sessions.Count > 0)
        {
            Console.WriteLine("  Recent Sessions:");
            int start = Math.Max(0, stats.Sessions.Count - 5);
            for (int i = start; i < stats.Sessions.Count; i++)
            {
                var s = stats.Sessions[i];
                int total = s.Correct + s.Wrong;
                double acc = total > 0 ? (double)s.Correct / total * 100.0 : 0.0;
                Console.WriteLine($"    {s.Date}: {s.Correct}C / {s.Wrong}W ({acc:F1}%)");
            }
        }

        var weak = stats.GetWeaknesses(5);
        if (weak.Count > 0)
        {
            Console.WriteLine(C("  ⚠️ Weaknesses (countries with <60% accuracy):", Yellow));
            foreach (var (country, acc) in weak)
                Console.WriteLine($"    {country}: {acc:F1}%");
        }
        else
        {
            Console.WriteLine(C("  🎉 No significant weaknesses!", Green));
        }
        Console.WriteLine(C(new string('═', 50), Dim));
    }

    private void ResetStats()
    {
        if (!AskConfirm("⚠️  Delete ALL stats? This cannot be undone!")) return;
        stats.Reset();
        Console.WriteLine(C("🗑️  All statistics cleared.", Yellow));
    }

    private void ShowMenu()
    {
        Console.WriteLine("\n" + C(new string('═', 50), Cyan));
        Console.WriteLine(C("🗺️ MAIN MENU", Bright + Cyan));
        Console.WriteLine(C(new string('═', 50), Cyan));
        Console.WriteLine("  1. 🧠 Start Quiz (10 questions)");
        Console.WriteLine("  2. 📊 Show Statistics");
        Console.WriteLine("  3. 🗑️  Reset Statistics");
        Console.WriteLine("  0. 🚪 Exit");
        Console.WriteLine(C(new string('═', 50), Cyan));
    }

    public void Run()
    {
        Console.Clear();
        Console.WriteLine(C("\n🗺️ Capital Quest – Learn World Capitals", Bright + Cyan));
        Console.WriteLine(C("Master the map, one capital at a time!", Dim));

        while (true)
        {
            ShowMenu();
            string choice = Ask("Your choice: ");
            switch (choice)
            {
                case "1":
                    RunQuiz(10);
                    break;
                case "2":
                    ShowStats();
                    break;
                case "3":
                    ResetStats();
                    break;
                case "0":
                    Console.WriteLine(C("👋 Goodbye! Keep learning!", Cyan));
                    return;
                default:
                    Console.WriteLine(C("❌ Invalid choice.", Red));
                    break;
            }
            if (choice != "0")
            {
                Console.Write("\nPress Enter to continue...");
                Console.ReadLine();
            }
        }
    }

    // ─── Main ──────────────────────────────────────────────────────────────

    public static void Main(string[] args)
    {
        try
        {
            var app = new CapitalQuest();
            app.Run();
        }
        catch (Exception ex)
        {
            Console.WriteLine(C($"❌ Unexpected error: {ex.Message}", Red));
            Environment.Exit(1);
        }
    }
}
