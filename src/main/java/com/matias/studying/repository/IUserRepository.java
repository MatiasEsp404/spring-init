package com.matias.studying.repository;

import com.matias.studying.model.UserEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Integer> {

  UserEntity findByEmail(String email);

  @Query(value = "SELECT u FROM UserEntity u WHERE u.softDeleted=false")
  List<UserEntity> findAllActiveUsers();

}