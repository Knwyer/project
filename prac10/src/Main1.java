import java.util.ArrayList;
import java.util.List;

// Абстрактный класс для компонентов организации
abstract class OrganizationComponent {
    protected String name;

    public OrganizationComponent(String name) {
        this.name = name;
    }

    public abstract void displayHierarchy();
    public abstract int getTotalEmployees();
    public abstract double getBudget();
}

// Класс для представления сотрудника
class Employee extends OrganizationComponent {
    private String position;
    private double salary;

    public Employee(String name, String position, double salary) {
        super(name);
        this.position = position;
        this.salary = salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public void displayHierarchy() {
        System.out.println("Сотрудник: " + name + ", Должность: " + position + ", Зарплата: " + salary);
    }

    @Override
    public int getTotalEmployees() {
        return 1;
    }

    @Override
    public double getBudget() {
        return salary;
    }
}

// Класс для представления отдела, который может содержать сотрудников и другие отделы
class Department extends OrganizationComponent {
    private List<OrganizationComponent> components = new ArrayList<>();

    public Department(String name) {
        super(name);
    }

    public void addComponent(OrganizationComponent component) {
        components.add(component);
    }

    public void removeComponent(OrganizationComponent component) {
        components.remove(component);
    }

    public OrganizationComponent findEmployeeByName(String name) {
        for (OrganizationComponent component : components) {
            if (component instanceof Employee && component.name.equals(name)) {
                return component;
            } else if (component instanceof Department) {
                OrganizationComponent found = ((Department) component).findEmployeeByName(name);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    @Override
    public void displayHierarchy() {
        System.out.println("Отдел: " + name);
        for (OrganizationComponent component : components) {
            component.displayHierarchy();
        }
    }

    @Override
    public int getTotalEmployees() {
        int totalEmployees = 0;
        for (OrganizationComponent component : components) {
            totalEmployees += component.getTotalEmployees();
        }
        return totalEmployees;
    }

    @Override
    public double getBudget() {
        double totalBudget = 0;
        for (OrganizationComponent component : components) {
            totalBudget += component.getBudget();
        }
        return totalBudget;
    }

    public void displayAllEmployees() {
        System.out.println("Список сотрудников отдела " + name + " и его подотделов:");
        for (OrganizationComponent component : components) {
            if (component instanceof Employee) {
                System.out.println(" - " + component.name);
            } else if (component instanceof Department) {
                ((Department) component).displayAllEmployees();
            }
        }
    }
}

// Клиентский код
public class Main1 {
    public static void main(String[] args) {
        // Создание сотрудников
        Employee emp1 = new Employee("Alice", "Developer", 70000);
        Employee emp2 = new Employee("Bob", "Designer", 60000);
        Employee emp3 = new Employee("Charlie", "Manager", 90000);

        // Создание подотделов
        Department devDept = new Department("Development Department");
        devDept.addComponent(emp1);

        Department designDept = new Department("Design Department");
        designDept.addComponent(emp2);

        // Создание основного отдела и добавление сотрудников и подотделов
        Department mainDept = new Department("Main Department");
        mainDept.addComponent(emp3);
        mainDept.addComponent(devDept);
        mainDept.addComponent(designDept);

        // Вывод иерархии, количества сотрудников и бюджета
        mainDept.displayHierarchy();
        System.out.println("Общее количество сотрудников: " + mainDept.getTotalEmployees());
        System.out.println("Общий бюджет отдела: " + mainDept.getBudget());

        // Изменение зарплаты сотрудника и пересчет бюджета
        emp1.setSalary(75000);
        System.out.println("Обновленный бюджет отдела после изменения зарплаты: " + mainDept.getBudget());

        // Поиск сотрудника по имени
        OrganizationComponent foundEmployee = mainDept.findEmployeeByName("Alice");
        if (foundEmployee != null) {
            System.out.println("Сотрудник найден: " + foundEmployee.name);
            foundEmployee.displayHierarchy();
        } else {
            System.out.println("Сотрудник не найден.");
        }

        // Отображение всех сотрудников в иерархии
        mainDept.displayAllEmployees();
    }
}

