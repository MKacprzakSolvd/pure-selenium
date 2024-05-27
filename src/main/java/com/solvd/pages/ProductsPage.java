package com.solvd.pages;

import com.solvd.components.ProductCard;
import com.solvd.components.ProductFilter;
import com.solvd.components.ShoppingCartPopup;
import com.solvd.model.Product;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.*;

public class ProductsPage {
    private WebDriver driver;

    @FindBy(css = ".products .product-items .product-item")
    private List<WebElement> productCardElements;
    private List<ProductCard> productCards = new ArrayList<>();

    @FindBy(xpath = "//*[contains(@class,'page-header')]//*[@data-block='minicart']")
    private WebElement shoppingCartPopupElement;
    private ShoppingCartPopup shoppingCartPopup;

    // TODO: extract string 'Size' and 'Color' from this and move it somewhere else (as constant)?
    // select filter block that have 'Size' in title
    @FindBy(xpath = "//*[@id='layered-filter-block']" +
            "//*[contains(@class,'filter-options-item')]" +
            "[.//*[contains(@class,'filter-options-title')][text()='Size']]")
    private WebElement sizeFilterElement;
    private ProductFilter sizeFilter;

    // select filter block that have 'Color' in title
    @FindBy(xpath = "//*[@id='layered-filter-block']" +
            "//*[contains(@class,'filter-options-item')]" +
            "[.//*[contains(@class,'filter-options-title')][text()='Color']]")
    private WebElement colorFilterElement;
    private ProductFilter colorFilter;

    // maps from filters enum to filter object
    private Map<Filter, ProductFilter> filtersMap = new EnumMap<>(Filter.class);


    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

        for (WebElement productCardElement : productCardElements) {
            this.productCards.add(new ProductCard(productCardElement, driver));
        }

        this.shoppingCartPopup = new ShoppingCartPopup(this.shoppingCartPopupElement, this.driver);

        this.sizeFilter = new ProductFilter(this.sizeFilterElement, this.driver);
        this.filtersMap.put(Filter.SIZE, this.sizeFilter);
        this.colorFilter = new ProductFilter(this.colorFilterElement, this.driver);
        this.filtersMap.put(Filter.COLOR, this.colorFilter);
    }


    public List<ProductCard> getProductCards() {
        return Collections.unmodifiableList(this.productCards);
    }

    /**
     * finds product card corresponding to passed product
     */
    public Optional<ProductCard> findProductCard(Product product) {
        for (ProductCard productCard : this.productCards) {
            // TODO: create method representsProduct(Product) in ProductCard and use it here
            if (productCard.getName().equals(product.getName())) {
                return Optional.of(productCard);
            }
        }
        return Optional.empty();
    }

    public List<Product> getProducts() {
        return this.productCards.stream()
                .map(productCard -> productCard.getProductData())
                .toList();
    }

    public ShoppingCartPopup getShoppingCartPopup() {
        return this.shoppingCartPopup;
    }

    //FIXME: add support for case where filter is used (and thus inaccessible)
    public List<String> getFilterOptions(Filter filter) {
        return this.filtersMap.get(filter).getOptions();
    }

    //FIXME: add support for case where filter is used (and thus inaccessible)
    public ProductsPage filterBy(Filter filter, String option) {
        return this.filtersMap.get(filter).filterBy(option);
    }

    // TODO: implement
    // isFilterApplied
    // getAppliedFilters


    public enum Filter {
        SIZE,
        COLOR
    }
}
