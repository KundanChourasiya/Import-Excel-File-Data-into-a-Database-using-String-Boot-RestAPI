package com.demo.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "product_no")
    private Integer product_no;

    @Column(name = "product_name")
    private String product_name;

    @Column(name = "product_price")
    private Long product_price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "category")
    private String category;

}