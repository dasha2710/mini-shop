package university.shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import university.shop.entities.Product;
import university.shop.entities.Section;

/**
 * Created by Dasha on 20.11.2015.
 */
@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    @Query(value = "select s from SECTION s where s.name = :name")
    Section findByName(@Param("name") String code);
}
