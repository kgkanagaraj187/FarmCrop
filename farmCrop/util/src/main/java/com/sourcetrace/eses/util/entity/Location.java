package com.sourcetrace.eses.util.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.GroupSequence;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.sourcetrace.eses.util.First;
import com.sourcetrace.eses.util.Second;

@GroupSequence({ Location.class, First.class, Second.class })
@Entity
@Table(name = "LOCATION")
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	@Column(name = "CODE")
	@NotBlank(message = "empty.code")
	@Size(groups = Second.class, max = 10, message = "length.code")
	@Pattern(groups = First.class, regexp = EntityInfo.CODE_NAME_REGEX, message = "pattern.code")
	private String code;
	@Column(name = "NAME")
	@NotBlank(message = "empty.name")
	@Size(groups = Second.class, max = 45, message = "length.name")
	@Pattern(groups = First.class, regexp = EntityInfo.CODE_NAME_REGEX, message = "pattern.name")
	private String name;
	@ManyToOne
	@JoinColumn(name = "PARENT_ID", referencedColumnName = "id")
	private Location parent;
	@ManyToOne
	@JoinColumn(name = "LEVEL_ID", referencedColumnName = "id")
	private LocationLevel level;
	@OneToMany(mappedBy = "parent")
	private Set<Location> childs;
	@Column(name = "IS_ACTIVE")
	private int status;
	@Column(name = "CREATE_DT")
	private Date createdDT;
	@Column(name = "CREATE_USER_NAME")
	private String createdUserName;
	@Column(name = "LAST_UPDATE_DT")
	private Date lastUpdatedDT;
	@Column(name = "UPDATE_USER_NAME")
	private String lastUpdatedUserName;
	@Column(name = "REVISION_NO")
	private long revisionNumber;

	// Trancient
	@Transient
	private String levelCode;

	public String getCode() {

		return code;
	}

	public void setCode(String code) {

		this.code = code;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Location getParent() {
		return parent;
	}

	public void setParent(Location parent) {
		this.parent = parent;
	}

	public LocationLevel getLevel() {
		return level;
	}

	public void setLevel(LocationLevel level) {
		this.level = level;
	}

	public Set<Location> getChilds() {
		return childs;
	}

	public void setChilds(Set<Location> childs) {
		this.childs = childs;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreatedDT() {
		return createdDT;
	}

	public void setCreatedDT(Date createdDT) {
		this.createdDT = createdDT;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public Date getLastUpdatedDT() {
		return lastUpdatedDT;
	}

	public void setLastUpdatedDT(Date lastUpdatedDT) {
		this.lastUpdatedDT = lastUpdatedDT;
	}

	public String getLastUpdatedUserName() {
		return lastUpdatedUserName;
	}

	public void setLastUpdatedUserName(String lastUpdatedUserName) {
		this.lastUpdatedUserName = lastUpdatedUserName;
	}

	public long getRevisionNumber() {
		return revisionNumber;
	}

	public void setRevisionNumber(long revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	@Override
	public String toString() {

		return level.getName() + "-" + code + "-" + name;
	}

}
