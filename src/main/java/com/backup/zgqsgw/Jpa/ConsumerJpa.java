package com.backup.zgqsgw.Jpa;

import com.backup.zgqsgw.Entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * @author hwb
 * @create 2018/5/31 8:18
 */
public interface ConsumerJpa extends JpaRepository<Consumer,Long>,JpaSpecificationExecutor<Consumer>,Serializable {
}
