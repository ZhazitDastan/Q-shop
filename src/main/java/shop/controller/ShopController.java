package shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ShopController {


    @GetMapping("/home")
    public String home(){
        return "Home";
    }

    @GetMapping("page2")
    public String page2(){
        return "page2";
    }

    @GetMapping("page3")
    public String page3(){
        return "Page3";

    }

}
