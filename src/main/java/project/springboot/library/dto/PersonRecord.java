package project.springboot.library.dto;

import java.math.BigDecimal;

public record PersonRecord(int id, String name, int year, String password,
        String role, int version, long totalBooks, long totalPages, BigDecimal totalPrice) {

}
