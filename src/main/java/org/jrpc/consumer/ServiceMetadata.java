/**
 * 
 */
package org.jrpc.consumer;

import java.io.Serializable;

/**
 * @author caoguo(jiwei.guo)
 *
 */
public class ServiceMetadata implements Serializable{

	private static final long serialVersionUID = 1L;

	private String version;
	
	private String group;
	
	private String providerName;
	
	public ServiceMetadata(){
		//
	}
	
	public ServiceMetadata(String version, String group, String providerName){
		this.version = version;
		this.group = group;
		this.providerName = providerName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((providerName == null) ? 0 : providerName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		ServiceMetadata other = (ServiceMetadata) obj;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (providerName == null) {
			if (other.providerName != null)
				return false;
		} else if (!providerName.equals(other.providerName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceMetadata [version=" + version + ", group=" + group + ", providerName=" + providerName + "]";
	}
}
