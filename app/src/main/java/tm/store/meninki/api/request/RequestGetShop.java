package tm.store.meninki.api.request;

public class RequestGetShop {
    private int sortType = 0;
    private boolean descending;
    private String categoryIds;
    private int pageNumber = 1;
    private int take = 4;

    public RequestGetShop(int sortType, boolean descending, String categoryIds, int pageNumber, int take) {
        this.sortType = sortType;
        this.descending = descending;
        this.categoryIds = categoryIds;
        this.pageNumber = pageNumber;
        this.take = take;
    }

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

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
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
}
