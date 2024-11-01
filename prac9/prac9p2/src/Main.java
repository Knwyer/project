import java.util.HashMap;
import java.util.Map;

// Интерфейс для внутренней службы доставки
interface IInternalDeliveryService {
    void deliverOrder(String orderId);
    String getDeliveryStatus(String orderId);
    double calculateDeliveryCost(String orderId);
}

// Класс для внутренней службы доставки
class InternalDeliveryService implements IInternalDeliveryService {
    private Map<String, String> deliveryStatus = new HashMap<>();

    @Override
    public void deliverOrder(String orderId) {
        deliveryStatus.put(orderId, "Заказ в пути");
        System.out.println("Внутренняя доставка: Заказ " + orderId + " обрабатывается.");
    }

    @Override
    public String getDeliveryStatus(String orderId) {
        return deliveryStatus.getOrDefault(orderId, "Заказ не найден");
    }

    @Override
    public double calculateDeliveryCost(String orderId) {
        // Фиктивный расчет стоимости
        return 100.0;
    }
}

// Сторонняя служба логистики A
class ExternalLogisticsServiceA {
    public void shipItem(int itemId) {
        System.out.println("Сторонняя логистика A: Товар " + itemId + " отправлен.");
    }

    public String trackShipment(int shipmentId) {
        return "Груз " + shipmentId + " в пути";
    }

    public double getShippingCost(int itemId) {
        return 150.0;
    }
}

// Сторонняя служба логистики B
class ExternalLogisticsServiceB {
    public void sendPackage(String packageInfo) {
        System.out.println("Сторонняя логистика B: Посылка " + packageInfo + " отправлена.");
    }

    public String checkPackageStatus(String trackingCode) {
        return "Посылка " + trackingCode + " доставлена";
    }

    public double calculatePackageCost(String packageInfo) {
        return 200.0;
    }
}

// Адаптер для интеграции ExternalLogisticsServiceA
class LogisticsAdapterA implements IInternalDeliveryService {
    private ExternalLogisticsServiceA externalService;

    public LogisticsAdapterA(ExternalLogisticsServiceA externalService) {
        this.externalService = externalService;
    }

    @Override
    public void deliverOrder(String orderId) {
        int itemId = Integer.parseInt(orderId);
        externalService.shipItem(itemId);
    }

    @Override
    public String getDeliveryStatus(String orderId) {
        int shipmentId = Integer.parseInt(orderId);
        return externalService.trackShipment(shipmentId);
    }

    @Override
    public double calculateDeliveryCost(String orderId) {
        int itemId = Integer.parseInt(orderId);
        return externalService.getShippingCost(itemId);
    }
}

// Адаптер для интеграции ExternalLogisticsServiceB
class LogisticsAdapterB implements IInternalDeliveryService {
    private ExternalLogisticsServiceB externalService;

    public LogisticsAdapterB(ExternalLogisticsServiceB externalService) {
        this.externalService = externalService;
    }

    @Override
    public void deliverOrder(String orderId) {
        externalService.sendPackage(orderId);
    }

    @Override
    public String getDeliveryStatus(String orderId) {
        return externalService.checkPackageStatus(orderId);
    }

    @Override
    public double calculateDeliveryCost(String orderId) {
        return externalService.calculatePackageCost(orderId);
    }
}

// Дополнительная служба логистики C
class ExternalLogisticsServiceC {
    public void dispatchOrder(String details) {
        System.out.println("Сторонняя логистика C: Отправка заказа " + details);
    }

    public String getOrderStatus(String details) {
        return "Заказ " + details + " на складе";
    }

    public double getOrderCost(String details) {
        return 250.0;
    }
}

// Адаптер для интеграции ExternalLogisticsServiceC
class LogisticsAdapterC implements IInternalDeliveryService {
    private ExternalLogisticsServiceC externalService;

    public LogisticsAdapterC(ExternalLogisticsServiceC externalService) {
        this.externalService = externalService;
    }

    @Override
    public void deliverOrder(String orderId) {
        externalService.dispatchOrder(orderId);
    }

    @Override
    public String getDeliveryStatus(String orderId) {
        return externalService.getOrderStatus(orderId);
    }

    @Override
    public double calculateDeliveryCost(String orderId) {
        return externalService.getOrderCost(orderId);
    }
}

// Фабрика для создания служб доставки
class DeliveryServiceFactory {
    public static IInternalDeliveryService getDeliveryService(String type) {
        switch (type) {
            case "Внутренняя":
                return new InternalDeliveryService();
            case "ВнешняяA":
                return new LogisticsAdapterA(new ExternalLogisticsServiceA());
            case "ВнешняяB":
                return new LogisticsAdapterB(new ExternalLogisticsServiceB());
            case "ВнешняяC":
                return new LogisticsAdapterC(new ExternalLogisticsServiceC());
            default:
                throw new IllegalArgumentException("Некорректный тип службы доставки: " + type);
        }
    }
}

// Клиентский код
public class Main {
    public static void main(String[] args) {
        // Внутренняя доставка
        IInternalDeliveryService internalService = DeliveryServiceFactory.getDeliveryService("Внутренняя");
        internalService.deliverOrder("123");
        System.out.println(internalService.getDeliveryStatus("123"));
        System.out.println("Стоимость: " + internalService.calculateDeliveryCost("123") + "\n");

        // Внешняя служба доставки A
        IInternalDeliveryService externalServiceA = DeliveryServiceFactory.getDeliveryService("ВнешняяA");
        externalServiceA.deliverOrder("456");
        System.out.println(externalServiceA.getDeliveryStatus("456"));
        System.out.println("Стоимость: " + externalServiceA.calculateDeliveryCost("456") + "\n");

        // Внешняя служба доставки B
        IInternalDeliveryService externalServiceB = DeliveryServiceFactory.getDeliveryService("ВнешняяB");
        externalServiceB.deliverOrder("789");
        System.out.println(externalServiceB.getDeliveryStatus("789"));
        System.out.println("Стоимость: " + externalServiceB.calculateDeliveryCost("789") + "\n");

        // Внешняя служба доставки C
        IInternalDeliveryService externalServiceC = DeliveryServiceFactory.getDeliveryService("ВнешняяC");
        externalServiceC.deliverOrder("101112");
        System.out.println(externalServiceC.getDeliveryStatus("101112"));
        System.out.println("Стоимость: " + externalServiceC.calculateDeliveryCost("101112") + "\n");
    }
}
