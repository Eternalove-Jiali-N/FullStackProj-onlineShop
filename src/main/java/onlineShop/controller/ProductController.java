package onlineShop.controller;

//前端发送的指令如何被后端响应
//Controller 帮助自动在MVC中注册，mapping

import onlineShop.model.Product;
import onlineShop.service.ProductService;
import org.hibernate.validator.constraints.SafeHtml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/getAllProducts", method = RequestMethod.GET)
    public ModelAndView getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ModelAndView("productList","products",products);
    }
    //返回一个具体商品的时候
    @RequestMapping(value = "/getProductById/{productId}", method = RequestMethod.GET)
    public ModelAndView getProductById(@PathVariable(value = "productId") int productId) {
        Product product = productService.getProductById(productId);
        return new ModelAndView("productPage","product",product);
    }

    //返回一个添加商品的表单页面
    @RequestMapping(value = "/admin/product/addProduct", method = RequestMethod.GET)
    public ModelAndView getProductForm() {
        return new ModelAndView("addProduct", "productForm", new Product());
    }

    //具体开始添加商品
    @RequestMapping(value = "/admin/product/addProduct", method = RequestMethod.POST)
    public String addProduct(@ModelAttribute("productForm") Product product, BindingResult result) {
        if (result.hasErrors()){
            return "addProduct";
        }
        productService.addProduct(product);
        MultipartFile image = product.getProductImage();

        if(image!=null && !image.isEmpty()){
            Path path = Paths.get("/Users/wangzile/products/" + product.getId() +".jpg");

            try{
                image.transferTo(new File(path.toString()));
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return "redirect:/getAllProducts";
    }

    @RequestMapping(value = "/admin/delete/{productId}")
    public String deleteProduct(@PathVariable(value = "productId") int productId) {
        Path path = Paths.get("/Users/wangzile/products/" + productId + ".jpg");

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productService.deleteProduct(productId);
        return "redirect:/getAllProducts";
    }

    @RequestMapping(value = "/admin/product/editProduct/{productId}", method = RequestMethod.GET)
    public ModelAndView getEditForm(@PathVariable(value = "productId") int productId) {
        Product product = productService.getProductById(productId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editProduct");
        modelAndView.addObject("editProductObj", product);
        //添加productId，将来返回做post请求
        modelAndView.addObject("productId",productId);
        return modelAndView;
    }

    @RequestMapping(value = "/admin/product/editProduct/{productId}", method = RequestMethod.POST)
    public String editProduct(@ModelAttribute(value = "editProductObj") Product product,
                              @PathVariable(value = "productId") int productId) {
        product.setId(productId);
        productService.updateProduct(product);
        return "redirect:/getAllProducts";
    }
}
