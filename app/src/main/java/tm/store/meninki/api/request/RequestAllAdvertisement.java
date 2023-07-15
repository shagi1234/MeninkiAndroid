package tm.store.meninki.api.request;

public class RequestAllAdvertisement {
    int sortType;
    boolean descending;
    String[] categoryIds;
    int pageNumber;
    int take;
    int[] welayats;

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public boolean isDescending() {
        return descending;
    }

    public void setDescending(boolean descending) {
        this.descending = descending;
    }

    public String[] getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String[] categoryIds) {
        this.categoryIds = categoryIds;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    public int[] getWelayats() {
        return welayats;
    }

    public void setWelayats(int[] welayats) {
        this.welayats = welayats;
    }
}
