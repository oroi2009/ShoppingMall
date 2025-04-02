package ShoppingMall;

import java.util.*;

public class Product {
    private String name, category, reviews, details;
    private String[] imagePath;
    private Vector<String> rivewerId;
    private int price, reviewNum;
    private double rate;
    public Product(String name, String category, String[] imagePath, int price, String details, String reviews, int reviewNum, double rate, Vector<String> rivewerId) {
    	this.name = name;
    	this.category = category;
    	this.imagePath = imagePath;
    	this.price = price;
    	this.details = details;
    	this.reviews = reviews;
    	this.reviewNum = reviewNum;
    	this.rate = rate;
    	this.rivewerId = rivewerId;
    }
    public String getName(){ return name; }
    public String getCategory() {return category; }
    public String[] getImagePath() { return imagePath; }
    public int getPrice() { return price; }
    public String getDetails() { return details; }
    public String getReviews() { return reviews; }
    public double getRate() { return rate; }
    public int getReviewNum() { return reviewNum; }
    public void setRate(double newRate) {
    	rate *= reviewNum++;
    	rate += newRate;
    	rate /= reviewNum;
    	rate = Math.round(rate * 100.0) / 100.0;
    }
    public void addReview(String newReview) {
    	StringBuilder sb = new StringBuilder(reviews);
    	if (!reviews.isEmpty()) { sb.append("\n"); }
    	sb.append(newReview);
    	reviews = sb.toString();
    }
    public boolean checkBuy(User user) {
    	Vector<String> buyProduct = user.getbuyProduct();
    	return buyProduct.contains(name);
    }
    public boolean checkReviewer(User user) { return rivewerId.contains(user.getId()); }
    public void setReviewerId(Vector<String> rivewerId) { this.rivewerId = rivewerId; }
    public void addReviewerId(String name) { rivewerId.add(name); }
    public Vector<String> getReviewerId() { return rivewerId; }
}