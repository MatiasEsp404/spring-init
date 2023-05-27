package com.matias.studying.repository;

import com.matias.studying.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Integer> {

  RoleEntity findByName(String name);

}
