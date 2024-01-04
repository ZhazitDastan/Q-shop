package shop.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.models.Product;
import shop.repositories.ProjectRepository;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Product> listProducts(String title) {
        if (title != null) {
            return projectRepository.findByTitle(title);
        }
        return projectRepository.findAll();
    }


    @Transactional
    public void saveProduct(Product product) {
        log.info("saving {}", product);
        projectRepository.save(product);
    }

    public void deleteProduct(Long id) {
        projectRepository.deleteById(id);
    }

    public Product getProductById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }
}
