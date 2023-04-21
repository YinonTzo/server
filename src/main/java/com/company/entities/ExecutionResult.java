package com.company.entities;

import com.company.common.statuses.ExecutionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "execution_result")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "execution_result_seq")
    @SequenceGenerator(name = "execution_result_seq", sequenceName = "execution_result_sequence", allocationSize = 1)
    private Integer id;

    private Integer messageId;

    private ExecutionStatus status;

    private String base64Result = "";
}