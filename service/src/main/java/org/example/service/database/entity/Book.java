package org.example.service.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(exclude = "orders")
@ToString(exclude = "orders")
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Book implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Author author;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Category category;

    private Integer publishYear;

    @Column(length = 3000)
    private String description;

    @Column(nullable = false)
    private Integer number;

    @Column(length = 128)
    private String picture;

    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();
}
