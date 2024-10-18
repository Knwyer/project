interface ICostCalculationStrategy {
    double calculateCost(double distance, int passengers, String serviceClass);
}

class AirplaneCostCalculation implements ICostCalculationStrategy {
    @Override
    public double calculateCost(double distance, int passengers, String serviceClass) {
        double basePrice = 0.1; // Цена за километр
        double cost = basePrice * distance * passengers;

        if (serviceClass.equals("business")) {
            cost *= 1.5; // Увеличение цены на 50% для бизнес-класса
        }
        return cost;
    }
}

class TrainCostCalculation implements ICostCalculationStrategy {
    @Override
    public double calculateCost(double distance, int passengers, String serviceClass) {
        double basePrice = 0.05; // Цена за километр
        double cost = basePrice * distance * passengers;

        if (serviceClass.equals("business")) {
            cost *= 1.2; // Увеличение цены на 20% для бизнес-класса
        }
        return cost;
    }
}

class BusCostCalculation implements ICostCalculationStrategy {
    @Override
    public double calculateCost(double distance, int passengers, String serviceClass) {
        double basePrice = 0.03; // Цена за километр
        double cost = basePrice * distance * passengers;

        if (serviceClass.equals("business")) {
            cost *= 1.1; // Увеличение цены на 10% для бизнес-класса
        }
        return cost;
    }
}

class TravelBookingContext {
    private ICostCalculationStrategy strategy;

    public void setStrategy(ICostCalculationStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculateTripCost(double distance, int passengers, String serviceClass) {
        return strategy.calculateCost(distance, passengers, serviceClass);
    }
}

public class Main {
    public static void main(String[] args) {
        TravelBookingContext context = new TravelBookingContext();

        context.setStrategy(new AirplaneCostCalculation());
        System.out.println("Стоимость поездки на самолете: " + context.calculateTripCost(500, 2, "business"));

        context.setStrategy(new TrainCostCalculation());
        System.out.println("Стоимость поездки на поезде: " + context.calculateTripCost(500, 2, "economy"));

        context.setStrategy(new BusCostCalculation());
        System.out.println("Стоимость поездки на автобусе: " + context.calculateTripCost(500, 2, "business"));
    }
}
