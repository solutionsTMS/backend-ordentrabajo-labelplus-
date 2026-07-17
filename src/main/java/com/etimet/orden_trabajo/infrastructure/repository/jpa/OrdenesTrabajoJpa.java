package com.etimet.orden_trabajo.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.etimet.orden_trabajo.domain.entities.OrdenesTrabajo;

public interface OrdenesTrabajoJpa
        extends JpaRepository<OrdenesTrabajo, Long>, JpaSpecificationExecutor<OrdenesTrabajo> {
}
