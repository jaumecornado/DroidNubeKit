package net.moddity.droidnubekit.utils;

/**
 * Created by jaume on 14/7/15.
 */
public enum DNKOperationType {
    CREATE("create"),
    UPDATE("update"),
    FORCE_UPDATE("forceUpdate"),
    REPLACE("replace"),
    FORCE_REPLACE("forceReplace"),
    DELETE("delete"),
    FORCE_DELETE("forceDelete");

    private final String text;

    /**
     * @param text
     */
    private DNKOperationType(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

    public static DNKOperationType fromString(String stringtype) {
        for(DNKOperationType type : DNKOperationType.values()) {
            if(type.toString().equals(stringtype))
                return type;
        }

        return null;
    }
}
