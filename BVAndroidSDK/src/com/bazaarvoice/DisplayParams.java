package com.bazaarvoice;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Handles the parameters for a display request. <br>
 * Use of this class will rely on knowledge of the <a
 * href="http://developer.bazaarvoice.com/">Bazaarvoice API</a>. You should use
 * this site as a reference for which parameters to pass using this class.
 * 
 * 
 * <p>
 * Created on 7/9/12. Copyright (c) 2012 BazaarVoice. All rights reserved.
 * 
 * @author Bazaarvoice Engineering
 */
public class DisplayParams extends BazaarParams {
	private List<String> includes;
	private List<String> attributes;
	private List<String> sort;
	private List<String> search = null;
	private Integer offset;
	private Integer limit;
	private String locale;
	private List<String> stats;
	private List<String> sortType;
	private List<String> filters;
	private List<String> limitType;

	/**
	 * Creates a DisplayParams instance.
	 */
	public DisplayParams() {
	}
	
	/**
	 * Add an "Attribute" parameter to the request.
	 * 
	 * @param attribute
	 *            the new attribute
	 */
	public void addAttribute(String attribute){
		if (attributes == null)
			attributes = new ArrayList<String>();
		this.attributes.add(encode(attribute));
	}

	/**
	 * Add an "Include" parameter to the request.
	 * 
	 * @param include
	 *            the new include
	 */
	public void addInclude(IncludeType include) {
		if (includes == null) {
			includes = new ArrayList<String>();
		}
		this.includes.add(encode(include.getDisplayName()));
	}

	/**
	 * Add an attribute to the "Sort" parameter list. Sorts by the schema of the
	 * given attribute.
	 * 
	 * @param attribute
	 *            the attribute name
	 * @param asc
	 *            true for ascending, false for descending
	 */
	public void addSort(String attribute, boolean asc) {
		if (sort == null) {
			sort = new ArrayList<String>();
		}
		sort.add(attribute + ":" + (asc ? "asc" : "desc"));
	}

	/**
	 * Add a content type to the "Stats" parameter. Retrieves statistics for
	 * that type of content along with the response.
	 * 
	 * @param type
	 *            the type of content for which to add stats
	 */
	public void addStats(String type) {
		if (stats == null) {
			stats = new ArrayList<String>();
		}
		this.stats.add(encode(type));
	}

	/**
	 * Add a single term to the "Search" parameter. Limits the results to match
	 * a search string.
	 * 
	 * <p>
	 * <b>Note:</b> Be sure to remove any leading or trailing white space as it
	 * could interfere with search performance.
	 * 
	 * <p><b>Usage:</b><br> This is the recommended way to add search terms to a request.
	 *        Although, if you prefer to add them all at once, see
	 *        {@link #setSearch(List)}.
	 * 
	 * @param searchString
	 *            the string to search for
	 */
	public void addSearch(String searchString) {
		if (search == null) {
			search = new ArrayList<String>();
		}
		search.add("\"" + encode(searchString) + "\"");
	}

	/**
	 * Add a "Sort_[TYPE]" parameter to the request. Sorts any included content
	 * by the schema of the given attribute.
	 * 
	 * @param type
	 *            the type to sort: comments, reviews, etc
	 * @param attribute
	 *            the attribute name
	 * @param asc
	 *            true for ascending, false for descending
	 */
	public void addSortType(String type, String attribute, boolean asc) {
		if (sortType == null) {
			sortType = new ArrayList<String>();
		}
		sortType.add("sort_" + type + "=" + attribute + ":" + asc);
	}

	/**
	 * Add a "Limit_[TYPE]" parameter to the request. Limits any included
	 * content to a maximum amount.
	 * 
	 * @param type
	 *            the type to limit: comments, reviews, etc
	 * @param limitVal
	 *            the number to limit
	 */
	public void addLimitType(String type, int limitVal) {
		if (limitType == null) {
			limitType = new ArrayList<String>();
		}
		sortType.add("limit_" + type + "=" + limitVal);
	}

