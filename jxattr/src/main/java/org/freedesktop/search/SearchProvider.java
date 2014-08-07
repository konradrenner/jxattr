/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.freedesktop.search;

/**
 * Provider which can create a QueryBuilder. The QueryBuilder is used to build a
 * query which can find items
 *
 * @author Konrad Renner
 */
public interface SearchProvider {

    QueryBuilder searchForItems(Operation spec);
}
