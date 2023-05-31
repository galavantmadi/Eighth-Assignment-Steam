package Shared;

import java.io.Serializable;

public class GameResponse implements Serializable {
    private String id;
    private String title;
    private String developer;
    private String genre;
    private String price;
    private String release_year;
    private String controller_support;
    private String reviews;
    private String size;
    private String file_path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRelease_year() {
        return release_year;
    }

    public void setRelease_year(String release_year) {
        this.release_year = release_year;
    }

    public String getController_support() {
        return controller_support;
    }

    public void setController_support(String controller_support) {
        this.controller_support = controller_support;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }
}
