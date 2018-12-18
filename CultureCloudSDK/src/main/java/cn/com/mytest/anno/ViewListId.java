package cn.com.mytest.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wangcw
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited	//若不加这个，则子类将无法获得父类的注解，使用后可获得，并且如果如果子类使用了和父类相同的注解，则父类的注解将被擦除，非常好
public @interface ViewListId {
	int value();
}
