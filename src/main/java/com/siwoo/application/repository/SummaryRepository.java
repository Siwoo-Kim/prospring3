package com.siwoo.application.repository;

import com.siwoo.application.domain.EmployeeSummary;

import java.util.List;

public interface SummaryRepository {
    void showSummary();
    List<EmployeeSummary> findAll();
}
