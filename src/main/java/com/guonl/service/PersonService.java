package com.guonl.service;

import com.guonl.domain.Person;

import java.util.List;

/**
 * Created on 2017/11/7.
 *
 * @author zlf
 * @since 1.0
 */
public interface PersonService {
    Person save(Person person);

    List<Person> findByAge(Integer age);

    List<Person> findAll();

    Person findOne(Integer id);


    void delete(Integer id);
}
