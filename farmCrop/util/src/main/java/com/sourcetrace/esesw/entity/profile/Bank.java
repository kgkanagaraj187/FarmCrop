package com.sourcetrace.esesw.entity.profile;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.util.entity.Image;

/**
 * The Class Bank.
 */
public class Bank {

    /** The Constant MAX_LENGTH_CODE. */
    public static final int MAX_LENGTH_CODE = 12;

    /** The Constant MAX_LENGTH_NAME. */
    public static final int MAX_LENGTH_NAME = 35;

    /** The Constant MAX_LENGTH_DESC. */
    public static final int MAX_LENGTH_DESC = 255;

    /** The code. */
    private String code;

    /** The description. */
    private String description;

    /** The id. */
    private long id;

    /** The name. */
    private String name;

    /** The logo. */
    private Image logo;

    /**
     * Gets the code.
     * @return the code
     */
    @Length(max = MAX_LENGTH_CODE, message = "length.code")
    @Pattern(regexp= "[^\\p{Punct}]+$", message = "pattern.code")
    @NotEmpty(message = "empty.code")
    public String getCode() {

        return code;
    }

    /**
     * Gets the description.
     * @return the description
     */
    @Length(max = MAX_LENGTH_DESC, message = "length.description")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.description")
    @NotEmpty(message = "empty.description")
    public String getDescription() {

        return description;
    }

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Gets the name.
     * @return the name
     */
    @Length(max = MAX_LENGTH_NAME, message = "length.name")
    @Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.name")
    @NotEmpty(message = "empty.name")
    public String getName() {

        return name;
    }

    /**
     * Sets the code.
     * @param code the new code
     */
    public void setCode(String code) {

        this.code = code;
    }

    /**
     * Sets the description.
     * @param description the new description
     */
    public void setDescription(String description) {

        this.description = description;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Sets the name.
     * @param name the new name
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Gets the logo.
     * @return the logo
     */
    public Image getLogo() {

        return logo;
    }

    /**
     * Sets the logo.
     * @param logo the new logo
     */
    public void setLogo(Image logo) {

        this.logo = logo;
    }

    /**
     * To string.
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return name;
    }
}
