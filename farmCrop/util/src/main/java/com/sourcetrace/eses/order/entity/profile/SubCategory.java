package com.sourcetrace.eses.order.entity.profile;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.sourcetrace.eses.util.profile.Product;

// TODO: Auto-generated Javadoc
/**
 * The Class SubCategory.
 */
public class SubCategory {

	public static final int MAX_LENGTH_NAME = 50;

	private long id;
	private String code;
	private String name;
	private Category category;
	private Set<Product> products;
	private String branchId;
	
	
	/**
	 * Transient variable
	 */
	private List<String> branchesList;
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the code.
	 * 
	 * @return the code
	 */
	@Pattern(regexp = "[^\\p{Punct}]+$", message = "pattern.code")
	@NotEmpty(message = "empty.code")
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 * 
	 * @param code
	 *            the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	@Length(max = MAX_LENGTH_NAME, message = "length.name")
	@NotEmpty(message = "empty.name")
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the category.
	 * 
	 * @param category
	 *            the new category
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * Gets the category.
	 * 
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Gets the products.
	 * 
	 * @return the products
	 */
	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * Sets the products.
	 * 
	 * @param products
	 *            the new products
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Override
    public String toString() {

        return name;
    }

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public List<String> getBranchesList() {
		return branchesList;
	}

	public void setBranchesList(List<String> branchesList) {
		this.branchesList = branchesList;
	}

   
    
}
