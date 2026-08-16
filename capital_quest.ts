# capital_quest.ts
/**
 * 🗺️ Capital Quest – Learn World Capitals (TypeScript Edition)
 * Fully typed, interactive quiz with stats, weaknesses, colored CLI
 */

import * as fs from 'fs';
import * as path from 'path';
import * as os from 'os';
import * as readline from 'readline';

// ─── Types ──────────────────────────────────────────────────────────────────

interface CountryStats {
    asked: number;
    correct: number;
}

interface SessionData {
    date: string;
    correct: number;
    wrong: number;
}

interface StatsData {
    totalQuestions: number;
    correctAnswers: number;
    wrongAnswers: number;
    currentStreak: number;
    bestStreak: number;
    sessions: SessionData[];
    countryStats: Record<string, CountryStats>;
}

// ─── Data ────────────────────────────────────────────────────────────────────

const COUNTRIES: Record<string, string> = {
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
    "Zimbabwe": "Harare"
};

// ─── Colors ──────────────────────────────────────────────────────────────────

const colors = {
    reset: '\x1b[0m',
    bright: '\x1b[1m',
    dim: '\x1b[2m',
    red: '\x1b[31m',
    green: '\x1b[32m',
    yellow: '\x1b[33m',
    blue: '\x1b[34m',
    magenta: '\x1b[35m',
    cyan: '\x1b[36m',
};

const c = (str: string, color: string): string => `${color}${str}${colors.reset}`;

// ─── Stats Manager ──────────────────────────────────────────────────────────

class Stats {
    private dataDir: string;
    private dataFile: string;
    public totalQuestions: number = 0;
    public correctAnswers: number = 0;
    public wrongAnswers: number = 0;
    public currentStreak: number = 0;
    public bestStreak: number = 0;
    public sessions: SessionData[] = [];
    public countryStats: Record<string, CountryStats> = {};

    constructor() {
        this.dataDir = path.join(os.homedir(), '.capital_quest');
        this.dataFile = path.join(this.dataDir, 'stats.json');
        this._load();
    }

    private _load(): void {
        if (!fs.existsSync(this.dataDir)) fs.mkdirSync(this.dataDir, { recursive: true });
        if (fs.existsSync(this.dataFile)) {
            try {
                const raw = fs.readFileSync(this.dataFile, 'utf8');
                const data: StatsData = JSON.parse(raw);
                this.totalQuestions = data.totalQuestions || 0;
                this.correctAnswers = data.correctAnswers || 0;
                this.wrongAnswers = data.wrongAnswers || 0;
                this.currentStreak = data.currentStreak || 0;
                this.bestStreak = data.bestStreak || 0;
                this.sessions = data.sessions || [];
                this.countryStats = data.countryStats || {};
            } catch (_) {}
        }
    }

    private _save(): void {
        const data: StatsData = {
            totalQuestions: this.totalQuestions,
            correctAnswers: this.correctAnswers,
            wrongAnswers: this.wrongAnswers,
            currentStreak: this.currentStreak,
            bestStreak: this.bestStreak,
            sessions: this.sessions,
            countryStats: this.countryStats,
        };
        fs.writeFileSync(this.dataFile, JSON.stringify(data, null, 2));
    }

    recordAnswer(country: string, correct: boolean): void {
        this.totalQuestions++;
        if (correct) {
            this.correctAnswers++;
            this.currentStreak++;
            if (this.currentStreak > this.bestStreak) this.bestStreak = this.currentStreak;
        } else {
            this.wrongAnswers++;
            this.currentStreak = 0;
        }
        if (!this.countryStats[country]) this.countryStats[country] = { asked: 0, correct: 0 };
        this.countryStats[country].asked++;
        if (correct) this.countryStats[country].correct++;
        this._save();
    }

    addSession(correct: number, wrong: number): void {
        this.sessions.push({ date: new Date().toISOString().slice(0,10), correct, wrong });
        if (this.sessions.length > 10) this.sessions = this.sessions.slice(-10);
        this._save();
    }

    getAccuracy(): number {
        if (this.totalQuestions === 0) return 0;
        return this.correctAnswers / this.totalQuestions * 100;
    }

