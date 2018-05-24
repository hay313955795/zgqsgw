package com.backup.zgqsgw.Utils;

import com.backup.zgqsgw.Entity.DBinfo;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author hwb
 * @create 2018/5/15 13:22
 */
public class PojoUtils {

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

}
