package com.solvd.pages;

import com.solvd.components.ShoppingCart;
import com.solvd.model.Product;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetailsPage {
    private WebDriver driver;

    @FindBy(xpath = "//*[contains(@class,'page-header')]//*[@data-block='minicart']")
    private WebElement shoppingCartElement;
    private ShoppingCart shoppingCart;

    @FindBy(xpath = "//*[@role='alert']/*/*")
    private List<WebElement> alerts;

    @FindBy(xpath = "//*[contains(@class,'product-info-main')]//*[@itemprop='name']")
    private WebElement productName;
    @FindBy(xpath = "//*[contains(@class,'product-info-main')]//*[contains(@class,'price-wrapper')]")
    private WebElement productPrice;

    @FindBy(xpath = "//*[@id='product-options-wrapper']" +
            "//*[@attribute-code='size']//*[contains(@class,'swatch-option')]")
    private List<WebElement> productSizes;
    @FindBy(xpath = "//*[@id='product-options-wrapper']" +
            "//*[@attribute-code='color']//*[contains(@class,'swatch-option')]")
    private List<WebElement> productColors;

    @FindBy(css = ".product-add-form [type='submit']")
    private WebElement addToCartButton;

    @FindBy(id = "tab-label-reviews")
    private WebElement reviewsTab;
    @FindBy(id = "tab-label-reviews-title")
    private WebElement reviewsTabTitle;

    @FindBy(xpath = "//*[contains(@class,'review-control-vote')]//*[contains(@class,'rating-')]")
    private List<WebElement> reviewRatings;
    @FindBy(id = "nickname_field")
    private WebElement reviewNickname;
    @FindBy(id = "summary_field")
    private WebElement reviewSummary;
    @FindBy(id = "review_field")
    private WebElement reviewReviewText;
    @FindBy(xpath = "//*[@id='review-form']//*[@type='submit']")
    private WebElement reviewSubmitButton;


    public ProductDetailsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.shoppingCart = new ShoppingCart(
                this.shoppingCartElement, this.driver);
    }


    public ShoppingCart getShoppingCart() {
        return this.shoppingCart;
    }

    public String getProductName() {
        return this.productName.getText();
    }

    public BigDecimal getProductPrice() {
        return new BigDecimal(
                this.productPrice.getAttribute("data-price-amount"));
    }

    public boolean isForElement(Product product) {
        // TODO improve (move comparison to Product class)
        return product.getName().equals(getProductName())
                // warning: you cannot replace compareTo with equals
                //          because for equals scale have to be the same
                && product.getPrice().compareTo(getProductPrice()) == 0;
    }

    // TODO add option to select color and size
    public void addToCart() {
        // select first color and size
        this.productSizes.getFirst().click();
        this.productColors.getFirst().click();
        this.addToCartButton.click();
    }

    public ProductDetailsPage addReview(int rating, String nickname, String summary, String review) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Provided rating (%d) must be between 1 and 5 (inclusive)"
                    .formatted(rating));
        }

        // open ratings tab
        this.reviewsTabTitle.click();

        // fill in the form
        // click on star corresponding to selected rating
        WebElement selectedRating = this.reviewRatings.get(rating - 1);
        int starsHeigth = selectedRating.getSize().getHeight();
        int starsWidth = selectedRating.getSize().getWidth();
        Actions actions = new Actions(this.driver);
        // click on the last star (assumes starWidth == starHeigth)
        actions.moveToElement(selectedRating, starsWidth / 2 - starsHeigth / 2, 0)
                .click()
                .perform();

        this.reviewNickname.sendKeys(nickname);
        this.reviewSummary.sendKeys(summary);
        this.reviewReviewText.sendKeys(review);
        this.reviewSubmitButton.click();

        return new ProductDetailsPage(this.driver);
    }

    /**
     * informs whether alert that review was added successfully is shown
     */
    public boolean isReviewAddedSuccessfullyAlertShown() {
        for (WebElement message : this.alerts) {
            if (message.getText().equals("You submitted your review for moderation.")) {
                return true;
            }
        }
        return false;
    }

    // add goToDetailsPage to ProductCard class
    // goToCheckout()
    // boolean addReview()
}