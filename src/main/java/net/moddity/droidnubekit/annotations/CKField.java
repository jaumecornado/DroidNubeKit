package net.moddity.droidnubekit.annotations;

import net.moddity.droidnubekit.utils.DNKFieldTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by jaume on 16/7/15.
 */
@Target(ElementType.FIELD)
public @interface CKField {
    DNKFieldTypes value();
}
