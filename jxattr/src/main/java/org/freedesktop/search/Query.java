/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.freedesktop.search;

import java.util.Set;

/**
 *
 * @author Konrad Renner
 */
public interface Query {

    /**
     * Finds a set of items, depending on the query which was built
     *
     * @return Set<ItemIdentifier<?>>
     */
    Set<ItemIdentifier<?>> find();

    /**
     * Finds a set of items of a given type, depending on the query which was
     * built
     *
     * @param <T>
     * @return Set<ItemIdentifier<?>>
     */
    <T> Set<ItemIdentifier<T>> find(Class<T> types);
}
