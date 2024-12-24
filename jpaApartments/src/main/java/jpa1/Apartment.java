package jpa1;

import javax.persistence.*;

//мапимо наш Java-клас на таблицю в DataBase
@Entity //анотація, яка вказує Hibernate, що наш клас спеціальний і його об'єкти потрібно зберігати в DataBase
@Table(name="Apartments") //анотація, яка дозволяє вказати ім'я таблиці в DataBase, з якою буде пов'язаний наш клас (якщо
//ім'я класу і ім'я таблиці збігаються, то анотацію можна не вказувати, таблиця візьме назву від імені класу. Якщо
//наш додаток працює з кількома схемами, то потрібно вказати назву схеми (name="Apartments", schema="..."))
public class Apartment { //сам клас
    @Id //анотація, якою задаємо PrimaryKey
    @GeneratedValue //анотація, якою Hibernate автоматично генерує ID нових об'єктів цього класу
    @Column(name="myid") //анотація для контролю нюансів мапінгу (вказуємо ім'я колонки таблиці для поля класу)
    private long id; //ідентифікаційний номер для нової квартири в таблиці

    @Column(nullable = false, length = 50) //анотація для контролю нюансів мапінгу (це поле не може бути NULL і
    //довжина назви району не повинна перевищувати 50 символів)
    private String neighborhood; // район розташування квартири

    @Column(unique = true, nullable = false) //анотація для контролю нюансів мапінгу (це поле повинно мати унікальне
    //назву і не може бути NULL)
    private String address; //адреса квартири

    @Column(nullable = false) //анотація для контролю нюансів мапінгу (це поле не може бути NULL)
    private double area; //площа квартири

    @Column(nullable = false) //анотація для контролю нюансів мапінгу (це поле не може бути NULL)
    private int numberOfRooms; //кількість кімнат у квартирі

    @Column(nullable = false) //анотація для контролю нюансів мапінгу (це поле не може бути NULL)
    private double price; //вартість квартири

    public Apartment() {} //конструктор за замовчуванням

    public Apartment(String neighborhood, String address, double area, int numberOfRooms, double price) { //конструктор для
        //ініціалізації полів класу при створенні нового об'єкта (рядка в таблиці)
        this.neighborhood = neighborhood;
        this.address = address;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.price = price;
    }

        //Геттери та Сеттери
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // перевизначення методу для приведення до рядкового вигляду під час виведення інформації на консоль
    @Override
    public String toString() {
        return "Apartment{" +
                "id=" + id +
                ", neighborhood='" + neighborhood + '\'' +
                ", address='" + address + '\'' +
                ", area=" + area +
                ", numberOfRooms=" + numberOfRooms +
                ", price=" + price +
                '}';
    }
}
