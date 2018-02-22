package com.consensys.hitesh.consumer.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"@id",
	"imageLocation",
	"imageName",
	"imagePdfName",
	"imageOwner",
	"timestamp"
})
public class ImageDTO implements Serializable
{

	private static final long serialVersionUID = 3152035651745415687L;

	@JsonProperty("@id")
	private Integer id;
	@JsonProperty("imageLocation")
	private String imageLocation;
	@JsonProperty("imageName")
	private String imageName;	
	@JsonProperty("imagePdfName")
	private String imagePdfName;
	@JsonProperty("imageOwner")
	private String imageOwner;
	@JsonProperty("timestamp")
	private Date timestamp;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();


	@JsonProperty("@id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("@id")
	public void setId(Integer id) {
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
	public String toString() {
		return "ImageDTO [id=" + id + ", imageLocation=" + imageLocation + ", imageName=" + imageName
				+ ", imagePdfName=" + imagePdfName + ", imageOwner=" + imageOwner + ", timestamp=" + timestamp + "]";
	}
	@JsonProperty("imagePdfName")
	public String getImagePdfName() {
		return imagePdfName;
	}
	@JsonProperty("imagePdfName")
	public void setImagePdfName(String imagePdfName) {
		this.imagePdfName = imagePdfName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((imageLocation == null) ? 0 : imageLocation.hashCode());
		result = prime * result + ((imageName == null) ? 0 : imageName.hashCode());
		result = prime * result + ((imageOwner == null) ? 0 : imageOwner.hashCode());
		result = prime * result + ((imagePdfName == null) ? 0 : imagePdfName.hashCode());
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
		ImageDTO other = (ImageDTO) obj;
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
		if (imagePdfName == null) {
			if (other.imagePdfName != null)
				return false;
		} else if (!imagePdfName.equals(other.imagePdfName))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}


}