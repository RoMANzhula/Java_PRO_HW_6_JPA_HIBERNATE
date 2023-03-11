package jpa1;

import javax.persistence.*;

//мапим наш Java-class на таблицу в DataBase
@Entity //аннотация, которая указывает Hibernate, что наш класс специальный и его обьекты нужно хранить в DataBase
@Table(name="Apartments") //аннотация, позволяющаю задать имя таблице в DataBase, с которой будет связан наш класс (если
//имя класса и имя аннотации совпадают, то аннотацию можно не указывать, таблица возьмет название от имени класса. Если
//наше приложенние работает с несколькими схемами - то нужно указать название схемы (name="Apartments", schema="..."))
public class Apartment { //сам класс
    @Id //аннотация, с помощью которой задаем PrimaryKey
    @GeneratedValue //аннотация, с помощью которой Hibernate автоматически генерирует ID новых обьектов данного класса
    @Column(name="myid") //аннотация для контроля ньюансов мапинга (задаем имя колонки таблицы для поля класса)
    private long id; //идентификационный номер для новой квартиры в таблице

    @Column(nullable = false, length = 50) //аннотация для контроля ньюансов мапинга (данное поле не может быть NULL и
    //длина названия района не должна быть более 50 символов)
    private String neighborhood; // район расположения квартиры

    @Column(unique = true, nullable = false) //аннотация для контроля ньюансов мапинга (данное поле должно иметь уникальное
    //название и не может быть NULL)
    private String address; //адрес квартиры

    @Column(nullable = false) //аннотация для контроля ньюансов мапинга (данное поле не может быть NULL)
    private double area; //площадь квартиры

    @Column(nullable = false) //аннотация для контроля ньюансов мапинга (данное поле не может быть NULL)
    private int numberOfRooms; //количество комнат в квартире

    @Column(nullable = false) //аннотация для контроля ньюансов мапинга (данное поле не может быть NULL)
    private double price; //стоимость квартиры

    public Apartment() {} //конструктор по умолчанию

    public Apartment(String neighborhood, String address, double area, int numberOfRooms, double price) { //конструктор для
        //инициализации полей класса при создании нового обьекта(строчки в таблице)
        this.neighborhood = neighborhood;
        this.address = address;
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.price = price;
    }

        //Геттеры и Сеттеры
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

    //переопределение метода для приведения к строковому виду при выводе информации на консоль
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
