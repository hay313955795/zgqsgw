package com.backup.zgqsgw.Utils;

import com.backup.zgqsgw.Entity.DBentity;
import com.backup.zgqsgw.Entity.DBinfo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author hwb
 * @create 2018/5/15 13:22
 */
public class PojoUtils {


    /**
     * 给实体类赋值
     * @param object
     * @param valueMap
     * @return
     */
    public static Object getPojo(Object object, Map<String,String> valueMap) {

        Field[ ] fields = object.getClass().getDeclaredFields();
        for ( Field field : fields )
        {
            // 假设不为空。设置可见性，然后返回
            field.setAccessible( true );
            try
            {
                field.set(object,valueMap.get(field.getName()));
            }
            catch ( Exception e )
            {

            }
        }
       return object;
    }

    /**
     * 判断实体类是否有字段为空
     * @param obj
     * @return
     * @throws Exception
     */
    public static boolean isAllFieldNotNull(Object obj) throws Exception{
        /**
         * 得到类对象
         */
        Class stuCla = (Class) obj.getClass();
        /**
         * 得到属性集合
         */
        Field[] fs = stuCla.getDeclaredFields();
        boolean flag = true;
        for (Field f : fs) {
            /**
             * // 设置属性是可以访问的(私有的也可以)
             */
            f.setAccessible(true);
            /**
             *  得到此属性的值
             */
            Object val = f.get(obj);
            /**
             * 只要有1个属性为空,那么就不能通过校验
             */
            if(val==null) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    public static boolean checkIsInList(DBentity dBentity, List<DBentity> dBentityList){


        for(DBentity dBentity1 :dBentityList){
            if(dBentity1.equals(dBentity)){
                return false;
            }
        }
        return true;
    }
}
