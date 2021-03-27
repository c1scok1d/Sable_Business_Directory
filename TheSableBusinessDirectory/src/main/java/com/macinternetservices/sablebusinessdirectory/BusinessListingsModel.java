package com.macinternetservices.sablebusinessdirectory;

import java.io.Serializable;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BusinessListingsModel implements Serializable, Parcelable {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private Title title;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("link")
    @Expose
    private String link;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("author")
    @Expose
    private Integer author;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("date_gmt")
    @Expose
    private String dateGmt;
    @SerializedName("modified")
    @Expose
    private String modified;
    @SerializedName("modified_gmt")
    @Expose
    private String modifiedGmt;
    @SerializedName("content")
    @Expose
    private Content content;
    @SerializedName("default_category")
    @Expose
    private String defaultCategory;
    @SerializedName("post_category")
    @Expose
    private List<PostCategory> postCategory = null;
    @SerializedName("post_tags")
    @Expose
    private List<PostTag> postTags = null;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("mapview")
    @Expose
    private Object mapview;
    @SerializedName("mapzoom")
    @Expose
    private String mapzoom;
    @SerializedName("logo")
    @Expose
    private Object logo;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("special_offers")
    @Expose
    private String specialOffers;
    /*@SerializedName("business_hours")
    @Expose
    private Object businessHours;*/
    @SerializedName("claimed")
    @Expose
    private Claimed claimed;
    @SerializedName("featured")
    @Expose
    private Boolean featured;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("rating_count")
    @Expose
    private Integer ratingCount;
    @SerializedName("featured_media")
    @Expose
    private Integer featuredMedia;
    @SerializedName("featured_image")
    @Expose
    private FeaturedImage featuredImage;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("distance")
    @Expose
    private Distance distance;
    @SerializedName("comment_status")
    @Expose
    private String commentStatus;
    @SerializedName("ping_status")
    @Expose
    private String pingStatus;
    @SerializedName("meta")
    @Expose
    private List<Object> meta = null;
    @SerializedName("guid")
    @Expose
    private Guid guid;
    @SerializedName("yoast_head")
    @Expose
    private String yoastHead;
    @SerializedName("_links")
    @Expose
    private Links links;
    public final static Parcelable.Creator<BusinessListingsModel> CREATOR = new Creator<BusinessListingsModel>() {

        @SuppressWarnings({
                "unchecked"
        })
        public BusinessListingsModel createFromParcel(Parcel in) {
            return new BusinessListingsModel(in);
        }

        public BusinessListingsModel[] newArray(int size) {
            return (new BusinessListingsModel[size]);
        }

    };
    private final static long serialVersionUID = 7716484615034773872L;

    protected BusinessListingsModel(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.title = ((Title) in.readValue((Title.class.getClassLoader())));
        this.slug = ((String) in.readValue((String.class.getClassLoader())));
        this.link = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.type = ((String) in.readValue((String.class.getClassLoader())));
        this.author = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.dateGmt = ((String) in.readValue((String.class.getClassLoader())));
        this.modified = ((String) in.readValue((String.class.getClassLoader())));
        this.modifiedGmt = ((String) in.readValue((String.class.getClassLoader())));
        this.content = ((Content) in.readValue((Content.class.getClassLoader())));
        this.defaultCategory = ((String) in.readValue((String.class.getClassLoader())));
        //in.readList(this.postCategory, (com.macinternetservices.sablebusinessdirectory.PostCategory.class.getClassLoader()));
        //in.readList(this.postTags, (com.macinternetservices.sablebusinessdirectory.PostTag.class.getClassLoader()));
        this.street = ((String) in.readValue((String.class.getClassLoader())));
        this.country = ((String) in.readValue((String.class.getClassLoader())));
        this.region = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.zip = ((String) in.readValue((String.class.getClassLoader())));
        this.latitude = ((String) in.readValue((String.class.getClassLoader())));
        this.longitude = ((String) in.readValue((String.class.getClassLoader())));
        this.mapview = ((Object) in.readValue((Object.class.getClassLoader())));
        this.mapzoom = ((String) in.readValue((String.class.getClassLoader())));
        this.logo = ((Object) in.readValue((Object.class.getClassLoader())));
        this.phone = ((String) in.readValue((String.class.getClassLoader())));
        this.email = ((String) in.readValue((String.class.getClassLoader())));
        this.website = ((String) in.readValue((String.class.getClassLoader())));
        this.twitter = ((String) in.readValue((String.class.getClassLoader())));
        this.facebook = ((String) in.readValue((String.class.getClassLoader())));
        this.video = ((String) in.readValue((String.class.getClassLoader())));
        this.specialOffers = ((String) in.readValue((String.class.getClassLoader())));
        //this.businessHours = ((Object) in.readValue((Object.class.getClassLoader())));
        this.claimed = ((Claimed) in.readValue((Claimed.class.getClassLoader())));
        this.featured = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.rating = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.ratingCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.featuredMedia = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.featuredImage = ((FeaturedImage) in.readValue((FeaturedImage.class.getClassLoader())));
        //in.readList(this.images, (com.macinternetservices.sablebusinessdirectory.Image.class.getClassLoader()));
        this.distance = ((Distance) in.readValue((Distance.class.getClassLoader())));
        this.commentStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.pingStatus = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.meta, (java.lang.Object.class.getClassLoader()));
        this.guid = ((Guid) in.readValue((Guid.class.getClassLoader())));
        this.yoastHead = ((String) in.readValue((String.class.getClassLoader())));
        this.links = ((Links) in.readValue((Links.class.getClassLoader())));
    }



    public BusinessListingsModel() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAuthor() {
        return author;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateGmt() {
        return dateGmt;
    }

    public void setDateGmt(String dateGmt) {
        this.dateGmt = dateGmt;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModifiedGmt() {
        return modifiedGmt;
    }

    public void setModifiedGmt(String modifiedGmt) {
        this.modifiedGmt = modifiedGmt;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(String defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public List<PostCategory> getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(List<PostCategory> postCategory) {
        this.postCategory = postCategory;
    }

    public List<PostTag> getPostTags() {
        return postTags;
    }

    public void setPostTags(List<PostTag> postTags) {
        this.postTags = postTags;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Object getMapview() {
        return mapview;
    }

    public void setMapview(Object mapview) {
        this.mapview = mapview;
    }

    public String getMapzoom() {
        return mapzoom;
    }

    public void setMapzoom(String mapzoom) {
        this.mapzoom = mapzoom;
    }

    public Object getLogo() {
        return logo;
    }

    public void setLogo(Object logo) {
        this.logo = logo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getSpecialOffers() {
        return specialOffers;
    }

    public void setSpecialOffers(String specialOffers) {
        this.specialOffers = specialOffers;
    }

    /*public Object getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(Object businessHours) {
        this.businessHours = businessHours;
    }*/

    public Claimed getClaimed() {
        return claimed;
    }

    public void setClaimed(Claimed claimed) {
        this.claimed = claimed;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Integer getFeaturedMedia() {
        return featuredMedia;
    }

    public void setFeaturedMedia(Integer featuredMedia) {
        this.featuredMedia = featuredMedia;
    }

    public FeaturedImage getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(FeaturedImage featuredImage) {
        this.featuredImage = featuredImage;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public String getPingStatus() {
        return pingStatus;
    }

    public void setPingStatus(String pingStatus) {
        this.pingStatus = pingStatus;
    }

    public List<Object> getMeta() {
        return meta;
    }

    public void setMeta(List<Object> meta) {
        this.meta = meta;
    }

    public Guid getGuid() {
        return guid;
    }

    public void setGuid(Guid guid) {
        this.guid = guid;
    }

    public String getYoastHead() {
        return yoastHead;
    }

    public void setYoastHead(String yoastHead) {
        this.yoastHead = yoastHead;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(title);
        dest.writeValue(slug);
        dest.writeValue(link);
        dest.writeValue(status);
        dest.writeValue(type);
        dest.writeValue(author);
        dest.writeValue(date);
        dest.writeValue(dateGmt);
        dest.writeValue(modified);
        dest.writeValue(modifiedGmt);
        dest.writeValue(content);
        dest.writeValue(defaultCategory);
        dest.writeList(postCategory);
        dest.writeList(postTags);
        dest.writeValue(street);
        dest.writeValue(country);
        dest.writeValue(region);
        dest.writeValue(city);
        dest.writeValue(zip);
        dest.writeValue(latitude);
        dest.writeValue(longitude);
        dest.writeValue(mapview);
        dest.writeValue(mapzoom);
        dest.writeValue(logo);
        dest.writeValue(phone);
        dest.writeValue(email);
        dest.writeValue(website);
        dest.writeValue(twitter);
        dest.writeValue(facebook);
        dest.writeValue(video);
        dest.writeValue(specialOffers);
        //dest.writeValue(businessHours);
        dest.writeValue(claimed);
        dest.writeValue(featured);
        dest.writeValue(rating);
        dest.writeValue(ratingCount);
        dest.writeValue(featuredMedia);
        dest.writeValue(featuredImage);
        dest.writeList(images);
        dest.writeValue(distance);
        dest.writeValue(commentStatus);
        dest.writeValue(pingStatus);
        dest.writeList(meta);
        dest.writeValue(guid);
        dest.writeValue(yoastHead);
        dest.writeValue(links);
    }

    public int describeContents() {
        return 0;
    }

    public class Claimed implements Serializable, Parcelable {

        @SerializedName("raw")
        @Expose
        private String raw;
        @SerializedName("rendered")
        @Expose
        private String rendered;
        public final Parcelable.Creator<Claimed> CREATOR = new Creator<Claimed>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Claimed createFromParcel(Parcel in) {
                return new Claimed(in);
            }

            public Claimed[] newArray(int size) {
                return (new Claimed[size]);
            }

        };
        private final static long serialVersionUID = 3769689412659352443L;

        protected Claimed(Parcel in) {
            this.raw = ((String) in.readValue((String.class.getClassLoader())));
            this.rendered = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Claimed() {
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(raw);
            dest.writeValue(rendered);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class Collection implements Serializable, Parcelable {

        @SerializedName("href")
        @Expose
        private String href;
        public final Parcelable.Creator<Collection> CREATOR = new Creator<Collection>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Collection createFromParcel(Parcel in) {
                return new Collection(in);
            }

            public Collection[] newArray(int size) {
                return (new Collection[size]);
            }

        };
        private final static long serialVersionUID = 7253994909151651151L;

        protected Collection(Parcel in) {
            this.href = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Collection() {
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(href);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class Content implements Serializable, Parcelable {

        @SerializedName("raw")
        @Expose
        private String raw;
        @SerializedName("rendered")
        @Expose
        private String rendered;
        @SerializedName("protected")
        @Expose
        private Boolean _protected;
        public final Parcelable.Creator<Content> CREATOR = new Creator<Content>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Content createFromParcel(Parcel in) {
                return new Content(in);
            }

            public Content[] newArray(int size) {
                return (new Content[size]);
            }

        };
        private final static long serialVersionUID = -7541696071082690904L;

        protected Content(Parcel in) {
            this.raw = ((String) in.readValue((String.class.getClassLoader())));
            this.rendered = ((String) in.readValue((String.class.getClassLoader())));
            this._protected = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        }

        public Content() {
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        public Boolean getProtected() {
            return _protected;
        }

        public void setProtected(Boolean _protected) {
            this._protected = _protected;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(raw);
            dest.writeValue(rendered);
            dest.writeValue(_protected);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class Cury implements Serializable, Parcelable {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("href")
        @Expose
        private String href;
        @SerializedName("templated")
        @Expose
        private Boolean templated;
        public final Parcelable.Creator<Cury> CREATOR = new Creator<Cury>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Cury createFromParcel(Parcel in) {
                return new Cury(in);
            }

            public Cury[] newArray(int size) {
                return (new Cury[size]);
            }

        };
        private final static long serialVersionUID = -4906722727269074837L;

        protected Cury(Parcel in) {
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.href = ((String) in.readValue((String.class.getClassLoader())));
            this.templated = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        }

        public Cury() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public Boolean getTemplated() {
            return templated;
        }

        public void setTemplated(Boolean templated) {
            this.templated = templated;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(name);
            dest.writeValue(href);
            dest.writeValue(templated);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class Distance implements Serializable, Parcelable {

        @SerializedName("raw")
        @Expose
        private String raw;
        @SerializedName("rendered")
        @Expose
        private String rendered;
        public final Parcelable.Creator<Distance> CREATOR = new Creator<Distance>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Distance createFromParcel(Parcel in) {
                return new Distance(in);
            }

            public Distance[] newArray(int size) {
                return (new Distance[size]);
            }

        };
        private final static long serialVersionUID = -349861011564751622L;

        protected Distance(Parcel in) {
            this.raw = ((String) in.readValue((String.class.getClassLoader())));
            this.rendered = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Distance() {
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(raw);
            dest.writeValue(rendered);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class FeaturedImage implements Serializable, Parcelable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("src")
        @Expose
        private String src;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("width")
        @Expose
        private Integer width;
        @SerializedName("height")
        @Expose
        private Integer height;
        public final  Parcelable.Creator<FeaturedImage> CREATOR = new Creator<FeaturedImage>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public FeaturedImage createFromParcel(Parcel in) {
                return new FeaturedImage(in);
            }

            public FeaturedImage[] newArray(int size) {
                return (new FeaturedImage[size]);
            }

        };
        private final static long serialVersionUID = 7491759274061466398L;

        protected FeaturedImage(Parcel in) {
            this.id = ((String) in.readValue((String.class.getClassLoader())));
            this.title = ((String) in.readValue((String.class.getClassLoader())));
            this.src = ((String) in.readValue((String.class.getClassLoader())));
            this.thumbnail = ((String) in.readValue((String.class.getClassLoader())));
            this.width = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.height = ((Integer) in.readValue((Integer.class.getClassLoader())));
        }

        public FeaturedImage() {
        }

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

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(id);
            dest.writeValue(title);
            dest.writeValue(src);
            dest.writeValue(thumbnail);
            dest.writeValue(width);
            dest.writeValue(height);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class Guid implements Serializable, Parcelable {

        @SerializedName("rendered")
        @Expose
        private String rendered;
        public final  Parcelable.Creator<Guid> CREATOR = new Creator<Guid>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Guid createFromParcel(Parcel in) {
                return new Guid(in);
            }

            public Guid[] newArray(int size) {
                return (new Guid[size]);
            }

        };
        private final static long serialVersionUID = -3961515636558399L;

        protected Guid(Parcel in) {
            this.rendered = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Guid() {
        }

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(rendered);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class Image implements Serializable, Parcelable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("src")
        @Expose
        private String src;
        @SerializedName("thumbnail")
        @Expose
        private String thumbnail;
        @SerializedName("featured")
        @Expose
        private Boolean featured;
        @SerializedName("position")
        @Expose
        private String position;
        public final  Parcelable.Creator<Image> CREATOR = new Creator<Image>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Image createFromParcel(Parcel in) {
                return new Image(in);
            }

            public Image[] newArray(int size) {
                return (new Image[size]);
            }

        };
        private final static long serialVersionUID = -6927490951290719123L;

        protected Image(Parcel in) {
            this.id = ((String) in.readValue((String.class.getClassLoader())));
            this.title = ((String) in.readValue((String.class.getClassLoader())));
            this.src = ((String) in.readValue((String.class.getClassLoader())));
            this.thumbnail = ((String) in.readValue((String.class.getClassLoader())));
            this.featured = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.position = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Image() {
        }

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

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Boolean getFeatured() {
            return featured;
        }

        public void setFeatured(Boolean featured) {
            this.featured = featured;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(id);
            dest.writeValue(title);
            dest.writeValue(src);
            dest.writeValue(thumbnail);
            dest.writeValue(featured);
            dest.writeValue(position);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class Links implements Serializable, Parcelable {

        @SerializedName("self")
        @Expose
        private List<Self> self = null;
        @SerializedName("collection")
        @Expose
        private List<Collection> collection = null;
        /*@SerializedName("about")
        @Expose
        private List<About> about = null;
        @SerializedName("author")
        @Expose
        private List<Author> author = null; */
        @SerializedName("replies")
        @Expose
        private List<Reply> replies = null;
        @SerializedName("version-history")
        @Expose
        private List<VersionHistory> versionHistory = null;
        @SerializedName("wp:featuredmedia")
        @Expose
        private List<WpFeaturedmedium> wpFeaturedmedia = null;
        @SerializedName("wp:attachment")
        @Expose
        private List<WpAttachment> wpAttachment = null;
        @SerializedName("wp:term")
        @Expose
        private List<WpTerm> wpTerm = null;
        @SerializedName("curies")
        @Expose
        private List<Cury> curies = null;
        public final  Parcelable.Creator<Links> CREATOR = new Creator<Links>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Links createFromParcel(Parcel in) {
                return new Links(in);
            }

            public Links[] newArray(int size) {
                return (new Links[size]);
            }

        };
        private final static long serialVersionUID = 1514313225627615312L;

        protected Links(Parcel in) {
            in.readList(this.self, (Self.class.getClassLoader()));
            in.readList(this.collection, (Collection.class.getClassLoader()));
            //in.readList(this.about, (About.class.getClassLoader()));
            //in.readList(this.author, (Author.class.getClassLoader()));
            in.readList(this.replies, (Reply.class.getClassLoader()));
            in.readList(this.versionHistory, (VersionHistory.class.getClassLoader()));
            in.readList(this.wpFeaturedmedia, (WpFeaturedmedium.class.getClassLoader()));
            in.readList(this.wpAttachment, (WpAttachment.class.getClassLoader()));
            in.readList(this.wpTerm, (WpTerm.class.getClassLoader()));
            in.readList(this.curies, (Cury.class.getClassLoader()));
        }

        public Links() {
        }

        public List<Self> getSelf() {
            return self;
        }

        public void setSelf(List<Self> self) {
            this.self = self;
        }

        public List<Collection> getCollection() {
            return collection;
        }

        public void setCollection(List<Collection> collection) {
            this.collection = collection;
        }

  /* public List<About> getAbout() {
        return about;
    }

    public void setAbout(List<About> about) {
        this.about = about;
    }

    public List<Author> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author> author) {
        this.author = author;
    }  */

        public List<Reply> getReplies() {
            return replies;
        }

        public void setReplies(List<Reply> replies) {
            this.replies = replies;
        }

        public List<VersionHistory> getVersionHistory() {
            return versionHistory;
        }

        public void setVersionHistory(List<VersionHistory> versionHistory) {
            this.versionHistory = versionHistory;
        }

        public List<WpFeaturedmedium> getWpFeaturedmedia() {
            return wpFeaturedmedia;
        }

        public void setWpFeaturedmedia(List<WpFeaturedmedium> wpFeaturedmedia) {
            this.wpFeaturedmedia = wpFeaturedmedia;
        }

        public List<WpAttachment> getWpAttachment() {
            return wpAttachment;
        }

        public void setWpAttachment(List<WpAttachment> wpAttachment) {
            this.wpAttachment = wpAttachment;
        }

        public List<WpTerm> getWpTerm() {
            return wpTerm;
        }

        public void setWpTerm(List<WpTerm> wpTerm) {
            this.wpTerm = wpTerm;
        }

        public List<Cury> getCuries() {
            return curies;
        }

        public void setCuries(List<Cury> curies) {
            this.curies = curies;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(self);
            dest.writeList(collection);
            //dest.writeList(about);
            //dest.writeList(author);
            dest.writeList(replies);
            dest.writeList(versionHistory);
            dest.writeList(wpFeaturedmedia);
            dest.writeList(wpAttachment);
            dest.writeList(wpTerm);
            dest.writeList(curies);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class PostCategory implements Serializable, Parcelable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("slug")
        @Expose
        private String slug;
        public final  Parcelable.Creator<PostCategory> CREATOR = new Creator<PostCategory>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public PostCategory createFromParcel(Parcel in) {
                return new PostCategory(in);
            }

            public PostCategory[] newArray(int size) {
                return (new PostCategory[size]);
            }

        };
        private final static long serialVersionUID = 2697886802501092885L;

        protected PostCategory(Parcel in) {
            this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.slug = ((String) in.readValue((String.class.getClassLoader())));
        }

        public PostCategory() {
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(id);
            dest.writeValue(name);
            dest.writeValue(slug);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class PostTag implements Serializable, Parcelable {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("slug")
        @Expose
        private String slug;
        public final  Parcelable.Creator<PostTag> CREATOR = new Creator<PostTag>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public PostTag createFromParcel(Parcel in) {
                return new PostTag(in);
            }

            public PostTag[] newArray(int size) {
                return (new PostTag[size]);
            }

        };
        private final static long serialVersionUID = -5368321391226713836L;

        protected PostTag(Parcel in) {
            this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            this.name = ((String) in.readValue((String.class.getClassLoader())));
            this.slug = ((String) in.readValue((String.class.getClassLoader())));
        }

        public PostTag() {
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(id);
            dest.writeValue(name);
            dest.writeValue(slug);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class Reply implements Serializable, Parcelable {

        @SerializedName("embeddable")
        @Expose
        private Boolean embeddable;
        @SerializedName("href")
        @Expose
        private String href;
        public final  Parcelable.Creator<Reply> CREATOR = new Creator<Reply>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Reply createFromParcel(Parcel in) {
                return new Reply(in);
            }

            public Reply[] newArray(int size) {
                return (new Reply[size]);
            }

        };
        private final static long serialVersionUID = 5746485090145180080L;

        protected Reply(Parcel in) {
            this.embeddable = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.href = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Reply() {
        }

        public Boolean getEmbeddable() {
            return embeddable;
        }

        public void setEmbeddable(Boolean embeddable) {
            this.embeddable = embeddable;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(embeddable);
            dest.writeValue(href);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class Self implements Serializable, Parcelable {

        @SerializedName("href")
        @Expose
        private String href;
        public final  Parcelable.Creator<Self> CREATOR = new Creator<Self>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Self createFromParcel(Parcel in) {
                return new Self(in);
            }

            public Self[] newArray(int size) {
                return (new Self[size]);
            }

        };
        private final static long serialVersionUID = 7151256704678821866L;

        protected Self(Parcel in) {
            this.href = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Self() {
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(href);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class Title implements Serializable, Parcelable {

        @SerializedName("raw")
        @Expose
        private String raw;
        @SerializedName("rendered")
        @Expose
        private String rendered;
        public final  Parcelable.Creator<Title> CREATOR = new Creator<Title>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Title createFromParcel(Parcel in) {
                return new Title(in);
            }

            public Title[] newArray(int size) {
                return (new Title[size]);
            }

        };
        private final static long serialVersionUID = 5632908181418818289L;

        protected Title(Parcel in) {
            this.raw = ((String) in.readValue((String.class.getClassLoader())));
            this.rendered = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Title() {
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(raw);
            dest.writeValue(rendered);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class VersionHistory implements Serializable, Parcelable {

        @SerializedName("href")
        @Expose
        private String href;
        public final  Parcelable.Creator<VersionHistory> CREATOR = new Creator<VersionHistory>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public VersionHistory createFromParcel(Parcel in) {
                return new VersionHistory(in);
            }

            public VersionHistory[] newArray(int size) {
                return (new VersionHistory[size]);
            }

        };
        private final static long serialVersionUID = -5400994957591587086L;

        protected VersionHistory(Parcel in) {
            this.href = ((String) in.readValue((String.class.getClassLoader())));
        }

        public VersionHistory() {
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(href);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class WpAttachment implements Serializable, Parcelable {

        @SerializedName("href")
        @Expose
        private String href;
        public final  Parcelable.Creator<WpAttachment> CREATOR = new Creator<WpAttachment>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public WpAttachment createFromParcel(Parcel in) {
                return new WpAttachment(in);
            }

            public WpAttachment[] newArray(int size) {
                return (new WpAttachment[size]);
            }

        };
        private final static long serialVersionUID = -7624697014272575194L;

        protected WpAttachment(Parcel in) {
            this.href = ((String) in.readValue((String.class.getClassLoader())));
        }

        public WpAttachment() {
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(href);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class WpFeaturedmedium implements Serializable, Parcelable {

        @SerializedName("embeddable")
        @Expose
        private Boolean embeddable;
        @SerializedName("href")
        @Expose
        private String href;
        public final  Parcelable.Creator<WpFeaturedmedium> CREATOR = new Creator<WpFeaturedmedium>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public WpFeaturedmedium createFromParcel(Parcel in) {
                return new WpFeaturedmedium(in);
            }

            public WpFeaturedmedium[] newArray(int size) {
                return (new WpFeaturedmedium[size]);
            }

        };
        private final static long serialVersionUID = 7762362880293914874L;

        protected WpFeaturedmedium(Parcel in) {
            this.embeddable = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.href = ((String) in.readValue((String.class.getClassLoader())));
        }

        public WpFeaturedmedium() {
        }

        public Boolean getEmbeddable() {
            return embeddable;
        }

        public void setEmbeddable(Boolean embeddable) {
            this.embeddable = embeddable;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(embeddable);
            dest.writeValue(href);
        }

        public int describeContents() {
            return 0;
        }

    }

    public class WpTerm implements Serializable, Parcelable {

        @SerializedName("taxonomy")
        @Expose
        private String taxonomy;
        @SerializedName("embeddable")
        @Expose
        private Boolean embeddable;
        @SerializedName("href")
        @Expose
        private String href;
        public final Parcelable.Creator<WpTerm> CREATOR = new Creator<WpTerm>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public WpTerm createFromParcel(Parcel in) {
                return new WpTerm(in);
            }

            public WpTerm[] newArray(int size) {
                return (new WpTerm[size]);
            }

        };
        private final static long serialVersionUID = -1604617381627249799L;

        protected WpTerm(Parcel in) {
            this.taxonomy = ((String) in.readValue((String.class.getClassLoader())));
            this.embeddable = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
            this.href = ((String) in.readValue((String.class.getClassLoader())));
        }

        public WpTerm() {
        }

        public String getTaxonomy() {
            return taxonomy;
        }

        public void setTaxonomy(String taxonomy) {
            this.taxonomy = taxonomy;
        }

        public Boolean getEmbeddable() {
            return embeddable;
        }

        public void setEmbeddable(Boolean embeddable) {
            this.embeddable = embeddable;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(taxonomy);
            dest.writeValue(embeddable);
            dest.writeValue(href);
        }

        public int describeContents() {
            return 0;
        }
    }
}