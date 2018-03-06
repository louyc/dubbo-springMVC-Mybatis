package com.semioe.api.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.semioe.common.pageUtil.PageInfo;

public class Keywords extends PageInfo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column keywords.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column keywords.name
     *
     * @mbg.generated
     */
    private String name;

    private Integer keyType;
    
    public Integer getKeyType() {
		return keyType;
	}

	public void setKeyType(Integer keyType) {
		this.keyType = keyType;
	}

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column keywords.create_time
     *
     * @mbg.generated
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column keywords.update_time
     *
     * @mbg.generated
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
	private Date updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column keywords.in_use
     *
     * @mbg.generated
     */
    private Integer inUse;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column keywords.id
     *
     * @return the value of keywords.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column keywords.id
     *
     * @param id the value for keywords.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column keywords.name
     *
     * @return the value of keywords.name
     *
     * @mbg.generated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column keywords.name
     *
     * @param name the value for keywords.name
     *
     * @mbg.generated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column keywords.create_time
     *
     * @return the value of keywords.create_time
     *
     * @mbg.generated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column keywords.create_time
     *
     * @param createTime the value for keywords.create_time
     *
     * @mbg.generated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column keywords.update_time
     *
     * @return the value of keywords.update_time
     *
     * @mbg.generated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column keywords.update_time
     *
     * @param updateTime the value for keywords.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column keywords.in_use
     *
     * @return the value of keywords.in_use
     *
     * @mbg.generated
     */
    public Integer getInUse() {
        return inUse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column keywords.in_use
     *
     * @param inUse the value for keywords.in_use
     *
     * @mbg.generated
     */
    public void setInUse(Integer inUse) {
        this.inUse = inUse;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Keywords other = (Keywords) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getInUse() == null ? other.getInUse() == null : this.getInUse().equals(other.getInUse()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getInUse() == null) ? 0 : getInUse().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table keywords
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", inUse=").append(inUse);
        sb.append("]");
        return sb.toString();
    }
}