    getWeaknesses(topN: number = 5): [string, number][] {
        const weak: [string, number][] = [];
        for (const [country, stats] of Object.entries(this.countryStats)) {
            if (stats.asked >= 3) {
                const acc = stats.correct / stats.asked * 100;
                if (acc < 60) weak.push([country, acc]);
            }
        }
        weak.sort((a, b) => a[1] - b[1]);
        return weak.slice(0, topN);
    }

    reset(): void {
        this.totalQuestions = 0;
        this.correctAnswers = 0;
        this.wrongAnswers = 0;
        this.currentStreak = 0;
        this.bestStreak = 0;
        this.sessions = [];
        this.countryStats = {};
        this._save();
    }
}

// ─── Quiz Engine ────────────────────────────────────────────────────────────

class CapitalQuiz {
    private rl: readline.Interface;
    private stats: Stats;
    private countryList: string[];

    constructor() {
        this.rl = readline.createInterface({
            input: process.stdin,
            output: process.stdout,
        });
        this.stats = new Stats();
        this.countryList = Object.keys(COUNTRIES);
    }

    private _ask(question: string): Promise<string> {
        return new Promise(resolve => this.rl.question(question, resolve));
    }

    private async _askInt(question: string, max: number): Promise<number> {
        while (true) {
            const answer = await this._ask(question);
            const num = parseInt(answer.trim());
            if (!isNaN(num) && num >= 1 && num <= max) return num;
            console.log(c(`Please enter a number between 1 and ${max}`, colors.yellow));
        }
    }

    private async _askConfirm(question: string): Promise<boolean> {
        const answer = await this._ask(`${question} (yes/no): `);
        const a = answer.trim().toLowerCase();
        return a === 'yes' || a === 'y';
    }

    private _getQuestion(): { country: string; correct: string; options: string[] } {
        const country = this.countryList[Math.floor(Math.random() * this.countryList.length)];
        const correct = COUNTRIES[country];
        const allCapitals = Object.values(COUNTRIES);
        const wrongs = allCapitals.filter(c => c !== correct);
        const shuffledWrongs = wrongs.sort(() => Math.random() - 0.5).slice(0, 3);
        while (shuffledWrongs.length < 3) shuffledWrongs.push('Unknown');
        const options = [correct, ...shuffledWrongs];
        options.sort(() => Math.random() - 0.5);
        return { country, correct, options };
    }

    private _showProgress(current: number, total: number): void {
        const barLen = 30;
        const filled = Math.floor(current / total * barLen);
        const bar = '█'.repeat(filled) + '░'.repeat(barLen - filled);
        process.stdout.write(`\r  ${bar} ${current}/${total}`);
    }

    async runQuiz(rounds: number = 10): Promise<void> {
        let correctCount = 0;
        let wrongCount = 0;

        console.log(c('\n🧠 Starting Quiz! Answer 10 questions.', colors.bright + colors.cyan));

        for (let i = 0; i < rounds; i++) {
            const { country, correct, options } = this._getQuestion();
            console.log(`\n${c(`Q${i+1}.`, colors.yellow)} What is the capital of ${c(country, colors.bright)}?`);
            options.forEach((opt, idx) => console.log(`  ${idx+1}. ${opt}`));
            const choice = await this._askInt('Your choice (1-4): ', 4);
            const selected = options[choice-1];
            const isCorrect = (selected === correct);
            if (isCorrect) {
                correctCount++;
                console.log(c(`✅ Correct! ${country} → ${correct}`, colors.green));
            } else {
                wrongCount++;
                console.log(c(`❌ Wrong! ${country} → ${correct} (you said ${selected})`, colors.red));
                if (this.stats.countryStats[country] && this.stats.countryStats[country].asked >= 3) {
                    const acc = this.stats.countryStats[country].correct / this.stats.countryStats[country].asked * 100;
                    console.log(c(`  Your accuracy for ${country}: ${acc.toFixed(1)}%`, colors.dim));
                }
            }
            this.stats.recordAnswer(country, isCorrect);
            this._showProgress(i+1, rounds);
        }

        this.stats.addSession(correctCount, wrongCount);
        console.log(`\n\n${c('Quiz finished!', colors.bright)} Correct: ${c(correctCount, colors.green)}, Wrong: ${c(wrongCount, colors.red)}`);
        if (correctCount >= 8) {
            console.log(c('🏆 Great job! You\'re a geography master!', colors.bright + colors.cyan));
        }
    }

