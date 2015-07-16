package net.moddity.droidnubekit.utils;

/**
 * Created by jaume on 13/7/15.
 */
public enum DNKFieldTypes {
    INT64("INT64"),
    STRING("STRING"),
    TIMESTAMP("TIMESTAMP"),
    REFERENCE_LIST("REFERENCE_LIST"),
    UNKNOWN("UNKNOWN");


    private final String text;

    /**
     * @param text
     */
    private DNKFieldTypes(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

    public static DNKFieldTypes fromString(String stringtype) {
        for(DNKFieldTypes type : DNKFieldTypes.values()) {
            if(type.toString().equals(stringtype))
                return type;
        }

        return UNKNOWN;
    }
}
