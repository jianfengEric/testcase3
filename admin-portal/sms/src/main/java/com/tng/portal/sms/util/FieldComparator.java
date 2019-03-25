/**
 * 
 */
package com.tng.portal.sms.util;

import java.lang.reflect.Field;
import java.util.Comparator;

/**   
 *  
 * 
 * @Description:   
 * @Author:       XiongLiangSheng  
 * @CreateDate:   2014 year 10 month 9 day   Afternoon 4:29:41   
 * @Version:      v1.0
 *    
 * Date    	CR/DEFECT   Modified By    Description of change
 */
public class FieldComparator implements Comparator<Object>
{
  public int compare(Object arg0, Object arg1)
  {
    Field fieldOne = (Field)arg0;
    Field fieldTwo = (Field)arg1;
    if (fieldOne.isAnnotationPresent(UrlParameter.class) && fieldTwo.isAnnotationPresent(UrlParameter.class)) {
    	UrlParameter annoOne = fieldOne.getAnnotation(UrlParameter.class);
    	UrlParameter annoTwo = fieldTwo.getAnnotation(UrlParameter.class);
        return annoOne.pos() - annoTwo.pos();
    } else {
        return 0;
    }
  }
}