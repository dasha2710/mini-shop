package university.shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import university.shop.entities.Product;

import java.util.List;

/**
 * Created by Dasha on 20.11.2015.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query(value = "select p from Product p where p.title = :title")
    Product findByTitleIgnoreCase(@Param("title") String title);

    @Query(value = "select p from Product p where p.code = :code")
    Product findByCodeIgnoreCase(@Param("code") String code);

    @Query(value = "select p from Product p where p.code = :code and p.title = :title")
    Product findByCodeAndTitleIgnoreCase(@Param("code") String code, @Param("title") String title);

    @Query(value = "select p from Product p inner join p.section s where s.name = :name")
    List<Product> findBySectionNameIgnoreCase(@Param("name") String name);

    @Modifying
    @Query("delete from Product s where s.code = :code")
    int delete(@Param("code") String code);
}
