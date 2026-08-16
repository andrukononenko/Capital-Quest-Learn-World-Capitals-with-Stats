# capital_quest.go
/**
 * 🗺️ Capital Quest – Learn World Capitals (Go Edition)
 * Interactive quiz with stats, weakness detection, colorful CLI
 */

package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"math/rand"
	"os"
	"path/filepath"
	"sort"
	"strconv"
	"strings"
	"time"
)

// ─── Types ──────────────────────────────────────────────────────────────────

type CountryStats struct {
	Asked   int `json:"asked"`
	Correct int `json:"correct"`
}

type Session struct {
	Date    string `json:"date"`
	Correct int    `json:"correct"`
	Wrong   int    `json:"wrong"`
}

type StatsData struct {
	TotalQuestions int                    `json:"totalQuestions"`
	CorrectAnswers int                    `json:"correctAnswers"`
	WrongAnswers   int                    `json:"wrongAnswers"`
	CurrentStreak  int                    `json:"currentStreak"`
	BestStreak     int                    `json:"bestStreak"`
	Sessions       []Session              `json:"sessions"`
	CountryStats   map[string]CountryStats `json:"countryStats"`
}

// ─── Data ────────────────────────────────────────────────────────────────────

var countries = map[string]string{
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

// ─── Colors ──────────────────────────────────────────────────────────────────

const (
	reset   = "\x1b[0m"
	bright  = "\x1b[1m"
	dim     = "\x1b[2m"
	red     = "\x1b[31m"
	green   = "\x1b[32m"
	yellow  = "\x1b[33m"
	blue    = "\x1b[34m"
	magenta = "\x1b[35m"
	cyan    = "\x1b[36m"
)

func c(str, color string) string {
	return color + str + reset
}

// ─── Stats Manager ──────────────────────────────────────────────────────────

type Stats struct {
	dataDir  string
	dataFile string
	TotalQuestions int
	CorrectAnswers int
	WrongAnswers   int
	CurrentStreak  int
	BestStreak     int
	Sessions       []Session
	CountryStats   map[string]CountryStats
}

func NewStats() *Stats {
	home, _ := os.UserHomeDir()
	dataDir := filepath.Join(home, ".capital_quest")
	os.MkdirAll(dataDir, 0755)
	s := &Stats{
		dataDir:    dataDir,
		dataFile:   filepath.Join(dataDir, "stats.json"),
		CountryStats: make(map[string]CountryStats),
	}
	s.load()
	return s
}

func (s *Stats) load() {
	if _, err := os.Stat(s.dataFile); os.IsNotExist(err) {
		return
	}
	raw, err := ioutil.ReadFile(s.dataFile)
	if err != nil {
		return
	}
	var data StatsData
	if err := json.Unmarshal(raw, &data); err != nil {
		return
	}
	s.TotalQuestions = data.TotalQuestions
	s.CorrectAnswers = data.CorrectAnswers
	s.WrongAnswers = data.WrongAnswers
	s.CurrentStreak = data.CurrentStreak
	s.BestStreak = data.BestStreak
	s.Sessions = data.Sessions
	s.CountryStats = data.CountryStats
	if s.CountryStats == nil {
		s.CountryStats = make(map[string]CountryStats)
	}
}

func (s *Stats) save() {
	data := StatsData{
		TotalQuestions: s.TotalQuestions,
		CorrectAnswers: s.CorrectAnswers,
		WrongAnswers:   s.WrongAnswers,
		CurrentStreak:  s.CurrentStreak,
		BestStreak:     s.BestStreak,
		Sessions:       s.Sessions,
		CountryStats:   s.CountryStats,
	}
	raw, _ := json.MarshalIndent(data, "", "  ")
	ioutil.WriteFile(s.dataFile, raw, 0644)
}

func (s *Stats) RecordAnswer(country string, correct bool) {
	s.TotalQuestions++
	if correct {
		s.CorrectAnswers++
		s.CurrentStreak++
		if s.CurrentStreak > s.BestStreak {
			s.BestStreak = s.CurrentStreak
		}
	} else {
		s.WrongAnswers++
		s.CurrentStreak = 0
	}
	cs := s.CountryStats[country]
	cs.Asked++
	if correct {
		cs.Correct++
	}
	s.CountryStats[country] = cs
	s.save()
}

func (s *Stats) AddSession(correct, wrong int) {
	s.Sessions = append(s.Sessions, Session{
		Date:    time.Now().Format("2006-01-02"),
		Correct: correct,
		Wrong:   wrong,
	})
	if len(s.Sessions) > 10 {
		s.Sessions = s.Sessions[len(s.Sessions)-10:]
	}
	s.save()
}

func (s *Stats) GetAccuracy() float64 {
	if s.TotalQuestions == 0 {
		return 0
	}
	return float64(s.CorrectAnswers) / float64(s.TotalQuestions) * 100
}

func (s *Stats) GetWeaknesses(topN int) [][2]interface{} {
	type pair struct {
		Country string
		Acc     float64
	}
	var weak []pair
	for country, stats := range s.CountryStats {
		if stats.Asked >= 3 {
			acc := float64(stats.Correct) / float64(stats.Asked) * 100
			if acc < 60 {
				weak = append(weak, pair{country, acc})
			}
		}
	}
	sort.Slice(weak, func(i, j int) bool { return weak[i].Acc < weak[j].Acc })
	var result [][2]interface{}
	for i := 0; i < topN && i < len(weak); i++ {
		result = append(result, [2]interface{}{weak[i].Country, weak[i].Acc})
	}
	return result
}

func (s *Stats) Reset() {
	s.TotalQuestions = 0
	s.CorrectAnswers = 0
	s.WrongAnswers = 0
	s.CurrentStreak = 0
	s.BestStreak = 0
	s.Sessions = nil
	s.CountryStats = make(map[string]CountryStats)
	s.save()
}

// ─── Quiz Engine ────────────────────────────────────────────────────────────

type CapitalQuiz struct {
	stats      *Stats
	countryList []string
	reader     *bufio.Reader
}

func NewCapitalQuiz() *CapitalQuiz {
	rand.Seed(time.Now().UnixNano())
	countryList := make([]string, 0, len(countries))
	for k := range countries {
		countryList = append(countryList, k)
	}
	return &CapitalQuiz{
		stats:      NewStats(),
		countryList: countryList,
		reader:     bufio.NewReader(os.Stdin),
	}
}

func (q *CapitalQuiz) ask(prompt string) string {
	fmt.Print(prompt)
	line, _ := q.reader.ReadString('\n')
	return strings.TrimSpace(line)
}

func (q *CapitalQuiz) askInt(prompt string, max int) int {
	for {
		ans := q.ask(prompt)
		if num, err := strconv.Atoi(ans); err == nil && num >= 1 && num <= max {
			return num
		}
		fmt.Println(c(fmt.Sprintf("Please enter a number between 1 and %d", max), yellow))
	}
}

func (q *CapitalQuiz) askConfirm(prompt string) bool {
	ans := q.ask(prompt + " (yes/no): ")
	ans = strings.ToLower(ans)
	return ans == "yes" || ans == "y"
}

func (q *CapitalQuiz) getQuestion() (country, correct string, options []string) {
	country = q.countryList[rand.Intn(len(q.countryList))]
	correct = countries[country]
	allCapitals := make([]string, 0, len(countries))
	for _, v := range countries {
		allCapitals = append(allCapitals, v)
	}
	var wrongs []string
	for _, c := range allCapitals {
		if c != correct {
			wrongs = append(wrongs, c)
		}
	}
	rand.Shuffle(len(wrongs), func(i, j int) { wrongs[i], wrongs[j] = wrongs[j], wrongs[i] })
	if len(wrongs) > 3 {
		wrongs = wrongs[:3]
	}
	for len(wrongs) < 3 {
		wrongs = append(wrongs, "Unknown")
	}
	options = append([]string{correct}, wrongs...)
	rand.Shuffle(len(options), func(i, j int) { options[i], options[j] = options[j], options[i] })
	return
}

func (q *CapitalQuiz) showProgress(current, total int) {
	barLen := 30
	filled := current * barLen / total
	bar := strings.Repeat("█", filled) + strings.Repeat("░", barLen-filled)
	fmt.Printf("\r  %s %d/%d", bar, current, total)
}

func (q *CapitalQuiz) runQuiz(rounds int) {
	correctCount := 0
	wrongCount := 0

	fmt.Println(c("\n🧠 Starting Quiz! Answer 10 questions.", bright+cyan))

	for i := 0; i < rounds; i++ {
		country, correct, options := q.getQuestion()
		fmt.Printf("\n%s What is the capital of %s?\n", c(fmt.Sprintf("Q%d.", i+1), yellow), c(country, bright))
		for idx, opt := range options {
			fmt.Printf("  %d. %s\n", idx+1, opt)
		}
		choice := q.askInt("Your choice (1-4): ", 4)
		selected := options[choice-1]
		isCorrect := selected == correct
		if isCorrect {
			correctCount++
			fmt.Println(c(fmt.Sprintf("✅ Correct! %s → %s", country, correct), green))
		} else {
			wrongCount++
			fmt.Println(c(fmt.Sprintf("❌ Wrong! %s → %s (you said %s)", country, correct, selected), red))
			if cs, ok := q.stats.CountryStats[country]; ok && cs.Asked >= 3 {
				acc := float64(cs.Correct) / float64(cs.Asked) * 100
				fmt.Println(c(fmt.Sprintf("  Your accuracy for %s: %.1f%%", country, acc), dim))
			}
		}
		q.stats.RecordAnswer(country, isCorrect)
		q.showProgress(i+1, rounds)
	}

	q.stats.AddSession(correctCount, wrongCount)
	fmt.Printf("\n\n%s Correct: %s, Wrong: %s\n", c("Quiz finished!", bright), c(fmt.Sprintf("%d", correctCount), green), c(fmt.Sprintf("%d", wrongCount), red))
	if correctCount >= 8 {
		fmt.Println(c("🏆 Great job! You're a geography master!", bright+cyan))
	}
}

func (q *CapitalQuiz) showStats() {
	if q.stats.TotalQuestions == 0 {
		fmt.Println(c("📭 No data yet. Take a quiz first!", yellow))
		return
	}

	fmt.Println("\n" + c(strings.Repeat("═", 50), dim))
	fmt.Println(c("📊 YOUR STATISTICS", bright+magenta))
	fmt.Println(c(strings.Repeat("═", 50), dim))
	fmt.Printf("  Total Questions: %d\n", q.stats.TotalQuestions)
	fmt.Printf("  Correct Answers: %s\n", c(fmt.Sprintf("%d", q.stats.CorrectAnswers), green))
	fmt.Printf("  Wrong Answers:   %s\n", c(fmt.Sprintf("%d", q.stats.WrongAnswers), red))
	fmt.Printf("  Accuracy:        %s\n", c(fmt.Sprintf("%.1f%%", q.stats.GetAccuracy()), cyan))
	fmt.Printf("  Current Streak:  %s\n", c(fmt.Sprintf("%d", q.stats.CurrentStreak), yellow))
	fmt.Printf("  Best Streak:     %s\n", c(fmt.Sprintf("%d", q.stats.BestStreak), green))
	fmt.Println(c(strings.Repeat("─", 50), dim))

	if len(q.stats.Sessions) > 0 {
		fmt.Println("  Recent Sessions:")
		start := 0
		if len(q.stats.Sessions) > 5 {
			start = len(q.stats.Sessions) - 5
		}
		for _, s := range q.stats.Sessions[start:] {
			total := s.Correct + s.Wrong
			acc := 0.0
			if total > 0 {
				acc = float64(s.Correct) / float64(total) * 100
			}
			fmt.Printf("    %s: %dC / %dW (%.1f%%)\n", s.Date, s.Correct, s.Wrong, acc)
		}
	}

	weak := q.stats.GetWeaknesses(5)
	if len(weak) > 0 {
		fmt.Println(c("  ⚠️ Weaknesses (countries with <60% accuracy):", yellow))
		for _, w := range weak {
			fmt.Printf("    %s: %.1f%%\n", w[0], w[1])
		}
	} else {
		fmt.Println(c("  🎉 No significant weaknesses!", green))
	}
	fmt.Println(c(strings.Repeat("═", 50), dim))
}

func (q *CapitalQuiz) resetStats() {
	if !q.askConfirm("⚠️  Delete ALL stats? This cannot be undone!") {
		return
	}
	q.stats.Reset()
	fmt.Println(c("🗑️  All statistics cleared.", yellow))
}

func (q *CapitalQuiz) run() {
	fmt.Print("\033[H\033[2J")
	fmt.Println(c("\n🗺️ Capital Quest – Learn World Capitals", bright+cyan))
	fmt.Println(c("Master the map, one capital at a time!", dim))

	for {
		q.showMenu()
		choice := q.ask("Your choice: ")
		switch choice {
		case "1":
			q.runQuiz(10)
		case "2":
			q.showStats()
		case "3":
			q.resetStats()
		case "0":
			fmt.Println(c("👋 Goodbye! Keep learning!", cyan))
			return
		default:
			fmt.Println(c("❌ Invalid choice.", red))
		}
		if choice != "0" {
			fmt.Print("\nPress Enter to continue...")
			q.reader.ReadString('\n')
		}
	}
}

func (q *CapitalQuiz) showMenu() {
	fmt.Println("\n" + c(strings.Repeat("═", 50), cyan))
	fmt.Println(c("🗺️ MAIN MENU", bright+cyan))
	fmt.Println(c(strings.Repeat("═", 50), cyan))
	fmt.Println("  1. 🧠 Start Quiz (10 questions)")
	fmt.Println("  2. 📊 Show Statistics")
	fmt.Println("  3. 🗑️  Reset Statistics")
	fmt.Println("  0. 🚪 Exit")
	fmt.Println(c(strings.Repeat("═", 50), cyan))
}

// ─── Main ──────────────────────────────────────────────────────────────────

func main() {
	app := NewCapitalQuiz()
	app.run()
}
