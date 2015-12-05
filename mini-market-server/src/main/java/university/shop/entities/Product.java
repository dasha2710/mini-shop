package university.shop.entities;

import javax.persistence.*;

/**
 * Created by Dasha on 18.11.2015.
 */
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "CODE", nullable = false)
    private String code;
    @Column(name = "TITLE", nullable = false)
    private String title;
    @Column(name = "PRICE")
    private double price;
    @Column(name = "PRODUCER_COUNTRY")
    private String producerCountry;
    @JoinColumn(name = "SECTION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Section section;

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

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (Double.compare(product.price, price) != 0) return false;
        if (!code.equals(product.code)) return false;
        if (producerCountry != null ? !producerCountry.equals(product.producerCountry) : product.producerCountry != null)
            return false;
        if (section != null ? !section.equals(product.section) : product.section != null) return false;
        if (!title.equals(product.title)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + code.hashCode();
        result = 31 * result + title.hashCode();
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (producerCountry != null ? producerCountry.hashCode() : 0);
        result = 31 * result + (section != null ? section.hashCode() : 0);
        return result;
    }
}
