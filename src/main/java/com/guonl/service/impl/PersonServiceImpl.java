package com.guonl.service.impl;

import com.guonl.domain.Person;
import com.guonl.repository.PersonRepository;
import com.guonl.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 2017/11/7.
 *
 * @author zlf
 * @since 1.0
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    PersonRepository repository;

    @Override
    public Person save(Person person) {
        return repository.save(person);
    }

    @Override
    public List<Person> findByAge(Integer age) {
        return repository.findByAge(age);
    }

    @Override
    public List<Person> findAll() {
        return repository.findAll();
    }

    @Override
    public Person findOne(Integer id) {
        return repository.findOne(id);
    }

    @Override
    public void delete(Integer id) {
        repository.delete(id);
    }
}
