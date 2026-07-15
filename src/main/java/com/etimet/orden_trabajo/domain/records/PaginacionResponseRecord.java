package com.etimet.orden_trabajo.domain.records;

import java.util.List;

public record PaginacionResponseRecord<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int currentPage) {

}
