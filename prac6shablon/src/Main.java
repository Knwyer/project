import java.io.*;
import java.util.Date;
import java.util.*;

// Уровни сообщений
enum LogSeverity {
    LOW,
    MEDIUM,
    HIGH
}

// Одиночка для логирования
class LogSystem {
    private static LogSystem instance;
    private static Object lock = new Object();
    private LogSeverity logSeverity;
    private String logFile;

    // Приватный конструктор
    private LogSystem() {
        logSeverity = LogSeverity.LOW;
        logFile = "log_output.txt"; // Файл логов по умолчанию
    }

    // Возврат единственного экземпляра
    public static LogSystem getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LogSystem();
                }
            }
        }
        return instance;
    }

    // Установка уровня важности сообщений
    public void setLogSeverity(LogSeverity severity) {
        logSeverity = severity;
    }

    // Запись сообщений в лог
    public void writeLog(String message, LogSeverity severity) throws IOException {
        if (severity.ordinal() >= logSeverity.ordinal()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.write(new Date() + " [" + severity + "] " + message);
            writer.newLine();
            writer.close();
        }
    }

    // Загрузка конфигурации из файла
    public void loadConfig(String configFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(configFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("=");
            if (parts[0].equals("LogSeverity")) {
                setLogSeverity(LogSeverity.valueOf(parts[1]));
            } else if (parts[0].equals("LogFile")) {
                logFile = parts[1];
            }
        }
        reader.close();
    }
}

// Чтение логов с фильтрацией
class LogFilter {
    private String logFile;

    public LogFilter(String logFile) {
        this.logFile = logFile;
    }

    // Чтение и фильтрация логов по уровню
    public void filterLogs(LogSeverity severity) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(logFile));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("[" + severity + "]")) {
                System.out.println(line);
            }
        }
        reader.close();
    }
}

// Интерфейс для конструирования отчетов
interface ReportBuilder {
    ReportBuilder setTitle(String title);
    ReportBuilder setBody(String body);
    ReportBuilder setEnd(String end);
    ReportBuilder addSection(String sectionTitle, String sectionBody);
    ReportBuilder applyStyle(ReportFormat format);
    Report generateReport();
}

// Стиль отчета
class ReportFormat {
    private String background;
    private String text;
    private int font;

    public ReportFormat(String background, String text, int font) {
        this.background = background;
        this.text = text;
        this.font = font;
    }

    public String getBackground() {
        return background;
    }

    public String getText() {
        return text;
    }

    public int getFont() {
        return font;
    }
}

// Класс отчет
class Report {
    private String title;
    private String body;
    private String end;
    private List<String> sections;
    private ReportFormat format;

    public Report() {
        sections = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void addSection(String sectionTitle, String sectionBody) {
        sections.add(sectionTitle + ": " + sectionBody);
    }

    public void setFormat(ReportFormat format) {
        this.format = format;
    }

    @Override
    public String toString() {
        StringBuilder reportContent = new StringBuilder();
        reportContent.append("Отчет (Стиль: фон ")
                .append(format.getBackground())
                .append(", цвет текста ")
                .append(format.getText())
                .append(", размер шрифта ")
                .append(format.getFont())
                .append(")\n")
                .append("Заголовок: ").append(title).append("\n")
                .append("Содержание: ").append(body).append("\n");

        for (String section : sections) {
            reportContent.append("Раздел: ").append(section).append("\n");
        }

        reportContent.append("Заключение: ").append(end).append("\n");
        return reportContent.toString();
    }
}

// Текстовый отчет
class PlainTextReportBuilder implements ReportBuilder {
    private Report report;

    public PlainTextReportBuilder() {
        this.report = new Report();
    }

    public ReportBuilder setTitle(String title) {
        report.setTitle(title);
        return this;
    }

    public ReportBuilder setBody(String body) {
        report.setBody(body);
        return this;
    }

    public ReportBuilder setEnd(String end) {
        report.setEnd(end);
        return this;
    }

    public ReportBuilder addSection(String sectionTitle, String sectionBody) {
        report.addSection(sectionTitle, sectionBody);
        return this;
    }

    public ReportBuilder applyStyle(ReportFormat format) {
        report.setFormat(format);
        return this;
    }

    public Report generateReport() {
        return report;
    }
}

// HTML отчет
class HtmlReportBuilder implements ReportBuilder {
    private Report report;

    public HtmlReportBuilder() {
        this.report = new Report();
    }

    public ReportBuilder setTitle(String title) {
        report.setTitle("<h1>" + title + "</h1>");
        return this;
    }

    public ReportBuilder setBody(String body) {
        report.setBody("<p>" + body + "</p>");
        return this;
    }

