package shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.models.Image;
import shop.models.Product;
import shop.services.ProductService;

import java.io.IOException;

@Controller
public class ShopController {
    private final ProductService productService;

    @Autowired
    public ShopController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam(name = "title", required = false) String title, Model model) {
        model.addAttribute("products", productService.listProducts(title));
        return "product-details";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

//    @PostMapping("/add")
//    public String addProduct(@RequestParam("file1") MultipartFile file1,
//                             @RequestParam("file2") MultipartFile file2,
//                             @RequestParam("file3") MultipartFile file3,
//                             @ModelAttribute Product product) {
//        productService.saveProduct(product, file1, file2, file3);
//        return "redirect:/home";
//    }

    @PostMapping("/add")
    public String addProduct(@RequestParam(name = "file1", required = false) MultipartFile file1,
                             @RequestParam(name = "file2", required = false) MultipartFile file2,
                             @RequestParam(name = "file3", required = false) MultipartFile file3,
                             @ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            if (filesAreProvided(file1, file2, file3)) {
                productService.saveProduct(product, file1, file2, file3);
            } else {
                productService.saveProduct(product);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Product saved successfully.");
        } catch (Exception e) {
            System.err.println("Error saving product: " + e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving product.");
        }
        return "redirect:/home";
    }


    private boolean filesAreProvided(MultipartFile... files) {
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                return true;
            }
        }
        return false;
    }


    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/home";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "edit-product";
        } else {
            return "redirect:/home";
        }
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product updatedProduct,
                              @RequestParam("file1") MultipartFile file1,
                              @RequestParam("file2") MultipartFile file2,
                              @RequestParam("file3") MultipartFile file3, Model model) {

        Product existingProduct = productService.getProductById(id);

        if (existingProduct != null) {
            try {
                updateProductFields(existingProduct, updatedProduct);
                updateImages(existingProduct, file1, file2, file3);
                productService.saveProduct(existingProduct, file1, file2, file3);
                return "redirect:/home";
            } catch (IOException e) {
                model.addAttribute("error", "Error editing product: " + e.getMessage());
                return "edit-product";
            }
        } else {
            return "redirect:/home";
        }
    }


    private void updateProductFields(Product existingProduct, Product updatedProduct) {
        existingProduct.setTitle(updatedProduct.getTitle());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setCity(updatedProduct.getCity());
        existingProduct.setAuthor(updatedProduct.getAuthor());
    }

    private void updateImages(Product existingProduct, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        if (file1.getSize() != 0) {
            existingProduct.addImageToProduct(toImageEntity(file1, true));
        }

        if (file2.getSize() != 0) {
            existingProduct.addImageToProduct(toImageEntity(file2, false));
        }

        if (file3.getSize() != 0) {
            existingProduct.addImageToProduct(toImageEntity(file3, false));
        }
    }

    private Image toImageEntity(MultipartFile file, boolean isPreviewImage) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setImageBytes(file.getBytes());
        image.setIsPreviewImage(isPreviewImage);

        return image;
    }

    @GetMapping("/product-details/{id}")
    public String showProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "product-details";
        } else {
            model.addAttribute("products", productService.listProducts(null));
            return "home";
        }
    }



    @GetMapping("/home")
    public String showHomePage(Model model) {
        model.addAttribute("products", productService.listProducts(null));
        return "home";
    }
}
