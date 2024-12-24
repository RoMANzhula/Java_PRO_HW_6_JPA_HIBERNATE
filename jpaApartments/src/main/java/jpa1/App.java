package jpa1;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class App { // клас Програма
    static EntityManagerFactory emf; // створюємо статичну змінну типу EntityManagerFactory для створення
    // з'єднання з базою даних
    static EntityManager em; // створюємо статичну змінну типу EntityManager для роботи з базою даних

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // відкриваємо консоль для роботи з користувачами
        try {
            // створюємо з'єднання
            emf = Persistence.createEntityManagerFactory("JPATest"); // ініціалізуємо поле класу з'єднанням з базою даних
            em = emf.createEntityManager(); // об'єкт для роботи з базою даних
            try {
                while (true) { // створюємо меню для роботи з таблицею
                    System.out.println("1: додати квартиру");
                    System.out.println("2: видалити квартиру");
                    System.out.println("3: змінити квартиру для району");
                    System.out.println("4: переглянути квартири");
                    System.out.println("5: меню сортування");

                    System.out.print("-> ");

                    String s = sc.nextLine(); // читаємо відповідь користувача з консолі
                    switch (s) { // обробляємо відповіді користувачів
                        case "1":
                            addApartment(sc);
                            break;
                        case "2":
                            deleteApartment(sc);
                            break;
                        case "3":
                            changeApartment(sc);
                            break;
                        case "4":
                            viewApartments();
                            break;
                        case "5":
                            sortMenu(sc);
                            break;

                        default:
                            return;
                    }
                }
            } finally { // обов'язковий блок для виконання
                sc.close(); // закриваємо потік введення даних з консолі від користувачів
                em.close(); // закриваємо з'єднання з базою даних
                emf.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    private static void addApartment(Scanner sc) { // метод для додавання квартири до таблиці
        System.out.print("Enter apartment neighborhood: ");
        String neighborhood = sc.nextLine();

        System.out.print("Enter apartment address: ");
        String address = sc.nextLine();

        System.out.print("Enter apartment area: ");
        String strArea = sc.nextLine();
        double area = Double.parseDouble(strArea);

        System.out.print("Enter apartment number of rooms: ");
        String strNumOfRooms = sc.nextLine();
        int numOfRooms = Integer.parseInt(strNumOfRooms);

        System.out.print("Enter apartment price: ");
        String strPrice = sc.nextLine();
        double price = Double.parseDouble(strPrice);

        em.getTransaction().begin();
        try {
            Apartment apartment = new Apartment(neighborhood, address, area, numOfRooms, price);
            em.persist(apartment);
            em.getTransaction().commit();

            System.out.println(apartment.getId());
        } catch (Exception exception) {
            em.getTransaction().rollback();
        }
    }

    private static void deleteApartment(Scanner sc) { // метод для видалення квартири з абліци
        System.out.print("Enter apartment id: ");
        String apartmentId = sc.nextLine();
        long id = Long.parseLong(apartmentId);

        Apartment apartment = em.getReference(Apartment.class, id);
        if (apartment == null) {
            System.out.println("Apartment not found!");
            return;
        }

        em.getTransaction().begin();
        try {
            em.remove(apartment);
            em.getTransaction().commit();
        } catch (Exception exception) {
            em.getTransaction().rollback();
        }
    }

    private static void changeApartment(Scanner sc) { // метод для виправлення параметрів квартири у таблиці
        System.out.print("Enter apartment neighborhood: ");
        String neighborhood = sc.nextLine();

        System.out.println("Enter new address: ");
        String address = sc.nextLine();

        System.out.println("Enter new area: ");
        String strArea = sc.nextLine();
        double area = Double.parseDouble(strArea);

        System.out.println("Enter new number of rooms: ");
        String strNumOfRooms = sc.nextLine();
        int numOfRooms = Integer.parseInt(strNumOfRooms);

        System.out.println("Enter new price: ");
        String strPrice = sc.nextLine();
        double price = Double.parseDouble(strPrice);

        Apartment apartment = null;
        try {
            Query query = em.createQuery(
                    "SELECT a FROM Apartment a WHERE a.neighborhood = :neighborhood", Apartment.class);
            query.setParameter("neighborhood", neighborhood);
            apartment = (Apartment) query.getSingleResult();
        } catch (NoResultException exception) {
            System.out.println("Apartment not found!");
            return;
        } catch (NonUniqueResultException exception) {
            System.out.println("Non unique result!");
            return;
        }

        em.getTransaction().begin();
        try {
            apartment.setAddress(address);
            apartment.setArea(area);
            apartment.setNumberOfRooms(numOfRooms);
            apartment.setPrice(price);
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    private static void viewApartments() { // метод для відображення всього вмісту таблиці (всіх квартир)
        Query query = em.createQuery("SELECT a FROM Apartment a", Apartment.class);
        List<Apartment> list = (List<Apartment>) query.getResultList();

        for (Apartment a : list)
            System.out.println(a);
    }

    private static void sortMenu(Scanner sc) { // метод для виклику меню для сортування квартир за параметрами
        System.out.println("1: area sort: ");
        System.out.println("2: price sort: ");
        System.out.println("3: number of rooms sort: ");

        System.out.print("-> ");

        String s = sc.nextLine();
        switch (s) {
            case "1":
                selectToArea(sc);
                break;
            case "2":
                selectToPrice(sc);
                break;
            case "3":
                selectToNumOfRooms(sc);
                break;
            default:
                return;
        }
    }

    private static void selectToArea(Scanner sc) { // метод для вибору квартири на її площі
        System.out.println("Enter area to sort: ");
        String strArea = sc.nextLine();
        double area = Double.parseDouble(strArea);

        String hiberQuery = "SELECT a FROM Apartment a WHERE a.area = :area";
        Query query = em.createQuery(hiberQuery, Apartment.class);
        query.setParameter("area", area);
        List<Apartment> list = (List<Apartment>) query.getResultList();

        for (Apartment a : list) {
            System.out.println(a);
        }
    }

    private static void selectToPrice(Scanner sc) { // метод для вибору квартири за ціною
        System.out.println("Enter price to sort: ");
        String strPrice = sc.nextLine();
        double price = Double.parseDouble(strPrice);

        String hiberQuery = "SELECT a FROM Apartment a WHERE a.price = :price";
        Query query = em.createQuery(hiberQuery, Apartment.class);
        query.setParameter("price", price);
        List<Apartment> list = (List<Apartment>) query.getResultList();

        for (Apartment a : list) {
            System.out.println(a);
        }
    }

    private static void selectToNumOfRooms(Scanner sc) { // метод для вибору квартири за кількістю кімнат
        System.out.println("Enter number of rooms to sort: ");
        String strNumOfRooms = sc.nextLine();
        int numOfRooms = Integer.parseInt(strNumOfRooms);

        String hiberQuery = "SELECT a FROM Apartment a WHERE a.numberOfRooms = :numOfRooms";

        Query query = em.createQuery(hiberQuery, Apartment.class);
        query.setParameter("numOfRooms", numOfRooms);
        List<Apartment> list = (List<Apartment>) query.getResultList();

        for (Apartment a : list) {
            System.out.println(a);
        }
    }
}


