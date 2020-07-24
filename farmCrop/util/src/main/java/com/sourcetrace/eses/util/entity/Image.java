package com.sourcetrace.eses.util.entity;

import java.io.Serializable;

public class Image implements Serializable {

	private static final long serialVersionUID = 3886447574095142731L;
	private long id;
    private String imageId;
    private byte[] image;

    /**
     * Gets the id.
     * @return the id
     */
    public long getId() {

        return id;
    }

    /**
     * Sets the id.
     * @param id the new id
     */
    public void setId(long id) {

        this.id = id;
    }

    /**
     * Gets the image id.
     * @return the image id
     */
    public String getImageId() {

        return imageId;
    }

    /**
     * Sets the image id.
     * @param imageId the new image id
     */
    public void setImageId(String imageId) {

        this.imageId = imageId;
    }

    /**
     * Gets the image.
     * @return the image
     */
    public byte[] getImage() {
    
        return image;
    }

    /**
     * Sets the image.
     * @param image the image to set
     */
    public void setImage(byte[] image) {
    
        this.image = image;
    }
}
