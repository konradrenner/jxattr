/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.freedesktop.search;

/**
 * The QueryBuilder helps to build a query for finding items.
 *
 * @author Konrad Renner
 */
public interface QueryBuilder {
    /**
     * Adds a or condition to a query
     *
     * @param spec
     * @return QueryBuilder
     */
    QueryBuilder or(Operation spec);

    /**
     * Adds a and condition to a query
     *
     * @param spec
     * @return QueryBuilder
     */
    QueryBuilder and(Operation spec);

    /**
     * Creates the configured query, for finding the items
     *
     * @return Query
     */
    Query createQuery();

}
