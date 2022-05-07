package com.example.gccoffee.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Utils {
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}