    public ReportBuilder setEnd(String end) {
        report.setEnd("<footer>" + end + "</footer>");
        return this;
    }

    public ReportBuilder addSection(String sectionTitle, String sectionBody) {
        report.addSection("<h2>" + sectionTitle + "</h2>", "<p>" + sectionBody + "</p>");
        return this;
    }

    public ReportBuilder applyStyle(ReportFormat format) {
        report.setFormat(format);
        return this;
    }

    public Report generateReport() {
        return report;
    }
}

// Создатель отчета
class ReportCreator {
    public void buildReport(ReportBuilder builder, ReportFormat format) {
        builder.setTitle("Месячный отчет")
                .setBody("Данные за месяц.")
                .addSection("Результаты", "Прирост на 10%")
                .addSection("Расходы", "Сокращение на 3%")
                .setEnd("Конец.")
                .applyStyle(format);
    }
}

// Интерфейс для клонирования
interface CloneableItem<T> {
    T copy();
}

// Класс оружия
class Armament implements CloneableItem<Armament> {
    private String title;
    private int attackPower;

    public Armament(String title, int attackPower) {
        this.title = title;
        this.attackPower = attackPower;
    }

    public Armament copy() {
        return new Armament(this.title, this.attackPower);
    }

    @Override
    public String toString() {
        return "Оружие: " + title + ", Сила атаки: " + attackPower;
    }
}

// Класс защиты
class Defense implements CloneableItem<Defense> {
    private String title;
    private int protection;

    public Defense(String title, int protection) {
        this.title = title;
        this.protection = protection;
    }

    public Defense copy() {
        return new Defense(this.title, this.protection);
    }

    @Override
    public String toString() {
        return "Защита: " + title + ", Уровень защиты: " + protection;
    }
}

// Навыки
class Ability implements CloneableItem<Ability> {
    private String title;
    private String type;

    public Ability(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public Ability copy() {
        return new Ability(this.title, this.type);
    }

    @Override
    public String toString() {
        return "Навык: " + title + ", Тип: " + type;
    }
}

// Класс персонажа
class GameCharacter implements CloneableItem<GameCharacter> {
    private String name;
    private int vitality;
    private int power;
    private int agility;
    private int intellect;
    private Armament weapon;
    private Defense defense;
    private List<Ability> abilities;

    public GameCharacter(String name, int vitality, int power, int agility, int intellect, Armament weapon, Defense defense) {
        this.name = name;
        this.vitality = vitality;
        this.power = power;
        this.agility = agility;
        this.intellect = intellect;
        this.weapon = weapon;
        this.defense = defense;
        this.abilities = new ArrayList<>();
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
    }

    @Override
    public GameCharacter copy() {
        GameCharacter clonedCharacter = new GameCharacter(this.name, this.vitality, this.power, this.agility, this.intellect, this.weapon.copy(), this.defense.copy());
        for (Ability ability : this.abilities) {
            clonedCharacter.addAbility(ability.copy());
        }
        return clonedCharacter;
    }

    @Override
    public String toString() {
        StringBuilder abilitiesInfo = new StringBuilder();
        for (Ability ability : abilities) {
            abilitiesInfo.append(ability.toString()).append("\n");
        }

        return "Персонаж: " + name + "\n" +
                "Жизненная сила: " + vitality + "\n" +
                "Сила: " + power + "\n" +
                "Ловкость: " + agility + "\n" +
                "Интеллект: " + intellect + "\n" +
                weapon + "\n" +
                defense + "\n" +
                "Навыки:\n" + abilitiesInfo;
    }
}

// Пример использования
public class Main {
    public static void main(String[] args) {
        // Логирование
        LogSystem logSystem = LogSystem.getInstance();
        try {
            logSystem.writeLog("Система запущена", LogSeverity.LOW);
            logSystem.writeLog("Важное событие", LogSeverity.HIGH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Создание отчета
        ReportCreator reportCreator = new ReportCreator();
        ReportFormat format = new ReportFormat("Белый", "Чёрный", 12);
        ReportBuilder builder = new PlainTextReportBuilder();
        reportCreator.buildReport(builder, format);
        Report report = builder.generateReport();
        System.out.println(report);

        // Клонирование персонажа
        Armament sword = new Armament("Меч", 50);
        Defense shield = new Defense("Щит", 20);
        GameCharacter warrior = new GameCharacter("Воин", 100, 80, 60, 40, sword, shield);
        warrior.addAbility(new Ability("Сильный удар", "Физический"));
        GameCharacter warriorClone = warrior.copy();
        System.out.println("Оригинал:\n" + warrior);
        System.out.println("Клон:\n" + warriorClone);
    }
}