    showStats(): void {
        if (this.stats.totalQuestions === 0) {
            console.log(c('📭 No data yet. Take a quiz first!', colors.yellow));
            return;
        }

        console.log('\n' + c('═'.repeat(50), colors.dim));
        console.log(c('📊 YOUR STATISTICS', colors.bright + colors.magenta));
        console.log(c('═'.repeat(50), colors.dim));
        console.log(`  Total Questions: ${this.stats.totalQuestions}`);
        console.log(`  Correct Answers: ${c(this.stats.correctAnswers, colors.green)}`);
        console.log(`  Wrong Answers:   ${c(this.stats.wrongAnswers, colors.red)}`);
        console.log(`  Accuracy:        ${c(this.stats.getAccuracy().toFixed(1) + '%', colors.cyan)}`);
        console.log(`  Current Streak:  ${c(this.stats.currentStreak, colors.yellow)}`);
        console.log(`  Best Streak:     ${c(this.stats.bestStreak, colors.green)}`);
        console.log(c('─'.repeat(50), colors.dim));

        if (this.stats.sessions.length) {
            console.log('  Recent Sessions:');
            this.stats.sessions.slice(-5).forEach(s => {
                const total = s.correct + s.wrong;
                const acc = total > 0 ? (s.correct / total * 100).toFixed(1) : 0;
                console.log(`    ${s.date}: ${s.correct}C / ${s.wrong}W (${acc}%)`);
            });
        }

        const weak = this.stats.getWeaknesses();
        if (weak.length) {
            console.log(c('  ⚠️ Weaknesses (countries with <60% accuracy):', colors.yellow));
            weak.forEach(([country, acc]) => {
                console.log(`    ${country}: ${acc.toFixed(1)}%`);
            });
        } else {
            console.log(c('  🎉 No significant weaknesses!', colors.green));
        }
        console.log(c('═'.repeat(50), colors.dim));
    }

    async resetStats(): Promise<void> {
        const confirm = await this._askConfirm('⚠️  Delete ALL stats? This cannot be undone!');
        if (!confirm) return;
        this.stats.reset();
        console.log(c('🗑️  All statistics cleared.', colors.yellow));
    }

    async run(): Promise<void> {
        console.clear();
        console.log(c('\n🗺️ Capital Quest – Learn World Capitals', colors.bright + colors.cyan));
        console.log(c('Master the map, one capital at a time!', colors.dim));

        while (true) {
            await this._showMenu();
            const choice = await this._ask('Your choice: ');
            switch (choice.trim()) {
                case '1':
                    await this.runQuiz();
                    break;
                case '2':
                    this.showStats();
                    break;
                case '3':
                    await this.resetStats();
                    break;
                case '0':
                    console.log(c('👋 Goodbye! Keep learning!', colors.cyan));
                    this.rl.close();
                    return;
                default:
                    console.log(c('❌ Invalid choice.', colors.red));
            }
            if (choice !== '0') {
                console.log('\nPress Enter to continue...');
                await this._ask('');
            }
        }
    }

    private async _showMenu(): Promise<void> {
        console.log('\n' + c('═'.repeat(50), colors.cyan));
        console.log(c('🗺️ MAIN MENU', colors.bright + colors.cyan));
        console.log(c('═'.repeat(50), colors.cyan));
        console.log('  1. 🧠 Start Quiz (10 questions)');
        console.log('  2. 📊 Show Statistics');
        console.log('  3. 🗑️  Reset Statistics');
        console.log('  0. 🚪 Exit');
        console.log(c('═'.repeat(50), colors.cyan));
    }
}

// ─── Main ────────────────────────────────────────────────────────────────────

const main = async (): Promise<void> => {
    try {
        const app = new CapitalQuiz();
        await app.run();
    } catch (err: any) {
        console.error(c(`❌ Unexpected error: ${err.message}`, colors.red));
        process.exit(1);
    }
};

main();
