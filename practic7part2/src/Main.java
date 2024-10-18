import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface IObserver {
    void update(String stockSymbol, double price);
}

interface ISubject {
    void subscribe(IObserver observer, String stockSymbol);
    void unsubscribe(IObserver observer, String stockSymbol);
    void notifyObservers(String stockSymbol, double price);
}

class StockExchange implements ISubject {
    private Map<String, List<IObserver>> observers = new HashMap<>();

    @Override
    public void subscribe(IObserver observer, String stockSymbol) {
        observers.computeIfAbsent(stockSymbol, k -> new ArrayList<>()).add(observer);
        System.out.println("Наблюдатель подписан на акции: " + stockSymbol);
    }

    @Override
    public void unsubscribe(IObserver observer, String stockSymbol) {
        if (observers.containsKey(stockSymbol)) {
            observers.get(stockSymbol).remove(observer);
            System.out.println("Наблюдатель отписан от акций: " + stockSymbol);
        }
    }

    @Override
    public void notifyObservers(String stockSymbol, double price) {
        if (observers.containsKey(stockSymbol)) {
            for (IObserver observer : observers.get(stockSymbol)) {
                observer.update(stockSymbol, price);
            }
        }
    }

    public void changePrice(String stockSymbol, double newPrice) {
        System.out.println("Цена акции " + stockSymbol + " изменена на " + newPrice);
        notifyObservers(stockSymbol, newPrice);
    }
}

class Trader implements IObserver {
    private String name;

    public Trader(String name) {
        this.name = name;
    }

    @Override
    public void update(String stockSymbol, double price) {
        System.out.println("Трейдер " + name + " получил уведомление: " + stockSymbol + " - новая цена: " + price);
    }
}

class AutomatedTrader implements IObserver {
    private String name;
    private double threshold;

    public AutomatedTrader(String name, double threshold) {
        this.name = name;
        this.threshold = threshold;
    }

    @Override
    public void update(String stockSymbol, double price) {
        System.out.println("Автоматический трейдер " + name + " получает обновление: " + stockSymbol + " - новая цена: " + price);
        if (price < threshold) {
            System.out.println("Автоматический трейдер " + name + " покупает акции " + stockSymbol + " по цене " + price);
        } else {
            System.out.println("Автоматический трейдер " + name + " продает акции " + stockSymbol + " по цене " + price);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        StockExchange stockExchange = new StockExchange();

        Trader trader1 = new Trader("Алексей");
        Trader trader2 = new Trader("Мария");
        AutomatedTrader autoTrader = new AutomatedTrader("Робот-1", 150.0);

        stockExchange.subscribe(trader1, "AAPL");
        stockExchange.subscribe(trader2, "AAPL");
        stockExchange.subscribe(autoTrader, "TSLA");

        stockExchange.changePrice("AAPL", 145.0);
        stockExchange.changePrice("TSLA", 140.0);

        stockExchange.unsubscribe(trader1, "AAPL");

        stockExchange.changePrice("AAPL", 155.0);
        stockExchange.changePrice("TSLA", 135.0);
    }
}
