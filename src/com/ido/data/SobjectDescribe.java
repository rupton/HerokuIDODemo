package com.ido.data;


public class SobjectDescribe {

	public String name;
	public String type;
	public String soapType;
	public int length; 
	public boolean nillable; 
	public int precision;
	public int scale;
	
	public SobjectDescribe() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSoapType() {
		return soapType;
	}

	public void setSoapType(String soapType) {
		this.soapType = soapType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isNillable() {
		return nillable;
	}

	public void setNillable(boolean nillable) {
		this.nillable = nillable;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	@Override
	public String toString() {
		return "SobjectDescribe [name=" + name + ", type=" + type + ", soapType=" + soapType + ", length=" + length
				+ ", nillable=" + nillable + ", precision=" + precision + ", scale=" + scale + "]";
	}
	
	
	
}
