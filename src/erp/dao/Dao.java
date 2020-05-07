/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.dao;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author peukianm
 */
public interface Dao<T> {     
    Optional<T> get(long id);     
    List<T> getAll();     
    void save(T t);     
    void update(T t, String[] params);  
    void update(T t);
    void delete(T t);
}
