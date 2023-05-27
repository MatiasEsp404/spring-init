package com.matias.studying.config.seeder;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.matias.studying.config.security.common.Role;
import com.matias.studying.model.RoleEntity;
import com.matias.studying.repository.IRoleRepository;

@Component
public class RoleSeeder {

	@Autowired
	private IRoleRepository roleRepository;

	@EventListener
	public void seed(ContextRefreshedEvent event) {
		createRoleTable();
	}

	private void createRoleTable() {
		if (roleRepository.count() < 2) {
			roleRepository.saveAll(Arrays.asList(buildRole(Role.USER), buildRole(Role.ADMIN)));
		}
	}

	private RoleEntity buildRole(Role role) {
		return RoleEntity.builder().name(role.getFullRoleName()).build();
	}

}
