package com.backup.zgqsgw.Jpa;

import com.backup.zgqsgw.Entity.DBinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.io.Serializable;

/**
 * @author hwb
 * @create 2018/5/15 16:09
 */
public interface DBInfoJpa extends JpaRepository<DBinfo,Long>,JpaSpecificationExecutor<DBinfo>,Serializable{


}