	/**
	 * Add an "Offset" parameter to the request. Changes the index at which to
	 * begin returning results.
	 * 
	 * @param offset
	 *            new offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * Add a "Limit" parameter to the request. Limits the maximum amount of
	 * results. The default is 100.
	 * 
	 * @param limit
	 *            new limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * Add a "Filter_[TYPE]" parameter to the request. Filters any included
	 * content by the schema of the given attribute, Equality type, and values.
	 * <p>
	 * Allows multiple values.
	 * 
	 * @param filterType
	 *            the type to filter, i.e. Comments for Reviews. If null this is
	 *            a regular filter
	 * @param attribute
	 *            what to filter on, i.e. Rating or ProductId
	 * @param equality
	 *            comparison for filtering, greater than, equals etc
	 * @param values
	 *            the values for the filter
	 */
	public void addFilterType(IncludeType filterType, String attribute,
			Equality equality, String[] values) {
		if (filters == null) {
			filters = new ArrayList<String>();
		}
		String filterName = filterType == null ? "filter=" : "filter_"
				+ filterType.getDisplayName() + "=";
		StringBuilder filterString = new StringBuilder();
		filterString.append(filterName).append(attribute).append(":")
				.append(equality.getEquality()).append(":");

		for (int i = 0; i < values.length; ++i) {
			if (i > 0) {
				filterString.append(',');
			}
			filterString.append(encode(values[i]));
		}
		filters.add(filterString.toString());
	}

	/**
	 * Add a "Filter_[TYPE]" parameter to the request. Filters any included
	 * content by the schema of the given attribute, Equality type, and value.
	 * 
	 * <p>
	 * Allows only one value.
	 * 
	 * @param filterType
	 *            the type to filter, i.e. Comments for Reviews. If null this is
	 *            a regular filter
	 * @param attribute
	 *            what to filter on, i.e. Rating or ProductId
	 * @param equality
	 *            comparison for filtering, greater than, equals etc
	 * @param value
	 *            the value for the filter
	 */
	public void addFilterType(IncludeType filterType, String attribute,
			Equality equality, String value) {
		if (filters == null) {
			filters = new ArrayList<String>();
		}
		String filterName = filterType == null ? "filter=" : "filter_"
				+ filterType.getDisplayName() + "=";
		StringBuilder filterString = new StringBuilder();
		filterString.append(filterName).append(attribute).append(":")
				.append(equality.getEquality()).append(":")
				.append(encode(value));
		filters.add(filterString.toString());
	}

	/**
	 * Add a "Filter_[TYPE]" parameter to the request. Filters any included
	 * content by the schema of the given attribute, and value.
	 * 
	 * <p>
	 * Equality is set to "equals", and only one value is allowed.
	 * 
	 * @param filterType
	 *            can be any included nested content. i.e. Comments for Reviews.
	 *            If null this is a regular filter
	 * @param attribute
	 *            what to filter on, i.e. Rating or ProductId
	 * @param value
	 *            the value for the filter
	 */
	public void addFilterType(IncludeType filterType, String attribute, String value) {
		if (filters == null) {
			filters = new ArrayList<String>();
		}
		String filterName = filterType == null ? "filter=" : "filter_"
				+ filterType.getDisplayName() + "=";
		filters.add(filterName + attribute + ":" + encode(value));
	}

	/**
	 * Add a "Filter" parameter to the request. Same as calling
	 * addFilterType(null, attribute, equality, values);
	 * <p>
	 * Allows multiple values.
	 * 
	 * @param attribute
	 *            what to filter on, i.e. Rating or ProductId
	 * @param equality
	 *            comparison for filtering, greater than, equals etc
	 * @param values
	 *            the values for the filter
	 */
	public void addFilter(String attribute, Equality equality, String[] values) {
		addFilterType(null, attribute, equality, values);
	}

	/**
	 * Add a "Filter" parameter to the request. Same as calling
	 * addFilterType(null, attribute, equality, value);
	 * <p>
	 * Allows only one value.
	 * 
	 * @param attribute
	 *            what to filter on, i.e. Rating or ProductId
	 * @param equality
	 *            comparison for filtering, greater than, equals etc
	 * @param value
	 *            the value for the filter
	 */
	public void addFilter(String attribute, Equality equality, String value) {
		addFilterType(null, attribute, equality, value);
	}

	/**
	 * Add a "Filter" parameter to the request. Same as calling
	 * addFilterType(null, attribute, Equality.EQUALS, value);
	 * 
	 * <p>
	 * Equality is set to "equals", and only one value is allowed.
	 * 
	 * @param attribute
	 *            what to filter on, i.e. Rating or ProductId
	 * @param value
	 *            the value for the filter
	 */
	public void addFilter(String attribute, String value) {
		addFilterType(null, attribute, value);
	}

