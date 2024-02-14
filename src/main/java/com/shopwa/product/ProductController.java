package com.shopwa.product;

import com.shopwa.ControllerHelper;
import com.shopwa.category.CategoryService;
import com.shopwa.entity.Category;
import com.shopwa.entity.Customer;
import com.shopwa.entity.Question;
import com.shopwa.entity.Review;
import com.shopwa.entity.product.Product;
import com.shopwa.exception.CategoryNotFoundException;
import com.shopwa.exception.ProductNotFoundException;
import com.shopwa.question.QuestionService;
import com.shopwa.question.vote.QuestionVoteService;
import com.shopwa.review.ReviewService;
import com.shopwa.review.vote.ReviewVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewVoteService voteService;
    @Autowired
    private ControllerHelper controllerHelper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionVoteService questionVoteService;



    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
                                        Model model) throws CategoryNotFoundException {
        return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias,
                                     @PathVariable("pageNum") int pageNum,
                                     Model model) {
        try {
            Category category = categoryService.getCategory(alias);
            List<Category> listCategoryParents = categoryService.getCategoryParents(category);

            Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
            List<Product> listProducts = pageProducts.getContent();

            long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
            long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
            if (endCount > pageProducts.getTotalElements()) {
                endCount = pageProducts.getTotalElements();
            }


            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalPages", pageProducts.getTotalPages());
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            model.addAttribute("totalItems", pageProducts.getTotalElements());
            model.addAttribute("pageTitle", category.getName());
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("category", category);

            return "product/products_by_category";
        } catch (CategoryNotFoundException ex) {
            return "error/404";
        }
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model, HttpServletRequest request) {
        try {
            Product product = productService.getProduct(alias);
            List<Category> listCategoryParents = categoryService.getCategoryParents(product.getCategory());
            List<Question> listQuestions = questionService.getTop3VotedQuestions(product.getId());
            Page<Review> listReviews = reviewService.list3MostRecentReviewByProduct(product);

            Customer customer =  controllerHelper.getAuthenticatedCustomer(request);
            if (customer != null) {
                boolean customerReviewed = reviewService.didCustomerReviewProduct(customer, product.getId());
                voteService.markReviewsVotedForProductByCustomer(listReviews.getContent(), product.getId(), customer.getId());
                questionVoteService.markQuestionsVotedForProductByCustomer(listQuestions, product.getId(), customer.getId());
                if (customerReviewed) {
                    model.addAttribute("customerReviewed", customerReviewed);
                } else {
                    boolean customerCanReview = reviewService.canCustomerReviewProduct(customer, product.getId());
                    model.addAttribute("customerCanReview", customerCanReview);
                }
            }

            int numberOfQuestions = questionService.getNumberOfQuestions(product.getId());
            int numberOfAnsweredQuestions = questionService.getNumberOfAnsweredQuestions(product.getId());

            model.addAttribute("listQuestions", listQuestions);
            model.addAttribute("numberOfQuestions", numberOfQuestions);
            model.addAttribute("numberOfAnsweredQuestions", numberOfAnsweredQuestions);

            model.addAttribute("listReviews", listReviews);
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("product", product);
            model.addAttribute("pageTitle", product.getShortName());

            return "product/product_detail";
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String searchFirstPage(String keyword, Model model) {
        return searchByPage(keyword, model, 1);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(String keyword, Model model,
                               @PathVariable("pageNum") Integer pageNum) {
        Page<Product> pageProducts = productService.search(keyword, pageNum);
        List<Product> listResult = pageProducts.getContent();

        long startCount = (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;
        if (endCount > pageProducts.getTotalElements()) {
            endCount = pageProducts.getTotalElements();
        }


        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("pageTitle", keyword + " - Search Result");

        model.addAttribute("keyword", keyword);
        model.addAttribute("listResult", listResult);

        return "product/search_result";
    }



}
