package jpa1;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class App { //класс Приложение
    static EntityManagerFactory emf; //создаем статическую ссылочную переменную типа EntityManagerFactory для создания
    //соединения с DataBase
    static EntityManager em; //создаем статическую ссылочную переменную типа EntityManager для работы с DataBase

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); //открываем консоль для работы с пользователями
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("JPATest"); //инициализируем поле класса соединением с DataBase
            em = emf.createEntityManager(); //обьект для работы с DataBase
            try {
                while (true) { //создаем меню для работы с таблицой
                    System.out.println("1: add apartment");
                    System.out.println("2: delete apartment");
                    System.out.println("3: change apartment for neighborhood");
                    System.out.println("4: view apartments");
                    System.out.println("5: sort menu");

                    System.out.print("-> ");

                    String s = sc.nextLine(); //читаем с консоли ответ от пользователя
                    switch (s) { //ловим ответы от пользователей
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
            } finally { //обязательный блок для выполнения
                sc.close(); //закрываем поток ввода данных с консоли от пользователей
                em.close(); //закрываем соединене с DataBase
                emf.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    private static void addApartment(Scanner sc) { //метод для добавления квартиры в таблицу
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

    private static void deleteApartment(Scanner sc) { //метод для удаления квартиры из аблицы
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

    private static void changeApartment(Scanner sc) { //метод для исправления параметров квартиры в таблице
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

    private static void viewApartments() { //метод для отображения всего содержимого таблицы (всех квартир)
        Query query = em.createQuery("SELECT a FROM Apartment a", Apartment.class);
        List<Apartment> list = (List<Apartment>) query.getResultList();

        for (Apartment a : list)
            System.out.println(a);
    }

    private static void sortMenu(Scanner sc) { //метод для вызова меню для сортировки квартир по параметрам
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

    private static void selectToArea(Scanner sc) { //метод для выбора квартиры по ее площади
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

    private static void selectToPrice(Scanner sc) { //метод для выбора квартиры по ее цене
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

    private static void selectToNumOfRooms(Scanner sc) { //метод для выбора квартиры по количеству в ней комнат
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


