package com.ef.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log_row")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogRow {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ip;

    private String request;

    private int status;

    private String useragent;

    private DateTime startdate;
}
