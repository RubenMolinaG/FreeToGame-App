package com.example.freegamesapp.classes;

/**
 * Class used to reprent a Game. It contains all the useful information
 * found in the JSON and also a static class used as a builder.
 */

public class GameOO {

    //private final int id;
    private final String title;
    private final String thumbnail;
    private final String shortDescription;
    private final String gameUrl;
    private final String genre;
    private final String platform;
    private final String publisher;
    private final String releaseDate;

    private GameOO(final GameOOBuilder builder) {
        //id = builder.id;
        title = builder.title;
        thumbnail = builder.thumbnail;
        shortDescription = builder.shortDescription;
        gameUrl = builder.gameUrl;
        genre = builder.genre;
        platform = builder.platform;
        publisher = builder.publisher;
        releaseDate = builder.releaseDate;
    }

    /*public int getId() {
        return id;
    }*/

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "GameOO{" +
                ", title='" + title + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", gameUrl='" + gameUrl + '\'' +
                ", genre='" + genre + '\'' +
                ", platform='" + platform + '\'' +
                ", publisher='" + publisher + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    /**
     * Builder Class:
     * The one used to create the GameOO instances.
     */

    public static class GameOOBuilder {

        //private int id;
        private String title;
        private String thumbnail;
        private String shortDescription;
        private String gameUrl;
        private String genre;
        private String platform;
        private String publisher;
        private String releaseDate;

        /*public GameOOBuilder setId(int id){
            this.id = id;
            return this;
        }*/

        public GameOOBuilder setTitle(String title){
            this.title = title;
            return this;
        }

        public GameOOBuilder setThumbnail(String thumbnail){
            this.thumbnail = thumbnail;
            return this;
        }

        public GameOOBuilder setShortDescription(String shortDescription){
            this.shortDescription = shortDescription;
            return this;
        }

        public GameOOBuilder setGameURL(String gameURL){
            this.gameUrl = gameURL;
            return this;
        }

        public GameOOBuilder setGenre(String genre){
            this.genre = genre;
            return this;
        }

        public GameOOBuilder setPlatform(String platform){
            this.platform = platform;
            return this;
        }

        public GameOOBuilder setPublisher(String publisher){
            this.publisher = publisher;
            return this;
        }

        public GameOOBuilder setReleaseDate(String releaseDate){
            this.releaseDate = releaseDate;
            return this;
        }

        public GameOO build(){
            return new GameOO(this);
        }
    }
}
