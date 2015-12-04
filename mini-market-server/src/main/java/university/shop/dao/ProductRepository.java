package university.shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import university.shop.entities.Product;

import java.util.List;

/**
 * Created by Dasha on 20.11.2015.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "select p from PRODUCT p where p.title = :title")
    Product findByTitle(@Param("title") String title);

    @Query(value = "select p from PRODUCT p where p.code = :code")
    Product findByCode(@Param("code") String code);

    @Query(value = "select p from PRODUCT p where p.code = :code and p.title = :title")
    Product findByCodeAndTitle(@Param("code") String code, @Param("title") String title);

    @Query(value = "select p from PRODUCT p inner join SECTION s where p.id = s.id and s.name = :name")
    List<Product> findBySectionName(@Param("name") String name);

}
