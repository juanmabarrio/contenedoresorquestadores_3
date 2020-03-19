package es.codeurjc.mastercloudapps.p3.worker;

import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Integer> {

}