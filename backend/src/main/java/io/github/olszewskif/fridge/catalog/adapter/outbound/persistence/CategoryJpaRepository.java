package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, UUID>,
        JpaSpecificationExecutor<CategoryJpaEntity> {
    Set<CategoryJpaEntity> findByNameInIgnoreCase(Set<String> names);

    @Query("""
        SELECT DISTINCT c FROM CategoryJpaEntity c
        LEFT JOIN c.aliases a
        WHERE LOWER(c.name) IN :terms
           OR LOWER(a) IN :terms
    """)
    Set<CategoryJpaEntity> findByNameOrAliasInIgnoreCase(@Param("terms") Set<String> terms);
}
