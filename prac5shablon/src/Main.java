import java.util.Scanner;

// Абстрактный класс Document
abstract class Document {
    public abstract void open();
}

// Конкретные классы документов
class Report extends Document {
    @Override
    public void open() {
        System.out.println("Открытие отчета...");
    }
}

class Resume extends Document {
    @Override
    public void open() {
        System.out.println("Открытие резюме...");
    }
}

class Letter extends Document {
    @Override
    public void open() {
        System.out.println("Открытие письма...");
    }
}

class Invoice extends Document {  // Новый тип документа
    @Override
    public void open() {
        System.out.println("Открытие счета...");
    }
}

// Абстрактный класс для создания документа (фабричный метод)
abstract class DocumentCreator {
    public abstract Document createDocument();
}

// Конкретные классы фабрик для каждого типа документа
class ReportCreator extends DocumentCreator {
    @Override
    public Document createDocument() {
        return new Report();
    }
}

class ResumeCreator extends DocumentCreator {
    @Override
    public Document createDocument() {
        return new Resume();
    }
}

class LetterCreator extends DocumentCreator {
    @Override
    public Document createDocument() {
        return new Letter();
    }
}

class InvoiceCreator extends DocumentCreator {  // Фабрика для нового типа документа
    @Override
    public Document createDocument() {
        return new Invoice();
    }
}

// Основной класс программы
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите тип документа для создания:");
        System.out.println("1 - Отчет (Report)");
        System.out.println("2 - Резюме (Resume)");
        System.out.println("3 - Письмо (Letter)");
        System.out.println("4 - Счет (Invoice)");

        int choice = scanner.nextInt();
        DocumentCreator creator = null;

        // Динамический выбор типа документа на основе пользовательского ввода
        switch (choice) {
            case 1:
                creator = new ReportCreator();
                break;
            case 2:
                creator = new ResumeCreator();
                break;
            case 3:
                creator = new LetterCreator();
                break;
            case 4:
                creator = new InvoiceCreator();
                break;
            default:
                System.out.println("Неверный выбор.");
                return;
        }

        // Создание документа и вызов метода open()
        Document document = creator.createDocument();
        document.open();
    }
}