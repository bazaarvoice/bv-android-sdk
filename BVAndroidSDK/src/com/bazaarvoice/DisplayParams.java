package com.bazaarvoice;

import java.util.ArrayList;
import java.util.List;

/**
 * User: gary
 * Date: 4/9/12
 * Time: 9:24 PM
 */
public class DisplayParams extends BazaarParams {
    private List<String> includes;
    private List<String> sort;
    private List<String> search = null;
    private Integer offset;
    private Integer limit;
    private List<String> attributes;
    private String filterType;
    private String locale;
    private List<String> stats;
    private List<String> sortType;
    private List<String> filters;
    private List<String> limitType;

    public DisplayParams()
    {
    }

    /**
     * add an include to the parameter list
     *
     * @param include the new include
     */
    public void addInclude(String include){
        if (includes == null) {
            includes = new ArrayList<String>();
        }
        this.includes.add(encode(include));
    }

    /**
     * Add the sort term to the parameter list
     * @param name the name of the parameter
     * @param asc if true sort ascending, descending if false
     */
    public void addSort(String name, boolean asc) {
        if (sort == null) {
            sort = new ArrayList<String>();
        }
        sort.add(name + ":" + (asc?"asc":"desc"));
    }

    /**
     * Add a stat to the paramter list
     * @param stat the stat to add
     */
    public void addStats(String stat) {
        if (stats == null) {
            stats = new ArrayList<String>();
        }
        this.stats.add(encode(stat));
    }

    /**
     * Add a search term to the parameter list
     * @param searchString the string to search for
     */
    public void addSearch(String searchString) {
        if (search == null) {
            search = new ArrayList<String>();
        }
        search.add("\"" + encode(searchString) + "\"");
    }

    /**
     * Add a sort type to the parameter list, ie sort_reviews=asc
     * @param type the type of sort; comments, reviews, etc
     * @param sort the value to sort by
     * @param asc if true sort ascending, descending if false
     */
    public void addSortType(String type, String sort, boolean asc) {
        if (sortType == null) {
            sortType = new ArrayList<String>();
        }
        sortType.add("sort_" + type + "="+sort+":"+asc);
    }

    /**
     * Add limit type
     * @param type the type of limit; comments, reviews, etc
     * @param limitVal the number to limit
     */
    public void addLimitType(String type, int limitVal) {
        if (limitType == null) {
            limitType = new ArrayList<String>();
        }
        sortType.add("limit_" + type + "="+limitVal);
    }

    /**
     * @param offset new offset
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * @param limit new limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Add a filtering option for included nested content.
     * @param filterType can be any included nested content. i.e. Comments for Reviews.  If null this is a regular filter
     * @param type what to filter on, ie Rating or ProductId
     * @param equality comparison for filtering, greater than, equals etc
     * @param values the values for the filter
     */
    public void addFilterType(String filterType, String type, Equality equality, String[] values) {
        if (filters == null) {
            filters = new ArrayList<String>();
        }
        String filterName = filterType == null?"filter=":"filter_" + filterType + "=";
        StringBuilder filterString = new StringBuilder();
        filterString
                .append(filterName)
                .append(type)
                .append(":")
                .append(equality.getEquality())
                .append(":");

        for (int i = 0; i < values.length; ++i) {
            if (i > 0) {
                filterString.append(',');
            }
            filterString.append(encode(values[i]));
        }
        filters.add(filterString.toString());
    }

    /**
     * Add a filtering option for included nested content.
     * @param filterType can be any included nested content. i.e. Comments for Reviews.  If null this is a regular filter
     * @param type what to filter on, ie Rating or ProductId
     * @param equality comparison for filtering, greater than, equals etc
     * @param value the value for the filter
     */
    public void addFilterType(String filterType, String type, Equality equality, String value) {
        if (filters == null) {
            filters = new ArrayList<String>();
        }
        String filterName = filterType == null?"filter=":"filter_" + filterType +"=";
        StringBuilder filterString = new StringBuilder();
        filterString
                .append(filterName)
                .append(type)
                .append(":")
                .append(equality.getEquality())
                .append(":")
                .append(encode(value));
        filters.add(filterString.toString());
    }

    /**
     * Add a filtering option for included nested content, equality is equality is set to Equality.EQUAL
     * @param filterType can be any included nested content. i.e. Comments for Reviews.  If null this is a regular filter
     * @param type what to filter on, ie Rating or ProductId
     * @param value the values for the filter
     */
    public void addFilterType(String filterType, String type, String value) {
        if (filters == null) {
            filters = new ArrayList<String>();
        }
        String filterName = filterType == null?"filter=":"filter_" + filterType + "=";
        filters.add(filterName + type + ":" + encode(value));
    }

    /**
     * Filter criteria for primary content of the query. Multiple filter criteria are supported.  Same as calling
     * addFilterType(null, type, equality, values);
     * @param type what to filter on, ie Rating or ProductId
     * @param equality comparison for filtering, greater than, equals etc
     * @param values the values for the filter
     */
    public void addFilter(String type, Equality equality, String[] values) {
        addFilterType(null,type,equality,values);
    }

    /**
     * Filter criteria for primary content of the query. Multiple filter criteria are supported.  Same as calling
     * addFilterType(null, type, equality, value);
     * @param type what to filter on, ie Rating or ProductId
     * @param equality comparison for filtering, greater than, equals etc
     * @param value the value for the filter
     */
    public void addFilter(String type, Equality equality, String value) {
        addFilterType(null,type,equality,value);
    }

    /**
     * Filter criteria for primary content of the query. Multiple filter criteria are supported.  Same as calling
     * addFilterType(null, type, Equality.EQUALS, value);
     * @param type what to filter on, ie Rating or ProductId
     * @param value the values for the filter
     */
    public void addFilter(String type, String value) {
        addFilterType(null,type,value);
    }


    /**
     * Add all parameters on this class into a url
     * @param url the base url to append to
     * @return the full URL
     */
    public String toURL(String url) {

        if (filters != null) {
            char separator = url.contains("?")?'&':'?';
            for (String filter : filters) {
                url += separator + filter;
                separator = '&';
            }
        }

        url = addURLParamsFromList(url, "include", includes);
        url = addURLParamsFromList(url, "attribute", attributes);
        url = addURLParamsFromList(url, "stats", stats);
        url = addURLParamsFromList(url, "sort", sort);

        url = addURLParamsFromList(url,"search", search);

        url = addURLParameter(url, "offset", offset);
        url = addURLParameter(url, "limit", limit);

        if (filterType != null) {
            url = addURLParameter(url, "filterType", filterType+"");
        }

        if (locale != null) {
            url = addURLParameter(url, "locale", locale+"");
        }

        char separator = url.contains("?")?'&':'?';
        if (sortType != null) {
            for (String s : sortType) {
                url += separator + s;
                separator = '&';
            }
        }

        if (limitType != null) {
            for (String limit : limitType) {
                url += separator + limit;
                separator = '&';
            }
        }

        return url;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }


    public List<String> getSearch() {
        return search;
    }

    public void setSearch(List<String> search) {
        this.search = search;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public List<String> getStats() {
        return stats;
    }

    public void setStats(List<String> stats) {
        this.stats = stats;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public List<String> getSortType() {
        return sortType;
    }

    public void setSortType(List<String> sortType) {
        this.sortType = sortType;
    }

    public List<String> getLimitType() {
        return limitType;
    }

    public void setLimitType(List<String> limitType) {
        this.limitType = limitType;
    }



}
