package com.example.demo.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "distribution_centers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCenter {
    @Id
    private Long id;

    private String name;
    private Double latitude;
    private Double longitude;
}
