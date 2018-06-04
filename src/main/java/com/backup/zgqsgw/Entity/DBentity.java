package com.backup.zgqsgw.Entity;

import lombok.Data;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.Objects;


@Data
public class DBentity {
    private String ip;
    private String schemaname;
    private String user;
    private String password;
    private String dbType;


    @Override
    public boolean equals(Object o) {

        DBentity dBentity = (DBentity) o;
        return Objects.equals(ip, dBentity.ip) &&
                Objects.equals(schemaname, dBentity.schemaname) &&
                Objects.equals(user, dBentity.user) &&
                Objects.equals(password, dBentity.password) &&
                Objects.equals(dbType, dBentity.dbType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), ip, schemaname, user, password, dbType);
    }


}
