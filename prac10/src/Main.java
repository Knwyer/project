class RoomBookingSystem {
    public void bookRoom(int roomNumber) {
        System.out.println("Номер " + roomNumber + " забронирован.");
    }

    public void cancelRoomBooking(int roomNumber) {
        System.out.println("Бронирование номера " + roomNumber + " отменено.");
    }

    public boolean checkAvailability(int roomNumber) {
        System.out.println("Проверка доступности номера " + roomNumber);
        return true; // Для упрощения, всегда возвращаем доступность
    }
}

class RestaurantSystem {
    public void reserveTable(int tableNumber) {
        System.out.println("Столик " + tableNumber + " забронирован.");
    }

    public void orderFood(String food) {
        System.out.println("Заказано блюдо: " + food);
    }

    public void bookTaxi() {
        System.out.println("Такси вызвано.");
    }
}

class EventManagementSystem {
    public void bookConferenceRoom(int roomNumber) {
        System.out.println("Конференц-зал " + roomNumber + " забронирован.");
    }

    public void orderEquipment(String equipment) {
        System.out.println("Заказано оборудование: " + equipment);
    }
}

class CleaningService {
    public void scheduleCleaning(int roomNumber) {
        System.out.println("Уборка номера " + roomNumber + " запланирована.");
    }

    public void cleanRoom(int roomNumber) {
        System.out.println("Уборка номера " + roomNumber + " выполнена.");
    }
}

class HotelFacade {
    private RoomBookingSystem roomBookingSystem;
    private RestaurantSystem restaurantSystem;
    private EventManagementSystem eventManagementSystem;
    private CleaningService cleaningService;

    public HotelFacade() {
        roomBookingSystem = new RoomBookingSystem();
        restaurantSystem = new RestaurantSystem();
        eventManagementSystem = new EventManagementSystem();
        cleaningService = new CleaningService();
    }

    public void bookRoomWithService(int roomNumber, String food) {
        if (roomBookingSystem.checkAvailability(roomNumber)) {
            roomBookingSystem.bookRoom(roomNumber);
            restaurantSystem.orderFood(food);
            cleaningService.scheduleCleaning(roomNumber);
            System.out.println("Бронирование номера с заказом еды и услугами уборки выполнено.");
        }
    }

    public void organizeEvent(int roomNumber, String equipment) {
        eventManagementSystem.bookConferenceRoom(roomNumber);
        eventManagementSystem.orderEquipment(equipment);
        roomBookingSystem.bookRoom(roomNumber);
        System.out.println("Организация мероприятия с бронированием номеров и оборудования выполнена.");
    }

    public void bookTableWithTaxi(int tableNumber) {
        restaurantSystem.reserveTable(tableNumber);
        restaurantSystem.bookTaxi();
        System.out.println("Столик забронирован, такси вызвано.");
    }

    public void cancelRoomBooking(int roomNumber) {
        roomBookingSystem.cancelRoomBooking(roomNumber);
        System.out.println("Бронирование номера отменено.");
    }

    public void requestCleaning(int roomNumber) {
        cleaningService.cleanRoom(roomNumber);
        System.out.println("Уборка номера по запросу выполнена.");
    }
}

public class Main {
    public static void main(String[] args) {
        HotelFacade hotelFacade = new HotelFacade();

        hotelFacade.bookRoomWithService(101, "Суши");

        hotelFacade.organizeEvent(201, "Проектор");

        hotelFacade.bookTableWithTaxi(5);

        hotelFacade.cancelRoomBooking(101);

        hotelFacade.requestCleaning(101);
    }
}
