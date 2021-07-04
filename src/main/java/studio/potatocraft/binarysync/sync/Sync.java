package studio.potatocraft.binarysync.sync;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Sync {
    String field();
    SyncType type();
}
