package com.company.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "client_seq")
    @SequenceGenerator(name = "client_seq", sequenceName = "client_sequence", allocationSize = 1)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ExecutionResult> results = new ArrayList<>();
}