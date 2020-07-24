package com.sourcetrace.esesw.entity.profile;

public class ESELogo {
    
    private long id;
    private byte[] image;
    /**
     * @param id the id to set
     */
    public void setId(long id) {

        this.id = id;
    }
    /**
     * @return the id
     */
    public long getId() {

        return id;
    }
    /**
     * @param image the image to set
     */
    public void setImage(byte[] image) {

        this.image = image;
    }
    /**
     * @return the image
     */
    public byte[] getImage() {

        return image;
    }

}
