package shop.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.models.Image;
import shop.models.Product;
import shop.repositories.ProductRepository;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> listProducts(String title) {
        if (title != null) {
            return productRepository.findByTitle(title);
        }
        return productRepository.findAll();
    }

    public void saveProduct(Product product, MultipartFile file1, MultipartFile file2, MultipartFile file3) {
        try {
            addImageToProduct(product, file1, true);
            addImageToProduct(product, file2, false);
            addImageToProduct(product, file3, false);

            log.info("Saving new Product. Title: {};", product.getTitle());
            Product savedProduct = productRepository.save(product);
            savedProduct.setPreviewImageId(savedProduct.getImages().get(0).getId());
        } catch (IOException e) {
            log.error("Error saving product with title: {}", product.getTitle(), e);
        }
    }

    public void saveProduct(Product product) {
        try {
            log.info("Saving new Product. Title: {};", product.getTitle());
            productRepository.save(product);
        } catch (Exception e) {
            log.error("Error saving product with title: {}", product.getTitle(), e);
        }
    }



    private void addImageToProduct(Product product, MultipartFile file, boolean isPreviewImage) throws IOException {
        if (file != null && file.getSize() > 0) {
            Image image = createImageFromMultipartFile(file);
            image.setIsPreviewImage(isPreviewImage);
            product.addImageToProduct(image);
        }
    }

    private Image createImageFromMultipartFile(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setImageBytes(file.getBytes());
        return image;
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
}

