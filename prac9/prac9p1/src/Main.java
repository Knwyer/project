import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// Интерфейс IReport
interface IReport {
    String generate();
}

// Классы отчетов
class SalesReport implements IReport {
    @Override
    public String generate() {
        return "Sales Report: [date, sale amount, customer]";
    }
}

class UserReport implements IReport {
    @Override
    public String generate() {
        return "User Report: [user name, registration date, purchases]";
    }
}

// Абстрактный декоратор отчета
abstract class ReportDecorator implements IReport {
    protected IReport report;

    public ReportDecorator(IReport report) {
        this.report = report;
    }

    @Override
    public String generate() {
        return report.generate();
    }
}

// Декоратор фильтрации по датам
class DateFilterDecorator extends ReportDecorator {
    private String startDate;
    private String endDate;

    public DateFilterDecorator(IReport report, String startDate, String endDate) {
        super(report);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String generate() {
        return super.generate() + " | Filtered by date: " + startDate + " to " + endDate;
    }
}

// Декоратор сортировки данных
class SortingDecorator extends ReportDecorator {
    private String sortBy;

    public SortingDecorator(IReport report, String sortBy) {
        super(report);
        this.sortBy = sortBy;
    }

    @Override
    public String generate() {
        return super.generate() + " | Sorted by: " + sortBy;
    }
}

// Декоратор экспорта в CSV
class CsvExportDecorator extends ReportDecorator {
    public CsvExportDecorator(IReport report) {
        super(report);
    }

    @Override
    public String generate() {
        return super.generate() + " | Exported as CSV";
    }
}

// Декоратор экспорта в PDF
class PdfExportDecorator extends ReportDecorator {
    public PdfExportDecorator(IReport report) {
        super(report);
    }

    @Override
    public String generate() {
        return super.generate() + " | Exported as PDF";
    }
}

// Клиентский код
public class Main {
    public static void main(String[] args) {
        // Создаем отчет по продажам
        IReport salesReport = new SalesReport();

        // Применяем декораторы: фильтрация по дате, сортировка и экспорт в CSV
        IReport decoratedReport = new CsvExportDecorator(
                new SortingDecorator(
                        new DateFilterDecorator(salesReport, "2023-01-01", "2023-12-31"), "date"));

        System.out.println(decoratedReport.generate());

        // Создаем отчет по пользователям и применяем декораторы: сортировка и экспорт в PDF
        IReport userReport = new UserReport();
        IReport userDecoratedReport = new PdfExportDecorator(new SortingDecorator(userReport, "registration date"));

        System.out.println(userDecoratedReport.generate());
    }
}
