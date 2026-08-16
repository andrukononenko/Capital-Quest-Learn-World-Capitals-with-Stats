# CapitalQuest.java
/**
 * 🗺️ Capital Quest – Learn World Capitals (Java Edition)
 * Interactive quiz with stats, weakness detection, colorful CLI
 * Requires: Java 17+
 */

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

// ─── Data Classes ──────────────────────────────────────────────────────────

class CountryStats {
    public int asked;
    public int correct;
}

class SessionData {
    public String date;
    public int correct;
    public int wrong;
}

class StatsData {
    public int totalQuestions = 0;
    public int correctAnswers = 0;
    public int wrongAnswers = 0;
    public int currentStreak = 0;
    public int bestStreak = 0;
    public List<SessionData> sessions = new ArrayList<>();
    public Map<String, CountryStats> countryStats = new HashMap<>();
}

// ─── Main App ──────────────────────────────────────────────────────────────

public class CapitalQuest {
    // ─── Data ──────────────────────────────────────────────────────────────

    private static final Map<String, String> COUNTRIES = new LinkedHashMap<>();
    static {
        COUNTRIES.put("Afghanistan", "Kabul");
        COUNTRIES.put("Albania", "Tirana");
        COUNTRIES.put("Algeria", "Algiers");
        COUNTRIES.put("Andorra", "Andorra la Vella");
        COUNTRIES.put("Angola", "Luanda");
        COUNTRIES.put("Argentina", "Buenos Aires");
        COUNTRIES.put("Armenia", "Yerevan");
        COUNTRIES.put("Australia", "Canberra");
        COUNTRIES.put("Austria", "Vienna");
        COUNTRIES.put("Azerbaijan", "Baku");
        COUNTRIES.put("Bahamas", "Nassau");
        COUNTRIES.put("Bahrain", "Manama");
        COUNTRIES.put("Bangladesh", "Dhaka");
        COUNTRIES.put("Barbados", "Bridgetown");
        COUNTRIES.put("Belarus", "Minsk");
        COUNTRIES.put("Belgium", "Brussels");
        COUNTRIES.put("Belize", "Belmopan");
        COUNTRIES.put("Benin", "Porto-Novo");
        COUNTRIES.put("Bhutan", "Thimphu");
        COUNTRIES.put("Bolivia", "Sucre");
        COUNTRIES.put("Bosnia", "Sarajevo");
        COUNTRIES.put("Botswana", "Gaborone");
        COUNTRIES.put("Brazil", "Brasilia");
        COUNTRIES.put("Brunei", "Bandar Seri Begawan");
        COUNTRIES.put("Bulgaria", "Sofia");
        COUNTRIES.put("Burkina Faso", "Ouagadougou");
        COUNTRIES.put("Burundi", "Gitega");
        COUNTRIES.put("Cambodia", "Phnom Penh");
        COUNTRIES.put("Cameroon", "Yaounde");
        COUNTRIES.put("Canada", "Ottawa");
        COUNTRIES.put("Cape Verde", "Praia");
        COUNTRIES.put("Central African Republic", "Bangui");
        COUNTRIES.put("Chad", "N'Djamena");
        COUNTRIES.put("Chile", "Santiago");
        COUNTRIES.put("China", "Beijing");
        COUNTRIES.put("Colombia", "Bogota");
        COUNTRIES.put("Comoros", "Moroni");
        COUNTRIES.put("Congo", "Brazzaville");
        COUNTRIES.put("Costa Rica", "San Jose");
        COUNTRIES.put("Croatia", "Zagreb");
        COUNTRIES.put("Cuba", "Havana");
        COUNTRIES.put("Cyprus", "Nicosia");
        COUNTRIES.put("Czech Republic", "Prague");
        COUNTRIES.put("Denmark", "Copenhagen");
        COUNTRIES.put("Djibouti", "Djibouti");
        COUNTRIES.put("Dominican Republic", "Santo Domingo");
        COUNTRIES.put("Ecuador", "Quito");
        COUNTRIES.put("Egypt", "Cairo");
        COUNTRIES.put("El Salvador", "San Salvador");
        COUNTRIES.put("Eritrea", "Asmara");
        COUNTRIES.put("Estonia", "Tallinn");
        COUNTRIES.put("Eswatini", "Mbabane");
        COUNTRIES.put("Ethiopia", "Addis Ababa");
        COUNTRIES.put("Fiji", "Suva");
        COUNTRIES.put("Finland", "Helsinki");
        COUNTRIES.put("France", "Paris");
        COUNTRIES.put("Gabon", "Libreville");
        COUNTRIES.put("Gambia", "Banjul");
        COUNTRIES.put("Georgia", "Tbilisi");
        COUNTRIES.put("Germany", "Berlin");
        COUNTRIES.put("Ghana", "Accra");
        COUNTRIES.put("Greece", "Athens");
        COUNTRIES.put("Guatemala", "Guatemala City");
        COUNTRIES.put("Guinea", "Conakry");
        COUNTRIES.put("Guyana", "Georgetown");
        COUNTRIES.put("Haiti", "Port-au-Prince");
        COUNTRIES.put("Honduras", "Tegucigalpa");
        COUNTRIES.put("Hungary", "Budapest");
        COUNTRIES.put("Iceland", "Reykjavik");
        COUNTRIES.put("India", "New Delhi");
        COUNTRIES.put("Indonesia", "Jakarta");
        COUNTRIES.put("Iran", "Tehran");
        COUNTRIES.put("Iraq", "Baghdad");
        COUNTRIES.put("Ireland", "Dublin");
        COUNTRIES.put("Israel", "Jerusalem");
        COUNTRIES.put("Italy", "Rome");
        COUNTRIES.put("Jamaica", "Kingston");
        COUNTRIES.put("Japan", "Tokyo");
        COUNTRIES.put("Jordan", "Amman");
        COUNTRIES.put("Kazakhstan", "Nur-Sultan");
        COUNTRIES.put("Kenya", "Nairobi");
        COUNTRIES.put("Kuwait", "Kuwait City");
        COUNTRIES.put("Kyrgyzstan", "Bishkek");
        COUNTRIES.put("Laos", "Vientiane");
        COUNTRIES.put("Latvia", "Riga");
        COUNTRIES.put("Lebanon", "Beirut");
        COUNTRIES.put("Lesotho", "Maseru");
        COUNTRIES.put("Liberia", "Monrovia");
        COUNTRIES.put("Libya", "Tripoli");
        COUNTRIES.put("Liechtenstein", "Vaduz");
        COUNTRIES.put("Lithuania", "Vilnius");
        COUNTRIES.put("Luxembourg", "Luxembourg");
        COUNTRIES.put("Madagascar", "Antananarivo");
        COUNTRIES.put("Malawi", "Lilongwe");
        COUNTRIES.put("Malaysia", "Kuala Lumpur");
        COUNTRIES.put("Maldives", "Male");
        COUNTRIES.put("Mali", "Bamako");
        COUNTRIES.put("Malta", "Valletta");
        COUNTRIES.put("Marshall Islands", "Majuro");
        COUNTRIES.put("Mauritania", "Nouakchott");
        COUNTRIES.put("Mauritius", "Port Louis");
        COUNTRIES.put("Mexico", "Mexico City");
        COUNTRIES.put("Micronesia", "Palikir");
        COUNTRIES.put("Moldova", "Chisinau");
        COUNTRIES.put("Monaco", "Monaco");
        COUNTRIES.put("Mongolia", "Ulaanbaatar");
        COUNTRIES.put("Montenegro", "Podgorica");
        COUNTRIES.put("Morocco", "Rabat");
        COUNTRIES.put("Mozambique", "Maputo");
        COUNTRIES.put("Myanmar", "Naypyidaw");
        COUNTRIES.put("Namibia", "Windhoek");
        COUNTRIES.put("Nauru", "Yaren");
        COUNTRIES.put("Nepal", "Kathmandu");
        COUNTRIES.put("Netherlands", "Amsterdam");
        COUNTRIES.put("New Zealand", "Wellington");
        COUNTRIES.put("Nicaragua", "Managua");
        COUNTRIES.put("Niger", "Niamey");
        COUNTRIES.put("Nigeria", "Abuja");
        COUNTRIES.put("North Korea", "Pyongyang");
        COUNTRIES.put("Norway", "Oslo");
        COUNTRIES.put("Oman", "Muscat");
        COUNTRIES.put("Pakistan", "Islamabad");
        COUNTRIES.put("Palau", "Ngerulmud");
        COUNTRIES.put("Panama", "Panama City");
        COUNTRIES.put("Papua New Guinea", "Port Moresby");
        COUNTRIES.put("Paraguay", "Asuncion");
        COUNTRIES.put("Peru", "Lima");
        COUNTRIES.put("Philippines", "Manila");
        COUNTRIES.put("Poland", "Warsaw");
        COUNTRIES.put("Portugal", "Lisbon");
        COUNTRIES.put("Qatar", "Doha");
        COUNTRIES.put("Romania", "Bucharest");
        COUNTRIES.put("Russia", "Moscow");
        COUNTRIES.put("Rwanda", "Kigali");
        COUNTRIES.put("Saint Kitts", "Basseterre");
        COUNTRIES.put("Saint Lucia", "Castries");
        COUNTRIES.put("Samoa", "Apia");
        COUNTRIES.put("San Marino", "San Marino");
        COUNTRIES.put("Sao Tome", "Sao Tome");
        COUNTRIES.put("Saudi Arabia", "Riyadh");
        COUNTRIES.put("Senegal", "Dakar");
        COUNTRIES.put("Serbia", "Belgrade");
        COUNTRIES.put("Seychelles", "Victoria");
        COUNTRIES.put("Sierra Leone", "Freetown");
        COUNTRIES.put("Singapore", "Singapore");
        COUNTRIES.put("Slovakia", "Bratislava");
        COUNTRIES.put("Slovenia", "Ljubljana");
        COUNTRIES.put("Solomon Islands", "Honiara");
        COUNTRIES.put("Somalia", "Mogadishu");
        COUNTRIES.put("South Africa", "Pretoria");
        COUNTRIES.put("South Korea", "Seoul");
        COUNTRIES.put("South Sudan", "Juba");
        COUNTRIES.put("Spain", "Madrid");
        COUNTRIES.put("Sri Lanka", "Sri Jayawardenepura Kotte");
        COUNTRIES.put("Sudan", "Khartoum");
        COUNTRIES.put("Suriname", "Paramaribo");
        COUNTRIES.put("Sweden", "Stockholm");
        COUNTRIES.put("Switzerland", "Bern");
        COUNTRIES.put("Syria", "Damascus");
        COUNTRIES.put("Taiwan", "Taipei");
        COUNTRIES.put("Tajikistan", "Dushanbe");
        COUNTRIES.put("Tanzania", "Dodoma");
        COUNTRIES.put("Thailand", "Bangkok");
        COUNTRIES.put("Timor-Leste", "Dili");
        COUNTRIES.put("Togo", "Lome");
        COUNTRIES.put("Tonga", "Nuku'alofa");
        COUNTRIES.put("Trinidad and Tobago", "Port of Spain");
        COUNTRIES.put("Tunisia", "Tunis");
        COUNTRIES.put("Turkey", "Ankara");
        COUNTRIES.put("Turkmenistan", "Ashgabat");
        COUNTRIES.put("Tuvalu", "Funafuti");
        COUNTRIES.put("Uganda", "Kampala");
        COUNTRIES.put("Ukraine", "Kyiv");
        COUNTRIES.put("United Arab Emirates", "Abu Dhabi");
        COUNTRIES.put("United Kingdom", "London");
        COUNTRIES.put("United States", "Washington");
        COUNTRIES.put("Uruguay", "Montevideo");
        COUNTRIES.put("Uzbekistan", "Tashkent");
        COUNTRIES.put("Vanuatu", "Port Vila");
        COUNTRIES.put("Vatican City", "Vatican City");
        COUNTRIES.put("Venezuela", "Caracas");
        COUNTRIES.put("Vietnam", "Hanoi");
        COUNTRIES.put("Yemen", "Sana'a");
        COUNTRIES.put("Zambia", "Lusaka");
        COUNTRIES.put("Zimbabwe", "Harare");
    }

