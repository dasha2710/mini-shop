package university.shop.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import university.shop.entities.Section;

/**
 * Created by Dasha on 20.11.2015.
 */
@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    @Query(value = "select s from Section s where s.name = :name")
    Section findByNameIgnoreCase(@Param("name") String name);

    @Modifying
    @Query("update Section s set s.name = :newName, s.description = :description where s.name = :name")
    void update(@Param("newName") String newName, @Param("description") String description, @Param("name") String name);

    @Modifying
    @Query("delete from Section s where s.name = :name")
    int delete(@Param("name") String name);
}
