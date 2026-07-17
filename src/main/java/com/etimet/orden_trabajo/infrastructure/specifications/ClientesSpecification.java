package com.etimet.orden_trabajo.infrastructure.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.etimet.orden_trabajo.domain.entities.Clientes;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class ClientesSpecification {
    public static Specification<Clientes> busquedaGlobal(String termino, List<String> camposDeTexto) {
        return (root, query, criteriaBuilder) -> {
            // Si no hay término de búsqueda, no filtramos nada (retorna true)
            if (termino == null || termino.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            // Preparamos el término para un "LIKE %termino%" y lo pasamos a minúsculas
            String patronBusqueda = "%" + termino.toLowerCase() + "%";
            List<Predicate> predicados = new ArrayList<>();

            // Recorremos los campos que definiste y armamos los OR
            for (String campo : camposDeTexto) {
                if (campo.contains(".")) {
                    // Si tiene un punto, es una relación (ej: lineaInventario.descripcion)
                    String[] partes = campo.split("\\.");
                    String tablaRelacion = partes[0];
                    String campoRelacion = partes[1];

                    // Hacemos un LEFT JOIN con la tabla relacionada
                    Join<Object, Object> joinTabla = root.join(tablaRelacion, JoinType.LEFT);

                    predicados.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(joinTabla.get(campoRelacion)),
                                    patronBusqueda));
                } else {
                    // Es un campo directo (codigo, nombre, siglas)
                    predicados.add(
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get(campo)),
                                    patronBusqueda));
                }
            }

            // Unimos todos los predicados con un OR lógico
            return criteriaBuilder.or(predicados.toArray(new Predicate[0]));
        };
    }

    public static Specification<Clientes> activos() {
        return (root, query, cb) -> cb.isTrue(root.get("estadoCliente"));
    }

}