    // ─── Colors ─────────────────────────────────────────────────────────────

    private static final String RESET = "\u001B[0m";
    private static final String BRIGHT = "\u001B[1m";
    private static final String DIM = "\u001B[2m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String MAGENTA = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    private static String c(String text, String color) {
        return color + text + RESET;
    }

    // ─── Stats Manager ─────────────────────────────────────────────────────

    private static class Stats {
        private final String dataDir;
        private final String dataFile;
        public int totalQuestions = 0;
        public int correctAnswers = 0;
        public int wrongAnswers = 0;
        public int currentStreak = 0;
        public int bestStreak = 0;
        public List<SessionData> sessions = new ArrayList<>();
        public Map<String, CountryStats> countryStats = new HashMap<>();

        public Stats() {
            String home = System.getProperty("user.home");
            dataDir = home + "/.capital_quest";
            dataFile = dataDir + "/stats.json";
            try {
                Files.createDirectories(Paths.get(dataDir));
            } catch (IOException ignored) {}
            load();
        }

        private void load() {
            Path path = Paths.get(dataFile);
            if (!Files.exists(path)) return;
            try {
                String json = new String(Files.readAllBytes(path));
                // Simple parse (for brevity, in production use a JSON library)
                // We'll read fields manually
                totalQuestions = extractInt(json, "totalQuestions");
                correctAnswers = extractInt(json, "correctAnswers");
                wrongAnswers = extractInt(json, "wrongAnswers");
                currentStreak = extractInt(json, "currentStreak");
                bestStreak = extractInt(json, "bestStreak");
                // Sessions and countryStats not parsed for simplicity
            } catch (Exception ignored) {}
        }

        private int extractInt(String json, String key) {
            int idx = json.indexOf("\"" + key + "\":");
            if (idx < 0) return 0;
            int start = json.indexOf(":", idx) + 1;
            int end = json.indexOf(",", start);
            if (end < 0) end = json.indexOf("}", start);
            if (end < 0) return 0;
            try {
                return Integer.parseInt(json.substring(start, end).trim());
            } catch (NumberFormatException e) { return 0; }
        }

        private void save() {
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"totalQuestions\": ").append(totalQuestions).append(",\n");
            json.append("  \"correctAnswers\": ").append(correctAnswers).append(",\n");
            json.append("  \"wrongAnswers\": ").append(wrongAnswers).append(",\n");
            json.append("  \"currentStreak\": ").append(currentStreak).append(",\n");
            json.append("  \"bestStreak\": ").append(bestStreak).append(",\n");
            json.append("  \"sessions\": [\n");
            for (int i = 0; i < sessions.size(); i++) {
                SessionData s = sessions.get(i);
                json.append("    { \"date\": \"").append(s.date).append("\", \"correct\": ").append(s.correct).append(", \"wrong\": ").append(s.wrong).append(" }");
                if (i < sessions.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("  ],\n");
            json.append("  \"countryStats\": {\n");
            int idx = 0;
            for (Map.Entry<String, CountryStats> e : countryStats.entrySet()) {
                json.append("    \"").append(e.getKey()).append("\": { \"asked\": ").append(e.getValue().asked).append(", \"correct\": ").append(e.getValue().correct).append(" }");
                if (++idx < countryStats.size()) json.append(",");
                json.append("\n");
            }
            json.append("  }\n");
            json.append("}");
            try {
                Files.write(Paths.get(dataFile), json.toString().getBytes());
            } catch (IOException ignored) {}
        }

        public void recordAnswer(String country, boolean correct) {
            totalQuestions++;
            if (correct) {
                correctAnswers++;
                currentStreak++;
                if (currentStreak > bestStreak) bestStreak = currentStreak;
            } else {
                wrongAnswers++;
                currentStreak = 0;
            }
            CountryStats cs = countryStats.computeIfAbsent(country, k -> new CountryStats());
            cs.asked++;
            if (correct) cs.correct++;
            save();
        }

        public void addSession(int correct, int wrong) {
            SessionData s = new SessionData();
            s.date = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            s.correct = correct;
            s.wrong = wrong;
            sessions.add(s);
            if (sessions.size() > 10) sessions.remove(0);
            save();
        }

        public double getAccuracy() {
            if (totalQuestions == 0) return 0.0;
            return (double) correctAnswers / totalQuestions * 100.0;
        }

        public List<Map.Entry<String, Double>> getWeaknesses(int topN) {
            List<Map.Entry<String, Double>> weak = new ArrayList<>();
            for (Map.Entry<String, CountryStats> e : countryStats.entrySet()) {
                CountryStats cs = e.getValue();
                if (cs.asked >= 3) {
                    double acc = (double) cs.correct / cs.asked * 100.0;
                    if (acc < 60.0) weak.add(new AbstractMap.SimpleEntry<>(e.getKey(), acc));
                }
            }
            weak.sort(Comparator.comparingDouble(Map.Entry::getValue));
            if (weak.size() > topN) weak = weak.subList(0, topN);
            return weak;
        }

        public void reset() {
            totalQuestions = 0;
            correctAnswers = 0;
            wrongAnswers = 0;
            currentStreak = 0;
            bestStreak = 0;
            sessions.clear();
            countryStats.clear();
            save();
        }
    }

    // ─── Quiz Engine ──────────────────────────────────────────────────────

    private final Stats stats;
    private final List<String> countryList;
    private final Scanner scanner;
    private final Random random;

    public CapitalQuest() {
        stats = new Stats();
        countryList = new ArrayList<>(COUNTRIES.keySet());
        scanner = new Scanner(System.in);
        random = new Random();
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int askInt(String prompt, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(ask(prompt));
                if (val >= 1 && val <= max) return val;
            } catch (NumberFormatException ignored) {}
            System.out.println(c("Please enter a number between 1 and " + max, YELLOW));
        }
    }

    private boolean askConfirm(String prompt) {
        String ans = ask(prompt + " (yes/no): ").toLowerCase();
        return ans.equals("yes") || ans.equals("y");
    }

    private void showProgress(int current, int total) {
        int barLen = 30;
        int filled = current * barLen / total;
        String bar = "█".repeat(filled) + "░".repeat(barLen - filled);
        System.out.print("\r  " + bar + " " + current + "/" + total);
        System.out.flush();
    }

    private String[] getQuestion() {
        String country = countryList.get(random.nextInt(countryList.size()));
        String correct = COUNTRIES.get(country);

        List<String> allCapitals = new ArrayList<>(COUNTRIES.values());
        List<String> wrongs = new ArrayList<>();
        for (String cap : allCapitals) {
            if (!cap.equals(correct)) wrongs.add(cap);
        }
        Collections.shuffle(wrongs, random);
        while (wrongs.size() > 3) wrongs.remove(wrongs.size() - 1);
        while (wrongs.size() < 3) wrongs.add("Unknown");

        List<String> options = new ArrayList<>();
        options.add(correct);
        options.addAll(wrongs);
        Collections.shuffle(options, random);
        return new String[]{country, correct, options.get(0), options.get(1), options.get(2), options.get(3)};
    }

    private void runQuiz(int rounds) {
        int correctCount = 0;
        int wrongCount = 0;

        System.out.println(c("\n🧠 Starting Quiz! Answer 10 questions.", BRIGHT + CYAN));

        for (int i = 0; i < rounds; i++) {
            String[] q = getQuestion();
            String country = q[0], correct = q[1];
            System.out.println("\n" + c("Q" + (i+1) + ".", YELLOW) + " What is the capital of " + c(country, BRIGHT) + "?");
            for (int j = 0; j < 4; j++) {
                System.out.println("  " + (j+1) + ". " + q[2+j]);
            }
            int choice = askInt("Your choice (1-4): ", 4);
            String selected = q[choice+1];
            boolean isCorrect = selected.equals(correct);
            if (isCorrect) {
                correctCount++;
                System.out.println(c("✅ Correct! " + country + " → " + correct, GREEN));
            } else {
                wrongCount++;
                System.out.println(c("❌ Wrong! " + country + " → " + correct + " (you said " + selected + ")", RED));
                CountryStats cs = stats.countryStats.get(country);
                if (cs != null && cs.asked >= 3) {
                    double acc = (double) cs.correct / cs.asked * 100.0;
                    System.out.println(c("  Your accuracy for " + country + ": " + String.format("%.1f", acc) + "%", DIM));
                }
            }
            stats.recordAnswer(country, isCorrect);
            showProgress(i+1, rounds);
        }

        stats.addSession(correctCount, wrongCount);
        System.out.println("\n\n" + c("Quiz finished!", BRIGHT) + " Correct: " + c(String.valueOf(correctCount), GREEN) + ", Wrong: " + c(String.valueOf(wrongCount), RED));
        if (correctCount >= 8) {
            System.out.println(c("🏆 Great job! You're a geography master!", BRIGHT + CYAN));
        }
    }

    private void showStats() {
        if (stats.totalQuestions == 0) {
            System.out.println(c("📭 No data yet. Take a quiz first!", YELLOW));
            return;
        }

        System.out.println("\n" + c("═".repeat(50), DIM));
        System.out.println(c("📊 YOUR STATISTICS", BRIGHT + MAGENTA));
        System.out.println(c("═".repeat(50), DIM));
        System.out.println("  Total Questions: " + stats.totalQuestions);
        System.out.println("  Correct Answers: " + c(String.valueOf(stats.correctAnswers), GREEN));
        System.out.println("  Wrong Answers:   " + c(String.valueOf(stats.wrongAnswers), RED));
        System.out.println("  Accuracy:        " + c(String.format("%.1f", stats.getAccuracy()) + "%", CYAN));
        System.out.println("  Current Streak:  " + c(String.valueOf(stats.currentStreak), YELLOW));
        System.out.println("  Best Streak:     " + c(String.valueOf(stats.bestStreak), GREEN));
        System.out.println(c("─".repeat(50), DIM));

        if (!stats.sessions.isEmpty()) {
            System.out.println("  Recent Sessions:");
            int start = Math.max(0, stats.sessions.size() - 5);
            for (int i = start; i < stats.sessions.size(); i++) {
                SessionData s = stats.sessions.get(i);
                int total = s.correct + s.wrong;
                double acc = total > 0 ? (double) s.correct / total * 100.0 : 0.0;
                System.out.println("    " + s.date + ": " + s.correct + "C / " + s.wrong + "W (" + String.format("%.1f", acc) + "%)");
            }
        }

        List<Map.Entry<String, Double>> weak = stats.getWeaknesses(5);
        if (!weak.isEmpty()) {
            System.out.println(c("  ⚠️ Weaknesses (countries with <60% accuracy):", YELLOW));
            for (Map.Entry<String, Double> e : weak) {
                System.out.println("    " + e.getKey() + ": " + String.format("%.1f", e.getValue()) + "%");
            }
        } else {
            System.out.println(c("  🎉 No significant weaknesses!", GREEN));
        }
        System.out.println(c("═".repeat(50), DIM));
    }

    private void resetStats() {
        if (!askConfirm("⚠️  Delete ALL stats? This cannot be undone!")) return;
        stats.reset();
        System.out.println(c("🗑️  All statistics cleared.", YELLOW));
    }

    private void showMenu() {
        System.out.println("\n" + c("═".repeat(50), CYAN));
        System.out.println(c("🗺️ MAIN MENU", BRIGHT + CYAN));
        System.out.println(c("═".repeat(50), CYAN));
        System.out.println("  1. 🧠 Start Quiz (10 questions)");
        System.out.println("  2. 📊 Show Statistics");
        System.out.println("  3. 🗑️  Reset Statistics");
        System.out.println("  0. 🚪 Exit");
        System.out.println(c("═".repeat(50), CYAN));
    }

    public void run() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(c("\n🗺️ Capital Quest – Learn World Capitals", BRIGHT + CYAN));
        System.out.println(c("Master the map, one capital at a time!", DIM));

        while (true) {
            showMenu();
            String choice = ask("Your choice: ");
            switch (choice) {
                case "1":
                    runQuiz(10);
                    break;
                case "2":
                    showStats();
                    break;
                case "3":
                    resetStats();
                    break;
                case "0":
                    System.out.println(c("👋 Goodbye! Keep learning!", CYAN));
                    return;
                default:
                    System.out.println(c("❌ Invalid choice.", RED));
            }
            if (!choice.equals("0")) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    // ─── Main ──────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        try {
            CapitalQuest app = new CapitalQuest();
            app.run();
        } catch (Exception e) {
            System.err.println(c("❌ Unexpected error: " + e.getMessage(), RED));
            e.printStackTrace();
            System.exit(1);
        }
    }
}
