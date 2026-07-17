package com.etimet.orden_trabajo.infrastructure.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.etimet.orden_trabajo.domain.entities.OrdenesTrabajo;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class OrdenesTrabajoSpecification {

    public static Specification<OrdenesTrabajo> busquedaGlobal(String termino, List<String> camposDeTexto) {
        return (root, query, criteriaBuilder) -> {
            if (termino == null || termino.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String patronBusqueda = "%" + termino.toLowerCase() + "%";
            List<Predicate> predicados = new ArrayList<>();

            for (String campo : camposDeTexto) {
                if (campo.contains(".")) {
                    String[] partes = campo.split("\\.");
                    Join<Object, Object> joinTabla = root.join(partes[0], JoinType.LEFT);
                    predicados.add(criteriaBuilder.like(
                            criteriaBuilder.lower(joinTabla.get(partes[1]).as(String.class)),
                            patronBusqueda));
                } else {
                    predicados.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(campo).as(String.class)),
                            patronBusqueda));
                }
            }

            return criteriaBuilder.or(predicados.toArray(new Predicate[0]));
        };
    }
}