	/**
	 * Add the parameters set in this instance to the given url string.
	 * 
	 * @param url
	 *            the base url to append to
	 * @return the full url
	 */
	public String toURL(String url) {

		if (filters != null) {
			char separator = url.contains("?") ? '&' : '?';
			for (String filter : filters) {
				url += separator + filter;
				separator = '&';
			}
		}

		url = addURLParamsFromList(url, "include", includes);
		url = addURLParamsFromList(url, "stats", stats);
		url = addURLParamsFromList(url, "sort", sort);

		url = addURLParamsFromList(url, "search", search);

		url = addURLParameter(url, "offset", offset);
		url = addURLParameter(url, "limit", limit);

		if (locale != null) {
			url = addURLParameter(url, "locale", locale + "");
		}

		char separator = url.contains("?") ? '&' : '?';
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
	
	/**
	 * Get the list of attributes.
	 * 
	 * @return a list of attributes
	 */
	public List<String> getAttributes(){
		return attributes;
	}
	
	/**
	 * Set the list of attributes in the "Attributes" parameter
	 * manually.
	 * 
	 * <p><b>Usage:</b><br> Recommended usage is to add attributes one by one using
	 *        {@link #addAttribute(String)}.
	 * @param attributes
	 *            the list of attributes
	 */
	public void setAttributes(List<String> attributes){
		this.attributes = attributes;
	}
	
	/**
	 * Get the list of includes values.
	 * 
	 * @return a list of values
	 */
	public List<String> getIncludes(){
		return includes;
	}

	/**
	 * Set the list of subjects for inclusion in the "Include" parameter
	 * manually.
	 * 
	 * <p><b>Usage:</b><br> Recommended usage is to add subjects one by one using
	 *       {@link #addInclude(IncludeType)}.
	 * @param includes
	 *            the list of subjects to include
	 */
	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	/**
	 * Get the list of search terms.
	 * 
	 * @return a list of terms
	 */
	public List<String> getSearch() {
		return search;
	}

	/**
	 * Set the list of words to be used in the "Search" parameter manually. Be
	 * sure to remove any leading or trailing white space from each word as it
	 * may interfere with search performance.
	 * 
	 * <p><b>Usage:</b><br> This is for setting all of the search terms at once and relys on
	 *        the user encoding the terms correctly. Recommended usage is to add
	 *        terms one by one using {@link #addSearch(String)}.
	 * 
	 * @param search
	 *            the list of search terms
	 */
	public void setSearch(List<String> search) {
		this.search = search;
	}

	/**
	 * Get the list of filters.
	 * 
	 * @return a list of filters.
	 */
	public List<String> getFilters() {
		return filters;
	}

	/**
	 * Set the list of "Filter" parameters manually.
	 * 
	 * <p><b>Usage:</b><br> Recommended usage is to add filters one by one using
	 *        {@link #addFilter(String, Equality, String[])} or one of the other
	 *        overridden methods.
	 * @param filters
	 *            the list of filters
	 */
	public void setFilters(List<String> filters) {
		this.filters = filters;
	}

	/**
	 * Get the "Locale" parameter if it has been set.
	 * 
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * Add a "Locale" parameter to the request.
	 * 
	 * @param locale
	 *            the locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * Get the list of stats.
	 * 
	 * @return a list of stats
	 */
	public List<String> getStats() {
		return stats;
	}

	/**
	 * Set the list of content types for the "Stats" parameter manually.
	 * 
	 * <p><b>Usage:</b><br> Recommended usage is to add stats one by one using
	 *        {@link #addStats(String)}.
	 * 
	 * @param stats
	 *            the list of content types
	 */
	public void setStats(List<String> stats) {
		this.stats = stats;
	}

	/**
	 * Get the list of attributes for the "Sort" parameter.
	 * 
	 * @return a list of attributes
	 */
	public List<String> getSort() {
		return sort;
	}

	/**
	 * Set the list of attributes for the "Sort" parameter manually.
	 * 
	 * <p><b>Usage:</b><br> Recommended usage is to add attributes one by one using
	 *        {@link #addSort(String, boolean)}.
	 * @param sort
	 *            the list of attributes
	 */
	public void setSort(List<String> sort) {
		this.sort = sort;
	}

	/**
	 * Get the list of "Sort_[TYPE]" parameters.
	 * 
	 * @return a list of parameters
	 */
	public List<String> getSortType() {
		return sortType;
	}

	/**
	 * Set the list of "Sort_[TYPE]" parameters manually.
	 * 
	 * <p><b>Usage:</b><br> Recommended usage is to add parameters one by one using
	 *        {@link #addSortType(String, String, boolean)}.
	 * @param sortType
	 *            the list of parameters
	 */
	public void setSortType(List<String> sortType) {
		this.sortType = sortType;
	}

	/**
	 * Get the list of "Limit_[TYPE]" parameters.
	 * 
	 * @return a list of parameters
	 */
	public List<String> getLimitType() {
		return limitType;
	}

	/**
	 * Set the list of "Limit_[TYPE]" parameters manually.
	 * 
	 * <p><b>Usage:</b><br> Recommended usage is to add parameters one by one using
	 *        {@link #addLimitType(String, int)}.
	 * @param limitType
	 *            the list of parameters
	 */
	public void setLimitType(List<String> limitType) {
		this.limitType = limitType;
	}

}
