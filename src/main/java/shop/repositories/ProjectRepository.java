package shop.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import shop.models.Product;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Product , Long> {
    List<Product> findByTitle(String title);
}
