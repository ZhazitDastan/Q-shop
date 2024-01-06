package shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
