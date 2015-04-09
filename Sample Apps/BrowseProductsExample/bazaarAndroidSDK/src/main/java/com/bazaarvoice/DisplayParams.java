/*******************************************************************************
 * Copyright 2013 Bazaarvoice
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bazaarvoice;

import java.util.ArrayList;
import java.util.List;

import com.bazaarvoice.types.Equality;
import com.bazaarvoice.types.IncludeStatsType;
import com.bazaarvoice.types.IncludeType;

/**
 * 
 * Handles the parameters for a display request. <br>
 * Use of this class will rely on knowledge of the <a
 * href="http://developer.bazaarvoice.com/">Bazaarvoice API</a>. You should use
 * this site as a reference for which parameters to pass using this class.
 */
public class DisplayParams extends BazaarParams {
	private String search;
	private String locale;
	private Integer limit;
	private Integer offset;
	private Boolean excludeFamily;


	private List<String> includes;
	private List<String> attributes;
	private List<String> sort;	
	private List<String> stats;
	private List<String> sortType;
	private List<String> searchType;
	private List<String> filters;
	private List<String> limitType;

	/**
	 * Creates a DisplayParams instance.
	 */
	public DisplayParams() {
	}

	/**
	 * Get the search term
	 * 
	 * @return a list of terms
	 */
	public String getSearch() {
		return this.search;
	}

	
	/**
	 * Set the search term to query.  This is an "or" search.  For example, when querying for products with 
	 * the search term "Electric Dryer," the result returns products that have both "Electric" and "Dryer" 
	 * in the Product Name or Product Description.
	 * 
	 * @param search
	 *            the earch terms
	 */
	public void setSearch(String search) {
		this.search = search;
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
 	 * Locale to display Labels, Configuration, Product Attributes and Category Attributes in. The default value 
 	 * is the locale defined in the display associated with the API key. If specified, the locale value is also used 
 	 * as the default ContentLocale filter value.
 	 * 
	 * @param locale
	 *            the locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	/**
	 * Get the limit Parameter
	 * 
	 * @return limit parameter
	 */
	public Integer getLimit() {
		return this.limit;
	}

	/**
	 *  Limits the maximum number of results that may be returned.
	 * 
	 * @param limit
	 *            new limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * Get the offset Parameter
	 * 
	 * @return offset parameter
	 */
	public Integer getOffset() {
		return offset;
	}
	
	/**
	 * Changes the index at which to begin returning results.  Useful for paging, for instance.
	 * 
	 * @param offset
	 *            new offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	/**
	 * Get the excludeFamily parameter
	 * 
	 * @return excludeFamily parameter
	 */
	public Boolean getExcludeFamily() {
		return this.excludeFamily;
	}
	
	/**
	 * 	Boolean flag indicating whether to exclude content (reviews, questions, etc.) from other products in the same family as the requested product. This setting only affects any nested content that is returned. For example, "&filter=productid:eq:1101&include=reviews&excludeFamily=true" limits returned review content to just that of product 1101 and not any of the products in the same family. If a value is not defined, content on all products in the family is returned.
	 * 
	 * @param offset
	 *            new offset
	 */
	public void setExcludeFamily(Boolean excludeFamily) {
		this.excludeFamily = excludeFamily;
	}
	
	
	/**
	 * Related subjects to be included (e.g. Products, Categories, Authors, Reviews...).
	 * 
	 * @param type
	 *            the type to include
	 */
	public void addInclude(IncludeType type) {
		if (includes == null) {
			includes = new ArrayList<String>();
		}
		this.includes.add(encode(type.getTypeString()));
	}
	
	public List<String> getIncludes() {
		return includes;
	}
	
	/**
	 * Attributes to be included when returning content. For example, if includes 
	 * are requested along with the ModeratorCodes attribute, both the includes and
	 *  the results will contain moderator codes. In order to filter by ModeratorCode, 
	 *  you must request the ModeratorCodes attribute parameter.
	 * 
	 * @param attribute
	 *            the new attribute
	 */
	public void addAttribute(String attribute) {
		if (attributes == null) {
			attributes = new ArrayList<String>();
		}
		this.attributes.add(encode(attribute));
	}
	
	public List<String> getAttributes() {
		return attributes;
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
	
	public List<String> getSort() {
		return sort;
	}

	/**
	 * Add a content type to the "Stats" parameter. Retrieves statistics for
	 * that type of content along with the response.
	 * 
	 * @param type
	 *            the type of content for which to add stats
	 */
	public void addStats(IncludeStatsType type) {
		if (stats == null) {
			stats = new ArrayList<String>();
		}
		this.stats.add(encode(type.getTypeString()));
	}
	
	public List<String> getStats() {
		return stats;
	}
	
	/**
	 * Search query for included content.  This is an "or" search and must include a type.  For 
	 * example, when querying included products with the search term "Electric Dryer," the result 
	 * returns products that have both "Electric" and "Dryer" in the Product Name or Product 
	 * Description.
	 * 
	 * <p>
	 * <b>Note:</b> Be sure to remove any leading or trailing white space as it
	 * could interfere with search performance.
	 * 
	 * @param type
	 *            the type to search: comments, reviews, etc
	 * @param searchString
	 *            the string to search for
	 */
	public void addSearchOnIncludedType(IncludeType type, String searchString) {
		if (searchType == null) {
			searchType = new ArrayList<String>();
		}
		searchType.add("search_" + type.getTypeString() + "=" + encode(searchString));
	}

	public List<String> getSearchType() {
		return searchType;
	}
	
	/**
	 * Add a "Sort_[TYPE]" parameter to the request. Sorts any included content of the
	 * provided type by the schema of the given attribute.
	 * 
	 * @param type
	 *            the type to sort: comments, reviews, etc
	 * @param attribute
	 *            the attribute name
	 * @param asc
	 *            true for ascending, false for descending
	 */
	public void addSortOnIncludedType(IncludeType type, String attribute, boolean asc) {
		if (sortType == null) {
			sortType = new ArrayList<String>();
		}
		sortType.add("sort_" + type.getTypeString() + "=" + attribute + ":" + asc);
	}
	
	public List<String> getSortType() {
		return sortType;
	}

	/**
	 * Add a "Limit_[TYPE]" parameter to the request. Limits any included
	 * content of the provided type to a maximum amount.
	 * 
	 * @param type
	 *            the type to limit: comments, reviews, etc
	 * @param limitVal
	 *            the number to limit
	 */
	public void addLimitOnIncludedType(IncludeType type, int limitVal) {
		if (limitType == null) {
			limitType = new ArrayList<String>();
		}
		sortType.add("limit_" + type.getTypeString() + "=" + limitVal);
	}
	
	public List<String> getLimitType() {
		return limitType;
	}
	
	// Adds a filter with a single value
	private void addFilterHelper(String filterName, String attribute, 
			Equality equality, String value) {
		if (filters == null) {
			filters = new ArrayList<String>();
		}
		StringBuilder filterString = new StringBuilder();
		filterString.append(filterName).append(attribute).append(":")
				.append(equality.getEquality()).append(":")
				.append(encode(value));
		filters.add(filterString.toString());
	}
	
	public List<String> getFilters() {
		return filters;
	}
	
	// Adds a filter with multiple values
	private void addFilterHelper(String filterName, String attribute, 
			Equality equality, String[] values) {
		if (filters == null) {
			filters = new ArrayList<String>();
		}
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
	 * Filter criteria for primary content of the query.
	 * 
	 * @param attribute
	 *            what to filter on, i.e. Rating or ProductId
	 * @param equality
	 *            comparison for filtering, greater than, equals etc
	 * @param value
	 *            the value for the filter
	 */
	public void addFilter(String attribute, Equality equality, String value) {
		String filterName = "filter=";
		addFilterHelper(filterName, attribute, equality, value);
	}
	
	
	/**
	 * Filter criteria for primary content of the query.
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
		String filterName = "filter=";
		addFilterHelper(filterName, attribute, equality, values);
	}
	
	/**
	 * Add a "Filter_[TYPE]" parameter to the request. Filters any included
	 * content by the schema of the given attribute, Equality type, and value.
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
	public void addFilterOnIncludedType(IncludeType filterType, String attribute,
			Equality equality, String value) {
		String filterName = "filter_" + filterType.getTypeString() + "=";
		addFilterHelper(filterName, attribute, equality, value);
	}
	
	
	/**
	 * Add a "Filter_[TYPE]" parameter to the request. Filters any included
	 * content by the schema of the given attribute, Equality type, and values.
	 * <p>
	 * Allows multiple values.
	 * 
	 * @param filterType
	 *            the type to filter, i.e. Comments for Reviews.
	 * @param attribute
	 *            what to filter on, i.e. Rating or ProductId
	 * @param equality
	 *            comparison for filtering, greater than, equals etc
	 * @param values
	 *            the values for the filter
	 */
	public void addFilterOnIncludedType(IncludeType filterType, String attribute,
			Equality equality, String[] values) {
		String filterName = "filter_" + filterType.getTypeString() + "=";
		addFilterHelper(filterName, attribute, equality, values);
	}

	@Override
	protected String toURL(String apiVersion, String passKey) {
		char separator = '&';
		StringBuilder url = new StringBuilder();

		if (getFilters() != null) {
			for (String filter : getFilters()) {
				url.append(separator);
				url.append(filter);
			}
		}

		url.append(addURLParameter("search", getSearch()));
		if (getLocale() != null) {
			url.append(addURLParameter("locale", getLocale()));
		}
		url.append(addURLParameter("offset", getOffset()));
		url.append(addURLParameter("limit", getLimit()));
		url.append(addURLParameter("excludeFamily", getExcludeFamily()));

		
		url.append(addURLParameter("include", getIncludes()));
		url.append(addURLParameter("attributes", getAttributes()));
		url.append(addURLParameter("stats", getStats()));
		url.append(addURLParameter("sort", getSort()));

		//char separator = url.contains("?") ? '&' : '?';
		if (getSortType() != null) {
			for (String s : getSortType()) {
				url.append(separator + s);
			}
		}
		
		if (getSearchType() != null) {
			for (String s : getSearchType()) {
				url.append(separator + s);
			}
		}

		if (getLimitType() != null) {
			for (String limit : getLimitType()) {
				url.append(separator + limit);
			}
		}
		return url.toString();
	}

	@Override
	protected void addPostParameters(String apiVersion, String passKey,
			BazaarRequest request) {
		// just a method stub
	}

}
