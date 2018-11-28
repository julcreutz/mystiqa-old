package mystiqa.ecs.system;

import mystiqa.ecs.component.EntityComponent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireComponent {
    Class<? extends EntityComponent>[] value();
}
