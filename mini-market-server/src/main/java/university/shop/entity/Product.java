package university.shop.entity;

import javax.persistence.*;

/**
 * Created by Dasha on 18.11.2015.
 */
@Entity(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "CODE", nullable = false)
    private String code;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "PRICE")
    private double price;
    @Column(name = "PRODUCER_COUNTRY")
    private String producerCountry;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProducerCountry() {
        return producerCountry;
    }

    public void setProducerCountry(String producerCountry) {
        this.producerCountry = producerCountry;
    }
}
