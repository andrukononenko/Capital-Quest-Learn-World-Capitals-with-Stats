# capital_quest.cpp
/**
 * 🗺️ Capital Quest – Learn World Capitals (C++ Edition)
 * Interactive quiz with stats, weakness detection, colored output
 * Uses only STL, no external libraries
 */

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <map>
#include <set>
#include <algorithm>
#include <random>
#include <ctime>
#include <sstream>
#include <iomanip>
#include <filesystem>
#include <cstdlib>
#include <limits>

#ifdef _WIN32
#include <windows.h>
#endif

// ─── Data ────────────────────────────────────────────────────────────────────

const std::map<std::string, std::string> COUNTRIES = {
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

// ─── Colors ──────────────────────────────────────────────────────────────────

#ifdef _WIN32
HANDLE hConsole;
void setColor(int color) { SetConsoleTextAttribute(hConsole, color); }
#define RESET_COLOR setColor(7)
#define COLOR_RED setColor(12)
#define COLOR_GREEN setColor(10)
#define COLOR_YELLOW setColor(14)
#define COLOR_BLUE setColor(9)
#define COLOR_MAGENTA setColor(13)
#define COLOR_CYAN setColor(11)
#define COLOR_BRIGHT setColor(15)
#define COLOR_DIM setColor(8)
#else
#define RESET_COLOR std::cout << "\x1b[0m"
#define COLOR_RED std::cout << "\x1b[31m"
#define COLOR_GREEN std::cout << "\x1b[32m"
#define COLOR_YELLOW std::cout << "\x1b[33m"
#define COLOR_BLUE std::cout << "\x1b[34m"
#define COLOR_MAGENTA std::cout << "\x1b[35m"
#define COLOR_CYAN std::cout << "\x1b[36m"
#define COLOR_BRIGHT std::cout << "\x1b[1m"
#define COLOR_DIM std::cout << "\x1b[2m"
#endif

#define C(str, color) color << str << RESET_COLOR

// ─── Helpers ─────────────────────────────────────────────────────────────────

std::string get_today() {
    auto t = std::time(nullptr);
    auto tm = *std::localtime(&t);
    std::ostringstream oss;
    oss << std::put_time(&tm, "%Y-%m-%d");
    return oss.str();
}

std::string trim(const std::string& s) {
    auto start = s.find_first_not_of(" \t\n\r");
    if (start == std::string::npos) return "";
    auto end = s.find_last_not_of(" \t\n\r");
    return s.substr(start, end - start + 1);
}

// ─── Stats Manager ─────────────────────────────────────────────────────────

struct CountryStats {
    int asked = 0;
    int correct = 0;
};

struct Session {
    std::string date;
    int correct = 0;
    int wrong = 0;
};

struct StatsData {
    int totalQuestions = 0;
    int correctAnswers = 0;
    int wrongAnswers = 0;
    int currentStreak = 0;
    int bestStreak = 0;
    std::vector<Session> sessions;
    std::map<std::string, CountryStats> countryStats;
};

class Stats {
public:
    Stats() {
        home = get_home_dir();
        data_dir = home + "/.capital_quest";
        std::filesystem::create_directories(data_dir);
        data_file = data_dir + "/stats.json";
        load();
    }

    void load() {
        std::ifstream file(data_file);
        if (!file.is_open()) return;
        std::stringstream buffer;
        buffer << file.rdbuf();
        file.close();
        parse_json(buffer.str());
    }

    void save() {
        std::string json = to_json();
        std::string temp = data_file + ".tmp";
        std::ofstream out(temp);
        if (out.is_open()) {
            out << json;
            out.close();
            std::filesystem::rename(temp, data_file);
        }
    }

    void recordAnswer(const std::string& country, bool correct) {
        totalQuestions++;
        if (correct) {
            correctAnswers++;
            currentStreak++;
            if (currentStreak > bestStreak) bestStreak = currentStreak;
        } else {
            wrongAnswers++;
            currentStreak = 0;
        }
        auto& cs = countryStats[country];
        cs.asked++;
        if (correct) cs.correct++;
        save();
    }

    void addSession(int correct, int wrong) {
        sessions.push_back({get_today(), correct, wrong});
        if (sessions.size() > 10) sessions.erase(sessions.begin());
        save();
    }

    double getAccuracy() const {
        if (totalQuestions == 0) return 0.0;
        return static_cast<double>(correctAnswers) / totalQuestions * 100.0;
    }

    std::vector<std::pair<std::string, double>> getWeaknesses(int topN = 5) const {
        std::vector<std::pair<std::string, double>> weak;
        for (const auto& [country, stats] : countryStats) {
            if (stats.asked >= 3) {
                double acc = static_cast<double>(stats.correct) / stats.asked * 100.0;
                if (acc < 60.0) weak.push_back({country, acc});
            }
        }
        std::sort(weak.begin(), weak.end(), [](auto& a, auto& b){ return a.second < b.second; });
        if (weak.size() > topN) weak.resize(topN);
        return weak;
    }

    void reset() {
        totalQuestions = 0;
        correctAnswers = 0;
        wrongAnswers = 0;
        currentStreak = 0;
        bestStreak = 0;
        sessions.clear();
        countryStats.clear();
        save();
    }

    // Public fields for easy access
    int totalQuestions;
    int correctAnswers;
    int wrongAnswers;
    int currentStreak;
    int bestStreak;
    std::vector<Session> sessions;
    std::map<std::string, CountryStats> countryStats;

private:
    std::string home, data_dir, data_file;

    std::string get_home_dir() {
#ifdef _WIN32
        const char* h = std::getenv("USERPROFILE");
#else
        const char* h = std::getenv("HOME");
#endif
        return h ? std::string(h) : ".";
    }

    void parse_json(const std::string& json) {
        // Simple manual parsing for our format
        auto find_value = [&](const std::string& key) -> std::string {
            size_t pos = json.find("\"" + key + "\":");
            if (pos == std::string::npos) return "";
            pos = json.find(":", pos) + 1;
            while (pos < json.length() && (json[pos] == ' ' || json[pos] == '\n' || json[pos] == '\r')) pos++;
            if (json[pos] == '"') {
                pos++;
                size_t end = json.find("\"", pos);
                if (end == std::string::npos) return "";
                return json.substr(pos, end - pos);
            } else {
                size_t end = json.find_first_of(",}\n\r", pos);
                if (end == std::string::npos) return "";
                return json.substr(pos, end - pos);
            }
        };

        totalQuestions = std::stoi(find_value("totalQuestions"));
        correctAnswers = std::stoi(find_value("correctAnswers"));
        wrongAnswers = std::stoi(find_value("wrongAnswers"));
        currentStreak = std::stoi(find_value("currentStreak"));
        bestStreak = std::stoi(find_value("bestStreak"));
        // Sessions and countryStats parsing omitted for brevity (C++ version simplified)
        // In a full version, we would parse JSON properly with a library.
        // For demo, we keep them empty.
    }

    std::string to_json() const {
        std::ostringstream json;
        json << "{\n";
        json << "  \"totalQuestions\": " << totalQuestions << ",\n";
        json << "  \"correctAnswers\": " << correctAnswers << ",\n";
        json << "  \"wrongAnswers\": " << wrongAnswers << ",\n";
        json << "  \"currentStreak\": " << currentStreak << ",\n";
        json << "  \"bestStreak\": " << bestStreak << ",\n";
        json << "  \"sessions\": [\n";
        for (size_t i = 0; i < sessions.size(); ++i) {
            const auto& s = sessions[i];
            json << "    { \"date\": \"" << s.date << "\", \"correct\": " << s.correct << ", \"wrong\": " << s.wrong << " }";
            if (i+1 < sessions.size()) json << ",";
            json << "\n";
        }
        json << "  ],\n";
        json << "  \"countryStats\": {\n";
        size_t idx = 0;
        for (const auto& [country, stats] : countryStats) {
            json << "    \"" << country << "\": { \"asked\": " << stats.asked << ", \"correct\": " << stats.correct << " }";
            if (++idx < countryStats.size()) json << ",";
            json << "\n";
        }
        json << "  }\n";
        json << "}";
        return json.str();
    }
};

// ─── Quiz Engine ────────────────────────────────────────────────────────────

class CapitalQuiz {
public:
    CapitalQuiz() : rng(std::random_device{}()) {
        stats = new Stats();
        for (const auto& [country, _] : COUNTRIES) {
            countryList.push_back(country);
        }
    }

    ~CapitalQuiz() { delete stats; }

    void run() {
        std::cout << "\033[2J\033[1;1H";
        std::cout << C("\n🗺️ Capital Quest – Learn World Capitals", COLOR_BRIGHT) << C("", COLOR_CYAN) << std::endl;
        std::cout << C("Master the map, one capital at a time!", COLOR_DIM) << std::endl;

        while (true) {
            showMenu();
            std::string choice = ask("Your choice: ");
            if (choice == "1") {
                runQuiz(10);
            } else if (choice == "2") {
                showStats();
            } else if (choice == "3") {
                resetStats();
            } else if (choice == "0") {
                std::cout << C("👋 Goodbye! Keep learning!", COLOR_CYAN) << std::endl;
                break;
            } else {
                std::cout << C("❌ Invalid choice.", COLOR_RED) << std::endl;
            }
            if (choice != "0") {
                std::cout << "\nPress Enter to continue...";
                std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
                std::cin.get();
            }
        }
    }

private:
    Stats* stats;
    std::vector<std::string> countryList;
    std::mt19937 rng;

    std::string ask(const std::string& prompt) {
        std::cout << prompt;
        std::string line;
        std::getline(std::cin, line);
        return trim(line);
    }

    int askInt(const std::string& prompt, int max) {
        while (true) {
            std::string ans = ask(prompt);
            try {
                int val = std::stoi(ans);
                if (val >= 1 && val <= max) return val;
            } catch (...) {}
            std::cout << C("Please enter a number between 1 and " + std::to_string(max), COLOR_YELLOW) << std::endl;
        }
    }

    bool askConfirm(const std::string& prompt) {
        std::string ans = ask(prompt + " (yes/no): ");
        std::string lower = ans;
        std::transform(lower.begin(), lower.end(), lower.begin(), ::tolower);
        return lower == "yes" || lower == "y";
    }

    void showProgress(int current, int total) {
        int barLen = 30;
        int filled = current * barLen / total;
        std::string bar = std::string(filled, '█') + std::string(barLen - filled, '░');
        std::cout << "\r  " << bar << " " << current << "/" << total;
        std::cout.flush();
    }

    std::tuple<std::string, std::string, std::vector<std::string>> getQuestion() {
        std::uniform_int_distribution<int> dist(0, countryList.size()-1);
        std::string country = countryList[dist(rng)];
        std::string correct = COUNTRIES.at(country);

        // Wrong options
        std::vector<std::string> allCapitals;
        for (const auto& [_, cap] : COUNTRIES) allCapitals.push_back(cap);
        std::vector<std::string> wrongs;
        for (const auto& cap : allCapitals) {
            if (cap != correct) wrongs.push_back
