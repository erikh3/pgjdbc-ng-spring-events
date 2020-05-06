package cz.muni.fi.pa036.pgjdbcngspringdemo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
