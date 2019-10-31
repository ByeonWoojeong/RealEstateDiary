package app.cosmos.ghrealestatediary.DTO;

import com.google.gson.JsonObject;

/**
 * Created by User on 2017-01-18.
 */

public class SearchPoiDTO {

    public String totalCount;
    public String count;
    public String page;
    public JsonObject pois;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public JsonObject getPois() {
        return pois;
    }

    public void setPois(JsonObject pois) {
        this.pois = pois;
    }

    public SearchPoiDTO(String totalCount, String count, String page, JsonObject pois) {
        this.totalCount = totalCount;
        this.count = count;
        this.page = page;
        this.pois = pois;
    }

    @Override
    public String toString() {
        return "SearchPoiDTO{" +
                "totalCount='" + totalCount + '\'' +
                ", count='" + count + '\'' +
                ", page='" + page + '\'' +
                ", pois=" + pois +
                '}';
    }
}
