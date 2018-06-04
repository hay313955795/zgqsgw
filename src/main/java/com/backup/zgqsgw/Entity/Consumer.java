package com.backup.zgqsgw.Entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author hwb
 * @create 2018/5/31 8:17
 */
@Data
@Entity
@Table(name = "cunsumer")
public class Consumer {


    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "fullmessage")
    private String fullmessage;
}
