package shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.models.Product;
import shop.services.ProjectService;

@Controller
public class ShopController {
    private final ProjectService projectService;

    @Autowired
    public ShopController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam(name = "title", required = false) String title, Model model) {
        model.addAttribute("products", projectService.listProducts(title));
        return "products";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product) {
        projectService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        projectService.deleteProduct(id);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = projectService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "edit-product";
        } else {
            return "redirect:/products";
        }
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @ModelAttribute Product product) {
        Product existingProduct = projectService.getProductById(id);
        if (existingProduct != null) {
            existingProduct.setTitle(product.getTitle());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setCity(product.getCity());
            existingProduct.setAuthor(product.getAuthor());
            projectService.saveProduct(existingProduct);
        }
        return "redirect:/products";
    }

    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }
}
