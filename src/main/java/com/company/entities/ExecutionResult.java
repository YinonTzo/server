package com.company.entities;

import com.company.common.statuses.ExecutionStatus;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "client")
public class ExecutionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer messageId;

    private ExecutionStatus status;

    private String base64Result = "";
}