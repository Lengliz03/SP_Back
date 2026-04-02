package com.textile.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MouvementStatsDTO {
    private String date;
    private Long entrees;
    private Long sorties;
}