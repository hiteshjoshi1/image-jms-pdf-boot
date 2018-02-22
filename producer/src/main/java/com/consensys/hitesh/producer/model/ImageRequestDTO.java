package com.consensys.hitesh.producer.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "imageLocation", "imageName", "imageOwner", "timestamp" })
@Document(collection="imageMetadata")
public class ImageRequestDTO implements Serializable {
	private static final long serialVersionUID = -52257661398709314L;



	public ImageRequestDTO() {
		
	}
	
	public ImageRequestDTO(String imageLoc, String imageName, String imageOwner, Date timestamp) {
		this.imageLocation = imageLoc;
		this.imageName = imageName;
		this.imageOwner = imageOwner;
		this.timestamp = timestamp;
	}

	@Id
	public String id;
	@JsonProperty("imageLocation")
	private String imageLocation;
	@JsonProperty("imageName")
	private String imageName;
	@JsonProperty("imageOwner")
	private String imageOwner;
	@JsonProperty("timestamp")
	private Date timestamp;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@JsonProperty("imageLocation")
	public String getImageLocation() {
		return imageLocation;
	}

	@JsonProperty("imageLocation")
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	@JsonProperty("imageName")
	public String getImageName() {
		return imageName;
	}

	@JsonProperty("imageName")
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@JsonProperty("imageOwner")
	public String getImageOwner() {
		return imageOwner;
	}

	@JsonProperty("imageOwner")
	public void setImageOwner(String imageOwner) {
		this.imageOwner = imageOwner;
	}

	@JsonProperty("timestamp")
	public Date getTimestamp() {
		return timestamp;
	}

	@JsonProperty("timestamp")
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageLocation == null) ? 0 : imageLocation.hashCode());
		result = prime * result + ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + ((imageOwner == null) ? 0 : imageOwner.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageRequestDTO other = (ImageRequestDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageLocation == null) {
			if (other.imageLocation != null)
				return false;
		} else if (!imageLocation.equals(other.imageLocation))
			return false;
		if (imageName == null) {
			if (other.imageName != null)
				return false;
		} else if (!imageName.equals(other.imageName))
			return false;
		if (imageOwner == null) {
			if (other.imageOwner != null)
				return false;
		} else if (!imageOwner.equals(other.imageOwner))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImageRequestDTO [id=" + id + ", imageLocation=" + imageLocation + ", imageName=" + imageName
				+ ", imageOwner=" + imageOwner + ", timestamp=" + timestamp + "]";
	}

}