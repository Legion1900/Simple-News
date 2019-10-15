package com.legion1900.simplenews.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

    private String author;
    private String title;
    /*
    * Publication date.
    * */
    private String publishedAt;
    /*
    * Website where article was published.
    * */
    private String sourceName;
    /*
    * Article text.
    * */
    private String description;

    public Article(
            String author,
            String title,
            String publishedAt,
            String sourceName,
            String description
    ) {
        this.author = author;
        this.title = title;
        this.publishedAt = publishedAt;
        this.sourceName = sourceName;
        this.description = description;
    }

    protected Article(Parcel in) {
        author = in.readString();
        title = in.readString();
        publishedAt = in.readString();
        sourceName = in.readString();
        description = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(title);
        parcel.writeString(publishedAt);
        parcel.writeString(sourceName);
        parcel.writeString(description);
    }
}
