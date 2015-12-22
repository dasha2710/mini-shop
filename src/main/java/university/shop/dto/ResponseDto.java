package university.shop.dto;

import university.shop.entities.Product;
import university.shop.entities.Section;

import java.util.List;

/**
 * Created by dara on 12/5/2015.
 */
public class ResponseDto {
    public List<Section> sections;
    public List<Product> products;
    public Section section;
    public Product product;
    public String message;
}
